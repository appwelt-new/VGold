package com.cognifygroup.vgold.loginImage

import retrofit2.Call
import retrofit2.http.POST

interface LoginImageService {
    @get:POST("fetch_login_image.php?")
    val image: Call<LoginImageModel?>?
}
