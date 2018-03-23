package ch.bildspur.ivat.vision

import org.opencv.core.Mat
import processing.core.PApplet

interface PerspectiveTransformer {
    fun setup(parent : PApplet)
    fun transform(query : Mat, train : Mat) : Mat
}