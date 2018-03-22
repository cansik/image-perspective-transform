package ch.bildspur.ivat.util

import processing.core.PGraphics
import processing.core.PImage

fun PGraphics.imageAdjusted(image : PImage, x : Float, y : Float, width : Float, height : Float) {
    var referenceLength = image.width.toFloat()
    var referenceBound = width

    // check if height is limiting
    if (image.width < image.height) {
        referenceLength = image.height.toFloat()
        referenceBound = height
    }

    val scaleFactor =  referenceBound / referenceLength
    this.image(image, x, y, width * scaleFactor, height * scaleFactor)
}