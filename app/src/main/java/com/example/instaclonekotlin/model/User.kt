package com.example.instaclonekotlin.model

class User {

    var uid: String = ""
    var fullName: String = ""
    var email: String = ""
    var password: String = ""
    var userImg: String = ""

    var device_id = ""
    var device_type = "A"
    var device_token = ""

    var isFollowed: Boolean = false

    constructor(fullName: String, email: String) {
        this.fullName = fullName
        this.email = email
    }
    constructor(fullName: String, email: String, userImg: String) {
        this.fullName = fullName
        this.email = email
        this.userImg = userImg
    }
    constructor(fullName: String, email: String, password: String, userImg: String) {
        this.fullName = fullName
        this.email = email
        this.userImg = userImg
        this.password = password
    }
}