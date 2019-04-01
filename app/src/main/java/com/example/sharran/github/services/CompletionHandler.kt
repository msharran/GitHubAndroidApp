package com.example.sharran.github.services


interface CompletionHandler {

    fun <T> onSuccess(response : T )

    fun onFailure(throwable: Throwable)

}