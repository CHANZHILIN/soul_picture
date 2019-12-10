package com.soul_picture

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin_baselib.base.BaseViewModelFragment
import com.kotlin_baselib.base.EmptyViewModel
import com.kotlin_baselib.recyclerview.ListItem
import com.kotlin_baselib.recyclerview.ListItemAdapter
import com.kotlin_baselib.recyclerview.setMutiUp
import com.kotlin_baselib.utils.SnackbarUtil
import kotlinx.android.synthetic.main.fragment_picture.*
import kotlinx.android.synthetic.main.layout_item_picture.view.*
import kotlinx.android.synthetic.main.layout_item_picture2.view.*


private const val ARG_PARAM1 = "param1"

/**
 *  Created by CHEN on 2019/6/20
 *  Email:1181785848@qq.com
 *  Package:com.soul_picture
 *  Introduce: 图片
 **/
class PictureFragment : BaseViewModelFragment<EmptyViewModel>() {

    override fun providerVMClass(): Class<EmptyViewModel>? = EmptyViewModel::class.java

    override fun getResId(): Int = R.layout.fragment_picture

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


    override fun initData() {
        val data = ArrayList<Int>()
        for (i in 0..100) {
            data.add(i)
        }
//        fragment_picture_recyclerview.adapter =
//            FragmentPictureAdapter(data, R.layout.layout_item_picture)
//        fragment_picture_recyclerview.layoutManager = LinearLayoutManager(mContext)
        /*       fragment_picture_recyclerview.setSingleUp(
                   data,
                   R.layout.layout_item_picture,
                   LinearLayoutManager(mContext),
                   { holder, item ->
                       holder.itemView.tv_name.text = "你好${item}"
                   },
                   {
                       SnackbarUtil.ShortSnackbar(
                           fragment_picture_recyclerview,
                           "点击${it}！",
                           SnackbarUtil.CONFIRM
                       )
                           .show()
                   }
               )*/
        val adaptedUsers = data.mapIndexed { index, user ->
            ListItemAdapter(
                user,
                if (data.get(index) % 2 == 0) R.layout.layout_item_picture else R.layout.layout_item_picture2
            )
        }
        fragment_picture_recyclerview.setMutiUp(adaptedUsers, LinearLayoutManager(mContext),
            listItems = *arrayOf(ListItem(R.layout.layout_item_picture,
                { holder, item ->
                    holder.itemView.tv_name.text = "你好${item.data}"

                }, {
                    SnackbarUtil.ShortSnackbar(
                        fragment_picture_recyclerview,
                        "点击${it.data}！",
                        SnackbarUtil.CONFIRM
                    )
                        .show()
                }),
                ListItem(R.layout.layout_item_picture2,
                    { holder, item ->
                        holder.itemView.tv_name2.text = "他好${item.data}"
                    }, {
                        SnackbarUtil.ShortSnackbar(
                            fragment_picture_recyclerview,
                            "点击${it.data}！",
                            SnackbarUtil.CONFIRM
                        )
                            .show()
                    })
            )
        )
    }

    override fun initListener() {

    }


}
