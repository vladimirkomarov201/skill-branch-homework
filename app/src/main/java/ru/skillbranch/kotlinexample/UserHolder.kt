package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {

    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User{
        return User.makeUser(fullName, email, password)
            .also {
                if (map[it.login] != null)
                    throw IllegalArgumentException("A user with this email already exists")
                else
                    map[it.login] = it
            }

    }

    fun login(login: String, password: String): String?{
        TODO("not implemented")
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User{
        val phone = rawPhone.replace("""[^+\d]""".toRegex(), "")
        when{
            phone.length != 12 -> throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
            map.filter { it.value.phone == phone }.isNotEmpty() -> throw IllegalArgumentException("A user with this phone already exists")
        }
        return User.makeUser(fullName, phone = rawPhone).also {
            map[phone] = it
        }
    }

    fun loginUser(login: String, password: String): String?{
        val key = if (login.startsWith("+")) login.replace("""[^+\d]""".toRegex(), "") else login
        map[key].also {
            return if (it == null || !it.checkPassword(password))
                null
            else
                it.userInfo
        }
    }

    fun requestAccessCode(login: String){
        val key = if (login.startsWith("+")) login.replace("""[^+\d]""".toRegex(), "") else login
        map[key]?.also {
            it.updateAccessCode()
        }.let {
            map[key] = it ?: return
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }

}