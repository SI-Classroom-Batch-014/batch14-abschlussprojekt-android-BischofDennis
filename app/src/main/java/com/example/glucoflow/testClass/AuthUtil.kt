package com.example.glucoflow.testClass

class AuthUtil {

    /**
     * The input is not valid if ...
     *     1. username/password is empty
     *     2. password mismatch
     *     3. password size < 2
     */
    fun validateRegistrationInput(
        username: String,
        password: String,
        //passwordConfirm: String
    ): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            return false
        }
        //if (password != passwordConfirm) {
          //  return false
        //}
        if (password.count { it.isDigit() } < 2) {
            return false
        }
        return true
    }
}