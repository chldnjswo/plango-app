package com.plango.app.data.user

import com.google.gson.annotations.SerializedName

data class UserReadResponse(@SerializedName("publicId") val publicId:String,@SerializedName("nickname") val name:String, @SerializedName("mbti") val mbti:String )
