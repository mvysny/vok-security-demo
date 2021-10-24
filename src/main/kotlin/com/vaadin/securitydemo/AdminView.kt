package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.vokdataloader.Filter
import com.github.vokorm.dataloader.dataLoader
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import eu.vaadinonkotlin.security.AllowRoles
import eu.vaadinonkotlin.vaadin10.*
import eu.vaadinonkotlin.vaadin10.vokdb.setDataLoader

/**
 * The Administration view which only administrators may access. The administrator should be able to see/edit the list of users.
 */
@Route("admin", layout = MainLayout::class)
@AllowRoles("admin")
class AdminView : KComposite() {
    private val root = ui {
        verticalLayout {
            h1("Administration pages")
            grid<User> {
                setDataLoader(User.dataLoader)
                isExpand = true

                appendHeaderRow() // workaround for https://github.com/vaadin/vaadin-grid-flow/issues/912
                val filterBar: FilterBar<User, Filter<User>> = appendHeaderRow().asFilterBar(this)

                columnFor(User::id) {
                    filterBar.forField(NumberRangePopup(), this).inRange()
                }
                columnFor(User::username) {
                    filterBar.forField(TextField(), this).istartsWith()
                }
                columnFor(User::roles) {
                    filterBar.forField(TextField(), this).istartsWith()
                }
                columnFor(User::hashedPassword)
            }
        }
    }
}
