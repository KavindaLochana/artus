package com.mediumdeveloper.artus.utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream

object ImageUtils {

    /**
     * Adds a white frame around the given bitmap.
     *
     * @param bitmap The original bitmap to which the frame will be added.
     * @param frameThickness The thickness of the frame.
     * @return A new bitmap with a frame added.
     */
    fun addFrameToBitmap(bitmap: Bitmap, frameThickness: Int): Bitmap {
        val width = bitmap.width + frameThickness * 2
        val height = bitmap.height + frameThickness * 2
        val framedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(framedBitmap)

        // Draw the white background (frame)
        canvas.drawColor(Color.WHITE)

        // Draw the original image inside the frame
        canvas.drawBitmap(bitmap, frameThickness.toFloat(), frameThickness.toFloat(), null)

        return framedBitmap
    }
}
