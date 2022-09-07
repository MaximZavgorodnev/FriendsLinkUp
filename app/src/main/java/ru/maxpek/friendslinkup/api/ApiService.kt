package ru.maxpek.friendslinkup.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.maxpek.friendslinkup.auth.AuthState
import ru.maxpek.friendslinkup.dto.MediaResponse
import ru.maxpek.friendslinkup.dto.PostCreateRequest
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested


interface ApiService {
//    @POST("users/push-tokens")
//    suspend fun save(@Body pushToken: PushToken): Response<Unit>


//
//    @GET("posts/{id}/newer")
//    suspend fun getNewer(@Path("id") id: Long): Response<List<PostResponse>>
//
//    @GET("posts/{id}")
//    suspend fun getById(@Path("id") id: Long): Response<PostResponse>
//
    @POST("posts")
    suspend fun save(@Body postResponse: PostResponse): Response<PostResponse>
//
//    @DELETE("posts/{id}")
//    suspend fun removeById(@Path("id") id: Long): Response<Unit>
//
//    @POST("posts/{id}/likes")
//    suspend fun likeById(@Path("id") id: Long): Response<PostResponse>
//
//    @DELETE("posts/{id}/likes")
//    suspend fun dislikeById(@Path("id") id: Long): Response<PostResponse>

    //Аунтетификация пользователя
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun onSignIn(
        @Field("login") login: String,
        @Field("password") pass: String
    ): Response<AuthState>

    //Регистрация пользователя без аватарки
    @FormUrlEncoded
    @POST("users/registration")
    suspend fun onSignUpNoAva(
        @Field("login") login: String,
        @Field("password") pass: String,
        @Field("name") name: String,
        @Field("file") file: MultipartBody.Part?
    ): Response<AuthState>

    //Регистрация с аватаркой
    @Multipart
    @POST("users/registration")
    suspend fun onSignUpHasAva(
        @Part("login") login: RequestBody,
        @Part("password") pass: RequestBody,
        @Part("name") name: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<AuthState>

    //Получение одного пользователя
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): Response<UserRequested>

    //Получения списка зарегистрированных пользователей
    @GET("users")
    suspend fun getUsers(): Response<List<UserRequested>>

    //Добавление мультимедии(картинка, видео, музыка)
    @Multipart
    @POST("media")
    suspend fun addMultimedia(
        @Part("Authorization") token: String,
        @Part file: MultipartBody.Part?
    ): Response<MediaResponse>

    //Добавление нового поста
    @POST("posts")
    suspend fun addPost(
        @Header("Authorization") token: String,
        @Body post: PostCreateRequest,
    ): Response<PostResponse>

    //Загрузка всех постов
    @GET("posts")
    suspend fun getAll(): Response<List<PostResponse>>



    @GET("posts/latest")
    suspend fun getLatest(
        @Header("Authorization") token: String,
        @Query("count") count: Int): Response<List<PostResponse>>

    @GET("posts/{id}/before")
    suspend fun getBefore(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    @GET("posts/{id}/after")
    suspend fun getAfter(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>
}