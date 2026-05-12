package com.vaadin.securitydemo.admin

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.ktormvaadin.and
import com.github.mvysny.ktormvaadin.dataProvider
import com.github.mvysny.ktormvaadin.e
import com.github.mvysny.ktormvaadin.filter.FilterTextField
import com.github.mvysny.ktormvaadin.filter.NumberRangePopup
import com.github.mvysny.ktormvaadin.filter.between
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.securitydemo.MainLayout
import com.vaadin.securitydemo.security.User
import com.vaadin.securitydemo.security.Users
import jakarta.annotation.security.RolesAllowed
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.support.postgresql.ilike

/**
 * The Administration view which only administrators may access. The administrator should be able to see/edit the list of users.
 */
@Route("admin", layout = MainLayout::class)
@PageTitle("Administration")
@RolesAllowed("ROLE_ADMIN")
class AdminRoute : KComposite() {
    private val dataProvider = Users.dataProvider
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

                columnFor(User::id, key = Users.id.e.key) {
                    idFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = idFilter
                }
                columnFor(User::username, key = Users.username.e.key) {
                    usernameFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = usernameFilter
                }
                columnFor(User::roles, key = Users.roles.e.key) {
                    rolesFilter.addValueChangeListener { updateFilter() }
                    filterBar.getCell(this).component = rolesFilter
                }
                addColumn { it.hashedPassword } .setHeader("Hashed Password")
            }
        }
    }

    private fun updateFilter() {
        val conditions = mutableListOf<ColumnDeclaring<Boolean>?>()
        conditions += Users.id.between(idFilter.value.asLongInterval())
        if (usernameFilter.value.isNotBlank()) {
            conditions += Users.username.ilike("${usernameFilter.value.trim()}%")
        }
        if (rolesFilter.value.isNotBlank()) {
            conditions += Users.roles.ilike("${rolesFilter.value.trim()}%")
        }
        dataProvider.setFilter(conditions.and())
    }
}
