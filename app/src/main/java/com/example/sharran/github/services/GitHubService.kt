package com.example.sharran.github.services

import com.example.sharran.github.utils.Contributor
import com.example.sharran.github.utils.Repositories
import com.example.sharran.github.utils.RepositoryDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("/search/repositories")
    fun fetchRepos(@Query("q",encoded = true) q: String): Call<Repositories>

    @GET("repos/{fullName}/contributors")
    fun fetchContributors(@Path("fullName", encoded = true) fullName : String) : Call<List<Contributor>>

    @GET("{userRepos}")
    fun fetchUserRepos(@Path("userRepos", encoded = true) userRepos : String) : Call<List<RepositoryDetail>>
}