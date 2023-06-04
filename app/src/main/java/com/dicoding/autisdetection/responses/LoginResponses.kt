package com.dicoding.autisdetection.responses

import com.google.gson.annotations.SerializedName

data class LoginResponses(
    @SerializedName("error")
    val isError: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Result,
    @SerializedName("accessToken")
    val accessToken: String

)

data class Result(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)


data class LoginRequest(
    @SerializedName("email") val email :String,
    @SerializedName("password") val password: String
)