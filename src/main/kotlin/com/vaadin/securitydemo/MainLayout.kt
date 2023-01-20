package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.router.RouterLayout
import com.vaadin.securitydemo.admin.AdminRoute
import com.vaadin.securitydemo.security.loginService
import com.vaadin.securitydemo.user.UserView
import com.vaadin.securitydemo.welcome.WelcomeView
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
                button(icon = VaadinIcon.FILE_REMOVE.create()) {
                    addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON)
                    onLeftClick {
                        Notification.show("A toast!", 3000, Notification.Position.BOTTOM_CENTER)
                    }
                }
            }

            drawer {
                verticalLayout {
                    routerLink(VaadinIcon.NEWSPAPER, "Welcome", WelcomeView::class)
                    routerLink(VaadinIcon.LIST, "User", UserView::class)
                    routerLink(VaadinIcon.COG, "Admin", AdminRoute::class)
                    button("Log Out", VaadinIcon.SIGN_OUT.create()) {
                        onLeftClick { Session.loginService.logout() }
                    }
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
