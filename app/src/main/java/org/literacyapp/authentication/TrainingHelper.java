package org.literacyapp.authentication;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.StudentImage;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.model.StudentImageCollectionEvent;
import org.literacyapp.model.StudentImageFeature;
import org.literacyapp.dao.StudentImageFeatureDao;
import org.literacyapp.util.AiHelper;
import org.literacyapp.util.MultimediaHelper;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.PreProcessor.StandardPostprocessing.Resize;
import ch.zhaw.facerecognitionlibrary.Recognition.Recognition;
import ch.zhaw.facerecognitionlibrary.Recognition.SupportVectorMachine;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;

/**
 * Created by sladomic on 26.11.16.
 */

public class TrainingHelper {
    private Context context;
    private DaoSession daoSession;
    private StudentImageDao studentImageDao;
    private StudentImageFeatureDao studentImageFeatureDao;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private SupportVectorMachine svm;
    File svmTrainingFile;
    File svmTrainingModelFile;
    File svmArchiveFolderWithTimestamp;

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
        svmTrainingFile = new File(AiHelper.getSvmDirectory(), "training");
        svmTrainingModelFile = new File(svmTrainingFile.getAbsolutePath() + "_model");
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
                            Log.i(getClass().getName(), "StudentImageCollectionEvent with the id " + studentImage.getStudentImageCollectionEventId() + " has been deleted recursively because the feature extraction failed.");
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
    private synchronized TensorFlow getInitializedTensorFlow(){
        File modelFile = new File(AiHelper.getModelDirectory(), "vgg_faces.pb");
        if (!modelFile.exists()){
            String logMessage = "Model file: " + modelFile.getAbsolutePath() + " doesn't exist. Please copy it manually";
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
        // Initiate training if a StudentImageCollectionEvent has not been trained yet
        if (studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.SvmTrainingExecuted.eq(false)).count() > 0){
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
            Log.i(getClass().getName(), "Classifier training has started.");
            svm.train("-t 0 ");
            if (checkClassifierTrainingResult()){
                Log.i(getClass().getName(), "Classifier training has finished succuessfully.");
            } else {
                Log.e(getClass().getName(), "Classifier training has failed.");
            }
        }
    }

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

    private synchronized boolean checkClassifierTrainingResult(){
        if (svmTrainingFile.exists() && svmTrainingModelFile.exists()){
            return true;
        } else {
            // Move the newest archive files back to the main folder
            svmTrainingFile.renameTo(new File(AiHelper.getSvmDirectory(),svmTrainingFile.getName()));
            svmTrainingModelFile.renameTo(new File(AiHelper.getSvmDirectory(),svmTrainingModelFile.getName()));
            svmArchiveFolderWithTimestamp.delete();
            return false;
        }
    }
}
