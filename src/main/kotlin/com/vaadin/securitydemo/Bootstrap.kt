package com.vaadin.securitydemo

import eu.vaadinonkotlin.VaadinOnKotlin
import eu.vaadinonkotlin.security.LoggedInUserResolver
import eu.vaadinonkotlin.security.loggedInUserResolver
import eu.vaadinonkotlin.sql2o.dataSource
import eu.vaadinonkotlin.sql2o.dataSourceConfig
import eu.vaadinonkotlin.vaadin10.Session
import org.flywaydb.core.Flyway
import org.h2.Driver
import org.slf4j.LoggerFactory
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

/**
 * Called by the Servlet Container to bootstrap your app. We need to bootstrap the Vaadin-on-Kotlin framework,
 * in order to have support for the database; then we'll run Flyway migration scripts, to make sure that the database is up-to-date.
 * After that's done, your app is ready to be serving client browsers.
 */
@WebListener
class Bootstrap: ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) = try {
        log.info("Starting up")

        // this will configure your database. For demo purposes, an in-memory embedded H2 database is used. To use a production-ready database:
        // 1. fill in the proper JDBC URL here
        // 2. make sure to include the database driver into the classpath, by adding a dependency on the driver into the build.gradle file.
        VaadinOnKotlin.dataSourceConfig.apply {
            driverClassName = Driver::class.java.name
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
        }

        // Initializes the VoK framework
        log.info("Initializing VaadinOnKotlin")
        VaadinOnKotlin.init()

        // Makes sure the database is up-to-date
        log.info("Running DB migrations")
        val flyway = Flyway.configure()
            .dataSource(VaadinOnKotlin.dataSource)
            .load()
        flyway.migrate()

        // setup security
        VaadinOnKotlin.loggedInUserResolver = object : LoggedInUserResolver {
            override fun isLoggedIn(): Boolean = Session.loginManager.isLoggedIn
            override fun getCurrentUserRoles(): Set<String> = Session.loginManager.getCurrentUserRoles()
        }
        User(username = "admin", roles = "admin,user").apply { setPassword("admin"); save() }
        User(username = "user", roles = "user").apply { setPassword("user"); save() }

        log.info("Initialization complete")
    } catch (t: Throwable) {
        log.error("Bootstrap failed!", t)
        throw t
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        log.info("Shutting down");
        log.info("Destroying VaadinOnKotlin")
        VaadinOnKotlin.destroy()
        log.info("Shutdown complete")
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(Bootstrap::class.java)
    }
}
