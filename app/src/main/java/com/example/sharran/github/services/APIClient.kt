package com.example.sharran.github.services

import com.example.sharran.github.utils.AppConstants
import com.example.sharran.github.utils.Repositories
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

    fun fetchRepositories(searchQuery : String, completionHandler: CompletionHandler) {

        service.fetchRepositories(searchQuery)
            .enqueue(object : Callback<Repositories> {
                override fun onResponse(call: Call<Repositories>, response: Response<Repositories>) {
                    println(response.body())
                    completionHandler.onSuccess(response.body() ?: Repositories())
                }

                override fun onFailure(call: Call<Repositories>, t: Throwable) {
                    completionHandler.onFailure(t)
                }
            })
    }

}