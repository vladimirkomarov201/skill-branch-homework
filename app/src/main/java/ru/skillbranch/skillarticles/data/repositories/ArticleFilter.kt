package ru.skillbranch.skillarticles.data.repositories

data class ArticleFilter(
    val search: String? = null,
    val isBookmark: Boolean = false,
    val categories: List<String> = listOf(),
    val isHashtag: Boolean = false
){

    fun toQuery(): String{
        return "SELECT * FROM articles"
    }

}