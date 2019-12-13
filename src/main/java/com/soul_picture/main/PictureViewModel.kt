package com.soul_picture.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kotlin_baselib.base.BaseViewModel
import com.soul_picture.entity.PictureEntity

/**
 *  Created by CHEN on 2019/12/13
 *  Email:1181785848@qq.com
 *  Introduce:
 **/
class PictureViewModel : BaseViewModel() {
    private val data: MutableLiveData<MutableList<PictureEntity>> by lazy {
        MutableLiveData<MutableList<PictureEntity>>().also {
            loadDatas()
        }
    }


    private val repository = PictureRepository()

    fun getPictureListData(): LiveData<MutableList<PictureEntity>> {
        return data
    }

    private fun loadDatas() = launchUI {
        val result = repository.getPictureData()
        data.value = result
    }

}