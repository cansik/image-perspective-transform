package ch.bildspur.ivat.vision

import org.opencv.core.Mat
import processing.core.PApplet

interface PerspectiveTransformer {
    fun detectTransformMatrix(query : Mat, train : Mat) : Mat
    fun transform(image : Mat, transformMatrix : Mat)
}