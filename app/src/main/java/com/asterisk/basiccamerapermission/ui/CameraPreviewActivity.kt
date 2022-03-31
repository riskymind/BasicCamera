package com.asterisk.basiccamerapermission.ui


import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import com.asterisk.basiccamerapermission.R
import com.asterisk.basiccamerapermission.utils.Constants.CAMERA_ID
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 *  Display a [CameraPreview] of the first [Camera]
 *  an Error message is displayed if the camera is not available
 */
class CameraPreviewActivity : AppCompatActivity() {

    private var camera: Camera? = null
    private var cameraInfo: Camera.CameraInfo? = null

    companion object {
        const val TAG = "CameraPreviewActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Open an instance of the first camera and retrieve its info.
        camera = getCameraInstance(CAMERA_ID)
        cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(CAMERA_ID, cameraInfo)
    }

    override fun onResume() {
        super.onResume()

        if (camera == null) {
            // Camera not available, display error message
            setContentView(R.layout.activity_camera_unavailable)
        } else {
            setContentView(R.layout.activity_camera)

            // Get the rotation of the screen to adjust the preview image accordingly.
            val displayRotation = windowManager.defaultDisplay.rotation

            // Create the preview view and set it as the content of this Activity
            val cameraPreview = CameraPreview(this, null, 0, camera, cameraInfo, displayRotation)
            findViewById<FrameLayout>(R.id.camera_preview).addView(cameraPreview)

            val captureButton = findViewById<Button>(R.id.button_capture)
            captureButton.setOnClickListener {
                camera?.takePicture(null, null, mPicture)
            }
        }

    }

    private fun getCameraInstance(cameraId: Int): Camera? {
        var camera: Camera? = null
        try {
            camera = Camera.open(cameraId) // attempt to get camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or doesn't exits)
            Log.e(TAG, "Camera $cameraId is not available: ${e.message}")
        }
        return camera // return null if camera is unavailable
    }

    override fun onPause() {
        super.onPause()
        // Stop camera access
        releaseCamera()
    }

    private fun releaseCamera() {
        camera?.release()
        camera = null
    }


    private val mPicture = Camera.PictureCallback { bytes, camera ->
//        val pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
//            Log.d(TAG, ("Error creating media file, check storage permissions"))
//            return@PictureCallback
//        }

        try {
            val fos = FileOutputStream("pictureFile")
            fos.write(bytes)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: ${e.message}")
        }
    }
}