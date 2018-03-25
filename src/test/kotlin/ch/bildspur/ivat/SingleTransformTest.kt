package ch.bildspur.ivat

import ch.bildspur.ivat.vision.SimplePerspectiveTransformer
import ch.bildspur.ivat.vision.copy
import ch.bildspur.ivat.vision.format
import ch.bildspur.ivat.vision.zeros
import org.junit.Before
import org.junit.Test
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs

class SingleTransformTest {

    @Before fun before()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    @Test fun basicTest() {
        // load two images
        val train = Imgcodecs.imread("data/reference_bw.jpg")
        val query = Imgcodecs.imread("data/original.jpg")

        transform(train, query, 20)
    }

    @Test fun bridgeTest() {
        // load two images
        val train = Imgcodecs.imread("data/test/bridge_reference.jpg")
        val query = Imgcodecs.imread("data/test/bridge_original.jpg")

        transform(train, query, 50)
    }

    @Test fun carTest() {
        // load two images
        val train = Imgcodecs.imread("data/test/car_reference.jpg")
        val query = Imgcodecs.imread("data/test/car_original.jpg")

        transform(train, query)
    }

    private fun transform(train : Mat, query : Mat, nTopFeatures : Int = 20)
    {
        val originalTrain = train.copy()
        val result = query.copy()
        val blend = train.zeros()

        val transformer = SimplePerspectiveTransformer(nTopFeatures)
        val matrix = transformer.detectTransformMatrix(train, query)
        transformer.transform(result, matrix)

        // create image blend
        Core.addWeighted(originalTrain, 0.5, result, 0.5, 0.0, blend)

        // calculate difference
        val similarity = measureSimilarity(originalTrain, result, 10.0)
        println("Similar: ${(similarity * 100.0).format(2)}%")

        // show images
        HighGui.imshow("train", train)
        HighGui.imshow("query", query)
        HighGui.imshow("result", result)
        HighGui.imshow("blend", blend)

        Imgcodecs.imwrite("data/result/reference.png", train)
        Imgcodecs.imwrite("data/result/original.png", query)
        Imgcodecs.imwrite("data/result/result.png", result)
        Imgcodecs.imwrite("data/result/blend.png", blend)

        HighGui.waitKey()
    }

    private fun measureSimilarity(original : Mat, copy : Mat, maxDelta : Double = 5.0) : Double
    {
        var similarityCounter = 0
        for(y in 0 until original.height())
        {
            for(x in 0 until original.width())
            {
                val originalColor = original[y, x][0]
                val copyColor = copy[y, x][0]

                if(Math.abs(originalColor - copyColor) < maxDelta)
                    similarityCounter++
            }
        }

        return similarityCounter.toDouble() / (original.width() * original.height())
    }
}