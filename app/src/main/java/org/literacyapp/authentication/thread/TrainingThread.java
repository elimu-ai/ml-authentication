package org.literacyapp.authentication.thread;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.dao.StudentImageFeatureDao;
import org.literacyapp.model.Student;
import org.literacyapp.model.StudentImage;
import org.literacyapp.model.StudentImageFeature;
import org.literacyapp.model.analytics.StudentImageCollectionEvent;
import org.literacyapp.service.FaceRecognitionTrainingJobService;
import org.literacyapp.util.AiHelper;
import org.literacyapp.util.MultimediaHelper;
import org.literacyapp.util.StudentHelper;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;

/**
 * Created by sladomic on 26.11.16.
 */

public class TrainingThread extends Thread {
    private static final String MODEL_DOWNLOAD_LINK = "https://drive.google.com/open?id=0B3jQsJcchixPek9lU3BaOHpCUGc";
    private Context context;
    private DaoSession daoSession;
    private StudentImageDao studentImageDao;
    private StudentImageFeatureDao studentImageFeatureDao;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private StudentDao studentDao;
    private Gson gson;
    private FaceRecognitionTrainingJobService trainingJobService;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    @Override
    public void run() {
        extractFeatures();
        trainClassifier();
        if (trainingJobService != null){
            trainingJobService.jobFinished(trainingJobService.getJobParameters(), false);
        }
    }

    public TrainingThread(FaceRecognitionTrainingJobService trainingJobService){
        this(trainingJobService.getApplicationContext());
        this.trainingJobService = trainingJobService;
    }

    public TrainingThread(Context context){
        this.context = context;
        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        daoSession = literacyApplication.getDaoSession();
        studentImageDao = daoSession.getStudentImageDao();
        studentImageFeatureDao = daoSession.getStudentImageFeatureDao();
        studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();
        studentDao = daoSession.getStudentDao();
        gson = new Gson();
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
                        String featureVectorString = getFeatureVectorString(tensorFlow, studentImage);
                        if (!TextUtils.isEmpty(featureVectorString)){
                            storeStudentImageFeature(studentImage, featureVectorString);
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
     * @param featureVectorString - Extracted feature vector serialized to a string
     */
    private synchronized void storeStudentImageFeature(StudentImage studentImage, String featureVectorString){
        StudentImageFeature studentImageFeature = new StudentImageFeature(studentImage.getId(), Calendar.getInstance(), featureVectorString);
        studentImage.setStudentImageFeature(studentImageFeature);
        studentImageFeatureDao.insert(studentImageFeature);
        studentImageDao.update(studentImage);
        Log.i(getClass().getName(), "StudentImageFeature with Id " + studentImageFeature.getId() + " for StudentImage with Id " + studentImage.getId() + " has been extracted and stored.");
    }

    /**
     * Load image into OpenCV Mat object
     * Extract features from TensorFlow model
     * @param tensorFlow
     * @param studentImage
     * @return
     */
    private synchronized String getFeatureVectorString(TensorFlow tensorFlow, StudentImage studentImage){
        // Load image into OpenCV Mat object
        Mat img = Imgcodecs.imread(studentImage.getImageFileUrl());
        Log.i(getClass().getName(), "StudentImage has been loaded from file " + studentImage.getImageFileUrl());
        // Extract features from TensorFlow model
        Mat featureVector = tensorFlow.getFeatureVector(img);
        List<Float> featureVectorList = new ArrayList<>();
        Converters.Mat_to_vector_float(featureVector, featureVectorList);
        String serializedFeatureVector = gson.toJson(featureVectorList);
        Log.i(getClass().getName(), "Feature vector has been extracted for StudentImage: " + studentImage.getId());
        return serializedFeatureVector;
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

    /**
     * Calculate the meanFeatureVector for each StudentImageCollectionEvent using the extracted featureVectors
     */
    public synchronized void trainClassifier(){
        Log.i(getClass().getName(), "trainClassifier");
        // Initiate training if a StudentImageCollectionEvent has not been trained yet
        List<StudentImageCollectionEvent> studentImageCollectionEvents = studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.MeanFeatureVector.isNull()).list();
        Log.i(getClass().getName(), "Count of StudentImageCollectionEvents where MeanFeatureVector is null: " + studentImageCollectionEvents.size());
        if (studentImageCollectionEvents.size() > 0){
            for (StudentImageCollectionEvent studentImageCollectionEvent : studentImageCollectionEvents){
                Mat allFeatureVectors = new Mat();
                List<StudentImage> studentImages = studentImageCollectionEvent.getStudentImages();
                for (StudentImage studentImage : studentImages){
                    List<Float> featureVectorList = gson.fromJson(studentImage.getStudentImageFeature().getFeatureVector(), new TypeToken<List<Float>>(){}.getType());
                    Mat featureVector = Converters.vector_float_to_Mat(featureVectorList);
                    allFeatureVectors.push_back(featureVector.reshape(1, 1));
                }

                Mat meanFeatureVector = new Mat();
                Core.reduce(allFeatureVectors, meanFeatureVector, 0, Core.REDUCE_AVG);
                List<Float> meanFeatureVectorList = new ArrayList<>();
                Converters.Mat_to_vector_float(meanFeatureVector.reshape(1, meanFeatureVector.cols()), meanFeatureVectorList);
                String meanFeatureVectorString = gson.toJson(meanFeatureVectorList);
                studentImageCollectionEvent.setMeanFeatureVector(meanFeatureVectorString);

                Student student = createStudent(studentImages);

                studentImageCollectionEvent.setStudent(student);
                studentImageCollectionEventDao.update(studentImageCollectionEvent);
                Log.i(getClass().getName(), "StudentImageCollectionEvent with Id " + studentImageCollectionEvent.getId() + " has been trained in classifier");
            }
        }
    }

    /**
     * Create a student using a random StudentImage as Avatar image
     * @param studentImages
     * @return
     */
    private synchronized Student createStudent(List<StudentImage> studentImages){
        Student student = new Student();
        student.setUniqueId(StudentHelper.generateNextUniqueId(context, studentDao));

        int randomIndex = (int) (Math.random() * studentImages.size());
        StudentImage studentImageForAvatar = studentImages.get(randomIndex);
        File avatarFile = createAvatarFileFromStudentImage(studentImageForAvatar, student);
        if (avatarFile.exists()) {
            student.setAvatar(avatarFile.getAbsolutePath());
            Log.i(getClass().getName(), "Avatar with the path: " + avatarFile.getAbsolutePath() + " has been set for the Student " + student.getUniqueId());
        } else {
            Log.i(getClass().getName(), "Avatar couldn't be created from " + studentImageForAvatar.getImageFileUrl() + " for the Student " + student.getUniqueId());
        }

        student.setTimeCreated(Calendar.getInstance());
        studentDao.insert(student);
        Log.i(getClass().getName(), "Student with Id " + student.getId() + " and uniqueId " + student.getUniqueId() + " has been created.");
        return student;
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
            Log.e(getClass().getName(), null, e);
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
            Log.e(getClass().getName(), null, e);
        } catch (IOException e) {
            Log.e(getClass().getName(), null, e);
        }
        return avatarFile;
    }
}
