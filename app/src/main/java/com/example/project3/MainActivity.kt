package com.example.project3

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val button = findViewById<Button>(R.id.button)
        dispatchTakePictureIntent()
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val matrix = Matrix ()
            matrix.postRotate( 270F)

            val rotatedBitmap = Bitmap.createBitmap(
                imageBitmap, // source bitmap
                0, // x coordinate of the first pixel in source
                0, // y coordinate of the first pixel in source
                imageBitmap.width, // The number of pixels in each row
                imageBitmap.height, // The number of rows
                matrix, // Optional matrix to be applied to the pixels
                false // true if the source should be filtered
            )
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(rotatedBitmap)

            val image = InputImage.fromBitmap(rotatedBitmap, 0)
            // To use default options:
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    findViewById<TextView>(R.id.textView).text = labels.subList(0,5).toString()
                    // Task completed successfully
                    // ...
                }
                .addOnFailureListener { e ->
                    findViewById<TextView>(R.id.textView).text = e.toString()
                    // Task failed with an exception
                    // ...
                }


        }
    }
}