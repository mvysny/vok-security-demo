package com.vaadin.pwademo

import com.github.vok.framework.flow.Session
import com.github.vok.karibudsl.flow.button
import com.github.vok.karibudsl.flow.onLeftClick
import com.github.vok.karibudsl.flow.text
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

class LoginView : VerticalLayout() {
    init {
        text("todo login")
        button("Login User") {
            onLeftClick {
                Session.loginManager.login(User.findByUsername("user")!!)
            }
        }
        button("Login Admin") {
            onLeftClick {
                Session.loginManager.login(User.findByUsername("admin")!!)
            }
        }
    }
}
