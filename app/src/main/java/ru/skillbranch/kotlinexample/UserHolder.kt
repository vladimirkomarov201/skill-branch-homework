package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {

    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User{
        TODO("not implemented")
    }

    fun login(login: String, password: String): String?{
        TODO("not implemented")
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User{
        TODO("not implemented")
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }

}