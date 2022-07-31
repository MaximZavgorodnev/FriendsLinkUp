package ru.maxpek.friendslinkup.api

import android.graphics.Bitmap
import retrofit2.Response
import retrofit2.http.*
import ru.maxpek.friendslinkup.auth.AuthState
import ru.maxpek.friendslinkup.dto.Post
import ru.maxpek.friendslinkup.dto.PushToken
import ru.maxpek.friendslinkup.dto.UserRequested
import java.io.File


interface ApiService {
//    @POST("users/push-tokens")
//    suspend fun save(@Body pushToken: PushToken): Response<Unit>

    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Post>

    //Аунтетификация пользователя
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun onSignIn(
        @Field("login") login: String,
        @Field("password") pass: String
    ): Response<AuthState>

    //Регистрация пользователя
    @FormUrlEncoded
    @POST("users/registration")
    suspend fun onSignUp(
        @Field("login") login: String,
        @Field("password") pass: String,
        @Field("name") name: String,
        @Field("file") file: File?
    ): Response<AuthState>

    //Получение одного пользователя
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): Response<UserRequested>


    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>

    @GET("posts/{id}/after")
    suspend fun getAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<Post>>
}