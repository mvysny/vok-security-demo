package com.vaadin.securitydemo

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import com.vaadin.securitydemo.login.LoginView
import com.vaadin.securitydemo.security.User
import com.vaadin.securitydemo.security.loginService
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import eu.vaadinonkotlin.VaadinOnKotlin
import eu.vaadinonkotlin.security.LoggedInUserResolver
import eu.vaadinonkotlin.security.VokViewAccessChecker
import eu.vaadinonkotlin.security.loggedInUserResolver
import eu.vaadinonkotlin.vaadin.Session
import eu.vaadinonkotlin.vokdb.dataSource
import org.flywaydb.core.Flyway
import org.h2.Driver
import org.slf4j.LoggerFactory
import java.security.Principal
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
        val cfg = HikariConfig().apply {
            driverClassName = Driver::class.java.name
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            username = "sa"
            password = ""
        }
        VaadinOnKotlin.dataSource = HikariDataSource(cfg)

        // Initializes the VoK framework
        log.info("Initializing VaadinOnKotlin")
        VaadinOnKotlin.init()

        // Makes sure the database is up-to-date. See src/main/resources/db/migration for db init scripts.
        log.info("Running DB migrations")
        val flyway: Flyway = Flyway.configure()
            .dataSource(VaadinOnKotlin.dataSource)
            .load()
        flyway.migrate()

        // setup security
        VaadinOnKotlin.loggedInUserResolver = object : LoggedInUserResolver {
            override fun getCurrentUser(): Principal? = Session.loginService.getPrincipal()
            override fun getCurrentUserRoles(): Set<String> = Session.loginService.getCurrentUserRoles()
        }
        User(username = "admin", roles = "admin,user").apply { setPassword("admin"); save() }
        User(username = "user", roles = "user").apply { setPassword("user"); save() }

        log.info("Initialization complete")
    } catch (t: Throwable) {
        log.error("Bootstrap failed!", t)
        throw t
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        log.info("Shutting down")
        log.info("Destroying VaadinOnKotlin")
        VaadinOnKotlin.destroy()
        log.info("Shutdown complete")
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(Bootstrap::class.java)
    }
}

@PWA(name = "VoK Security Demo", shortName = "VoK Security Demo")
class AppShell: AppShellConfigurator

/**
 * Checks security and redirects to the LoginView if need be.
 */
class VaadinServiceInitListener : VaadinServiceInitListener {
    override fun serviceInit(e: ServiceInitEvent) {
        e.source.addUIInitListener { uiInitEvent ->
            val checker = VokViewAccessChecker()
            checker.setLoginView(LoginView::class.java)
            uiInitEvent.ui.addBeforeEnterListener(checker)
        }
    }
}
