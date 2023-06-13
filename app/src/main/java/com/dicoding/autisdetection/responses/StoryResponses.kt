package com.dicoding.autisdetection.responses

import com.google.gson.annotations.SerializedName


data class StoryResponses(
    @SerializedName("id_post")
    val idPost: Int,

    @SerializedName("id_user")
    val userId: Int,

    @SerializedName("post")
    val post: String,

    @SerializedName("sum_like")
    val sumLike: Int,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("userIdUser")
    val userIdUser: Int?,

    @SerializedName("user")
    val user: User?
)

data class User(
    @SerializedName("name")
    val name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String
)


data class StoryRequest(
    @SerializedName("post")
    val post: String
)
