package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import eu.vaadinonkotlin.security.AllowRoles
import eu.vaadinonkotlin.vaadin10.generateFilterComponents
import eu.vaadinonkotlin.vaadin10.sql2o.dataProvider

/**
 * The Administration view which only administrators may access. The administrator should be able to see/edit the list of users.
 */
@Route("admin", layout = MainLayout::class)
@AllowRoles("admin")
class AdminView : KComposite() {
    private val root = ui {
        verticalLayout {
            h1("Administration pages")
            grid<User>(dataProvider = User.dataProvider) {
                isExpand = true

                addColumnFor(User::id)
                addColumnFor(User::username)
                addColumnFor(User::roles)
                addColumnFor(User::hashedPassword)

                appendHeaderRow().generateFilterComponents(this, User::class)
            }
        }
    }
}
