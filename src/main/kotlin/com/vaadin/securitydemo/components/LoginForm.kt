package com.vaadin.securitydemo.components

import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import javax.validation.constraints.NotBlank

class LoginForm(appName: String) : VerticalLayout() {
    data class UsernamePassword(@field:NotBlank var username: String = "", @field:NotBlank var password: String = "")
    private val binder = beanValidationBinder<UsernamePassword>()
    val usernameField: TextField
    val passwordField: PasswordField
    private var loginHandler: (username: String, password: String)->Unit = { _, _ -> }

    fun onLogin(loginBlock: (username: String, password: String)->Unit) {
        this.loginHandler = loginBlock
    }

    init {
        binder.bean = UsernamePassword()
        h1("Welcome to $appName")
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
            loginHandler(binder.bean.username, binder.bean.password)
        }
    }
}

fun (@VaadinDsl HasComponents).loginForm(appName: String, block: (@VaadinDsl LoginForm).() -> Unit = {}) = init(LoginForm(appName), block)
