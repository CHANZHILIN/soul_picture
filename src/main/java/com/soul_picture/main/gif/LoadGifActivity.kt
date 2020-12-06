package com.soul_picture.main.gif

import android.graphics.Bitmap
import android.os.Environment
import android.os.Handler
import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import com.kotlin_baselib.api.Constants
import com.kotlin_baselib.base.BaseViewModelActivity
import com.kotlin_baselib.base.EmptyViewModel
import com.kotlin_baselib.utils.MyLog
import com.kotlin_baselib.utils.SdCardUtil.DEFAULT_PHOTO_PATH
import com.kotlin_baselib.utils.onClick
import com.soul_picture.R
import kotlinx.android.synthetic.main.activity_load_gif.*
import kotlinx.android.synthetic.main.activity_picture_detail.*
import kotlinx.android.synthetic.main.dialog_brush_bottom.*
import java.io.File

@Route(path = Constants.GIF_PICTURE_ACTIVITY_PATH)
class LoadGifActivity : BaseViewModelActivity<EmptyViewModel>() {


    override fun providerVMClass(): Class<EmptyViewModel> = EmptyViewModel::class.java

    private var delay_time = 40
    private lateinit var gifBitmap: Bitmap
    private lateinit var gifHandler: GifHandler
    val handler: Handler = Handler {
        when (it.what) {
            1 -> {
                val delay = gifHandler.updateFrame(gifBitmap)
                it.target.sendEmptyMessageDelayed(1, delay.toLong() + delay_time)
                iv_gif.setImageBitmap(gifBitmap)
            }
        }
        false
    }

    override fun getResId(): Int = R.layout.activity_load_gif

    override fun isTransparentPage(): Boolean = true
    override fun initData() {

    }

    override fun initListener() {

        seek_bar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                delay_time = progress
                btn_load?.text = "延迟时间${delay_time}ms"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        btn_load?.onClick {
            val file = File(DEFAULT_PHOTO_PATH, "t-mac.gif")
            gifHandler = GifHandler().load(file.absolutePath)
            val bitmapWidth = gifHandler.getWidth()
            val bitmapHeight = gifHandler.getHeight()
            MyLog.e("gifWidth=${bitmapWidth},gifHeight=${bitmapHeight}")
            gifBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
            val delay = gifHandler.updateFrame(gifBitmap)
            handler.sendEmptyMessageDelayed(1, delay.toLong())
        }
    }
}