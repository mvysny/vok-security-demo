package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setErrorMessage
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import eu.vaadinonkotlin.vaadin10.Session
import javax.security.auth.login.LoginException

/**
 * The login view which simply shows the login form full-screen. Allows the user to log in. After the user has been logged in,
 * the page is refreshed which forces the MainLayout to reinitialize. However, now that the user is present in the session,
 * the reroute to login view no longer happens and the MainLayout is displayed on screen properly.
 */
@BodySize(width = "100vw", height = "100vh")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Route("login")
class LoginView : KComposite(), BeforeEnterObserver {
    override fun beforeEnter(event: BeforeEnterEvent) {
        if (Session.loginManager.isLoggedIn) {
            event.rerouteTo("")
        }
    }

    private lateinit var loginForm: LoginForm
    private val root = ui {
        verticalLayout {
            setSizeFull(); isPadding = false; content { center() }

            val loginI18n: LoginI18n = loginI18n {
                header.title = "VoK Security Demo"
                additionalInformation = "Log in as user/user or admin/admin"
            }
            loginForm = loginForm(loginI18n) {
                addLoginListener { e ->
                    try {
                        Session.loginManager.login(e.username, e.password)
                    } catch (e: LoginException) {
                        log.warn("Login failed", e)
                        setErrorMessage("Login failed", e.message)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        private val log = org.slf4j.LoggerFactory.getLogger(LoginView::class.java)
    }
}
