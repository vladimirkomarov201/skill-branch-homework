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

    fun importUsers(list: List<String>): List<User>{
        val fullNameIndex = 0
        val emailIndex = 1
        val saltAndHashIndex = 2
        val phoneIndex = 3
        return list.map {
            val userString = it.split(";").map {stringItem ->
                stringItem.trim()
            }
            var salt: String? = null
            var hash: String? = null
            userString.getOrNull(saltAndHashIndex)?.split(":")?.let {saltAndHash ->
                salt = saltAndHash.getOrNull(0)
                hash = saltAndHash.getOrNull(1)
            }
            User.makeUser(
                fullName = userString.getOrNull(fullNameIndex) ?: "",
                phone = userString.getOrNull(phoneIndex).run {
                    if (this.isNullOrEmpty()) null else this
                },
                email = userString.getOrNull(emailIndex),
                salt = salt,
                passwordHash = hash,
                meta = mapOf("src" to "csv")
            ).also {user ->
                map[user.login] = user
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }

}