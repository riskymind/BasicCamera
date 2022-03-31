package com.asterisk.basiccamerapermission.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.asterisk.basiccamerapermission.R
import com.asterisk.basiccamerapermission.databinding.ActivityMainBinding
import com.asterisk.basiccamerapermission.utils.Constants.PERMISSION_REQUEST_CAMERA
import com.asterisk.basiccamerapermission.utils.checkSelfPermissionCompact
import com.asterisk.basiccamerapermission.utils.requestPermissionsCompact
import com.asterisk.basiccamerapermission.utils.shouldShowRequestPermissionRationaleCompact
import com.asterisk.basiccamerapermission.utils.showSnackBar
import com.google.android.material.snackbar.Snackbar

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Register a listener for the 'Show Camera Preview' button.
        binding.btnOpenCamera.setOnClickListener { showCameraPreview() }

    }

    private fun showCameraPreview() {
        // check if the camera permission has been granted
        if (
            checkSelfPermissionCompact(Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already available, start camera preview
            binding.mainContainer.showSnackBar(R.string.camera_permission_available,
                Snackbar.LENGTH_LONG)
            showCamera()
        } else {
            // Permission is missing and must be request
            requestCameraPermission()
        }
    }

    /**
     *  Requests the [android.Manifest.permission.CAMERA] permission.
     */
    private fun requestCameraPermission() {
        // Permission has not been granted and must be requested
        if (shouldShowRequestPermissionRationaleCompact(Manifest.permission.CAMERA)) {
            binding.root.showSnackBar(R.string.camera_access_required,
                Snackbar.LENGTH_LONG,
                R.string.ok) {
                requestPermissionsCompact(arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA)
            }
        } else {
            binding.root.showSnackBar(R.string.camera_permission_not_available,
                Snackbar.LENGTH_SHORT)
            // Request the permission. The result will be received in OnRequestPermissionResult().
            requestPermissionsCompact(arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA)
        }
    }

    private fun showCamera() {
        val intent = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has be granted. Start camera preview Activity
                binding.mainContainer.showSnackBar(R.string.camera_permission_granted,
                    Snackbar.LENGTH_SHORT)
                showCamera()
            }
        } else {
            // Permission request was denied.
            binding.root.showSnackBar(R.string.camera_permission_denied, Snackbar.LENGTH_SHORT)
        }
    }

}