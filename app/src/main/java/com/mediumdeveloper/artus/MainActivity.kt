package com.mediumdeveloper.artus

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mediumdeveloper.artus.utils.ImageUtils
import java.io.OutputStream

//class MainActivity : AppCompatActivity() {
//
//    private lateinit var imageView: ImageView
//    private lateinit var originalBitmap: Bitmap
//    private var framedBitmap: Bitmap? = null
//    private var frameThickness = 0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//
//        imageView = findViewById(R.id.imageView)
//        val saveButton = findViewById<Button>(R.id.saveButton)
//        val frameSeekBar = findViewById<SeekBar>(R.id.frameSeekBar)
//        val percentageTextView = findViewById<TextView>(R.id.percentageTextView)
//
//        // Initially disable and hide the SeekBar until an image is uploaded
//        frameSeekBar.isEnabled = false
//        frameSeekBar.visibility = View.INVISIBLE
//        percentageTextView.visibility = View.INVISIBLE
//
//
//
//        // Load Image
//        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                originalBitmap = bitmap
//                imageView.setImageBitmap(originalBitmap)
//
//                // Enable and show SeekBar once an image is uploaded
//                frameSeekBar.isEnabled = true
//                frameSeekBar.visibility = View.VISIBLE
//                percentageTextView.visibility = View.VISIBLE
//                updateFrame() // Update frame after image upload
//            }
//        }
//
//        findViewById<Button>(R.id.uploadButton).setOnClickListener {
//            pickImage.launch("image/*")
//        }
//
//        // Adjust Frame Size
//        frameSeekBar.max = 500
//        frameSeekBar.progress = 0
//        frameSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                if (::originalBitmap.isInitialized) {
//                    frameThickness = progress
//                    updateFrame()
//                    val percentage = (progress * 100) / seekBar!!.max
//                    percentageTextView.text = "$percentage%" // Update the percentage TextView
//                } else {
//                    Toast.makeText(this@MainActivity, "Please upload an image first", Toast.LENGTH_SHORT).show()
//                    seekBar?.progress = 0
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        })
//
//
//
//        // Save to Gallery
//        saveButton.setOnClickListener {
//            framedBitmap?.let {
//                saveImageToGallery(it)
//            } ?: run {
//                Toast.makeText(this, "Please add a frame first", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//
//    private fun updateFrame() {
////        framedBitmap = addFrameToBitmap(originalBitmap, frameThickness)
////        imageView.setImageBitmap(framedBitmap)
//        framedBitmap = ImageUtils.addFrameToBitmap(originalBitmap, frameThickness)
//        imageView.setImageBitmap(framedBitmap)
//    }
//
////    private fun addFrameToBitmap(bitmap: Bitmap, frameThickness: Int): Bitmap {
////        val width = bitmap.width + frameThickness * 2
////        val height = bitmap.height + frameThickness * 2
////        val framedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
////        val canvas = Canvas(framedBitmap)
////
////        // Draw white background (frame)
////        canvas.drawColor(Color.WHITE)
////
////        // Draw the original image on top of the white background (frame)
////        canvas.drawBitmap(bitmap, frameThickness.toFloat(), frameThickness.toFloat(), null)
////
////        return framedBitmap
////    }
//
//
//    private fun saveImageToGallery(bitmap: Bitmap) {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, "framed_image_${System.currentTimeMillis()}.jpg")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                put(MediaStore.Images.Media.IS_PENDING, 1)
//            }
//        }
//
//        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        uri?.let {
//            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
//            outputStream?.use {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                contentValues.clear()
//                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
//                contentResolver.update(uri, contentValues, null, null)
//            }
//
//            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
//        }
//    }
//}

class MainActivity : AppCompatActivity() {


    private lateinit var imageView: ImageView
    private lateinit var originalBitmap: Bitmap
    private var framedBitmap: Bitmap? = null
    private var frameThickness = 0
    private var selectedAspectRatioWidth = 1
    private var selectedAspectRatioHeight = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val frameSeekBar = findViewById<SeekBar>(R.id.frameSeekBar)
        val percentageTextView = findViewById<TextView>(R.id.percentageTextView)

