package com.example.kukuh.try1

/**
 * Created by shiva on 31-01-2018.
 */
internal class User {
    var displayname: String? = null
    var email: String? = null
    var createdAt: Long = 0

    constructor() {}
    constructor(displayname: String?, email: String?, createdAt: Long) {
        this.displayname = displayname
        this.email = email
        this.createdAt = createdAt
    }

}