package ru.skillbranch.kotlinexample

import org.junit.Assert
import org.junit.Test
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom

class CsvTest {
    @Test
    fun parse_csv_users() {
        val users = listOf(
            " John Doe ;JohnDoe@unknow.com;[B@7fbe847c:91a3c589fd7bd0861d06b023bdaebe1c;;",
            "John Doe;JohnDoe@unknow.com;[B@7fbe847c:91a3c589fd7bd0861d06b023bdaebe1c;;",
            "John;JohnDoe@unknow.com;[B@7fbe847c:91a3c589fd7bd0861d06b023bdaebe1c;;",
            " John Doe ;JohnDoe@unknow.com;[B@7fbe847c:91a3c589fd7bd0861d06b023bdaebe1c;+7 (917) 971 11-11;",
            " John Doe ;;[B@7fbe847c:91a3c589fd7bd0861d06b023bdaebe1c;+7 (917) 971 11-11;"
        )
        UserHolder.importUsers(users).forEach {
            Assert.assertNotNull(UserHolder.loginUser(it.login, "123456"))
        }

    }

    @Test
    fun md5() {
        val salt = salt()
        println(salt)
        println(salt.plus("123456").md5())
    }

    private fun salt() = ByteArray(16).also { SecureRandom().nextBytes(it) }.toString()

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(toByteArray())
        val hexString = BigInteger(1, digest).toString(16)
        return hexString.padStart(32, '0')
    }
}