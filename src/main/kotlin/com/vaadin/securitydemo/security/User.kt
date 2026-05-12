package com.vaadin.securitydemo.security

import com.github.mvysny.ktormvaadin.ActiveEntity
import com.github.mvysny.ktormvaadin.db
import com.github.mvysny.vaadinsimplesecurity.HasPassword
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Column
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.io.Serializable

/**
 * Represents a user. Stored in a database via ktorm. Implements [HasPassword] which provides password hashing.
 * Set the password via [HasPassword.setPassword] and verify it via [HasPassword.passwordMatches].
 *
 * The DB column `hashedPassword` is exposed as the [passwordHash] ktorm-backed property to avoid a JVM signature
 * clash with [HasPassword.getHashedPassword] / [HasPassword.setHashedPassword] (a Kotlin `var hashedPassword`
 * would generate methods with the same JVM signature as the inherited Java ones).
 */
interface User : ActiveEntity<User>, HasPassword, Serializable {
    var id: Long?
    var username: String
    var passwordHash: String?
    var roles: String

    override fun getHashedPassword(): String? = passwordHash
    override fun setHashedPassword(hashedPassword: String?) {
        passwordHash = hashedPassword
    }

    override val table: Table<User> get() = Users

    val roleSet: Set<String> get() = roles.split(",").toSet()

    companion object : Entity.Factory<User>() {
        fun findByUsername(username: String): User? = db {
            database.sequenceOf(Users).filter { Users.username eq username }.firstOrNull()
        }
    }
}

object Users : Table<User>("users") {
    val id: Column<Long> = long("id").primaryKey().bindTo { it.id }
    val username: Column<String> = varchar("username").bindTo { it.username }
    val passwordHash: Column<String> = varchar("hashedPassword").bindTo { it.passwordHash }
    val roles: Column<String> = varchar("roles").bindTo { it.roles }
}
