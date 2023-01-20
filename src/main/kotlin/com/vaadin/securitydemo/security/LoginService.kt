package com.vaadin.securitydemo.security

import com.github.mvysny.vaadinsimplesecurity.AbstractLoginService
import com.github.mvysny.vaadinsimplesecurity.SimpleUserWithRoles
import eu.vaadinonkotlin.vaadin.Session
import javax.security.auth.login.FailedLoginException

/**
 * Handles the logged-in user. Stored in a session per-user. Get the login manager instance via the [loginService] property, never construct
 * it yourself.
 */
class LoginService: AbstractLoginService<User>() {
    /**
     * Logs in user with given [username] and [password]. Fails with [javax.security.auth.login.LoginException]
     * on failure.
     */
    fun login(username: String, password: String) {
        val user: User = User.findByUsername(username)
            ?: throw FailedLoginException("Invalid username or password")
        if (!user.passwordMatches(password)) {
            throw FailedLoginException("Invalid username or password")
        }
        login(user)
    }

    override fun toUserWithRoles(user: User): SimpleUserWithRoles = SimpleUserWithRoles(user.username, user.roleSet)
}

/**
 * This code will tie the [LoginService] to the session. Make sure to always ask for the login manager via this property, never create it yourself.
 */
val Session.loginService: LoginService get() = getOrPut(LoginService::class) { LoginService() }
