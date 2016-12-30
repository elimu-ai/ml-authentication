package org.literacyapp.authentication;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.Student;
import org.literacyapp.model.StudentImage;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.model.analytics.StudentImageCollectionEvent;
import org.literacyapp.model.StudentImageFeature;
import org.literacyapp.dao.StudentImageFeatureDao;
import org.literacyapp.util.AiHelper;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.StudentHelper;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Recognition.SupportVectorMachine;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;

/**
 * Created by sladomic on 26.11.16.
 */

public class TrainingHelper {
    private static final String MODEL_DOWNLOAD_LINK = "https://drive.google.com/open?id=0B3jQsJcchixPek9lU3BaOHpCUGc";
    private Context context;
    private DaoSession daoSession;
    private StudentImageDao studentImageDao;
    private StudentImageFeatureDao studentImageFeatureDao;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private StudentDao studentDao;
    private SupportVectorMachine svm;
    private static final File svmTrainingFile = new File(AiHelper.getSvmDirectory(), "training");
    private static final File svmTrainingModelFile = new File(svmTrainingFile.getAbsolutePath() + "_model");
    private File svmArchiveFolderWithTimestamp;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    public TrainingHelper(Context context){
        this.context = context;
        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        daoSession = literacyApplication.getDaoSession();
        studentImageDao = daoSession.getStudentImageDao();
        studentImageFeatureDao = daoSession.getStudentImageFeatureDao();
        studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();
        studentDao = daoSession.getStudentDao();
        File svmPredictionFile = new File(AiHelper.getSvmDirectory(), "prediction");
        svm = new SupportVectorMachine(svmTrainingFile, svmPredictionFile);
    }


