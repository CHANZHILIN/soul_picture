package com.soul_picture.main

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kotlin_baselib.api.Constants
import com.kotlin_baselib.base.BaseViewModelFragment
import com.kotlin_baselib.glide.GlideUtil
import com.kotlin_baselib.recyclerview.SingleAdapter
import com.kotlin_baselib.recyclerview.decoration.StaggeredDividerItemDecoration
import com.kotlin_baselib.recyclerview.setSingleItemUp
import com.kotlin_baselib.utils.ScreenUtils
import com.soul_picture.R
import com.soul_picture.entity.PictureEntity
import kotlinx.android.synthetic.main.fragment_picture.*
import kotlinx.android.synthetic.main.layout_item_picture.view.*


private const val ARG_PARAM1 = "param1"

/**
 *  Created by CHEN on 2019/6/20
 *  Email:1181785848@qq.com
 *  Package:com.soul_picture
 *  Introduce: 图片
 **/
class PictureFragment : BaseViewModelFragment<PictureViewModel>() {


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            PictureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }


    override fun providerVMClass(): Class<PictureViewModel>? = PictureViewModel::class.java

    override fun getResId(): Int = R.layout.fragment_picture

    private var param1: String? = null

    private var singleAdapter: SingleAdapter<PictureEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }


    override fun initData() {
        showLoading()
        singleAdapter = fragment_picture_recyclerview.setSingleItemUp(
            mutableListOf(),
            R.layout.layout_item_picture,
            { _, holder, item ->
                val width = ScreenUtils.instance.getScreenWidth() //获取屏幕宽度
                val params = holder.itemView.item_picture_iv_image.layoutParams
                //设置图片的相对于屏幕的宽高比
                params.width =
                    (width - (Constants.SPAN_COUNT + 1) * Constants.ITEM_SPACE) / Constants.SPAN_COUNT
                params.height = item.randomHeight

                GlideUtil.instance.loadImage(
                    mContext,
                    item.picturePath,
                    holder.itemView.item_picture_iv_image
                )
                holder.itemView.item_picture_iv_image.setOnClickListener {
                    val i = Intent(mContext, PictureDetailActivity::class.java)
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        mContext,
                        holder.itemView.item_picture_iv_image,
                        "image"
                    )
                    i.putExtra("keyImage", item.picturePath)
                    startActivity(i, optionsCompat.toBundle())
                }
            },
            StaggeredGridLayoutManager(Constants.SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        )
        fragment_picture_recyclerview.addItemDecoration(
            StaggeredDividerItemDecoration(
                mContext,
                Constants.ITEM_SPACE
            )
        )

        viewModel.getPictureListData().observe(this, Observer {
            it?.run {
                singleAdapter?.replaceData(it)
                hideLoading()
            }
        })
/*        val adaptedUsers = data.mapIndexed { index, user ->
            ListItemAdapter(
                user,
                if (data.get(index) % 2 == 0) R.layout.layout_item_picture else R.layout.layout_item_picture2
            )
        }
        fragment_picture_recyclerview.setMultiUp(adaptedUsers, LinearLayoutManager(mContext),
            listItems = *arrayOf(ListItem(R.layout.layout_item_picture,
                { holder, item ->
                    holder.itemView.tv_name.text = "你好${item.data}"

                }, {
                    SnackBarUtil.ShortSnackbar(
                        fragment_picture_recyclerview,
                        "点击${it.data}！",
                        SnackBarUtil.CONFIRM
                    )
                        .show()
                }),
                ListItem(R.layout.layout_item_picture2,
                    { holder, item ->
                        holder.itemView.tv_name2.text = "他好${item.data}"
                    }, {
                        SnackBarUtil.ShortSnackbar(
                            fragment_picture_recyclerview,
                            "点击${it.data}！",
                            SnackBarUtil.CONFIRM
                        )
                            .show()
                    })
            )
        )*/
    }

    override fun initListener() {
        fragment_picture_refresh_layout.setOnRefreshListener {
            val pictureViewModel = PictureViewModel()
            pictureViewModel.getPictureListData().observe(this, Observer {
                it?.run {
                    singleAdapter?.replaceData(it)
                    fragment_picture_refresh_layout.isRefreshing = false

                    lifecycle.removeObserver(pictureViewModel)
                }
            })


        }
    }


}
