package ch.bildspur.ivat.io

import ch.bildspur.ivat.util.imageAspect
import ch.bildspur.ivat.vision.draw
import com.leapmotion.leap.Controller
import com.leapmotion.leap.Image
import com.leapmotion.leap.processing.LeapMotion
import processing.core.PApplet
import processing.core.PConstants.RGB
import processing.core.PGraphics
import processing.core.PImage
import processing.video.Capture
import javax.swing.Spring.height
import kotlin.experimental.and


class ZedLeapResizedSource : ImageSource {
    private val zedWidth = 1104
    private val zedHeight = 621

    private val leapWidth = 640
    private val leapHeight = 480

    private val leapScale = 2.0f

    private lateinit var zed: Capture
    private lateinit var zedCanvas: PGraphics

    private lateinit var leap: LeapMotion
    private lateinit var leapCanvas: PGraphics
    private lateinit var leapResized: PGraphics
    private lateinit var leapImage: PImage

    override fun setup(parent: PApplet) {
        zedCanvas = parent.createGraphics(leapWidth, leapHeight)

        leapCanvas = parent.createGraphics(leapWidth, leapHeight)
        leapResized = parent.createGraphics(leapWidth, leapHeight)
        leapImage = parent.createImage(leapWidth, leapHeight / 2, RGB)


        // setup zed
        zed = Capture(parent, zedWidth * 2, zedHeight, "ZED #3", 30)
        zed.start()


        // setup leap
        leap = LeapMotion(parent)
    }

    override fun readReference(): PImage {
        if (leap.controller() == null)
            return leapCanvas

        val controller = leap.controller()!!
        controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES)
        val frame = controller.frame()

        if (frame.isValid && !frame.images().isEmpty) {
            val img = frame.images().first()
            img.toPImage(leapImage)
        }

        leapResized.draw {
            it.background(0f)
            it.image(leapImage, 0f - (640f * (leapScale - 1f)) / 2, 0f - (640f * (leapScale - 1f)) / 2, 640f * leapScale, 480f * leapScale)
        }

        leapCanvas.draw {
            it.background(0f)
            it.imageAspect(leapResized, 0f, 0f, it.width.toFloat(), it.height.toFloat())
        }

        return leapCanvas.get()
    }

    override fun readOriginal(): PImage {
        if (zed.available())
            zed.read()

        zedCanvas.draw {
            val ratio = (leapWidth * 2f) / zedWidth
            it.image(zed, 0f, 0f, zedWidth * ratio, zedHeight * ratio)
        }

        return zedCanvas.get()
    }

    fun Image.toPImage(img: PImage) {
        val imageData = this.data()

        for (i in 0 until this.width() * this.height()) {
            val r = imageData[i].toInt() and 0xFF shl 16
            val g = imageData[i].toInt() and 0xFF shl 8
            val b = imageData[i].toInt() and 0xFF
            img.pixels[i] = r or g or b
        }
        img.updatePixels()
    }
}