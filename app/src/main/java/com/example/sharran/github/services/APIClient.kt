package com.example.sharran.github.services

import com.example.sharran.github.utils.AppConstants
import com.example.sharran.github.utils.Models
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

    fun fetchRepositories(query : String): Models.SearchResult? {
        var searchResult : Models.SearchResult? = null

        service.fetchRepositories(query)
            .enqueue(object : Callback<Models.SearchResult> {
                override fun onResponse(call: Call<Models.SearchResult>, response: Response<Models.SearchResult>) {
                    println(response.body())
                    searchResult = response.body()
                }

                override fun onFailure(call: Call<Models.SearchResult>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        return searchResult
    }

}