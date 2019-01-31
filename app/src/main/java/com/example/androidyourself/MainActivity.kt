package com.example.androidyourself


import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions

class MainActivity : AppCompatActivity() {

    private lateinit var customImageView: CustomImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val androidButton: Button = findViewById(R.id.androidButton)
        androidButton.setOnClickListener{
            Log.i("Button", "Clicked")
        }
        customImageView = findViewById(R.id.customView)
        customImageView.setImageResource(R.drawable.hrithik)

        firebaseFacialDetection()

    }

    fun firebaseFacialDetection() {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build()

        val bm = BitmapFactory.decodeResource(resources, R.drawable.hrithik)

        val image = FirebaseVisionImage.fromBitmap(bm)

        val detector = FirebaseVision.getInstance()
            .getVisionFaceDetector(options)

        val result = detector.detectInImage(image)
            .addOnSuccessListener {faces ->
                for(face in faces){
                    val bounds = face.boundingBox
                    customImageView.face = face

                    //print out facial bounds
                    Log.i("Bounds bottom", bounds.bottom.toString())
                    Log.i("Bounds top", bounds.top.toString())
                    Log.i("Bounds left", bounds.left.toString())
                    Log.i("Bounds right", bounds.right.toString())
                    Log.i("Bounds height", bounds.height().toString())
                    Log.i("Bounds width", bounds.width().toString())

                }
            }
            .addOnFailureListener {
                Log.i("Main", "Failure")
            }
    }
}