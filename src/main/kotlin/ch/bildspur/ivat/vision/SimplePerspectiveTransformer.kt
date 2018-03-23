package ch.bildspur.ivat.vision

import ch.bildspur.ivat.vision.feature.BinaryFeatureMatcher
import ch.bildspur.ivat.vision.feature.FeatureDetector
import ch.bildspur.ivat.vision.feature.FeatureDetectorResult
import ch.bildspur.ivat.vision.feature.KeyPointRectangle
import org.opencv.core.*
import org.opencv.features2d.Features2d
import org.opencv.features2d.ORB
import org.opencv.features2d.ORB.HARRIS_SCORE
import org.opencv.imgproc.Imgproc
import processing.core.PApplet
import org.opencv.core.Scalar



class SimplePerspectiveTransformer(private val nTopFeatures: Int = 20) : PerspectiveTransformer {

    private val detector: FeatureDetector
    private val matcher: BinaryFeatureMatcher

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

        // setup detector and matcher
        detector = FeatureDetector(
                ORB.create(500, 1.2f, 8, 31, 0, 2, HARRIS_SCORE, 31, 20))
        matcher = BinaryFeatureMatcher()
    }

    override fun detectTransformMatrix(query: Mat, train: Mat): Mat {
        // prepare input
        Imgproc.cvtColor(query, query, Imgproc.COLOR_BGRA2GRAY)
        Imgproc.cvtColor(train, train, Imgproc.COLOR_BGRA2GRAY)

        // detect features
        val queryFeatures = detector.detect(query)
        val trainFeatures = detector.detect(train)

        // draw keypoints
        Features2d.drawKeypoints(query.to8UC3(), queryFeatures.keypoints, query, Scalar(0.0, 0.0, 255.0), 0)
        Features2d.drawKeypoints(train.to8UC3(), trainFeatures.keypoints, train, Scalar(0.0, 0.0, 255.0), 0)

        println("Reference features: ${queryFeatures.keypoints.rows()}")
        println("Original features: ${trainFeatures.keypoints.rows()}")

        // draw refrence cross
        query.drawCross(query.imageCenter(), 50.0, Scalar(255.0, 0.0, 0.0))
        train.drawCross(train.imageCenter(), 50.0, Scalar(255.0, 0.0, 0.0))

        // match features and sort by distance
        val matches = matcher.match(queryFeatures.descriptors, trainFeatures.descriptors).matches.toList()
        matches.sortBy { it.distance }

        // select 4 best matching features points for perspective transformation
        val queryKeyPoints = queryFeatures.keypoints.toList()
        val trainKeyPoints = trainFeatures.keypoints.toList()

        val rectangle = findTransformationPoints(matches.take(nTopFeatures), queryKeyPoints)

        // show selected keypoints
        rectangle.matches.forEach{
            query.drawCircle(queryKeyPoints[it.queryIdx].pt, 10, Scalar(0.0, 255.0, 0.0), 2)
            train.drawCircle(trainKeyPoints[it.trainIdx].pt, 10, Scalar(0.0, 255.0, 0.0), 2)
        }

        // create perspective transformation
        val queryRect = MatOfPoint2f(*rectangle.matches.map { queryKeyPoints[it.queryIdx].pt }.toTypedArray())
        val trainRect = MatOfPoint2f(*rectangle.matches.map { trainKeyPoints[it.trainIdx].pt }.toTypedArray())

        return Imgproc.getPerspectiveTransform(trainRect, queryRect)
    }

    override fun transform(image : Mat, transformMatrix : Mat)
    {
        Imgproc.warpPerspective(image, image, transformMatrix, image.size())
    }


    private fun findTransformationPoints(matches: List<DMatch>, queryFeatures: List<KeyPoint>): KeyPointRectangle {
        val rectangle = KeyPointRectangle(queryFeatures[matches.first().queryIdx], matches.first())
        matches.forEach { rectangle.addPoint(queryFeatures[it.queryIdx], it) }
        return rectangle
    }
}