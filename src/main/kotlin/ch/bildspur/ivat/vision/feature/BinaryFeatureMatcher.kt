package ch.bildspur.ivat.vision.feature

import org.opencv.core.Mat
import org.opencv.core.MatOfDMatch
import org.opencv.features2d.BFMatcher

class BinaryFeatureMatcher(val matcher : BFMatcher = BFMatcher.create()!!) {

    fun match(queryDescriptors: Mat, trainDescriptors : Mat) : BinaryFeatureMatchResult
    {
        val matches = MatOfDMatch()
        matcher.match(queryDescriptors, trainDescriptors, matches)
        return BinaryFeatureMatchResult(matches)
    }
}