package com.bunbeauty.wtfclouds

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Toast
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class MainActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    lateinit var cameraBridgeViewBase: CameraBridgeViewBase
    lateinit var baseLoaderCallback: BaseLoaderCallback
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraBridgeViewBase = findViewById(R.id.MainCamera)
        cameraBridgeViewBase.visibility = SurfaceView.VISIBLE
        cameraBridgeViewBase.setCvCameraViewListener(this)

        baseLoaderCallback = object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                super.onManagerConnected(status)

                when (status) {
                    BaseLoaderCallback.SUCCESS -> cameraBridgeViewBase.enableView()
                    else -> super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onCameraViewStopped() {

    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        var frame: Mat = inputFrame?.rgba() ?: Mat()

        if (counter % 2 == 0) {
            Core.flip(frame, frame, 1)
            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY)
        }
        counter++

        return frame
    }

    override fun onCameraViewStarted(width: Int, height: Int) {

    }

    @SuppressLint("ShowToast")
    @Override
    override fun onResume() {
        super.onResume()

        if(!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "Problem", Toast.LENGTH_SHORT)
        } else {
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS)
        }
    }

    @Override
    override fun onPause() {
        super.onPause()

        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView()
        }
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()

        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView()
        }
    }
}
