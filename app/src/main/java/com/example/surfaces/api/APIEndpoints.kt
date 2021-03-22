package com.example.surfaces.api

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET

interface APIEndpoints {
    @GET("/prod/livebarn-interview-project")
    fun getSports(): Call<JsonArray>
}