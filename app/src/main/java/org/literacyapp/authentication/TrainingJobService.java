package org.literacyapp.authentication;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import org.literacyapp.R;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.List;

import ch.zhaw.facerecognitionlibrary.Helpers.FileHelper;
import ch.zhaw.facerecognitionlibrary.Helpers.MatName;
import ch.zhaw.facerecognitionlibrary.PreProcessor.PreProcessorFactory;
import ch.zhaw.facerecognitionlibrary.Recognition.Recognition;
import ch.zhaw.facerecognitionlibrary.Recognition.RecognitionFactory;

/**
 * Created by sladomic on 25.09.16.
 */

public class TrainingJobService extends JobService {
    private Thread thread;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(getClass().getName(), "onStartJob");

        final Handler handler = new Handler(Looper.getMainLooper());
        thread = new Thread(new Runnable() {
            public void run() {
                if(!Thread.currentThread().isInterrupted()){
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    int N = Integer.valueOf(sharedPref.getString("key_N", "25"));
                    PreProcessorFactory ppF = new PreProcessorFactory(getApplicationContext(), N);
                    String algorithm = sharedPref.getString("key_classification_method", getResources().getString(R.string.eigenfaces));

                    FileHelper fileHelper = new FileHelper();
                    fileHelper.createDataFolderIfNotExsiting();
                    final File[] persons = fileHelper.getTrainingList();
                    if (persons.length > 0) {
                        Recognition rec = RecognitionFactory.getRecognitionAlgorithm(getApplicationContext(), Recognition.TRAINING, algorithm);
                        for (File person : persons) {
                            if (person.isDirectory()){
                                File[] files = person.listFiles();
                                int counter = 1;
                                for (File file : files) {
                                    if (FileHelper.isFileAnImage(file)){
                                        Mat imgRgb = Imgcodecs.imread(file.getAbsolutePath());
                                        Imgproc.cvtColor(imgRgb, imgRgb, Imgproc.COLOR_BGRA2RGBA);
                                        Mat processedImage = new Mat();
                                        imgRgb.copyTo(processedImage);
                                        List<Mat> images = ppF.getProcessedImage(processedImage);
                                        if (images == null || images.size() > 1) {
                                            // More than 1 face detected --> cannot use this file for training
                                            continue;
                                        } else {
                                            processedImage = images.get(0);
                                        }
                                        if (processedImage.empty()) {
                                            continue;
                                        }
                                        // The last token is the name --> Folder name = Person name
                                        String[] tokens = file.getParent().split("/");
                                        final String name = tokens[tokens.length - 1];

                                        MatName m = new MatName("processedImage", processedImage);
                                        fileHelper.saveMatToImage(m, FileHelper.DATA_PATH);

                                        rec.addImage(processedImage, name, false);

                                        counter++;
                                    }
                                }
                            }
                        }
                    } else {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        thread.start();

        boolean asynchronousProcessing = false;
        return asynchronousProcessing;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(getClass().getName(), "onStopJob");

        boolean restartJob = false;
        return restartJob;
    }
}
