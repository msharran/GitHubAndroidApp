package com.example.sharran.github.utils

object APIModels {
    data class Search(val total_count: Long = 0,
                      val items : List<Repository> = emptyList()
    )

    data class Repository(val name : String,
                          val full_name : String,
                          val description : String,
                          val html_url : String,
                          val contributors_url : String,
                          val watchers : Int,
                          val language :String,
                          val owner: Owner
    )

    data class Owner(val login :String,
                     val avatar_url: String
    )

    data class Contributor(val login: String,
                           val avatar_url : String,
                           val repos_url : String
    )
}