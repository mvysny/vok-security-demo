package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import eu.vaadinonkotlin.security.AllowRoles
import eu.vaadinonkotlin.vaadin10.asFilterBar
import eu.vaadinonkotlin.vaadin10.ilike
import eu.vaadinonkotlin.vaadin10.inRange
import eu.vaadinonkotlin.vaadin10.vokdb.dataProvider

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

                appendHeaderRow() // workaround for https://github.com/vaadin/vaadin-grid-flow/issues/912
                val filterBar = appendHeaderRow().asFilterBar(this)

                addColumnFor(User::id) {
                    filterBar.forField(NumberRangePopup(), this).inRange()
                }
                addColumnFor(User::username) {
                    filterBar.forField(TextField(), this).ilike()
                }
                addColumnFor(User::roles) {
                    filterBar.forField(TextField(), this).ilike()
                }
                addColumnFor(User::hashedPassword)
            }
        }
    }
}
