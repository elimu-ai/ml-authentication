package org.literacyapp.authentication;

import android.content.Context;
import android.util.Log;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.model.StudentImage;
import org.literacyapp.dao.StudentImageDao;
import org.literacyapp.model.StudentImageFeature;
import org.literacyapp.dao.StudentImageFeatureDao;
import org.literacyapp.util.AiHelper;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Calendar;
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
    private SupportVectorMachine svm;

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
        svm = new SupportVectorMachine(context, Recognition.TRAINING);
    }


    /**
     * Get all the StudentImages where the features haven't been extracted yet
     * Extract features for every StudentImage and store them as StudentImageFeature
     */
    public void extractFeatures(){
        List<StudentImage> studentImageList = studentImageDao.queryBuilder()
                .where(StudentImageDao.Properties.StudentImageFeatureId.eq(0))
                .list();
        Log.i(getClass().getName(), "Number of StudentImages, where the features haven't been extracted yet: " + studentImageList.size());
        TensorFlow tensorFlow = getInitializedTensorFlow();
        for(StudentImage studentImage : studentImageList){
            String svmVector = getSvmVector(tensorFlow, studentImage);
            if (svmVector != null){
                storeStudentImageFeature(studentImage, svmVector);
            }
            // TODO housekeeping job #226
        }
    }

    /**
     * Stores a StudentImageFeature to the database
     * @param studentImage - StudentImage
     * @param svmVector - Extracted features converted to an SVM string for LIBSVM (without the label)
     */
    private void storeStudentImageFeature(StudentImage studentImage, String svmVector){
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
    private String getSvmVector(TensorFlow tensorFlow, StudentImage studentImage){
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
    private TensorFlow getInitializedTensorFlow(){
        String model = AiHelper.getModelDirectory() + "/vgg_faces.pb";
        int inputSize = 224;
        int outputSize = 4096;
        int imageMean = 128;
        String inputLayer = "Placeholder";
        String outputLayer = "fc7/fc7";
        TensorFlow tensorFlow = new TensorFlow(context, inputSize, imageMean, outputSize, inputLayer, outputLayer, model);
        return tensorFlow;
    }
}
