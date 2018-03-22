package ch.bildspur.ivat.vision

import ch.bildspur.ivat.vision.feature.BinaryFeatureMatcher
import ch.bildspur.ivat.vision.feature.FeatureDetector
import ch.bildspur.ivat.vision.feature.FeatureDetectorResult
import org.opencv.core.Core
import org.opencv.core.DMatch
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.features2d.Features2d
import org.opencv.features2d.ORB
import org.opencv.features2d.ORB.HARRIS_SCORE
import org.opencv.imgproc.Imgproc
import processing.core.PApplet

class SimplePerspectiveTransformer(private val nTopFeatures: Int = 20) : PerspectiveTransformer {

    lateinit var detector : FeatureDetector
    lateinit var matcher : BinaryFeatureMatcher

    override fun setup(parent : PApplet) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

        // setup detector and matcher
        detector = FeatureDetector(
                ORB.create(500, 1.2f, 8, 31, 0, 2, HARRIS_SCORE, 31, 20))
        matcher = BinaryFeatureMatcher()
    }

    override fun transform(query: Mat, train: Mat): Mat {
        // prepare input
        Imgproc.cvtColor(train, train, Imgproc.COLOR_BGR2GRAY)

        // detect features
        val queryFeatures = detector.detect(query)
        val trainFeatures = detector.detect(train)

        val result = train.copy()
        Features2d.drawKeypoints(train.to8UC3(), trainFeatures.keypoints, result, Scalar(255.0, 0.0, 0.0), 0)

        println("Reference features: ${queryFeatures.keypoints.rows()}")
        println("Original features: ${trainFeatures.keypoints.rows()}")

        // match features and sort by distance
        val matches = matcher.match(queryFeatures.descriptors, trainFeatures.descriptors).matches.toList()
        matches.sortBy { it.distance }

        // select 4 best matching features points for perspective transformation
        matches.take(nTopFeatures)

        return result
    }

    /*
    private fun findTransformationPoints(matches : List<DMatch>, queryFeatures : FeatureDetectorResult, trainFeatures : FeatureDetectorResult) :
    {

    }
    */
}