package org.literacyapp.authentication;

import android.util.Log;

import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.Student;
import org.literacyapp.model.analytics.StudentImageCollectionEvent;
import org.opencv.core.Mat;

import java.util.HashMap;
import java.util.StringTokenizer;

import ch.zhaw.facerecognitionlibrary.Recognition.SupportVectorMachine;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;

/**
 * Created by sladomic on 29.12.16.
 */

public class RecognitionThread extends Thread {
    private SupportVectorMachine svm;
    private TensorFlow tensorFlow;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private Mat img;
    private Student student;

    public RecognitionThread(SupportVectorMachine svm, TensorFlow tensorFlow, StudentImageCollectionEventDao studentImageCollectionEventDao) {
        this.svm = svm;
        this.tensorFlow = tensorFlow;
        this.studentImageCollectionEventDao = studentImageCollectionEventDao;
    }

    @Override
    public void run() {
        String svmProbability = svm.recognizeProbability(getSvmString(img));
        student = getStudentFromProbability(svmProbability);
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

    /**
     * Determines if and which Student has been recognized. The Student is only returned if the probability is above 90%
     * @param svmProbability
     * @return
     */
    private synchronized Student getStudentFromProbability(String svmProbability){
        // Example string for svmProbability: labels 1 2\n2 0.458817 0.541183
        StringTokenizer stringTokenizerSvmProbability = new StringTokenizer(svmProbability, "\n");
        // Example string for header: labels 1 2
        String header = stringTokenizerSvmProbability.nextToken();
        // Example string for content: 2 0.458817 0.541183
        String content = stringTokenizerSvmProbability.nextToken();

        StringTokenizer stringTokenizerHeader = new StringTokenizer(header, " ");
        // Skip first token
        stringTokenizerHeader.nextToken();
        StringTokenizer stringTokenizerContent = new StringTokenizer(content, " ");
        // First token shows the label with the highest probability
        int eventIdWithHighestProbability = Integer.valueOf(stringTokenizerContent.nextToken());

        HashMap<Integer, Double> probabilityMap = new HashMap<Integer, Double>();
        while(stringTokenizerHeader.hasMoreTokens()){
            probabilityMap.put(Integer.valueOf(stringTokenizerHeader.nextToken()), Double.valueOf(stringTokenizerContent.nextToken()));
        }

        Log.i(getClass().getName(), "The StudentImageCollectionEvent with the Id " + eventIdWithHighestProbability + " has the highest probability " + probabilityMap.get(eventIdWithHighestProbability));

        if (probabilityMap.get(eventIdWithHighestProbability) > 0.9){
            StudentImageCollectionEvent studentImageCollectionEvent = studentImageCollectionEventDao.load((long) eventIdWithHighestProbability);
            Student student = studentImageCollectionEvent.getStudent();
            return student;
        } else {
            return null;
        }
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public Student getStudent() {
        return student;
    }
}
