package com.example.testapp.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLogin(
    var login: String,
    var token: String,
    var fullName: String,

)
