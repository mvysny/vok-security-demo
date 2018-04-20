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
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
@Route("login")
class LoginView : VerticalLayout() {

    // @todo extract to a nice LoginForm component and move into the vok's vok-framework-v10 module
    private val loginForm: LoginForm

    init {
        setSizeFull(); isPadding = false; content { center() }

        loginForm = loginForm("VoK Security Demo") {
            classNames.add("loginform")
            text("Log in as user/user or admin/admin")
            onLogin { username, password ->
                val user = User.findByUsername(username)
                if (user == null) {
                    usernameField.isInvalid = true
                    usernameField.errorMessage = "No such user"
                } else if (!user.passwordMatches(password)) {
                    passwordField.isInvalid = true
                    passwordField.errorMessage = "Incorrect password"
                } else {
                    Session.loginManager.login(user)
                }
            }
        }
    }
}
