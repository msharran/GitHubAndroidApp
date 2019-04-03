package com.example.sharran.github.services

import com.example.sharran.github.utils.AppConstants
import com.example.sharran.github.utils.Contributor
import com.example.sharran.github.utils.Repositories
import com.example.sharran.github.utils.RepositoryDetail
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

    fun fetchRepos(searchQuery : String, completionHandler: CompletionHandler) {

        service.fetchRepos(searchQuery)
            .enqueue(object : Callback<Repositories> {
                override fun onResponse(call: Call<Repositories>, response: Response<Repositories>) {
                    println(response.raw().request().url())
                    completionHandler.onSuccess(response.body())
                }

                override fun onFailure(call: Call<Repositories>, t: Throwable) {
                    completionHandler.onFailure(t)
                }
            })
    }

    fun fetchContributors(fullName : String, completionHandler: CompletionHandler) {

        service.fetchContributors(fullName)
            .enqueue(object : Callback<List<Contributor>> {
                override fun onResponse(call: Call<List<Contributor>>, response: Response<List<Contributor>>) {
                    println(response.raw().request().url())
                    println(response.body())
                   completionHandler.onSuccess(response.body())
                }

                override fun onFailure(call: Call<List<Contributor>>, t: Throwable) {
                   completionHandler.onFailure(t)
                }
            })
    }

    fun fetchUserRepos(userReposUrl : String, completionHandler: CompletionHandler) {

        service.fetchUserRepos(userReposUrl)
            .enqueue(object : Callback<List<RepositoryDetail>> {
                override fun onResponse(call: Call<List<RepositoryDetail>>, response: Response<List<RepositoryDetail>>) {
                    println(response.raw().request().url())
                    println(response.body())
                    completionHandler.onSuccess(response.body())
                }

                override fun onFailure(call: Call<List<RepositoryDetail>>, t: Throwable) {
                    completionHandler.onFailure(t)
                }
            })
    }

}