package ru.maxpek.friendslinkup.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.maxpek.friendslinkup.auth.AuthState
import ru.maxpek.friendslinkup.dto.*


interface ApiService {

//
    @POST("posts")
    suspend fun save(@Body postResponse: PostResponse): Response<PostResponse>
//

//

//


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
    suspend fun addMultimedia(@Part file: MultipartBody.Part?): Response<MediaResponse>

    //Добавление нового поста
    @POST("posts")
    suspend fun addPost(@Body post: PostCreateRequest): Response<PostResponse>

    //Удаление поста
    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>

    //Лайк посту
    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Int): Response<PostResponse>

    //Дизлайк посту
    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Int): Response<PostResponse>

    //Получение поста
    @GET("posts/{post_id}")
    suspend fun getPost(@Path("post_id") id: Int): Response<PostResponse>


    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<PostResponse>>

    @GET("posts/{id}/before")
    suspend fun getBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    @GET("posts/{id}/after")
    suspend fun getAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>




    //Загрузка всех постов
    @GET("posts")
    suspend fun getAll(): Response<List<PostResponse>>


    //Переход на мою страницу и загрузку постов
    @GET("my/wall/latest/")
    suspend fun getPostMyWallLatest(@Query("count") count: Int): Response<List<PostResponse>>

    @GET("my/wall/{id}/before")
    suspend fun getPostMyWallBefore(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    @GET("my/wall/{id}/after")
    suspend fun getPostMyWallAfter(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    //Переход на страницу пользователя
    @GET("{user_id}/jobs")
    suspend fun getUserJob (
        @Path("user_id") id: String
    ): Response<List<Job>>


    //Создание работы
    @POST("my/jobs")
    suspend fun addJob(@Body job: Job): Response<Job>

    //Удаление работы
    @DELETE("my/jobs/{job_id}")
    suspend fun deleteJob(@Path("job_id") id: Int): Response<Unit>

    //Получить список моих работ
    @GET("my/jobs")
    suspend fun getMyJob (): Response<MutableList<Job>>




}