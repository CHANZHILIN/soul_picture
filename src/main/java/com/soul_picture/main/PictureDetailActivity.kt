package com.soul_picture.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.kotlin_baselib.base.BaseViewModelActivity
import com.kotlin_baselib.base.EmptyViewModel
import com.kotlin_baselib.glide.GlideUtil
import com.kotlin_baselib.recyclerview.setSingleUp
import com.kotlin_baselib.utils.ScreenUtils
import com.kotlin_baselib.utils.SnackbarUtil
import com.soul_picture.R
import kotlinx.android.synthetic.main.activity_picture_detail.*
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
        if (ScreenUtils.instance.checkDeviceHasNavigationBar()) {   //有虚拟导航栏

            val params = FrameLayout.LayoutParams(picture_detial_fl_bottom.layoutParams)
            params.gravity = Gravity.BOTTOM
            params.bottomMargin = ScreenUtils.instance.getNavigationBarHeight()
            picture_detial_fl_bottom.layoutParams = params
        }
        path = intent.getStringExtra("keyImage")

        GlideUtil.instance.loadImage(this, path, picture_detial_iv)


        data = ArrayList<String>()
        data.add("编辑")
        picture_detial_rv.setSingleUp(
            data,
            R.layout.layout_item_picture_detail_edit_bottom,
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false),
            { holder, item ->
                holder.itemView.item_picture_detail_bottom_tv.text = item
            },
            {
                if (it.equals("编辑")) {
                    val objectAnimatorX: ObjectAnimator =
                        ObjectAnimator.ofFloat(picture_detial_iv, "scaleX", 1f, 0.7f)
                    val objectAnimatorY: ObjectAnimator =
                        ObjectAnimator.ofFloat(picture_detial_iv, "scaleY", 1f, 0.7f)
                    val objectAnimatorTranlateY =
                        ObjectAnimator.ofFloat(picture_detial_iv, "translationY", 0f, -100f)
                    val animatorSet = AnimatorSet()
                    animatorSet.playTogether(
                        objectAnimatorX,
                        objectAnimatorY,
                        objectAnimatorTranlateY
                    )
                    animatorSet.start()

                    picture_detial_tv_switch_color.visibility = View.VISIBLE    //显示切换颜色的按钮
                    data.clear()
                    data.add("涂鸦")
                    data.add("添加文本")
                    data.add("马赛克")
                    data.add("上一步")
                    data.add("下一步")
                    data.add("完成")
                    picture_detial_rv.adapter!!.notifyDataSetChanged()
                    isReset = false
                }
                if (it.equals("涂鸦")) {
                    val layout =
                        LayoutInflater.from(mContext).inflate(R.layout.dialog_brush_bottom, null)
                    layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
                    picture_detial_fl_bottom.addView(layout)
                    isShowDialog = true

                    layout.findViewById<ImageView>(R.id.dialog_toolbar_close).setOnClickListener {
                        isShowDialog = false
                        picture_detial_fl_bottom.removeView(layout)
                    }
                    layout.findViewById<ImageView>(R.id.dialog_toolbar_done).setOnClickListener {
                        isShowDialog = false
                        picture_detial_fl_bottom.removeView(layout)
                    }


                }
                if (it.equals("完成")) {
                    reset()
                }
            }
        )

    }

    private fun reset() {

        val objectAnimatorX: ObjectAnimator =
            ObjectAnimator.ofFloat(picture_detial_iv, "scaleX", 0.7f, 1.0f)
        val objectAnimatorY: ObjectAnimator =
            ObjectAnimator.ofFloat(picture_detial_iv, "scaleY", 0.7f, 1.0f)
        val objectAnimatorTranlateY =
            ObjectAnimator.ofFloat(picture_detial_iv, "translationY", -100f, 0f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(
            objectAnimatorX,
            objectAnimatorY,
            objectAnimatorTranlateY
        )
        animatorSet.start()
        picture_detial_tv_switch_color.visibility = View.GONE       //隐藏切换颜色的按钮
        data.clear()
        data.add("编辑")
        picture_detial_rv.adapter!!.notifyDataSetChanged()
        isReset = true

    }

    override fun initListener() {
        picture_detial_tv_switch_color.setOnClickListener {
            ColorPickerDialogBuilder
                .with(this)
                .setTitle("选择颜色")
                .initialColor(Color.RED)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(
                    "ok"
                ) { dialog, selectedColor, allColors ->
                    SnackbarUtil.ShortSnackbar(
                        picture_detial_iv,
                        "${selectedColor}",
                        SnackbarUtil.CONFIRM
                    ).show()
//                    drawView.setPaintColor(selectedColor)
//                    drawView.setTextColor(selectedColor)
                }
                .build()
                .show()
        }
    }

    override fun onBackPressed() {
        if (isShowDialog) {
            picture_detial_fl_bottom.removeViewAt(picture_detial_fl_bottom.childCount - 1)
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