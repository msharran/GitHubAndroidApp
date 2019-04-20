package com.example.sharran.github.services

import com.example.sharran.github.disposable
import com.example.sharran.github.utils.Contributor
import com.example.sharran.github.utils.Repositories
import com.example.sharran.github.utils.RepositoryDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class GetAPI (private val getService : GetAPIService){
    fun search(query : String, onSuccess : (Repositories) -> Unit, onFailure : (String) -> Unit){
        disposable =  getService.search(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response : Response<Repositories> -> onResponse(response, onSuccess, onFailure) },
                { error: Throwable -> onError(error, onFailure) }
            )
    }

    fun contributors(fullName : String, onSuccess : (List<Contributor>) -> Unit, onFailure : (String) -> Unit) {
        disposable = getService.contributors(fullName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response : Response<List<Contributor>> -> onResponse(response,onSuccess,onFailure) },
                { error : Throwable-> onFailure(error.localizedMessage) } )
    }

    fun repositories(url : String, onSuccess : (List<RepositoryDetail>) -> Unit, onFailure : (String) -> Unit) {
        disposable = getService.repositories(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response : Response<List<RepositoryDetail>> -> onResponse(response,onSuccess,onFailure) },
                { error : Throwable-> onFailure(error.localizedMessage) } )
    }

    private fun <T>onResponse(result: Response<T>, onSuccess: (T) -> Unit, onFailure: (String) -> Unit) {
        println(result.raw().request().url())
        when (result.code()) {
            200 -> result.body()?.let { onSuccess(it) } ?: onFailure("Null response from server")
            else -> onFailure(result.message())
        }
    }

    private fun onError(error: Throwable, onFailure: (String) -> Unit) {
        error.printStackTrace()
        onFailure(error.localizedMessage)
    }
}