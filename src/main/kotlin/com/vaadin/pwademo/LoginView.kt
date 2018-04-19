package com.vaadin.pwademo

import com.github.vok.framework.flow.Session
import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import javax.validation.constraints.NotBlank

@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
@Route("login")
class LoginView : VerticalLayout() {

    // @todo extract to a nice LoginForm component and move into the vok's vok-framework-v10 module

    data class UsernamePassword(@field:NotBlank var username: String = "", @field:NotBlank var password: String = "")
    private val binder = beanValidationBinder<UsernamePassword>()
    private val usernameField: TextField
    private val passwordField: PasswordField

    init {
        binder.bean = UsernamePassword()
        h1("Welcome to vok-security-demo-v10!")
        text("Log in as user/user or admin/admin")
        usernameField = textField("Username") {
            bind(binder).asRequired().trimmingConverter().bind(UsernamePassword::username)
        }
        passwordField = passwordField("Password") {
            bind(binder).asRequired().trimmingConverter().bind(UsernamePassword::password)
        }
        button("Login") {
            onLeftClick { login() }
        }
    }

    private fun login() {
        if (binder.validate().isOk) {
            val user = User.findByUsername(binder.bean.username)
            if (user == null) {
                usernameField.isInvalid = true
                usernameField.errorMessage = "No such user"
            } else if (!user.passwordMatches(binder.bean.password)) {
                passwordField.isInvalid = true
                passwordField.errorMessage = "Incorrect password"
            } else {
                Session.loginManager.login(user)
            }
        }
    }
}
