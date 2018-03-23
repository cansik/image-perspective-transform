package ch.bildspur.ivat.vision.feature

import org.opencv.core.DMatch
import org.opencv.core.KeyPoint

data class KeyPointRectangle(private val initialKeyPoint: KeyPoint, private val initialMatch: DMatch) {
    private var topLeft: KeyPoint = initialKeyPoint // -x -y
    private var topRight: KeyPoint = initialKeyPoint // x -y
    private var bottomRight: KeyPoint = initialKeyPoint // x y
    private var bottomLeft: KeyPoint = initialKeyPoint // -x y

    var topLeftMatch: DMatch = initialMatch
    var topRightMatch: DMatch = initialMatch
    var bottomRightMatch: DMatch = initialMatch
    var bottomLeftMatch: DMatch = initialMatch

    val matches: List<DMatch>
        get() = listOf(topLeftMatch, topRightMatch, bottomRightMatch, bottomLeftMatch)

    fun addPoint(keyPoint: KeyPoint, match: DMatch) {
        // topleft
        if (keyPoint.pt.x < topLeft.pt.x && keyPoint.pt.y < topLeft.pt.y) {
            topLeft = keyPoint
            topLeftMatch = match
        }

        // topRight
        if (keyPoint.pt.x > topRight.pt.x && keyPoint.pt.y < topRight.pt.y) {
            topRight = keyPoint
            topRightMatch = match
        }

        // bottomRight
        if (keyPoint.pt.x > bottomRight.pt.x && keyPoint.pt.y > bottomRight.pt.y) {
            bottomRight = keyPoint
            bottomRightMatch = match
        }

        // bottomLeft
        if (keyPoint.pt.x < bottomLeft.pt.x && keyPoint.pt.y > bottomLeft.pt.y) {
            bottomLeft = keyPoint
            bottomLeftMatch = match
        }
    }
}