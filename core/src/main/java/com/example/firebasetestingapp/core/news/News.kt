package com.example.firebasetestingapp.core.news

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("category")
    val category: List<String>? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("published")
    val published: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("url")
    val url: String? = null
){
    companion object {
        fun mapToNews(map: Map.Entry<String, Any>): News {
            var id = ""
            var author: String? = null
            var category: List<String>? = null
            var description: String? = null
            var image: String? = null
            var language: String? = null
            var published: String? = null
            var title: String? = null
            var url: String? = null

            (map.value as Map<*, *>).forEach { entry ->
                when (entry.key) {
                    "id" -> {
                        id = entry.value.toString()
                    }

                    "image" -> {
                        image = entry.value.toString()
                    }

                    "author" -> {
                        author = entry.value.toString()
                    }

                    "description" -> {
                        description = entry.value.toString()
                    }

                    "image" -> {
                        image = entry.value.toString()
                    }

                    "language" -> {
                        language = entry.value.toString()
                    }

                    "published" -> {
                        published = entry.value.toString()
                    }

                    "title" -> {
                        title = entry.value.toString()
                    }

                    "url" -> {
                        url = entry.value.toString()
                    }
                }
            }

            return News(
                id = id,
                author = author,
                category = category,
                description = description,
                image = image,
                language = language,
                published = published,
                title = title,
                url = url
            )
        }

    }
}