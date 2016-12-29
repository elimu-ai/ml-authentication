package org.literacyapp.authentication;

import android.util.Log;

import org.literacyapp.model.Student;
import org.literacyapp.util.StudentUpdateHelper;
import org.opencv.core.Mat;

import ch.zhaw.facerecognitionlibrary.Recognition.SupportVectorMachine;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;

/**
 * Created by sladomic on 29.12.16.
 */

public class RecognitionThread extends Thread {
    private SupportVectorMachine svm;
    private TensorFlow tensorFlow;
    private Mat img;
    private String svmString;

    public RecognitionThread(SupportVectorMachine svm, TensorFlow tensorFlow) {
        this.svm = svm;
        this.tensorFlow = tensorFlow;
    }

    @Override
    public void run() {
        svmString = getSvmString(img);
    }

    /**
     * Returns the SVM string using the feature vector
     * @param img
     * @return
     */
    private synchronized String getSvmString(Mat img){
        Mat featureVector = tensorFlow.getFeatureVector(img);
        return svm.getSvmString(featureVector);
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public String getSvmString() {
        return svmString;
    }
}
