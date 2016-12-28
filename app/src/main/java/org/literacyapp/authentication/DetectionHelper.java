package org.literacyapp.authentication;

import org.literacyapp.authentication.animaloverlay.AnimalOverlay;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import ch.zhaw.facerecognitionlibrary.Helpers.MatOperation;

/**
 * Created by sladomic on 27.12.16.
 */

public class DetectionHelper {
    private static final Scalar RED_COLOR = new Scalar(255, 0, 0, 255);

    public static boolean isFaceInsideFrame(AnimalOverlay animalOverlay, Mat img, Rect face){
        if (animalOverlay != null){
            Point frameTopLeft = new Point(animalOverlay.getFrameStartX(), animalOverlay.getFrameStartY());
            Point frameBottomRight = new Point(animalOverlay.getFrameEndX(), animalOverlay.getFrameEndY());
            Rect frame = new Rect(frameTopLeft, frameBottomRight);
            if (face.tl().x >= frame.tl().x && face.tl().y >= frame.tl().y && face.br().x <= frame.br().x && face.br().y <= frame.br().y){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void drawArrowFromFaceToFrame(AnimalOverlay animalOverlay, Mat img, Rect face){
        Rect mirroredFace = MatOperation.getMirroredFaceForFrontCamera(img, face);
        Point pointFace = new Point(mirroredFace.tl().x + mirroredFace.width / 2, mirroredFace.tl().y + mirroredFace.height / 2);
        Point pointFrame = new Point(animalOverlay.getFrameStartX() + (animalOverlay.getFrameEndX() - animalOverlay.getFrameStartX()) / 2, animalOverlay.getFrameStartY() + (animalOverlay.getFrameEndY() - animalOverlay.getFrameStartY()) / 2);
        Imgproc.arrowedLine(img, pointFace, pointFrame, RED_COLOR, 20, Imgproc.LINE_8, 0, 0.2);
    }
}
