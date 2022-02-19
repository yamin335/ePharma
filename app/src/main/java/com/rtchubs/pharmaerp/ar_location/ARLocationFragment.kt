package com.rtchubs.pharmaerp.ar_location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.ar.core.*
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.databinding.ARLocationFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.util.showWarningToast
import com.rtchubs.pharmaerp.util.toRounded
import uk.co.appoly.arcorelocation.LocationMarker
import uk.co.appoly.arcorelocation.LocationScene
import uk.co.appoly.arcorelocation.rendering.LocationNodeRender
import uk.co.appoly.arcorelocation.utils.ARLocationPermissionHelper
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import com.rtchubs.pharmaerp.R

class ARLocationFragment : BaseFragment<ARLocationFragmentBinding, ARLocationViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_ar_location

    override val viewModel: ARLocationViewModel by viewModels { viewModelFactory }

    private val TAG: String = ARLocationFragment::class.java.simpleName

    private var installRequested = false
    private var hasFinishedLoading = false

    // Renderables for this example
    private var exampleLayoutRenderable: ViewRenderable? = null

    // Our ARCore-Location scene

    private var locationScene: LocationScene? = null

    private var systemUIConfigurationBackup: Int = 0

    private val args: ARLocationFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideSystemUI()
    }

    override fun onDetach() {
        super.onDetach()
        restoreSystemUI()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.back.setOnClickListener {
            navController.popBackStack()
        }

        // Build a renderable from a 2D View.
        val exampleLayout: CompletableFuture<ViewRenderable> = ViewRenderable.builder()
            .setView(requireContext(), R.layout.example_layout)
            .build()

        CompletableFuture.allOf(
            exampleLayout
        ).handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                // When you build a Renderable, Sceneform loads its resources in the background while
                // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
                // before calling get().
                if (throwable != null) {
                    DemoUtils.displayError(
                        requireContext(),
                        "Unable to load renderables",
                        throwable
                    )
                    return@handle null
                }
                try {
                    exampleLayoutRenderable = exampleLayout.get()
                    hasFinishedLoading = true
                } catch (ex: InterruptedException) {
                    DemoUtils.displayError(requireContext(), "Unable to load renderables", ex)
                } catch (ex: ExecutionException) {
                    DemoUtils.displayError(requireContext(), "Unable to load renderables", ex)
                }
                null
            }

        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.
        viewDataBinding.arSceneView
            .scene
            .addOnUpdateListener { frameTime: FrameTime? ->
                if (!hasFinishedLoading) {
                    return@addOnUpdateListener
                }
                if (locationScene == null) {
                    // If our locationScene object hasn't been setup yet, this is a good time to do it
                    // We know that here, the AR components have been initiated.
                    locationScene = LocationScene(
                        requireActivity(),
                        viewDataBinding.arSceneView
                    )

                    // Now lets create our location markers.
                    // First, a layout
                    val layoutLocationMarker = LocationMarker(
                        args.location.long,
                        args.location.lat,
                        getExampleView()
                    )

                    // An example "onRender" event, called every frame
                    // Updates the layout with the markers distance
                    layoutLocationMarker.renderEvent =
                        LocationNodeRender { node ->
                            val eView = exampleLayoutRenderable?.view
                            val shopName = eView?.findViewById<TextView>(R.id.shopName)
                            shopName?.text = args.location.placeName
                            val distanceView = eView?.findViewById<TextView>(R.id.distance)
                            val distance = node.distance

                            if (distance >= 1000) {
                                var dist = distance.toDouble() / 1000
                                dist = dist.toRounded(2)
                                distanceView?.text = "$dist KM"
                            } else {
                                distanceView?.text = node.distance.toString() + " M"
                            }
                        }
                    // Adding the marker
                    locationScene?.mLocationMarkers?.add(layoutLocationMarker)
                }
                val frame = viewDataBinding.arSceneView.arFrame ?: return@addOnUpdateListener
                if (frame.camera.trackingState != TrackingState.TRACKING) {
                    return@addOnUpdateListener
                }
                if (locationScene != null) {
                    locationScene?.processFrame(frame)
                }
                for (plane in frame.getUpdatedTrackables(
                    Plane::class.java
                )) {
                    if (plane.trackingState == TrackingState.TRACKING) {
                        hideLoadingMessage()
                    }
                }
            }

        // Lastly request CAMERA & fine location permission which is required by ARCore-Location.
        ARLocationPermissionHelper.requestPermission(requireActivity())
    }

    /**
     * Example node of a layout
     *
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getExampleView(): Node {
        val base = Node()
        base.renderable = exampleLayoutRenderable
        val c: Context = requireContext()
        // Add  listeners etc here
        val eView = exampleLayoutRenderable!!.view
        eView.setOnTouchListener { v: View?, event: MotionEvent? ->
            Toast.makeText(
                c, "Location marker touched.", Toast.LENGTH_LONG
            )
                .show()
            false
        }
        return base
    }

    override fun onResume() {
        super.onResume()

        // ARCore requires camera permissions to operate. If we did not yet obtain runtime
        // permission on Android M and above, now is a good time to ask the user for it.
        if (ARLocationPermissionHelper.hasPermission(requireActivity())) {

        } else {
            ARLocationPermissionHelper.requestPermission(requireActivity())
        }

        if (locationScene != null) {
            locationScene?.resume()
        }

        if (viewDataBinding.arSceneView.session == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                val session = DemoUtils.createArSession(requireActivity(), installRequested)
                if (session == null) {
                    installRequested = ARLocationPermissionHelper.hasPermission(requireActivity())
                    return
                } else {
                    viewDataBinding.arSceneView.setupSession(session)
                }
            } catch (e: UnavailableException) {
                DemoUtils.handleSessionException(requireActivity(), e)
            }
        }

        try {
            viewDataBinding.arSceneView.resume()
        } catch (ex: CameraNotAvailableException) {
            DemoUtils.displayError(requireContext(), "Unable to get camera", ex)
            navController.popBackStack()
            return
        }

        if (viewDataBinding.arSceneView.session != null) {
            showLoadingMessage()
        }
    }

    override fun onPause() {
        super.onPause()

        if (locationScene != null) {
            locationScene?.pause()
        }

        if (viewDataBinding.arSceneView != null) {
            viewDataBinding.arSceneView.pause()
        }
    }

//    override fun onDestroy() {
//        if (viewDataBinding != null) {
//            viewDataBinding.arSceneView.destroy()
//        }
//        super.onDestroy()
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!ARLocationPermissionHelper.hasPermission(requireActivity())) {
            if (!ARLocationPermissionHelper.shouldShowRequestPermissionRationale(requireActivity())) {
                // Permission denied with checking "Do not ask again".
                ARLocationPermissionHelper.launchPermissionSettings(requireActivity())
            } else {
                showWarningToast(
                    requireContext(),
                    "Camera permission is needed to run this application"
                )
            }
            navController.popBackStack()
        }
    }

    private fun showLoadingMessage() {
        if (viewDataBinding.loader != null) {
            viewDataBinding.loader.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingMessage() {
        if (viewDataBinding.loader != null) {
            viewDataBinding.loader.visibility = View.GONE
        }
    }

    private fun hideSystemUI() {
        // Standard Android full-screen functionality.
        systemUIConfigurationBackup = requireActivity().window.decorView.systemUiVisibility
        requireActivity().window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun restoreSystemUI() {
        requireActivity().window.decorView.systemUiVisibility = systemUIConfigurationBackup
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}