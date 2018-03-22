package ch.bildspur.ivat.io

import processing.core.PApplet
import processing.core.PImage

interface ImageSource {
    fun setup(parent : PApplet)

    fun readReference() : PImage
    fun readOriginal() : PImage
}