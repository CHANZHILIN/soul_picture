package com.soul_picture.activity

import android.view.WindowManager
import com.kotlin_baselib.base.BaseViewModelActivity
import com.kotlin_baselib.base.EmptyViewModel
import com.kotlin_baselib.glide.GlideUtil
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


    override fun preSetContentView() {
        super.preSetContentView()
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        window.navigationBarColor
    }


    override fun initData() {

        picture_detial_ll_bottom.setBackgroundColor(resources.getColor(R.color.transparent))
        val path = intent.getStringExtra("keyImage")

        GlideUtil.instance.loadImage(this, path, picture_detial_iv)
    }

    override fun initListener() {
        picture_detial_tv_edit.setOnClickListener {

        }
    }
}