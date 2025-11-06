package com.plango.app.data.user

import com.google.gson.annotations.SerializedName

data class UserRequest(@SerializedName("nickname") val name:String, @SerializedName("mbti") val mbti:String )
