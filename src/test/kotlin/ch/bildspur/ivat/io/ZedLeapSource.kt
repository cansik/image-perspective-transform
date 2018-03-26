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


class ZedLeapSource : ImageSource {
    private val zedWidth = 1104
    private val zedHeight = 621

    private val leapWidth = 640
    private val leapHeight = 240

    private lateinit var zed : Capture
    private lateinit var zedCanvas : PGraphics

    private lateinit var leap : LeapMotion
    private lateinit var leapCanvas : PGraphics
    private lateinit var leapImage : PImage

    override fun setup(parent: PApplet) {
        zedCanvas = parent.createGraphics(zedWidth, zedHeight)
        leapCanvas = parent.createGraphics(zedWidth, zedHeight)
        leapImage = parent.createImage(640, 240, RGB)


        // setup zed
        zed = Capture(parent, zedWidth * 2, zedHeight, "ZED #3", 30)
        zed.start()


        // setup leap
        leap = LeapMotion(parent)
    }

    override fun readReference(): PImage {
        if(leap.controller() == null)
            return leapCanvas

        leapCanvas.draw {
            val controller = leap.controller()!!
            controller.setPolicy(Controller.PolicyFlag.POLICY_IMAGES)
            val frame = controller.frame()

            if(frame.isValid && !frame.images().isEmpty)
            {
                val img = frame.images().first()
                img.toPImage(leapImage)
            }

            it.background(0f)
            //it.image(leapImage, 0f, 0f, 640f, 480f)
            it.imageAspect(leapImage, 0f, 0f, it.width.toFloat(), it.height.toFloat())
        }

        return leapCanvas.get()
    }

    override fun readOriginal(): PImage {
        if(zed.available())
            zed.read()

        zedCanvas.draw {
            it.image(zed, 0f, 0f)
        }

        return zedCanvas.get()
    }

    fun Image.toPImage(img : PImage)
    {
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