package com.rtchubs.pharmaerp.ui.login

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import com.daimajia.slider.library.SliderLayout
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.databinding.ViewPagerBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class ViewPagerFragment : BaseFragment<ViewPagerBinding, ViewPagerViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_view_pager
    override val viewModel: ViewPagerViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            requireActivity().finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.slideDataList.forEach { slideData ->
            val slide = SliderView(requireContext())
            slide.sliderTextTitle = slideData.textTitle
            slide.sliderTextDescription = slideData.descText
            slide.sliderImage(slideData.slideImage)
            viewDataBinding.sliderLayout.addSlider(slide)
        }

        // set Slider Transition Animation
        viewDataBinding.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default)
        viewDataBinding.sliderLayout.stopAutoCycle()

        // set Slider Transition Animation
        //viewDataBinding.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default)
        //viewDataBinding.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion)
        //viewDataBinding.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)


        viewDataBinding.btnLogin.setOnClickListener {
            //navController.navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToSignInFragment())
        }

        updateStatusBarBackgroundColor("#1E4356")
    }


}