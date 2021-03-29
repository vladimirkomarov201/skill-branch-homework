package ru.skillbranch.skillarticles.data.repositories

import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.local.PrefManager

object RootRepository {

    private val prefManager = PrefManager

    fun isAuth() : LiveData<Boolean> = prefManager.isAuth()
    fun setAuth(auth:Boolean) = prefManager.setAuth(auth)
}