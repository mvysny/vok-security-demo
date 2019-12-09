package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.h1
import com.github.mvysny.karibudsl.v10.text
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.router.Route
import eu.vaadinonkotlin.security.AllowRoles

/**
 * Demoes a view intended for both users and admins.
 */
@Route("user", layout = MainLayout::class)
@AllowRoles("user", "admin")
class UserView : KComposite() {
    private val root = ui {
        verticalLayout {
            h1("Important content for users")
            text("A page intended for users only. Only users and admins can see this view.")
        }
    }
}
