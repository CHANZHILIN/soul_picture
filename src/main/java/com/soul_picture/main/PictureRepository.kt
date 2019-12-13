package com.soul_picture.main

import android.util.Log
import com.kotlin_baselib.api.ApiEngine
import com.kotlin_baselib.base.BaseRepository
import com.kotlin_baselib.base.ResponseData
import com.kotlin_baselib.entity.EmptyEntity
import com.kotlin_baselib.utils.SdCardUtil
import com.soul_picture.entity.PictureEntity

/**
 *  Created by CHEN on 2019/12/13
 *  Email:1181785848@qq.com
 *  Introduce:
 **/
class PictureRepository: BaseRepository() {
/*    suspend fun getPictureData():ResponseData<EmptyEntity> = request {
        ApiEngine.apiService.getVersionData()
    }*/

    suspend fun getPictureData():MutableList<PictureEntity> = requestLocal{
        val fileData = SdCardUtil.getFilesAllName(SdCardUtil.DEFAULT_PHOTO_PATH)
        val pictureData = ArrayList<PictureEntity>()
        for (fileDatum in fileData) {   //封装实体类，加入随机高度，解决滑动过程中位置变换的问题
            pictureData.add(PictureEntity(fileDatum, (200 + Math.random() * 400).toInt()))
        }
        pictureData
    }
}