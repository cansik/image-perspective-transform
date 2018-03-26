package ch.bildspur.ivat.io

import processing.core.PApplet
import processing.core.PImage

class SingleImageSource(private val queryName : String, private val trainName : String) : ImageSource {
    private lateinit var queryImage : PImage
    private lateinit var trainImage : PImage

    override fun setup(parent : PApplet) {
        this.queryImage = parent.loadImage(this.queryName)
        this.trainImage = parent.loadImage(this.trainName)
    }


    override fun readReference(): PImage {
        return this.queryImage
    }

    override fun readOriginal(): PImage {
        return this.trainImage
    }
}