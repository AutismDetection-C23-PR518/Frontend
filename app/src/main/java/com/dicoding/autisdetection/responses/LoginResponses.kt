package com.dicoding.autisdetection.responses

import com.google.gson.annotations.SerializedName

data class LoginResponses(
    @SerializedName("error")
    val isError: Boolean,
    @SerializedName("msg")
    val message: String,
    @SerializedName("token")
    val token: Token,
    @SerializedName("accessToken")
    val accessToken: String

)

data class Token(
    @SerializedName("id")
    val tokenId: Int,
    @SerializedName("name")
    val tokenName: String
)


data class LoginRequest(
    @SerializedName("username") val username :String,
    @SerializedName("password") val password: String
)