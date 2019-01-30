package com.example.androidyourself

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark



class CustomImageView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    // The detected face
    var face: FirebaseVisionFace? = null
        set(value) {
            field = value

            // Trigger redraw when a new detected face object is passed in
            postInvalidate()
        }

    // The preview width
    var previewWidth: Int? = null

    // The preview height
    var previewHeight: Int? = null

    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f

    // The glasses bitmap
    private val glassesBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (face != null) {
            Log.i("onDraw", "Drawing")
            // Create local variables here so they canot not be changed anywhere else
            val face = face
            val previewWidth = previewWidth
            val previewHeight = previewHeight

            if (face != null && canvas != null && previewWidth != null && previewHeight != null) {

                // Calculate the scale factor??
                widthScaleFactor = canvas.width.toFloat() / previewWidth.toFloat()
                heightScaleFactor = canvas.height.toFloat() / previewHeight.toFloat()

                Log.i("**width scale", widthScaleFactor.toString())
                Log.i("**height scale", heightScaleFactor.toString())

                drawGlasses(canvas, face)
            }
        }
        else{
            Log.i("onDraw", "not drawing")
        }

    }


    //try locating the eyes and put these glasses on them
    private fun drawGlasses(canvas: Canvas, face: FirebaseVisionFace) {
        Log.i("drawGlasses", "Drawing")
        val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)
        val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)

        if (leftEye != null && rightEye != null) {
            Log.i("**left eye", leftEye.position.toString())
            Log.i("**right eye", rightEye.position.toString())


            Log.i("**width: ", this.width.toString())
            Log.i("**height: ", this.height.toString())


            val paint = Paint()
            paint.color = Color.GREEN

//            private val glassesImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses)
//            val eyeRect = Rect(
//                leftEye.position.x.toInt() - 20,
//                leftEye.position.y.toInt() - 20,
//                rightEye.position.x.toInt() + 20,
//                rightEye.position.y.toInt() + 20)

            //draw a line to the eye
//           canvas.drawLine(0.toFloat(), 0.toFloat(), leftEyePixelX, leftEyePixelY, paint)

//           canvas.drawBitmap(glassesImage, null, eyeRect, null)

        }
    }

    //pixel conversions?
    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}