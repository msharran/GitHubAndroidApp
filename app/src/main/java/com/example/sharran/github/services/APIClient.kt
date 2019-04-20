package com.example.sharran.github.services

import com.example.sharran.github.utils.AppConstants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class APIClient{
    private val service = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(AppConstants.BASE_URL)
        .build()

    val GET: GetAPI by lazy { GetAPI(service.create(GetAPIService::class.java)) }
}