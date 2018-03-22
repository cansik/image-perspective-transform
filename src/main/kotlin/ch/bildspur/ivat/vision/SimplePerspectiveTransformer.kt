package ch.bildspur.ivat.vision

import org.opencv.core.Core
import org.opencv.core.Mat
import processing.core.PApplet

class SimplePerspectiveTransformer : PerspectiveTransformer {
    override fun setup(parent : PApplet) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    override fun transform(reference: Mat, original: Mat): Mat {
        return original
    }
}