package com.example.musicstorehn.network;

// File: app/src/main/java/com/uth/musicstorehn/network/ApiService.java

import com.example.musicstorehn.models.Group;
import com.example.musicstorehn.models.Media;
import com.example.musicstorehn.models.Response;
import com.example.musicstorehn.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/register")
    Call<Response<User>> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/login")
    Call<Response<User>> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/verify")
    Call<Response<String>> verifyEmail(
            @Field("email") String email,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("auth/forgot-password")
    Call<Response<String>> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("auth/reset-password")
    Call<Response<String>> resetPassword(
            @Field("email") String email,
            @Field("temp_password") String tempPassword,
            @Field("new_password") String newPassword
    );

    @GET("user/profile")
    Call<Response<User>> getProfile(@Header("Authorization") String token);

    @FormUrlEncoded
    @PUT("user/update")
    Call<Response<User>> updateProfile(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("bio") String bio
    );

    @Multipart
    @POST("user/upload-image")
    Call<Response<String>> uploadProfileImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("user/fcm-token")
    Call<Response<String>> updateFcmToken(
            @Header("Authorization") String token,
            @Field("fcm_token") String fcmToken
    );

    @GET("groups")
    Call<Response<List<Group>>> getGroups(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("groups/create")
    Call<Response<Group>> createGroup(
            @Header("Authorization") String token,
            @Field("name") String name,
            @Field("description") String description
    );

    @FormUrlEncoded
    @POST("groups/join")
    Call<Response<String>> joinGroup(
            @Header("Authorization") String token,
            @Field("group_id") int groupId
    );

    @GET("groups/{id}/members")
    Call<Response<List<User>>> getGroupMembers(
            @Header("Authorization") String token,
            @Path("id") int groupId
    );

    @GET("media")
    Call<Response<List<Media>>> getAllMedia(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("media/user/{id}")
    Call<Response<List<Media>>> getUserMedia(
            @Header("Authorization") String token,
            @Path("id") int userId
    );

    @GET("media/group/{id}")
    Call<Response<List<Media>>> getGroupMedia(
            @Header("Authorization") String token,
            @Path("id") int groupId
    );

    @Multipart
    @POST("media/upload")
    Call<Response<Media>> uploadMedia(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("file_type") RequestBody fileType,
            @Part("is_private") RequestBody isPrivate,
            @Part("group_id") RequestBody groupId,
            @Part MultipartBody.Part file
    );

    @DELETE("media/{id}")
    Call<Response<String>> deleteMedia(
            @Header("Authorization") String token,
            @Path("id") int mediaId
    );
}

