package com.dicoding.autisdetection.api

import com.dicoding.autisdetection.responses.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    @Headers("Content-Type: application/json")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponses>


    @POST("login")
    @Headers("Content-Type: application/json")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponses>


    @GET("allpost")
    fun getAllPost(
        @Header("Authorization") token: String
    ): Call<List<StoryResponses>>



}