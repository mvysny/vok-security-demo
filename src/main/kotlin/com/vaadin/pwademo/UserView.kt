package com.vaadin.pwademo

import com.github.vok.karibudsl.flow.h1
import com.github.vok.karibudsl.flow.text
import com.github.vok.security.AllowRoles
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

/**
 * Demoes a view intended for both users and admins.
 */
@Route("user", layout = MainLayout::class)
@AllowRoles("user", "admin")
class UserView : VerticalLayout() {
    init {
        h1("Important content for users")
        text("A page intended for users only. Only users and admins can see this view.")
    }
}
