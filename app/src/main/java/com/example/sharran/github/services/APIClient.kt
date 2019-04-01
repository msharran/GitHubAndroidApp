package com.example.sharran.github.services

import com.example.sharran.github.utils.AppConstants
import com.example.sharran.github.utils.APIModels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class APIClient{
    private val service = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(GitHubService::class.java)

    fun fetchRepositories(query : String, completionHandler: CompletionHandler) {

        service.fetchRepositories(query)
            .enqueue(object : Callback<APIModels.Search> {
                override fun onResponse(call: Call<APIModels.Search>, response: Response<APIModels.Search>) {
                    println(response.body())
                    completionHandler.onSuccess(response.body() ?: APIModels.Search())
                }

                override fun onFailure(call: Call<APIModels.Search>, t: Throwable) {
                    completionHandler.onFailure(t)
                }
            })
    }

}