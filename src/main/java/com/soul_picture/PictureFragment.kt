package com.soul_picture

import android.os.Bundle
import com.kotlin_baselib.base.BaseFragment
import com.kotlin_baselib.base.EmptyModelImpl
import com.kotlin_baselib.base.EmptyPresenterImpl
import com.kotlin_baselib.base.EmptyView


private const val ARG_PARAM1 = "param1"

/**
 *  Created by CHEN on 2019/6/20
 *  Email:1181785848@qq.com
 *  Package:com.soul_picture
 *  Introduce: 图片
 **/
class PictureFragment : BaseFragment<EmptyView, EmptyModelImpl, EmptyPresenterImpl>(), EmptyView {

    override fun createPresenter(): EmptyPresenterImpl {
        return EmptyPresenterImpl(this)
    }

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                PictureFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun getResId(): Int {
        return R.layout.fragment_picture
    }

    override fun initData() {

    }

    override fun initListener() {

    }

    override fun lazyLoad() {
    }
}
