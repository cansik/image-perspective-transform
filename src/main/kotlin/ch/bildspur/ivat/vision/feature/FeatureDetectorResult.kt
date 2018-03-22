package ch.bildspur.ivat.vision.feature

import org.opencv.core.Mat
import org.opencv.core.MatOfKeyPoint

data class FeatureDetectorResult(val keypoints : MatOfKeyPoint, val descriptors : Mat)