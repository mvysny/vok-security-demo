package com.vaadin.securitydemo

import com.github.vokorm.KEntity
import com.github.vokorm.findOneBy
import com.gitlab.mvysny.jdbiorm.Dao
import com.vaadin.flow.component.UI
import eu.vaadinonkotlin.security.simple.HasPassword
import eu.vaadinonkotlin.vaadin10.Session
import java.io.Serializable

/**
 * Represents an user. Stored in a database; see [Entity] and [Accessing Databases](http://www.vaadinonkotlin.eu/databases.html) for more details.
 * Implements the [HasPassword] helper interface which provides password hashing functionality. Remember to set the
 * password via [HasPassword.setPassword] and verify the password via [HasPassword.passwordMatches].
 * @property username user name, unique
 * @property roles comma-separated list of roles
 */
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

    /**
     * Logs in user with given [username] and [password]. Returns true on success, false on failure.
     */
    fun login(username: String, password: String): Boolean {
        val user = User.findByUsername(username) ?: return false
        if (!user.passwordMatches(password)) return false
        login(user)
        return true
    }

    /**
     * Logs in an [user]. Fails if the user is already logged in.
     */
    private fun login(user: User) {
        check(this.user == null) { "An user is already logged in" }
        this.user = user
        // this will cause the UI to be re-created. Since the user is now logged in and present in the session,
        // the UI should now initialize properly and should not show the LoginView.
        UI.getCurrent().page.reload()
    }

    /**
     * Logs out the user, clears the session and reloads the page.
     */
    fun logout() {
        Session.current.close()
        // The UI is recreated by the page reload, and since there is no user in the session (since it has been cleared),
        // the UI will show the LoginView.
        UI.getCurrent().navigate("")
        UI.getCurrent().page.reload()
    }

    fun getCurrentUserRoles(): Set<String> {
        val roles = user?.roles ?: return setOf()
        return roles.split(",").toSet()
    }

    fun isUserInRole(role: String): Boolean = getCurrentUserRoles().contains(role)

    fun isAdmin(): Boolean = isUserInRole("admin")
}

/**
 * This code will tie the [LoginManager] to the session. Make sure to always ask for the login manager via this property, never create it yourself.
 */
val Session.loginManager: LoginManager get() = getOrPut { LoginManager() }
