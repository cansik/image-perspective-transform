package ch.bildspur.ivat.vision

import ch.bildspur.ivat.vision.feature.FeatureDetector
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfKeyPoint
import org.opencv.core.Scalar
import org.opencv.features2d.Feature2D
import org.opencv.features2d.Features2d
import org.opencv.features2d.ORB
import org.opencv.features2d.ORB.HARRIS_SCORE
import org.opencv.imgproc.Imgproc
import processing.core.PApplet

class SimplePerspectiveTransformer : PerspectiveTransformer {

    lateinit var detector : FeatureDetector

    override fun setup(parent : PApplet) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

        // setup orb
        detector = FeatureDetector(
                ORB.create(500, 1.2f, 8, 31, 0, 2, HARRIS_SCORE, 31, 20))
    }

    override fun transform(reference: Mat, original: Mat): Mat {
        // prepare input
        Imgproc.cvtColor(original, original, Imgproc.COLOR_BGR2GRAY)

        // detect features
        val referenceFeatures = detector.detect(reference)
        val originalFeatures = detector.detect(original)

        val result = original.copy()
        Features2d.drawKeypoints(original.to8UC3(), originalFeatures.keypoints, result, Scalar(255.0, 0.0, 0.0), 0)

        println("Reference features: ${referenceFeatures.keypoints.rows()}")
        println("Original features: ${originalFeatures.keypoints.rows()}")



        return result
    }
}