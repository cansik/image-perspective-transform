package ch.bildspur.ivat.live

class Live {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sketch = LiveSketch()
            sketch.run()
        }
    }
}