        // Initially disable and hide the controls until an image is uploaded
        frameSeekBar.isEnabled = false
        frameSeekBar.visibility = View.INVISIBLE
        percentageTextView.visibility = View.INVISIBLE

        // Replace the spinner setup with horizontal scrollable buttons
        val aspectRatioContainer = findViewById<LinearLayout>(R.id.aspectRatioContainer)
        val aspectRatios = arrayOf("Original", "1:1", "4:3", "3:2", "16:9", "9:16")
        val aspectRatioValues = arrayOf(
            Pair(0, 0),      // Original
            Pair(1, 1),      // 1:1
            Pair(4, 3),      // 4:3
            Pair(3, 2),      // 3:2
            Pair(16, 9),     // 16:9
            Pair(9, 16)      // 9:16
        )

        // Track the selected aspect ratio button
        var selectedAspectRatioButton: TextView? = null

        // Create aspect ratio buttons
        for (i in aspectRatios.indices) {
            val view = layoutInflater.inflate(R.layout.aspect_ratio_item, aspectRatioContainer, false)
            val textView = view.findViewById<TextView>(R.id.aspectRatioText)
            textView.text = aspectRatios[i]

            // Set initial selection (Original)
            if (i == 0) {
                textView.isSelected = true
                selectedAspectRatioButton = textView
            }

            textView.setOnClickListener {
                // Update selection state
                selectedAspectRatioButton?.isSelected = false
                textView.isSelected = true
                selectedAspectRatioButton = textView

                // Update aspect ratio values
                val aspectRatioPair = aspectRatioValues[i]
                selectedAspectRatioWidth = aspectRatioPair.first
                selectedAspectRatioHeight = aspectRatioPair.second

                // Update the image if one is loaded
                if (::originalBitmap.isInitialized) {
                    updateFrame()
                }
            }

            aspectRatioContainer.addView(view)
        }

        // Initially hide the aspect ratio container until an image is uploaded
        aspectRatioContainer.visibility = View.INVISIBLE

        // Load Image
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                originalBitmap = bitmap
                imageView.setImageBitmap(originalBitmap)

                // Enable and show controls once an image is uploaded
                frameSeekBar.isEnabled = true
                frameSeekBar.visibility = View.VISIBLE
                percentageTextView.visibility = View.VISIBLE
                aspectRatioContainer.visibility = View.VISIBLE
                updateFrame() // Update frame after image upload
            }
        }

        findViewById<Button>(R.id.uploadButton).setOnClickListener {
            pickImage.launch("image/*")
        }

        // Adjust Frame Size
        frameSeekBar.max = 500
        frameSeekBar.progress = 0
        frameSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (::originalBitmap.isInitialized) {
                    frameThickness = progress
                    updateFrame()
                    val percentage = (progress * 100) / seekBar!!.max
                    percentageTextView.text = "$percentage%" // Update the percentage TextView
                } else {
                    Toast.makeText(this@MainActivity, "Please upload an image first", Toast.LENGTH_SHORT).show()
                    seekBar?.progress = 0
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Save to Gallery
        saveButton.setOnClickListener {
            framedBitmap?.let {
                saveImageToGallery(it)
            } ?: run {
                Toast.makeText(this, "Please add a frame first", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateFrame() {
        framedBitmap = if (selectedAspectRatioWidth == 0 && selectedAspectRatioHeight == 0) {
            // Use original aspect ratio with frame
            ImageUtils.addFrameToBitmap(originalBitmap, frameThickness)
        } else {
            // Use selected aspect ratio with frame
            ImageUtils.addFrameWithAspectRatio(
                originalBitmap,
                frameThickness,
                selectedAspectRatioWidth,
                selectedAspectRatioHeight
            )
        }
        imageView.setImageBitmap(framedBitmap)
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "framed_image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }

            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
        }
    }
}
