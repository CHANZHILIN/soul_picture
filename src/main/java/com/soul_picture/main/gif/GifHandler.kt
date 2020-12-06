package com.soul_picture.main.gif

import android.graphics.Bitmap

class GifHandler {
    var longGifHandler: Long = -1L

    companion object {
        init {
            System.loadLibrary("native-gif-lib")
        }
    }

    constructor()
    constructor(gifHandler: Long) {
        this.longGifHandler = gifHandler
    }

    fun load(path: String): GifHandler {
        val gifHandler = loadGif(path)
        return GifHandler(gifHandler)
    }

    fun updateFrame(bitmap: Bitmap): Int = updateFrame(bitmap, longGifHandler)

    fun getWidth(): Int = getWidth(longGifHandler)
    fun getHeight(): Int = getHeight(longGifHandler)

    private external fun loadGif(path: String): Long

    private external fun getWidth(longGifHandler: Long): Int
    private external fun getHeight(longGifHandler: Long): Int

    private external fun updateFrame(bitmap: Bitmap, longGifHandler: Long): Int
}