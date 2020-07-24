package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom

class User private constructor(
    val firstName: String,
    val lastName: String?,
    email: String? = null,
    rawPhone: String? = null,
    meta: Map<String, Any>? = null
){

    private val fullName: String
        get() = listOfNotNull(firstName, lastName)
            .joinToString(" ")
            .capitalize()


    private val initials: String
        get() = listOfNotNull(firstName, lastName)
            .map { it.first().toUpperCase() }
            .joinToString(" ")

    var phone: String? = null
        set(value) {
            field = value?.replace("""[^+\d]""".toRegex(), "")
        }


    private var _login: String? = null
    var login: String
        set(value) {
            _login = value.toLowerCase()
        }
        get() = _login!!



    private var salt: String? = null

    private lateinit var passwordHash: String

    val userInfo: String

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    var accessCode: String? = null

    init {
        phone = rawPhone
        login = email ?: phone!!
        userInfo = """
          firstName: $firstName
          lastName: $lastName
          login: $login
          fullName: $fullName
          initials: $initials
          email: $email
          phone: $phone
          meta: $meta
        """.trimIndent()
        updateAccessCode().also {
            sendAccessCodeToUser(rawPhone ?: return@also, it)
        }
    }

    constructor(
        firstName: String,
        lastName: String?,
        email: String,
        password: String,
        meta: Map<String, Any>?,
        workaround: Int = 0
    ): this(firstName, lastName, email = email, meta = meta ?: mapOf("auth" to "password")){
        println("Secondary email constructor")
        passwordHash = encrypt(password)
    }

    constructor(
        firstName: String,
        lastName: String?,
        email: String?,
        rawPhone: String?,
        salt: String,
        passwordHash: String,
        meta: Map<String, Any>?
    ): this(firstName, lastName, email = email, rawPhone = rawPhone, meta = meta){
        this.salt = salt
        this.passwordHash = passwordHash
    }

    private fun generateAccessCode(): String {
        val possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklnopqrstuvwxyz0123456789"
        return StringBuilder().apply {
            repeat(6) {
                (possible.indices).random().also {
                    append(possible[it])
                }
            }
        }.toString()
    }

    fun updateAccessCode(): String {
        generateAccessCode().also {
            passwordHash = encrypt(it)
            accessCode = it
            return it
        }
    }

    fun checkPassword(pass: String): Boolean {
        return encrypt(pass) == passwordHash
    }

    private fun encrypt(password: String): String {
        return if (salt == null)
            ByteArray(16)
                .also { SecureRandom().nextBytes(it) }.toString()
                .also {
                    salt = it
                }.run {
                    this.plus(password).md5()
                }
        else
            salt.plus(password).md5()
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(toByteArray())
        val hexString = BigInteger(1, digest).toString(16)
        return hexString.padStart(32, '0')
    }

    private fun sendAccessCodeToUser(phone: String, code: String) {
        println("..... sending access code: $code on $phone")
    }

    companion object{

        fun makeUser(
            fullName: String,
            email: String? = null,
            password: String? = null,
            phone: String? = null,
            salt: String? = null,
            passwordHash: String? = null,
            meta: Map<String, Any>? = null
        ): User{

            val (firstName, lastName) = fullName.fullNameToPair()

            return when {
                !email.isNullOrBlank() && !salt.isNullOrBlank() && !passwordHash.isNullOrBlank() -> {
                    User(
                        firstName = firstName,
                        lastName = lastName,
                        rawPhone = phone,
                        email = email,
                        passwordHash = passwordHash,
                        salt = salt,
                        meta = meta
                    )
                }
                !phone.isNullOrBlank() -> User(firstName, lastName, rawPhone = phone, meta = meta ?: mapOf("auth" to "sms"))
                !email.isNullOrBlank() && !password.isNullOrBlank() -> User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    meta = meta
                )
                else -> throw IllegalArgumentException("Email or phone must not be null or blank")
            }

        }

        private fun String.fullNameToPair(): Pair<String, String?>{
            return this.split(" ")
                .filter { it.isNotBlank() }
                .run {
                    when(size){
                        1 -> first() to null
                        2 -> first() to last()
                        else -> throw IllegalArgumentException("FullName must contain first name and last name, current result is $this")
                    }
                }
        }

    }

}

