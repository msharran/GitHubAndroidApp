package com.example.sharran.github.services

import com.example.sharran.github.utils.Contributor
import com.example.sharran.github.utils.Repositories
import com.example.sharran.github.utils.RepositoryDetail
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetAPIService {
    @GET("/search/repositories")
    fun search(@Query("q", encoded = true) q: String): Observable<Response<Repositories>>

    @GET("repos/{fullName}/contributors")
    fun contributors(@Path("fullName", encoded = true) fullName : String) : Observable<Response<List<Contributor>>>

    @GET("{userRepos}")
    fun repositories(@Path("userRepos", encoded = true) userRepos : String) : Observable<Response<List<RepositoryDetail>>>
}