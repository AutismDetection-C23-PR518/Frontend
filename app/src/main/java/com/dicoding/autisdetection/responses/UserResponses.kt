package com.dicoding.autisdetection.responses

import com.google.gson.annotations.SerializedName

data class UserResponses(
    @SerializedName("id_user")
    val id_user: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

)


data class photo(
    @SerializedName("url")
    val url: String
)
