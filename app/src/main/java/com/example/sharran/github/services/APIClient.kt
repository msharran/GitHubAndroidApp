package com.example.sharran.github.services

import com.example.sharran.github.disposable
import com.example.sharran.github.utils.AppConstants
import com.example.sharran.github.utils.Contributor
import com.example.sharran.github.utils.Repositories
import com.example.sharran.github.utils.RepositoryDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class APIClient{
    private val service = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(AppConstants.BASE_URL)
        .build()
        .create(GitHubService::class.java)

    fun fetchRepos(searchQuery : String, onSuccess : (Repositories) -> Unit, onFailure : (Throwable) -> Unit){
        disposable =  service.fetchRepos(searchQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result : Repositories -> onSuccess(result) },
                { error : Throwable-> onFailure(error) } )
    }

    fun fetchContributors(fullName : String, onSuccess : (List<Contributor>) -> Unit, onFailure : (Throwable) -> Unit) {
        disposable = service.fetchContributors(fullName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result : List<Contributor> -> onSuccess(result) },
                { error : Throwable-> onFailure(error) } )
    }

    fun fetchUserRepos(url : String, onSuccess : (List<RepositoryDetail>) -> Unit, onFailure : (Throwable) -> Unit) {
        disposable = service.fetchUserRepos(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result : List<RepositoryDetail> -> onSuccess(result) },
                { error : Throwable-> onFailure(error) } )
    }
}