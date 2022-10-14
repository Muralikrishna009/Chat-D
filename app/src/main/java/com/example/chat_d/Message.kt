package com.example.chat_d

class Message {

    var senderId: String? = null
    var message: String? = null

    constructor(){}

    constructor(message: String, senderId : String){
        this.senderId = senderId
        this.message = message
    }
}