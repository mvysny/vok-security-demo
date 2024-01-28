package com.vaadin.securitydemo.admin

import com.github.mvysny.karibudsl.v10.*
import com.github.vokorm.exp
import com.gitlab.mvysny.jdbiorm.vaadin.filter.FilterTextField
import com.gitlab.mvysny.jdbiorm.vaadin.filter.NumberRangePopup
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.securitydemo.MainLayout
import com.vaadin.securitydemo.security.User
import eu.vaadinonkotlin.vaadin.*
import eu.vaadinonkotlin.vaadin.vokdb.dataProvider
import jakarta.annotation.security.RolesAllowed

/**
 * The Administration view which only administrators may access. The administrator should be able to see/edit the list of users.
 */
@Route("admin", layout = MainLayout::class)
@PageTitle("Administration")
@RolesAllowed("ROLE_ADMIN")
class AdminRoute : KComposite() {
    private val root = ui {
        verticalLayout {
            h1("Administration pages")
            grid<User>(User.dataProvider) {
                isExpand = true

                appendHeaderRow() // workaround for https://github.com/vaadin/vaadin-grid-flow/issues/912
                val filterBar: FilterBar<User> = appendHeaderRow().asFilterBar(this)

                columnFor(User::id) {
                    setSortProperty(User::id.exp)
                    filterBar.forField(NumberRangePopup(), this).inRange()
                }
                columnFor(User::username) {
                    setSortProperty(User::username.exp)
                    filterBar.forField(FilterTextField(), this).istartsWith()
                }
                columnFor(User::roles) {
                    setSortProperty(User::roles.exp)
                    filterBar.forField(FilterTextField(), this).istartsWith()
                }
                addColumn { it.hashedPassword } .setHeader("Hashed Password")
            }
        }
    }
}
