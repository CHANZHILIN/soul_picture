package com.soul_picture

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kotlin_baselib.api.Constants
import com.kotlin_baselib.base.BaseViewModelFragment
import com.kotlin_baselib.base.EmptyViewModel
import com.kotlin_baselib.glide.GlideUtil
import com.kotlin_baselib.recyclerview.decoration.StaggeredDividerItemDecoration
import com.kotlin_baselib.recyclerview.setSingleUp
import com.kotlin_baselib.utils.SdCardUtil
import com.soul_picture.activity.PictureDetailActivity
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
class PictureFragment : BaseViewModelFragment<EmptyViewModel>() {


    companion object {


        @JvmStatic
        fun newInstance(param1: String) =
            PictureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }


    override fun providerVMClass(): Class<EmptyViewModel>? = EmptyViewModel::class.java

    override fun getResId(): Int = R.layout.fragment_picture

    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }


    override fun initData() {
        val pictureSrc = SdCardUtil.DEFAULT_PHOTO_PATH;
        val fileData = SdCardUtil.getFilesAllName(pictureSrc)

        val pictureData = ArrayList<PictureEntity>()
        for (fileDatum in fileData) {   //封装实体类，加入随机高度，解决滑动过程中位置变换的问题
            pictureData.add(PictureEntity(fileDatum, (200 + Math.random() * 400).toInt()))
        }

        fragment_picture_recyclerview.setSingleUp(
            pictureData,
            R.layout.layout_item_picture,
            StaggeredGridLayoutManager(Constants.SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL),
            { holder, item ->
                val width =
                    (holder.itemView.item_picture_iv_image.getContext() as Activity).windowManager.defaultDisplay.width //获取屏幕宽度
                val params = holder.itemView.item_picture_iv_image.getLayoutParams()
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
            {
                /*     SnackbarUtil.ShortSnackbar(
                         fragment_picture_recyclerview,
                         "点击${it.picturePath}！",
                         SnackbarUtil.CONFIRM
                     )
                         .show()*/
            }
        )
        fragment_picture_recyclerview.addItemDecoration(
            StaggeredDividerItemDecoration(
                mContext,
                Constants.ITEM_SPACE
            )
        )
/*        val adaptedUsers = data.mapIndexed { index, user ->
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
        )*/
    }

    override fun initListener() {

    }


}
