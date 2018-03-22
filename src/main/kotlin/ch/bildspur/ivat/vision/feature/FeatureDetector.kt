package ch.bildspur.ivat.vision.feature

import ch.bildspur.ivat.vision.to8UC3
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfKeyPoint
import org.opencv.core.Scalar
import org.opencv.features2d.Feature2D

class FeatureDetector(val detector: Feature2D) {
    fun detect(image : Mat) : FeatureDetectorResult
    {
        val keypoints = MatOfKeyPoint()
        val descriptors = Mat()
        val mask = Mat(image.size(), CvType.CV_8UC1, Scalar(255.0))

        detector.detectAndCompute(image.to8UC3(), mask, keypoints, descriptors)
        return FeatureDetectorResult(keypoints, descriptors)
    }
}