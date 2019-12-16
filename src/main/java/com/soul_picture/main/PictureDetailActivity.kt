package com.soul_picture.main

import android.animation.ObjectAnimator
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import com.kotlin_baselib.base.BaseViewModelActivity
import com.kotlin_baselib.base.EmptyViewModel
import com.kotlin_baselib.glide.GlideApp
import com.kotlin_baselib.glide.GlideUtil
import com.kotlin_baselib.utils.BitmapUtil
import com.kotlin_baselib.utils.ScreenUtils
import com.soul_picture.R
import kotlinx.android.synthetic.main.activity_picture_detail.*

/**
 *  Created by CHEN on 2019/12/11
 *  Email:1181785848@qq.com
 *  Introduce:  图片详情
 **/
class PictureDetailActivity : BaseViewModelActivity<EmptyViewModel>() {
    override fun providerVMClass(): Class<EmptyViewModel> = EmptyViewModel::class.java
    override fun getResId(): Int = R.layout.activity_picture_detail

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

            val params = FrameLayout.LayoutParams(picture_detial_ll_bottom.layoutParams)
            params.gravity = Gravity.BOTTOM
            params.bottomMargin = ScreenUtils.instance.getNavigationBarHeight()
            picture_detial_ll_bottom.layoutParams = params
        }
         path = intent.getStringExtra("keyImage")

        GlideUtil.instance.loadImage(this, path, picture_detial_iv)


    }

    override fun initListener() {
        picture_detial_tv_edit.setOnClickListener {
            val objectAnimatorX = ObjectAnimator.ofFloat(picture_detial_iv,"scaleX",1f,0.5f)
            val objectAnimatorY = ObjectAnimator.ofFloat(picture_detial_iv,"scaleY",1f,0.5f)
            objectAnimatorX.start()
            objectAnimatorY.start()

        }
    }
}