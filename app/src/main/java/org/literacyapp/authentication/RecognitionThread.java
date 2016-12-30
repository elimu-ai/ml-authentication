package org.literacyapp.authentication;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.Student;
import org.literacyapp.model.analytics.StudentImageCollectionEvent;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;

/**
 * Created by sladomic on 29.12.16.
 */

public class RecognitionThread extends Thread {
    private static final double SIMILARITY_THRESHOLD = 0.5;
    private TensorFlow tensorFlow;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private Mat img;
    private Student student;
    private Gson gson;

    public RecognitionThread(TensorFlow tensorFlow, StudentImageCollectionEventDao studentImageCollectionEventDao) {
        this.tensorFlow = tensorFlow;
        this.studentImageCollectionEventDao = studentImageCollectionEventDao;
        gson = new Gson();
    }

    @Override
    public void run() {
        Mat featureVectorToRecognize = getFeatureVector(img);
        student = getMostSimilarStudentIfInThreshold(featureVectorToRecognize);
    }

    /**
     * Returns the featureVector derived from the Neural Network
     * @param img
     * @return
     */
    private synchronized Mat getFeatureVector(Mat img){
        return tensorFlow.getFeatureVector(img);
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public Student getStudent() {
        return student;
    }

    /**
     * Returns the recognized Student if the cosineSimilarity was above the threshold
     * @param featureVectorToRecognize
     * @return
     */
    private synchronized Student getMostSimilarStudentIfInThreshold(Mat featureVectorToRecognize){
        List<StudentImageCollectionEvent> studentImageCollectionEvents = studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.MeanFeatureVector.isNotNull()).list();
        List<Student> studentsInThreshold = new ArrayList<>();
        for (StudentImageCollectionEvent studentImageCollectionEvent : studentImageCollectionEvents){
            List<Float> featureVectorList = gson.fromJson(studentImageCollectionEvent.getMeanFeatureVector(), new TypeToken<List<Float>>(){}.getType());
            Mat featureVector = Converters.vector_float_to_Mat(featureVectorList);
            double dotProduct = featureVector.dot(featureVectorToRecognize);
            double normFeatureVector = Core.norm(featureVector, Core.NORM_L2);
            double normFeatureVectorToRecognize = Core.norm(featureVectorToRecognize, Core.NORM_L2);
            double cosineSimilarity = dotProduct / (normFeatureVector * normFeatureVectorToRecognize);
            double absoluteCosineSimilarity = Math.abs(cosineSimilarity);
            Student student = studentImageCollectionEvent.getStudent();
            Log.i(getClass().getName(), "getMostSimilarStudentIfInThreshold: absoluteCosineSimilarity: " + absoluteCosineSimilarity + " with Student: " + student.getUniqueId());
            if (absoluteCosineSimilarity > SIMILARITY_THRESHOLD){
                studentsInThreshold.add(student);
            }
        }
        int numberOfStudentsInThreshold = studentsInThreshold.size();
        if (numberOfStudentsInThreshold == 1){
            Student student = studentsInThreshold.get(0);
            Log.i(getClass().getName(), "getMostSimilarStudentIfInThreshold: The Student was recognized as " + student.getUniqueId());
            return studentsInThreshold.get(0);
        } else {
            Log.i(getClass().getName(), "getMostSimilarStudentIfInThreshold: No Student was recognized, because the numberOfStudentsInThreshold was: " + numberOfStudentsInThreshold);
            return null;
        }
    }
}
