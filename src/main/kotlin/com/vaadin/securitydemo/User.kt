package com.vaadin.securitydemo

import com.github.vokorm.KEntity
import com.github.vokorm.findOneBy
import com.gitlab.mvysny.jdbiorm.Dao
import com.gitlab.mvysny.jdbiorm.Table
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinRequest
import com.vaadin.flow.server.VaadinService
import eu.vaadinonkotlin.security.BasicUserPrincipal
import eu.vaadinonkotlin.security.simple.HasPassword
import eu.vaadinonkotlin.vaadin.Session
import java.io.Serializable
import java.security.Principal
import javax.security.auth.login.FailedLoginException

/**
 * Represents an user. Stored in a database; see [KEntity] and [Accessing Databases](http://www.vaadinonkotlin.eu/databases.html) for more details.
 * Implements the [HasPassword] helper interface which provides password hashing functionality. Remember to set the
 * password via [HasPassword.setPassword] and verify the password via [HasPassword.passwordMatches].
 * @property username user name, unique
 * @property roles comma-separated list of roles
 */
@Table("users")
data class User(override var id: Long? = null,
                var username: String = "",
                override var hashedPassword: String = "",
                var roles: String = "") : KEntity<Long>, HasPassword {
    companion object : Dao<User, Long>(User::class.java) {
        /**
         * Finds user by his [username]. If there is no such user, returns `null`.
         */
        fun findByUsername(username: String): User? = findOneBy { User::username eq username }
    }
}

/**
 * Handles the logged-in user. Stored in a session per-user. Get the login manager instance via the [loginManager] property, never construct
 * it yourself.
 */
class LoginManager: Serializable {
    /**
     * The currently logged-in user, `null` if there is no user logged in.
     */
    var user: User? = null
    private set

    /**
     * `true` if the user is logged in (the [user] property is not null), `false` if not.
     */
    val isLoggedIn: Boolean get() = user != null

    fun getPrincipal(): Principal? {
        val user = user
        return if (user == null) null else BasicUserPrincipal(user.username)
    }

    /**
     * Logs in user with given [username] and [password]. Fails with [javax.security.auth.login.LoginException]
     * on failure.
     */
    fun login(username: String, password: String) {
        val user: User = User.findByUsername(username) ?: throw FailedLoginException("Invalid username or password")
        if (!user.passwordMatches(password)) {
            throw FailedLoginException("Invalid username or password")
        }
        login(user)
    }

    /**
     * Logs in given [user].
     */
    private fun login(user: User) {
        this.user = user

        // creates a new session after login, to prevent session fixation attack
        VaadinService.reinitializeSession(VaadinRequest.getCurrent())

        // navigate the user away from the LoginView and to the landing page.
        // all logged-in users must be able to see the landing page, otherwise they will
        // be redirected back to the LoginView.
        UI.getCurrent().navigate(WelcomeView::class.java)
    }

    /**
     * Logs out the user, clears the session and reloads the page.
     */
    fun logout() {
        Session.current.close()
        // The UI is recreated by the page reload, and since there is no user in the session (since it has been cleared),
        // the UI will show the LoginView.
        UI.getCurrent().page.reload()
    }

    fun getCurrentUserRoles(): Set<String> {
        val roles: String = user?.roles ?: return setOf()
        return roles.split(",").toSet()
    }

    fun isUserInRole(role: String): Boolean = getCurrentUserRoles().contains(role)

    fun isAdmin(): Boolean = isUserInRole("admin")
}

/**
 * This code will tie the [LoginManager] to the session. Make sure to always ask for the login manager via this property, never create it yourself.
 */
val Session.loginManager: LoginManager get() = getOrPut(LoginManager::class) { LoginManager() }
