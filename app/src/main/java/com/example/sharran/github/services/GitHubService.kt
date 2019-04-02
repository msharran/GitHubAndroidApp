package com.example.sharran.github.services

import com.example.sharran.github.utils.Contributor
import com.example.sharran.github.utils.Repositories
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("/search/repositories")
    fun fetchRepositories(@Query("q") q: String): Call<Repositories>

    @GET("repos/{fullName}/contributors")
    fun fetchContributers(@Path("fullName") fullName : String) : Call<List<Contributor>>
}