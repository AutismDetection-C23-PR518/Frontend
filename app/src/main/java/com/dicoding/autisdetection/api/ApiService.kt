package com.dicoding.autisdetection.api

import com.dicoding.autisdetection.responses.LoginRequest
import com.dicoding.autisdetection.responses.LoginResponses
import com.dicoding.autisdetection.responses.RegisterRequest
import com.dicoding.autisdetection.responses.RegisterResponses
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    @Headers("Content-Type: application/json")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponses>


    @POST("login")
    @Headers("Content-Type: application/json")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponses>

}