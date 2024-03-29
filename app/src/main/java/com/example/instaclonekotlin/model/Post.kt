package com.example.instaclonekotlin.model

class Post {
    var id: String = ""
    var caption: String = ""
    var postImg: String = ""
    var currentDate: String = ""

    var uid: String = ""
    var fullName: String = ""
    var userImg: String = ""

    var isLiked = false

    constructor(image: String) {
        this.postImg = image
    }

    constructor(caption: String, postImg: String) {
        this.caption = caption
        this.postImg = postImg
    }

    constructor(id: String, caption: String,postImg: String) {
        this.id = id
        this.caption = caption
        this.postImg = postImg
    }
}