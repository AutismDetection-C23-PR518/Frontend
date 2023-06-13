package com.dicoding.autisdetection.responses

import com.google.gson.annotations.SerializedName

data class GetStoryResponse (

    val stories: List<StoryResponse>

)

data class StoryResponse(
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
    val user: Users?
)

data class Users(
    @SerializedName("name")
    val name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String
)

