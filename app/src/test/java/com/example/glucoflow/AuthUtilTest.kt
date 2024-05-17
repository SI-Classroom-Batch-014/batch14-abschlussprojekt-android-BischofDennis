package com.example.glucoflow

import com.example.glucoflow.testClass.AuthUtil
import org.junit.Assert
import org.junit.Test

//https://developer.android.com/training/testing/fundamentals?hl=de
class AuthUtilTest {


    @Test
    fun emptyEmail() {
        val result = AuthUtil().validateRegistrationInput(
            "",
            "123",
            //"123"
        )
        Assert.assertEquals(result, false)
    }

    @Test
    fun emptyPassword() {
        val result = AuthUtil().validateRegistrationInput(
            "Tester",
            "",
           // ""
        )
        Assert.assertEquals(result, false)
    }

    @Test
    fun passwordMismatch() {
        val result = AuthUtil().validateRegistrationInput(
            "Tester",
            "213",
            //"123"
        )
        Assert.assertEquals(result, false)
    }

    @Test
    fun passwordToShort() {
        val result = AuthUtil().validateRegistrationInput(
            "Tester",
            "1",
            //"1"
        )
        Assert.assertEquals(result, false)
    }

    @Test
    fun validInput() {
        val result = AuthUtil().validateRegistrationInput(
            "Tester",
            "123",
            //"123"
        )
        Assert.assertEquals(result, true)
    }
}