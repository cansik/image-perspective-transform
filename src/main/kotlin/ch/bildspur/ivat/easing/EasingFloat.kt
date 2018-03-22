package ch.bildspur.ivat.easing

class EasingFloat(var alpha: Float = 0.1f) : EasingObject {
    @Volatile var value: Float = 0f
    @Volatile var target: Float = 0f

    override fun update() {
        value += (target - value) * alpha
    }
}