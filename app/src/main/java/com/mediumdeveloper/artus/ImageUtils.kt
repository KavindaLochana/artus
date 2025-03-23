package com.mediumdeveloper.artus.utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream
import androidx.core.graphics.createBitmap

//object ImageUtils {
//
//    /**
//     * Adds a white frame around the given bitmap.
//     *
//     * @param bitmap The original bitmap to which the frame will be added.
//     * @param frameThickness The thickness of the frame.
//     * @return A new bitmap with a frame added.
//     */
//    fun addFrameToBitmap(bitmap: Bitmap, frameThickness: Int): Bitmap {
//        val width = bitmap.width + frameThickness * 2
//        val height = bitmap.height + frameThickness * 2
//        val framedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
//        val canvas = Canvas(framedBitmap)
//
//        // Draw the white background (frame)
//        canvas.drawColor(Color.WHITE)
//
//        // Draw the original image inside the frame
//        canvas.drawBitmap(bitmap, frameThickness.toFloat(), frameThickness.toFloat(), null)
//
//        return framedBitmap
//    }
//}


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

    /**
     * Adds a white frame around the given bitmap with a specific aspect ratio.
     *
     * @param bitmap The original bitmap to be framed.
     * @param frameThickness The thickness of the frame.
     * @param aspectRatioWidth The width component of the aspect ratio.
     * @param aspectRatioHeight The height component of the aspect ratio.
     * @return A new bitmap with the specified aspect ratio and frame.
     */
    fun addFrameWithAspectRatio(
        bitmap: Bitmap,
        frameThickness: Int,
        aspectRatioWidth: Int,
        aspectRatioHeight: Int
    ): Bitmap {
        // Calculate the content area dimensions (without frame)
        val contentWidth: Int
        val contentHeight: Int

        // Determine the content dimensions based on aspect ratio
        if (aspectRatioWidth * bitmap.height > aspectRatioHeight * bitmap.width) {
            // Width is the limiting factor
            contentWidth = (bitmap.height * aspectRatioWidth) / aspectRatioHeight
            contentHeight = bitmap.height
        } else {
            // Height is the limiting factor
            contentWidth = bitmap.width
            contentHeight = (bitmap.width * aspectRatioHeight) / aspectRatioWidth
        }

        // Create a bitmap with the content dimensions plus frame thickness on all sides
        val totalWidth = contentWidth + frameThickness * 2
        val totalHeight = contentHeight + frameThickness * 2
        val framedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, bitmap.config)
        val canvas = Canvas(framedBitmap)

        // Draw the white background (frame)
        canvas.drawColor(Color.WHITE)

        // Calculate position to center the original image
        val left = frameThickness + (contentWidth - bitmap.width) / 2
        val top = frameThickness + (contentHeight - bitmap.height) / 2

        // Draw the original image centered inside the frame
        canvas.drawBitmap(bitmap, left.toFloat(), top.toFloat(), null)

        return framedBitmap
    }
}
