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
import java.util.List;

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
    private List<Student> recognizedStudents;
    private Gson gson;
    private boolean featuresAlreadyExtracted;

    public RecognitionThread(TensorFlow tensorFlow, StudentImageCollectionEventDao studentImageCollectionEventDao) {
        this.tensorFlow = tensorFlow;
        this.studentImageCollectionEventDao = studentImageCollectionEventDao;
        gson = new Gson();
        featuresAlreadyExtracted = false;
        recognizedStudents = new ArrayList<>();
    }

    @Override
    public void run() {
        Mat featureVectorToRecognize;
        if (!featuresAlreadyExtracted){
            featureVectorToRecognize = getFeatureVector(img);
        } else {
            featureVectorToRecognize = img;
        }
        recognizedStudents = getMostSimilarStudentIfInThreshold(featureVectorToRecognize);
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

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Student> getRecognizedStudent() {
        return recognizedStudents;
    }

    public void setFeaturesAlreadyExtracted(boolean featuresAlreadyExtracted) {
        this.featuresAlreadyExtracted = featuresAlreadyExtracted;
    }

    /**
     * Returns the recognized Student if the cosineSimilarity was above the threshold
     * @param featureVectorToRecognize
     * @return
     */
    private synchronized List<Student> getMostSimilarStudentIfInThreshold(Mat featureVectorToRecognize){
        List<StudentImageCollectionEvent> studentImageCollectionEvents = studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.MeanFeatureVector.isNotNull()).list();
        List<Student> studentsInThreshold = new ArrayList<>();
        for (StudentImageCollectionEvent studentImageCollectionEvent : studentImageCollectionEvents){
            Student currentStudent = studentImageCollectionEvent.getStudent();
            // Skip if the students are identical (same UniqueId)
            if (!areStudentsIdentical(currentStudent)){
                List<Float> featureVectorList = gson.fromJson(studentImageCollectionEvent.getMeanFeatureVector(), new TypeToken<List<Float>>(){}.getType());
                Mat featureVector = Converters.vector_float_to_Mat(featureVectorList);
                double dotProduct = featureVector.dot(featureVectorToRecognize);
                double normFeatureVector = Core.norm(featureVector, Core.NORM_L2);
                double normFeatureVectorToRecognize = Core.norm(featureVectorToRecognize, Core.NORM_L2);
                double cosineSimilarity = dotProduct / (normFeatureVector * normFeatureVectorToRecognize);
                double absoluteCosineSimilarity = Math.abs(cosineSimilarity);
                Log.i(getClass().getName(), "getMostSimilarStudentIfInThreshold: absoluteCosineSimilarity: " + absoluteCosineSimilarity + " with Student: " + currentStudent.getUniqueId());
                if (absoluteCosineSimilarity > SIMILARITY_THRESHOLD){
                    studentsInThreshold.add(currentStudent);
                }
            } else {
                Log.i(getClass().getName(), "getMostSimilarStudentIfInThreshold: currentStudent: " + currentStudent.getUniqueId() + " was skipped because it is identical with the student: " + student.getUniqueId());
            }
        }
        return studentsInThreshold;
    }

    private boolean areStudentsIdentical(Student currentStudent){
        boolean areStudentsIdentical = false;
        if (student != null){
            if (currentStudent.getUniqueId().equals(student.getUniqueId())){
                areStudentsIdentical = true;
            }
        }
        return areStudentsIdentical;
    }
}
