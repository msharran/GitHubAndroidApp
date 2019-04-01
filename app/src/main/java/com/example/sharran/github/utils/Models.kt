package com.example.sharran.github.utils

object Models {
    data class SearchResult(val total_count: Long,
                            val items : List<Repository>
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