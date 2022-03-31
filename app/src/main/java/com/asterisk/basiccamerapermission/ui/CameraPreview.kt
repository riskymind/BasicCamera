package com.asterisk.basiccamerapermission.ui

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.asterisk.basiccamerapermission.ui.CameraPreviewActivity.Companion.TAG
import com.asterisk.basiccamerapermission.utils.calculatePreviewOrientation
import java.io.IOException


/**
 * Camera preview that displays a [Camera].
 */
class CameraPreview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val camera: Camera? = null,
    private val cameraInfo: Camera.CameraInfo? = null,
    private val displayOrientation: Int = 0,
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    init {
        // Do not initialise if no camera has been set
        if (camera != null && cameraInfo != null) {
            // install the SurfaceHolder.Callback
            holder.addCallback(this@CameraPreview)
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        // The Surface has been created. now tell the camera where to draw the preview
        try {
            camera?.run {
                setPreviewDisplay(holder)
                startPreview()
            }
            Log.d(TAG, "Camera Preview started")
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: ${e.message}")
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure you stop the preview before resizing or reformatting it.
        if (holder.surface == null) {
            // preview surface does not exists
            Log.d(TAG, "Preview surface does not exists")
            return
        }

        Log.d(TAG, "Preview stopped")
        camera?.run {
            // stop preview before making changes
            stopPreview()
            cameraInfo?.let {
                setDisplayOrientation(it.calculatePreviewOrientation(displayOrientation))
            }
            setPreviewDisplay(holder)
            startPreview()
            Log.d(TAG, "Camera preview started.")
        }
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }
}