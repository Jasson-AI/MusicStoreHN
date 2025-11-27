package com.example.musicstorehn.utils;

// File: app/src/main/java/com/uth/musicstorehn/utils/Constants.java

public class Constants {
    public static final String BASE_URL = "http://34.136.90.148/api/";

    public static final String LOGIN = "auth/login";
    public static final String REGISTER = "auth/register";
    public static final String VERIFY_EMAIL = "auth/verify";
    public static final String FORGOT_PASSWORD = "auth/forgot-password";
    public static final String RESET_PASSWORD = "auth/reset-password";

    public static final String GET_PROFILE = "user/profile";
    public static final String UPDATE_PROFILE = "user/update";
    public static final String UPLOAD_PROFILE_IMAGE = "user/upload-image";

    public static final String GET_GROUPS = "groups";
    public static final String CREATE_GROUP = "groups/create";
    public static final String JOIN_GROUP = "groups/join";
    public static final String GET_GROUP_MEMBERS = "groups/{id}/members";

    public static final String GET_MEDIA = "media";
    public static final String GET_USER_MEDIA = "media/user/{id}";
    public static final String GET_GROUP_MEDIA = "media/group/{id}";
    public static final String UPLOAD_MEDIA = "media/upload";
    public static final String DELETE_MEDIA = "media/{id}";

    public static final String UPDATE_FCM_TOKEN = "user/fcm-token";

    public static final String PREF_NAME = "MusicStorePrefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_AUTH_TOKEN = "auth_token";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    public static final int REQUEST_PERMISSION_AUDIO = 100;
    public static final int REQUEST_PERMISSION_STORAGE = 101;
    public static final int REQUEST_PICK_AUDIO = 200;
    public static final int REQUEST_PICK_VIDEO = 201;
    public static final int REQUEST_PICK_IMAGE = 202;
    public static final int REQUEST_RECORD_AUDIO = 300;
}
