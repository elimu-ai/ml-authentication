package org.literacyapp.authentication.thread;

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
    private PreProcessorFactory ppF;
    private TrainingThread trainingThread;
    private StudentDao studentDao;
    private StudentImageCollectionEventDao studentImageCollectionEventDao;
    private Gson gson;
    private MergeSimilarStudentsJobService mergeService;

    public MergeThread(MergeSimilarStudentsJobService mergeService){
        Context context = mergeService.getApplicationContext();
        trainingThread = new TrainingThread(context);
        ppF = new PreProcessorFactory(context);
        LiteracyApplication literacyApplication = (LiteracyApplication) context.getApplicationContext();
        DaoSession daoSession = literacyApplication.getDaoSession();
        studentDao = daoSession.getStudentDao();
        studentImageCollectionEventDao = daoSession.getStudentImageCollectionEventDao();
        gson = new Gson();
        this.mergeService = mergeService;
    }

    @Override
    public void run() {
        findAndMergeSimilarStudents();
        mergeService.jobFinished(mergeService.getJobParameters(), false);
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
                        recognitionThread.setStudent(student);
                        Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages: recognitionThread will be started to recognize student: " + student.getUniqueId());
                        recognitionThread.start();
                        try {
                            recognitionThread.join();
                            List<Student> recognizedStudents = recognitionThread.getRecognizedStudent();
                            if (recognizedStudents.size() > 0){
                                for (Student recognizedStudent : recognizedStudents){
                                    if (recognizedStudent != null){
                                        Log.i(getClass().getName(), "findSimilarStudentsUsingAvatarImages: The student " + student.getUniqueId() + " has been recognized as " + recognizedStudent.getUniqueId());
                                        mergeSimilarStudents(student, recognizedStudent);
                                    }
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
        // Iterate through all StudentImageCollectionEvents, where the Student is not null
        List<StudentImageCollectionEvent> studentImageCollectionEvents = studentImageCollectionEventDao.queryBuilder().where(StudentImageCollectionEventDao.Properties.StudentId.notEq(0)).list();
        Log.i(getClass().getName(), "studentImageCollectionEvents.size(): " + studentImageCollectionEvents.size());
        for (StudentImageCollectionEvent studentImageCollectionEvent : studentImageCollectionEvents){
            Student student = studentImageCollectionEvent.getStudent();
            // Take the meanFeatureVector of the StudentImageCollectionEvent
            List<Float> meanFeatureVectorList = gson.fromJson(studentImageCollectionEvent.getMeanFeatureVector(), new TypeToken<List<Float>>(){}.getType());
            Mat meanFeatureVector = Converters.vector_float_to_Mat(meanFeatureVectorList);
            RecognitionThread recognitionThread = new RecognitionThread(tensorFlow, studentImageCollectionEventDao);
            recognitionThread.setImg(meanFeatureVector);
            recognitionThread.setStudent(student);
            // To indicate, that this Mat object contains the already extracted features and therefore this step can be skipped in the RecognitionThread
            recognitionThread.setFeaturesAlreadyExtracted(true);
            Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector: recognitionThread will be started to recognize student: " + student.getUniqueId());
            recognitionThread.start();
            try {
                recognitionThread.join();
                List<Student> recognizedStudents = recognitionThread.getRecognizedStudent();
                if (recognizedStudents.size() > 0){
                    for (Student recognizedStudent : recognizedStudents){
                        if (recognizedStudent != null){
                            Log.i(getClass().getName(), "findSimilarStudentsUsingMeanFeatureVector: The student " + student.getUniqueId() + " has been recognized as " + recognizedStudent.getUniqueId());
                            mergeSimilarStudents(student, recognizedStudent);
                        }
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
