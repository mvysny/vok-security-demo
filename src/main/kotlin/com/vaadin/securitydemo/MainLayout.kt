package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.router.RouterLayout
import com.vaadin.securitydemo.admin.AdminRoute
import com.vaadin.securitydemo.components.navMenuBar
import com.vaadin.securitydemo.security.loginService
import com.vaadin.securitydemo.user.UserRoute
import com.vaadin.securitydemo.welcome.WelcomeRoute
import eu.vaadinonkotlin.vaadin.Session
import javax.annotation.security.PermitAll

/**
 * The main layout. It uses the app-layout component which makes the app look like an Android Material app.
 */
@PermitAll
class MainLayout : KComposite(), RouterLayout {

    private lateinit var contentPane: Div
    private val root = ui {
        appLayout {
            navbar {
                drawerToggle()
                h3("Vaadin Kotlin Security Demo")
            }

            drawer {
                navMenuBar {
                    addRoute(VaadinIcon.NEWSPAPER, WelcomeRoute::class)
                    addRoute(VaadinIcon.LIST, UserRoute::class)
                    addRoute(VaadinIcon.COG, AdminRoute::class)
                    addButton(VaadinIcon.SIGN_OUT, "Log Out") { Session.loginService.logout() }
                }
            }
            content {
                contentPane = div {
                    setSizeFull(); classNames.add("app-content")
                }
            }
        }
    }

    override fun showRouterLayoutContent(content: HasElement) {
        contentPane.element.appendChild(content.element)
    }
}