    /**
     * Get all the StudentImages where the features haven't been extracted yet
     * Extract features for every StudentImage and store them as StudentImageFeature
     */
    public synchronized void extractFeatures(){
        List<StudentImage> studentImageList = studentImageDao.queryBuilder()
                .where(StudentImageDao.Properties.StudentImageFeatureId.eq(0))
                .list();
        Log.i(getClass().getName(), "Number of StudentImages, where the features haven't been extracted yet: " + studentImageList.size());
        if (studentImageList.size() > 0){
            TensorFlow tensorFlow = getInitializedTensorFlow();
            if (tensorFlow != null){
                for(StudentImage studentImage : studentImageList){
                    if (isStudentImageValid(studentImage)){
                        String svmVector = getSvmVector(tensorFlow, studentImage);
                        if (svmVector != null){
                            storeStudentImageFeature(studentImage, svmVector);
                        } else {
                            Log.w(getClass().getName(), "StudentImageCollectionEvent with the id " + studentImage.getStudentImageCollectionEventId() + " will be deleted recursively because the feature extraction failed.");
                            deleteStudentImagesRecursive(studentImage, "the feature extraction failed.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Stores a StudentImageFeature to the database
     * @param studentImage - StudentImage
     * @param svmVector - Extracted features converted to an SVM string for LIBSVM (without the label)
     */
    private synchronized void storeStudentImageFeature(StudentImage studentImage, String svmVector){
        StudentImageFeature studentImageFeature = new StudentImageFeature(studentImage.getId(), Calendar.getInstance(), svmVector);
        studentImage.setStudentImageFeature(studentImageFeature);
        studentImageFeatureDao.insert(studentImageFeature);
        studentImageDao.update(studentImage);
        Log.i(getClass().getName(), "StudentImageFeature with Id " + studentImageFeature.getId() + " for StudentImage with Id " + studentImage.getId() + " has been extracted and stored.");
    }

    /**
     * Load image into OpenCV Mat object
     * Extract features from TensorFlow model
     * Convert feature to SVM string
     * @param tensorFlow
     * @param studentImage
     * @return
     */
    private synchronized String getSvmVector(TensorFlow tensorFlow, StudentImage studentImage){
        // Load image into OpenCV Mat object
        Mat img = Imgcodecs.imread(studentImage.getImageFileUrl());
        Log.i(getClass().getName(), "StudentImage has been loaded from file " + studentImage.getImageFileUrl());
        // Extract features from TensorFlow model
        Mat featureVector = tensorFlow.getFeatureVector(img);
        Log.i(getClass().getName(), "Feature vector has been extracted for StudentImage: " + studentImage.getId());
        // Convert featureVector to SVM string
        return svm.getSvmString(featureVector);
    }

    /**
     * Initialize TensorFlow model
     * @return
     */
    public synchronized TensorFlow getInitializedTensorFlow(){
        File modelFile = new File(AiHelper.getModelDirectory(), "vgg_faces.pb");
        if (!modelFile.exists()){
            File modelDownloadFile = createModelDownloadFile(modelFile);
            String logMessage = "Model file: " + modelFile.getAbsolutePath() + " doesn't exist. Please copy it manually";
            if (modelDownloadFile != null){
                logMessage = logMessage + ". Find the download link in the file " + modelDownloadFile.getAbsolutePath();

            } else {
                logMessage = logMessage + " from " + MODEL_DOWNLOAD_LINK;
            }
            Log.e(getClass().getName(), logMessage);
            Toast.makeText(context, logMessage, Toast.LENGTH_LONG).show();

            return null;
        }
        int inputSize = 224;
        int outputSize = 4096;
        int imageMean = 128;
        String inputLayer = "Placeholder";
        String outputLayer = "fc7/fc7";
        TensorFlow tensorFlow = new TensorFlow(context, inputSize, imageMean, outputSize, inputLayer, outputLayer, modelFile.getAbsolutePath());
        return tensorFlow;
    }

    /**
     * Check if StudentImage is valid
     * The StudentImage is invalid if
     *      a) no StudentImageCollectionEvent is assigned --> in this case the StudentImage gets deleted from the db
     *      b) the StudentImage file on the sdcard doesn't exist --> in this case all related StudentImages, StudentImageFeatures and the StudentImageCollectionEvent get deleted recursively from the db
     * @param studentImage
     * @return
     */
    private synchronized boolean isStudentImageValid(StudentImage studentImage){
        boolean valid = true;
        File studentImageFile = new File(studentImage.getImageFileUrl());
        if (studentImage.getStudentImageCollectionEvent() == null){
            studentImageDao.delete(studentImage);
            Log.i(getClass().getName(), "StudentImage with the id " + studentImage.getId() + " has been deleted.");
            valid = false;
        } else if (!studentImageFile.exists()){
            Log.i(getClass().getName(), "StudentImageCollectionEvent with the id " + studentImage.getStudentImageCollectionEventId() + " has been deleted recursively because the file " + studentImage.getImageFileUrl() + " doesn't exist.");
            deleteStudentImagesRecursive(studentImage, "the file " + studentImage.getImageFileUrl() + " doesn't exist.");
            valid = false;
        }
        return valid;
    }

    // Delete StudentImageCollectionEvent and all StudentImages if a file doesn't exist anymore or the feature extraction failed for another reason
    private synchronized void deleteStudentImagesRecursive(StudentImage studentImage, String reason){
        // Delete all StudentImages and StudentImageFeatures which have already been extracted for this StudentImageCollectionEvent
        List<StudentImage> studentImagesToDelete = studentImageDao.queryBuilder()
                .where(StudentImageDao.Properties.StudentImageCollectionEventId.eq(studentImage.getStudentImageCollectionEventId()))
                .where(StudentImageDao.Properties.StudentImageFeatureId.notEq(0))
                .list();
        for (StudentImage studentImageToDelete : studentImagesToDelete){
            studentImageDao.delete(studentImageToDelete);
            Log.i(getClass().getName(), "StudentImage with the id " + studentImageToDelete.getId() + " has been deleted because " + reason);
            studentImageFeatureDao.delete(studentImageToDelete.getStudentImageFeature());
            Log.i(getClass().getName(), "StudentImageFeature with the id " + studentImageToDelete.getStudentImageFeatureId() + " has been deleted because " + reason);
        }
        // Delete the StudentImageCollectionEvent
        studentImageCollectionEventDao.delete(studentImage.getStudentImageCollectionEvent());
        Log.i(getClass().getName(), "StudentImageCollectionEvent with the id " + studentImage.getStudentImageCollectionEventId() + " has been deleted " + reason);
        // Delete the StudentImage, where the file doesn't exist anymore
        studentImageDao.delete(studentImage);
        Log.i(getClass().getName(), "StudentImage with the id " + studentImage.getId() + " has been deleted because " + reason);
    }

    public synchronized void trainClassifier(){
        Log.i(getClass().getName(), "trainClassifier");
        // Initiate training if a StudentImageCollectionEvent has not been trained yet
        long count = studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.SvmTrainingExecuted.eq(false)).count();
        Log.i(getClass().getName(), "Count of StudentImageCollectionEvents where SvmTrainingExecuted is false: " + count);
        if (count > 0){
            List<StudentImage> studentImages = studentImageDao.queryBuilder()
                    .where(StudentImageDao.Properties.StudentImageFeatureId.notEq(0))
                    .list();
            for (StudentImage studentImage : studentImages){
                svm.addImage(studentImage.getStudentImageFeature().getSvmVector(), Long.toString(studentImage.getStudentImageCollectionEventId()));
            }
            if (archiveClassifierFiles()){
                Log.i(getClass().getName(), "Classifier files have been archived.");
            } else {
                Log.e(getClass().getName(), "Failed to archive classifier files.");
            }
            Log.i(getClass().getName(), "Classifier training has started with linear kernel (LIBSVM -t 0).");
            svm.trainProbability("-t 0 ");
            if (checkClassifierTrainingResult()){
                for (StudentImage studentImage : studentImages){
                    StudentImageCollectionEvent studentImageCollectionEvent = studentImage.getStudentImageCollectionEvent();
                    if (!studentImageCollectionEvent.getSvmTrainingExecuted()){
                        // Create new Student
                        Student student = new Student();
                        student.setUniqueId(StudentHelper.generateNextUniqueId(context, studentDao));
                        File avatarFile = createAvatarFileFromStudentImage(studentImage, student);
                        if (avatarFile.exists()) {
                            student.setAvatar(avatarFile.getAbsolutePath());
                            Log.i(getClass().getName(), "Avatar with the path: " + avatarFile.getAbsolutePath() + " has been set for the Student " + student.getUniqueId());
                        } else {
                            Log.i(getClass().getName(), "Avatar couldn't be created from " + studentImage.getImageFileUrl() + " for the Student " + student.getUniqueId());
                        }
                        student.setTimeCreated(Calendar.getInstance());
                        studentDao.insert(student);
                        Log.i(getClass().getName(), "Student with Id " + student.getId() + " and uniqueId " + student.getUniqueId() + " has been created.");
                        // Add Student to StudentImageCollectionEvent
                        studentImageCollectionEvent.setStudent(student);
                        studentImageCollectionEvent.setSvmTrainingExecuted(true);
                        studentImageCollectionEventDao.update(studentImageCollectionEvent);
                        Log.i(getClass().getName(), "StudentImageCollectionEvent with Id " + studentImageCollectionEvent.getId() + " has been trained in classifier");
                    } else if (TextUtils.isEmpty(studentImageCollectionEvent.getStudent().getAvatar())){
                        // Try to create an Avatar for the Student if no Avatar has been created yet
                        Student student = studentImageCollectionEvent.getStudent();
                        File avatarFile = createAvatarFileFromStudentImage(studentImage, student);
                        if (avatarFile.exists()) {
                            student.setAvatar(avatarFile.getAbsolutePath());
                            Log.i(getClass().getName(), "Avatar with the path: " + avatarFile.getAbsolutePath() + " has been set for the Student " + student.getUniqueId());
                        } else {
                            Log.i(getClass().getName(), "Avatar couldn't be created from " + studentImage.getImageFileUrl() + " for the Student " + student.getUniqueId());
                        }
                        studentDao.update(student);
                    }
                }
                Log.i(getClass().getName(), "Classifier training has finished succuessfully.");
            } else {
                Log.e(getClass().getName(), "Classifier training has failed.");
            }
        }
    }

    /**
     * Archive the existing classifier files before training
     *      a) for debugging purposes
     *      b) for a backup if training fails --> so the last training files can be restored
     * @return
     */
    private synchronized boolean archiveClassifierFiles(){
        boolean success = true;
        svmArchiveFolderWithTimestamp = new File(AiHelper.getSvmArchiveDirectory().getAbsolutePath(), Long.toString(new Date().getTime()));
        Log.i(getClass().getName(), "Create archive directory " + svmArchiveFolderWithTimestamp.getAbsolutePath());
        svmArchiveFolderWithTimestamp.mkdir();
        success = success && svmTrainingFile.renameTo(new File(svmArchiveFolderWithTimestamp, svmTrainingFile.getName()));
        success = success && svmTrainingModelFile.renameTo(new File(svmArchiveFolderWithTimestamp, svmTrainingModelFile.getName()));
        if (!success){
            svmArchiveFolderWithTimestamp.delete();
        }
        return success;
    }

    /**
     * Checks whether the 2 training files exist. If not, restore the backup files.
     * @return
     */
    private synchronized boolean checkClassifierTrainingResult(){
        if (classifierFilesExist()){
            return true;
        } else {
            // Move the newest archive files back to the main folder
            svmTrainingFile.renameTo(new File(AiHelper.getSvmDirectory(),svmTrainingFile.getName()));
            svmTrainingModelFile.renameTo(new File(AiHelper.getSvmDirectory(),svmTrainingModelFile.getName()));
            svmArchiveFolderWithTimestamp.delete();
            return false;
        }
    }

    public static synchronized boolean classifierFilesExist(){
        if (svmTrainingFile.exists() && svmTrainingModelFile.exists()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a text file with the TensorFlow model download linke in case the file doesn't exist
     * @param modelFile
     * @return
     */
    private File createModelDownloadFile(File modelFile){
        File modelDownloadFile = new File(AiHelper.getModelDirectory(), "download_link.txt");
        try {
            FileWriter fileWriter = new FileWriter(modelDownloadFile, false);
            fileWriter.append(MODEL_DOWNLOAD_LINK + "\n");
            fileWriter.append("Copy to: " + modelFile.getAbsolutePath());
            fileWriter.close();
            Log.i(getClass().getName(), "Model download file has been created at " + modelDownloadFile.getAbsolutePath() + " with the link " + MODEL_DOWNLOAD_LINK);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelDownloadFile;
    }

    /**
     * Create the Avatar file for a Student using a StudentImage
     * @param studentImage
     * @return
     */
    private File createAvatarFileFromStudentImage(StudentImage studentImage, Student student){
        String imageFilePath = StudentHelper.getStudentAvatarDirectory() + "/" + student.getUniqueId() + ".png";
        File avatarFile = new File(imageFilePath);
        try {
            Log.i(getClass().getName(), "createAvatarFileFromStudentImage: Preparing InputStream and OutputStream to copy the StudentImage into the Avatar directory");
            Log.i(getClass().getName(), "createAvatarFileFromStudentImage: InputStream with the file: " + studentImage.getImageFileUrl());
            InputStream inputStream = new FileInputStream(studentImage.getImageFileUrl());
            Log.i(getClass().getName(), "createAvatarFileFromStudentImage: OutputStream with the file: " + avatarFile.getAbsolutePath());
            OutputStream outputStream = new FileOutputStream(avatarFile);
            Log.i(getClass().getName(), "createAvatarFileFromStudentImage: Start copying of the file...");
            MultimediaHelper.copyFile(inputStream, outputStream);
            Log.i(getClass().getName(), "createAvatarFileFromStudentImage: Finished file copying.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return avatarFile;
    }

    public SupportVectorMachine getSvm() {
        return svm;
    }
}
