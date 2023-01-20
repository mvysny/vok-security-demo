package com.vaadin.securitydemo.login

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setErrorMessage
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.router.Route
import com.vaadin.securitydemo.security.loginService
import eu.vaadinonkotlin.vaadin.Session
import org.slf4j.LoggerFactory
import javax.security.auth.login.LoginException

/**
 * The login view which simply shows the login form full-screen. Allows the user to log in. After the user has been logged in,
 * the page is refreshed which forces the MainLayout to reinitialize. However, now that the user is present in the session,
 * the reroute to login view no longer happens and the MainLayout is displayed on screen properly.
 */
@Route("login")
class LoginView : KComposite() {
    private lateinit var loginForm: LoginForm
    private val root = ui {
        verticalLayout {
            setSizeFull(); isPadding = false; content { center() }

            val loginI18n: LoginI18n = loginI18n {
                header.title =
                    "VoK Security Demo" // doesn't work at the moment: https://github.com/vaadin/flow/issues/15729
                additionalInformation = "Log in as user/user or admin/admin"
            }
            loginForm = loginForm(loginI18n)
        }
    }

    init {
        loginForm.addLoginListener { e ->
            try {
                Session.loginService.login(e.username, e.password)
            } catch (e: LoginException) {
                log.warn("Login failed", e)
                loginForm.setErrorMessage("Login failed", e.message)
            } catch (e: Exception) {
                log.error("Internal error", e)
                loginForm.setErrorMessage("Internal error", e.message)
            }
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(LoginView::class.java)
    }
}