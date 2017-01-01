package org.literacyapp.authentication;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.literacyapp.LiteracyApplication;
import org.literacyapp.dao.DaoSession;
import org.literacyapp.dao.StudentDao;
import org.literacyapp.dao.StudentImageCollectionEventDao;
import org.literacyapp.model.Student;
import org.literacyapp.model.analytics.StudentImageCollectionEvent;
import org.literacyapp.service.synchronization.MergeSimilarStudentsJobService;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.utils.Converters;

import java.util.List;

import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.TensorFlow;

/**
 * Created by sladomic on 01.01.17.
 */

public class MergeThread extends Thread {
    PreProcessorFactory ppF;
    TrainingThread trainingThread;
    StudentDao studentDao;
    StudentImageCollectionEventDao studentImageCollectionEventDao;
    Gson gson;

    public MergeThread(Context context) {
        trainingThread = new TrainingThread(context);
        ppF = new PreProcessorFactory(context);
        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        DaoSession daoSession = literacyApplication.getDaoSession();
        studentDao = daoSession.getStudentDao();
        studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();
        gson = new Gson();
    }

    @Override
    public void run() {
        findAndMergeSimilarStudents();
    }

    /**
     * Find similar students
     *
     *
     */
    public synchronized void findAndMergeSimilarStudents(){
        Log.i(getClass().getName(), "findAndMergeSimilarStudents");
        TensorFlow tensorFlow = trainingThread.getInitializedTensorFlow();
        findSimilarStudentsUsingAvatarImages(ppF, tensorFlow);
        findSimilarStudentsUsingMeanFeatureVector(ppF, tensorFlow);
    }

    /**
     * Find similar students
     * Case 1: Student was added during fallback but in the meantime the same person has an existing StudentImageCollectionEvent and a new Student entry
     * ---> Use the avatar image as input for the recognition
     * @param ppF
     * @param tensorFlow
     */
    private synchronized void findSimilarStudentsUsingAvatarImages(PreProcessorFactory ppF, TensorFlow tensorFlow){
        Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages");
        // Iterate through all Students
        List<Student> students = studentDao.loadAll();
        for (Student student : students){
            // Take the avatar image of the Student
            Mat avatarImage = Imgcodecs.imread(student.getAvatar());
            // Search for faces in the avatar image
            List<Mat> faceImages = ppF.getCroppedImage(avatarImage);
            if (faceImages != null && faceImages.size() == 1) {
                // Proceed if exactly one face has been detected
                Mat faceImage = faceImages.get(0);
                if (faceImage != null) {
                    // Get detected face rectangles
                    Rect[] faces = ppF.getFacesForRecognition();
                    if (faces != null && faces.length == 1) {
                        // Proceed if exactly one face rectangle exists
                        RecognitionThread recognitionThread = new RecognitionThread(tensorFlow, studentImageCollectionEventDao);
                        recognitionThread.setImg(faceImage);
                        Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages: recognitionThread will be started to recognize student: " + student.getUniqueId());
                        recognitionThread.start();
                        try {
                            recognitionThread.join();
                            Student recognizedStudent = recognitionThread.getStudent();
                            if (recognizedStudent != null){
                                Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages: The student " + student.getUniqueId() + " has been recognized as " + recognizedStudent.getUniqueId());
                                if (recognizedStudent.getUniqueId().equals(student.getUniqueId())){
                                    Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages: Merging will be skipped because the students are identical.");
                                } else {
                                    Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages: Merging will be started for student: " + student.getUniqueId() + " recognizedStudent: " + recognizedStudent.getUniqueId());
                                    mergeSimilarStudents(student, recognizedStudent);
                                }
                            } else {
                                Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages: The student " + student.getUniqueId() + " was not recognized");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Find similar students
     * Case 2: Student was added regularly but maybe on another tablet or due to some reason the authentication didn't recognize the student correctly in the numberOfTries
     * ---> Use the meanFeatureVector as input for the cosineSimilarityScore calculation
     * @param ppF
     * @param tensorFlow
     */
    private synchronized void findSimilarStudentsUsingMeanFeatureVector(PreProcessorFactory ppF, TensorFlow tensorFlow){
        Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector");
        // Iterate through all StudentImageCollectionEvents
        List<StudentImageCollectionEvent> studentImageCollectionEvents = studentImageCollectionEventDao.loadAll();
        for (StudentImageCollectionEvent studentImageCollectionEvent : studentImageCollectionEvents){
            // Take the meanFeatureVector of the StudentImageCollectionEvent
            List<Float> meanFeatureVectorList = gson.fromJson(studentImageCollectionEvent.getMeanFeatureVector(), new TypeToken<List<Float>>(){}.getType());
            Mat meanFeatureVector = Converters.vector_float_to_Mat(meanFeatureVectorList);
            RecognitionThread recognitionThread = new RecognitionThread(tensorFlow, studentImageCollectionEventDao);
            recognitionThread.setImg(meanFeatureVector);
            // To indicate, that this Mat object contains the already extracted features and therefore this step can be skipped in the RecognitionThread
            recognitionThread.setFeaturesAlreadyExtracted(true);
            Student student = studentImageCollectionEvent.getStudent();
            Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector: recognitionThread will be started to recognize student: " + student.getUniqueId());
            recognitionThread.start();
            try {
                recognitionThread.join();
                Student recognizedStudent = recognitionThread.getStudent();
                if (recognizedStudent != null){
                    Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector: The student " + student.getUniqueId() + " has been recognized as " + recognizedStudent.getUniqueId());
                    if (recognizedStudent.getUniqueId().equals(student.getUniqueId())){
                        Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector: Merging will be skipped because the students are identical.");
                    } else {
                        Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector: Merging will be started for student: " + student.getUniqueId() + " recognizedStudent: " + recognizedStudent.getUniqueId());
                        mergeSimilarStudents(student, recognizedStudent);
                    }
                } else {
                    Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector: The student " + student.getUniqueId() + " was not recognized");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Merge 2 students which have been found identical
     * @param student1
     * @param student2
     */
    private synchronized void mergeSimilarStudents(Student student1, Student student2){
        Log.i(getClass().getName(), "mergeSimilarStudents: student1: " + student1.getUniqueId() + " student2: " + student2.getUniqueId());
        // TODO Implement merging of students (maybe in another class like StudentMergeHelper)
    }
}
