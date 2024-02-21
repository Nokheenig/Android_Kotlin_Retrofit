package com.example.retrofit.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface JsonPlaceHolderApi {

    @GET("posts")
    fun getPosts(
        @Query("_sort") sort: String,
        @Query("_order") order: String,
        @Query("userId") vararg userId: Int,
    ) : Call<MutableList<Post>>

    @GET("posts")
    fun getPosts(
            @QueryMap parameters: Map<String, String>
    ) : Call<MutableList<Post>>

    @GET("posts/{id}/comments")
    fun getComments(@Path("id") postId: Int) : Call<MutableList<Comment>>

    @GET
    fun getComments(@Url url: String) : Call<MutableList<Comment>>
}