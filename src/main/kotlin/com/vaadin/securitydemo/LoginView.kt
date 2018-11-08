package com.vaadin.securitydemo

import com.github.vok.framework.flow.LoginForm
import com.github.vok.framework.flow.Session
import com.github.vok.framework.flow.loginForm
import com.github.vok.karibudsl.flow.content
import com.github.vok.karibudsl.flow.text
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

/**
 * The login view which simply shows the login form full-screen. Allows the user to log in. After the user has been logged in,
 * the page is refreshed which forces the MainLayout to reinitialize. However, now that the user is present in the session,
 * the reroute to login view no longer happens and the MainLayout is displayed on screen properly.
 */
@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
@Route("login")
class LoginView : VerticalLayout(), BeforeEnterObserver {

    override fun beforeEnter(event: BeforeEnterEvent) {
        if (Session.loginManager.isLoggedIn) {
            event.rerouteTo("")
        }
    }

    private val loginForm: LoginForm
    init {
        setSizeFull(); isPadding = false; content { center() }

        loginForm = loginForm("VoK Security Demo") {
            classNames.add("loginform")
            text("Log in as user/user or admin/admin")
            onLogin { username, password ->
                if (!Session.loginManager.login(username, password)) {
                    usernameField.isInvalid = true
                    usernameField.errorMessage = "No such user or invalid password"
                    passwordField.isInvalid = true
                    passwordField.errorMessage = "No such user or invalid password"
                }
            }
        }
    }
}
