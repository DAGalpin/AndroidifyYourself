package com.example.androidyourself

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark



class CustomImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {

    // The detected face
    var face: FirebaseVisionFace? = null
        set(value) {
            field = value

            // Trigger redraw when a new detected face object is passed in
            postInvalidate()
        }

    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f

    // The glasses bitmap
    private val glassesBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (face != null && canvas != null) {
            drawGlasses(canvas, face!!)
        }
        else{
            Log.i("onDraw", "not drawing")
        }

    }


    //try locating the eyes and put these glasses on them
    private fun drawGlasses(canvas: Canvas, face: FirebaseVisionFace) {
        Log.i("drawGlasses", "Drawing")

        Log.i("onDraw", "Drawing")
        // super messy calculation for this.  What the ImageView will do is center the image and maintain the
        // aspect ratio by default
        val imageHeight = drawable.intrinsicHeight.toFloat()
        val imageWidth = drawable.intrinsicWidth.toFloat()

        val canvasHeight = canvas.height.toFloat()
        val canvasWidth = canvas.width.toFloat()

        val imageAdjustedWidth: Float
        val imageAdjustedHeight: Float
        val imageHorizontalOffset: Float
        val imageVerticalOffset: Float
        val scaleFactor: Float
        if ( canvasHeight/imageHeight > canvasWidth/imageWidth ) {
            // we're going to center horizontally
            imageAdjustedWidth = canvasWidth
            imageHorizontalOffset = 0f
            scaleFactor = canvasWidth/imageWidth
            imageAdjustedHeight = scaleFactor*imageHeight
            imageVerticalOffset = (canvasHeight-imageAdjustedHeight)/2f
        } else {
            imageAdjustedHeight = canvasHeight
            imageVerticalOffset = 0f
            scaleFactor = canvasHeight/imageHeight
            imageAdjustedWidth = scaleFactor*imageHeight
            imageHorizontalOffset = (canvasWidth-imageAdjustedWidth)/2f
        }

        val bounds = RectF(imageHorizontalOffset,imageVerticalOffset,imageHorizontalOffset+imageAdjustedWidth,
            imageVerticalOffset+imageAdjustedHeight)

        val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)
        val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)

        if (leftEye != null && rightEye != null) {
            Log.i("**left eye", leftEye.position.toString())
            Log.i("**right eye", rightEye.position.toString())


            Log.i("**width: ", this.width.toString())
            Log.i("**height: ", this.height.toString())


            val paint = Paint()
            paint.color = Color.GREEN

//            canvas.drawRect(bounds, paint)

           val glassesImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses)
           val eyeRect = RectF(
                leftEye.position.x*scaleFactor+imageHorizontalOffset,
                leftEye.position.y*scaleFactor+imageVerticalOffset,
                rightEye.position.x*scaleFactor+imageHorizontalOffset,
                rightEye.position.y*scaleFactor+imageVerticalOffset)

            //draw a line to the eye
//           canvas.drawLine(0.toFloat(), 0.toFloat(), leftEyePixelX, leftEyePixelY, paint)

           canvas.drawBitmap(glassesImage, null, eyeRect, null)

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