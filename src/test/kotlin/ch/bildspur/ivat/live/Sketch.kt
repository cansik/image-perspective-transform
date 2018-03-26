package ch.bildspur.ivat.live

import ch.bildspur.ivat.io.ImageSource
import ch.bildspur.ivat.io.SingleImageSource
import ch.bildspur.ivat.io.ZedLeapSource
import ch.bildspur.ivat.util.ExponentialMovingAverage
import ch.bildspur.ivat.util.imageAspect
import ch.bildspur.ivat.vision.*
import processing.core.PApplet
import processing.core.PFont


class LiveSketch : PApplet() {
    private val source : ImageSource = ZedLeapSource()
    private val detector : PerspectiveTransformer = SimplePerspectiveTransformer()

    private val fpsAverage = ExponentialMovingAverage(0.1)
    private val imageSize = 600


    private lateinit var font : PFont

    override fun settings() {
        size(imageSize * 3, imageSize, FX2D)
    }

    override fun setup() {
        source.setup(this)

        // setup style
        font = createFont("Helvetica", 20f)
        textFont(font, 18f)
    }

    override fun draw() {
        background(22f)

        if(frameCount < 5)
            return

        // read data
        val queryImage = source.readReference()
        val trainImage = source.readOriginal()

        /*
        image(queryImage, 0f, 0f)
        g.imageAspect(trainImage, imageSize.toFloat(), 0f, imageSize.toFloat(), imageSize.toFloat())
        */

        // create images for opencv
        val queryMat = queryImage.toMat()
        val trainMat = trainImage.toMat()
        val resultMat = trainImage.toMat()

        // do recognition
        if(frameCount > 10) {
            val transformMatrix = detector.detectTransformMatrix(queryMat, trainMat)
            detector.transform(resultMat, transformMatrix)
        }

        // draw images
        g.imageAspect(queryMat.toPImage(), 0f, 0f, imageSize.toFloat(), imageSize.toFloat())
        g.imageAspect(trainMat.toPImage(), imageSize.toFloat(), 0f, imageSize.toFloat(), imageSize.toFloat())
        g.imageAspect(resultMat.toPImage(), imageSize * 2f, 0f, imageSize.toFloat(), imageSize.toFloat())

        // show fps
        fpsAverage += frameRate.toDouble()
        surface.setTitle("IVAT Live | FPS: ${frameRate.format(2)}\tAVG: ${fpsAverage.average.format(2)}\t")
    }

    override fun stop() {
    }

    fun run() {
        runSketch()
    }
}