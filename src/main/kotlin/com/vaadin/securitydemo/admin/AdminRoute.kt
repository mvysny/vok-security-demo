package com.vaadin.securitydemo.admin

import com.github.mvysny.karibudsl.v10.*
import com.github.vokorm.exp
import com.gitlab.mvysny.jdbiorm.condition.Condition
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
    private val dataProvider = User.dataProvider
    private val idFilter = NumberRangePopup()
    private val usernameFilter = FilterTextField()
    private val rolesFilter = FilterTextField()

    private val root = ui {
        verticalLayout {
            h1("Administration pages")
            grid<User>(dataProvider) {
                isExpand = true

                appendHeaderRow() // workaround for https://github.com/vaadin/vaadin-grid-flow/issues/912
                val filterBar = appendHeaderRow()

                columnFor(User::id) {
                    setSortProperty(User::id.exp)
                    idFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = idFilter
                }
                columnFor(User::username) {
                    setSortProperty(User::username.exp)
                    usernameFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = usernameFilter
                }
                columnFor(User::roles) {
                    setSortProperty(User::roles.exp)
                    rolesFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = rolesFilter
                }
                addColumn { it.hashedPassword } .setHeader("Hashed Password")
            }
        }
    }

    private fun updateFilter() {
        var c: Condition = Condition.NO_CONDITION
        if (!idFilter.isEmpty) {
            c = c.and(idFilter.value.asLongInterval().contains(User::id.exp))
        }
        if (usernameFilter.value.isNotBlank()) {
            c = c.and(User::username.exp.startsWithIgnoreCase(usernameFilter.value))
        }
        if (rolesFilter.value.isNotBlank()) {
            c = c.and(User::roles.exp.startsWithIgnoreCase(rolesFilter.value))
        }
        dataProvider.filter = c
    }
}
