package com.soul_picture.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.kotlin_baselib.base.BaseViewModelActivity
import com.kotlin_baselib.base.EmptyViewModel
import com.kotlin_baselib.glide.GlideUtil
import com.kotlin_baselib.recyclerview.setSingleItemUp
import com.kotlin_baselib.utils.SdCardUtil
import com.kotlin_baselib.utils.SnackBarUtil
import com.soul_picture.R
import kotlinx.android.synthetic.main.activity_picture_detail.*
import kotlinx.android.synthetic.main.dialog_brush_bottom.*
import kotlinx.android.synthetic.main.layout_item_picture_detail_edit_bottom.view.*


/**
 *  Created by CHEN on 2019/12/11
 *  Email:1181785848@qq.com
 *  Introduce:  图片详情
 **/
class PictureDetailActivity : BaseViewModelActivity<EmptyViewModel>() {
    override fun providerVMClass(): Class<EmptyViewModel> = EmptyViewModel::class.java
    override fun getResId(): Int = R.layout.activity_picture_detail

    private var isReset: Boolean = true
    private var isShowDialog = false

    private lateinit var data: java.util.ArrayList<String>
    var path = ""
    override fun preSetContentView() {
        super.preSetContentView()
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }


    override fun initData() {
        /*    if (ScreenUtils.instance.checkDeviceHasNavigationBar()) {   //有虚拟导航栏

                val params = ConstraintLayout.LayoutParams(picture_detial_fl_bottom.layoutParams)
                params.bottomMargin = ScreenUtils.instance.getNavigationBarHeight()
                picture_detial_fl_bottom.layoutParams = params
            }*/
        path = intent.getStringExtra("keyImage")

        GlideUtil.instance.loadImage(this, path, picture_detail_iv)


        brush.apply {
            dialog_bursh_seekbar?.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    dialog_bursh_text?.text = "画笔粗细值为：${progress}"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

        }

        brush?.translationY = 800f
        brush?.findViewById<TextView>(R.id.dialog_toolbar_close)
            ?.setOnClickListener {
                isShowDialog = false
                brush?.animate()?.apply {
                    duration = 200
                    interpolator = DecelerateInterpolator()
                    translationY(brush.height.toFloat())
                }?.start()
                brush?.postDelayed({
                    brush?.visibility = View.GONE
                }, 200)
            }
        brush?.findViewById<TextView>(R.id.dialog_toolbar_done)
            ?.setOnClickListener {
                isShowDialog = false
                brush?.animate()?.apply {
                    duration = 200
                    interpolator = DecelerateInterpolator()
                    translationY(brush.height.toFloat())
                }?.start()
                brush?.postDelayed({
                    brush?.visibility = View.GONE
                }, 200)

            }


        data = ArrayList<String>()
        data.add("发送")
        data.add("编辑")
        data.add("收藏")
        data.add("删除")
        data.add("更多")
        picture_detial_rv.setSingleItemUp(
            data,
            R.layout.layout_item_picture_detail_edit_bottom,

            { _, holder, item ->
                holder.itemView.item_picture_detail_bottom_tv.text = item
            },
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false),
            { position, it ->
                when (it) {
                    "编辑" -> {
                        showEditBottomView()
                    }
                    "删除" ->{
                        SdCardUtil.deleteFile(path){
                            SnackBarUtil.shortSnackBar(
                                picture_detail_iv,
                                if (it) "删除图片成功" else "删除图片失败！",
                                SnackBarUtil.CONFIRM
                            ).show()
                            if (it){
                                picture_detial_rv?.postDelayed({
                                    finishAfterTransition()
                                },500)
                            }
                        }
                    }
                }
            }
        )

    }

    private fun reset() {
        val ivTranslateY =
            ObjectAnimator.ofFloat(picture_detail_iv, "translationY", -100f, 0f)
        val rvEditTranslateY =
            ObjectAnimator.ofFloat(
                picture_detial_rv,
                "translationY",
                100f,
                0f
            )
        rvEditTranslateY.duration = 500
        rvEditTranslateY?.interpolator = DecelerateInterpolator()
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            ivTranslateY, rvEditTranslateY
        )
        animatorSet.start()
        picture_detial_tv_switch_color?.visibility = View.GONE       //隐藏切换颜色的按钮
        picture_detial_rv?.visibility = View.VISIBLE
        rv_edit?.visibility = View.GONE
        isReset = true

    }

    override fun initListener() {
        picture_detial_tv_switch_color?.setOnClickListener {
            ColorPickerDialogBuilder
                .with(this)
                .setTitle("选择颜色")
                .initialColor(Color.RED)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(
                    "确定"
                )
                { dialog, selectedColor, allColors ->
                    SnackBarUtil.shortSnackBar(
                        picture_detail_iv,
                        "${selectedColor}",
                        SnackBarUtil.CONFIRM
                    ).show()
//                    drawView.setPaintColor(selectedColor)
//                    drawView.setTextColor(selectedColor)
                }
                .build()
                .show()
        }
    }

    private fun showEditBottomView() {
        rv_edit?.setSingleItemUp(
            mutableListOf("涂鸦", "添加文本", "上一步", "下一步", "完成"),
            R.layout.layout_item_picture_detail_edit_bottom,
            { _, holder, item ->
                holder.itemView.item_picture_detail_bottom_tv.text = item
            },
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false),
            { position, data ->
                when (data) {
                    "涂鸦" -> {
                        brush?.alpha = 0f
                        brush?.visibility = View.VISIBLE
                        brush?.animate()
                            ?.setDuration(200)
                            ?.alpha(1f)
                            ?.setInterpolator(DecelerateInterpolator())
                            ?.translationY(0f)
                            ?.setStartDelay(100)
                            ?.start()
                        isShowDialog = true
                    }
                    "完成" -> {
                        reset()
                    }
                }
            }
        )

        rv_edit?.visibility = View.VISIBLE
        picture_detial_tv_switch_color?.visibility = View.VISIBLE    //显示切换颜色的按钮
        picture_detial_rv?.visibility = View.GONE
        isReset = false

        val ivTranslateY =
            ObjectAnimator.ofFloat(picture_detail_iv, "translationY", 0f, -100f)
        ivTranslateY.duration = 500
        ivTranslateY?.interpolator = DecelerateInterpolator()
        val rvEditTranslateY =
            ObjectAnimator.ofFloat(
                rv_edit,
                "translationY",
                100f,
                0f
            )
        rvEditTranslateY.duration = 500
        rvEditTranslateY?.interpolator = DecelerateInterpolator()
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            ivTranslateY, rvEditTranslateY
        )
        animatorSet.start()
    }


    override fun onBackPressed() {
        if (isShowDialog) {
            brush?.visibility = View.GONE
            isShowDialog = false
            return
        }
        if (!isReset) {
            reset()
            return
        }
        super.onBackPressed()
    }
}