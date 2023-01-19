package com.vaadin.securitydemo

import com.github.vokorm.KEntity
import com.github.vokorm.findOneBy
import com.gitlab.mvysny.jdbiorm.Dao
import com.gitlab.mvysny.jdbiorm.Table
import eu.vaadinonkotlin.security.simple.HasPassword
import eu.vaadinonkotlin.vaadin.Session

/**
 * Represents a user. Stored in a database; see [KEntity] and [Accessing Databases](http://www.vaadinonkotlin.eu/databases.html) for more details.
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
