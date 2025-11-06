package com.plango.app.data.user

import com.google.gson.annotations.SerializedName

data class UserResponse(@SerializedName("publicId") val publicId:String,@SerializedName("nickname") val name:String, @SerializedName("mbti") val mbti:String )
