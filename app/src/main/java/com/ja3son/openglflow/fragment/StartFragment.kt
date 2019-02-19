package com.ja3son.openglflow.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.ja3son.openglflow.R
import com.ja3son.openglflow.viewmodel.StartViewModel
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        seek_bar.setOnSeekBarChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        gl_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        gl_view.onPause()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        gl_view.setLightOffset((seekBar!!.max / 2.0f - progress) / (seekBar.max / 2.0f) * -4f)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
