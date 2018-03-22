package ch.bildspur.ivat

import ch.bildspur.ivat.io.ImageSource
import ch.bildspur.ivat.io.SingleImageSource
import ch.bildspur.ivat.util.ExponentialMovingAverage
import ch.bildspur.ivat.util.imageAdjusted
import ch.bildspur.ivat.vision.*
import processing.core.PApplet
import processing.core.PFont
import processing.core.PImage


class Sketch : PApplet() {

    private val source : ImageSource = SingleImageSource("data/reference_bw.jpg", "data/original.jpg")
    private val detector : PerspectiveTransformer = SimplePerspectiveTransformer()

    private val fpsAverage = ExponentialMovingAverage(0.1)
    private val imageSize = 400


    private lateinit var font : PFont

    override fun settings() {
        size(imageSize * 3, imageSize, FX2D)
    }

    override fun setup() {
        source.setup(this)
        detector.setup(this)

        // setup style
        font = createFont("Helvetica", 20f)
        textFont(font, 18f)

        noLoop()
    }

    override fun draw() {
        background(22f)

        // read data
        val referenceImage = source.readReference()
        val originalImage = source.readOriginal()

        // resize images
        referenceImage.resize(imageSize, 0)
        originalImage.resize(imageSize, 0)

        // do recognition
        val result = detector.transform(referenceImage.toMat(), originalImage.toMat())
        val resultImage = PImage(result.cols(), result.rows())
        result.toPImage(resultImage)

        // draw images
        g.image(referenceImage, 0f, 0f)
        g.image(originalImage, imageSize.toFloat(), 0f)
        g.image(resultImage, imageSize * 2f, 0f)

        // show fps
        fpsAverage += frameRate.toDouble()
        surface.setTitle("IVAT | FPS: ${frameRate.format(2)}\tAVG: ${fpsAverage.average.format(2)}\t")
    }

    override fun stop() {
    }

    fun run() {
        runSketch()
    }
}