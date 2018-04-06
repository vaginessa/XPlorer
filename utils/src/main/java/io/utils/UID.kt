package io.utils

import java.security.SecureRandom

object UID {
    private const val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    private const val alphabetLength = alphabet.length
    private val rnd = SecureRandom()

    fun randomUid(length: Int): String {
        val sb = StringBuilder(length)
        for (i in 0 until length)
            sb.append(alphabet[rnd.nextInt(alphabetLength)])
        return sb.toString()
    }
}