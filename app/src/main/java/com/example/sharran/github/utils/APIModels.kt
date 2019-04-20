package com.example.sharran.github.utils

data class Repositories(
    val total_count: Long = 0,
    val items: List<RepositoryDetail> = emptyList()
)

data class RepositoryDetail(
    val name: String = "",
    val full_name: String = "",
    val description: String? = null,
    val html_url: String = "",
    val contributors_url: String = "",
    val watchers: Int = 0,
    val language: String = "",
    val owner: Owner = Owner()
)

data class Owner(
    val login: String = "",
    val avatar_url: String = ""
)

data class Contributor(
    val login: String = "",
    val avatar_url: String = "",
    val repos_url: String = "",
    val contributions : Int = 0
)
