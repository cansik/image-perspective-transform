package ch.bildspur.ivat.io

import processing.core.PApplet
import processing.core.PImage

class SingleImageSource(val referenceImageName : String, val originalImageName : String) : ImageSource {
    private lateinit var referenceImage : PImage
    private lateinit var originalImage : PImage

    override fun setup(parent : PApplet) {
        referenceImage = parent.loadImage(referenceImageName)
        originalImage = parent.loadImage(originalImageName)
    }


    override fun readReference(): PImage {
        return referenceImage
    }

    override fun readOriginal(): PImage {
        return originalImage
    }
}