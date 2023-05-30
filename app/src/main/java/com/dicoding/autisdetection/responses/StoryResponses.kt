package com.dicoding.autisdetection.responses

import com.google.gson.annotations.SerializedName

data class StoryResponses(
    @SerializedName("id_post")
    val id_post: Int,

    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("stori")
    val stori: String,

    @SerializedName("sum_like")
    val sum_like: Int,

    @SerializedName("created_at")
    val created_at: String,
)
