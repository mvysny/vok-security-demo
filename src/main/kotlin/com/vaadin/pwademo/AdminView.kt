package com.vaadin.pwademo

import com.github.vok.framework.sql2o.vaadin.dataProvider
import com.github.vok.framework.sql2o.vaadin.generateFilterComponents
import com.github.vok.karibudsl.flow.addColumnFor
import com.github.vok.karibudsl.flow.grid
import com.github.vok.karibudsl.flow.h1
import com.github.vok.karibudsl.flow.isExpand
import com.github.vok.security.AllowRoles
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

/**
 * The Administration view which only administrators may access. The administrator should be able to see/edit the list of users.
 */
@Route("admin", layout = MainLayout::class)
@AllowRoles("admin")
class AdminView : VerticalLayout() {
    init {
        h1("Administration pages")
        grid<User>(dataProvider = User.dataProvider) {
            isExpand = true

            addColumnFor(User::id)
            addColumnFor(User::username)
            addColumnFor(User::roles)
            addColumnFor(User::hashedPassword)

            generateFilterComponents(User::class)
        }
    }
}
