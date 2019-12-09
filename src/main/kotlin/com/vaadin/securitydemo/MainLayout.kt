package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import eu.vaadinonkotlin.vaadin10.Session
import eu.vaadinonkotlin.vaadin10.VokSecurity

/**
 * The main layout. It uses the app-layout component which makes the app look like an Android Material app.
 */
@BodySize(width = "100vw", height = "100vh")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
class MainLayout : KComposite(), RouterLayout, BeforeEnterObserver {
    override fun beforeEnter(event: BeforeEnterEvent) {
        if (!Session.loginManager.isLoggedIn) {
            event.rerouteTo(LoginView::class.java)
        } else {
            VokSecurity.checkPermissionsOfView(event.navigationTarget)
        }
    }

    private lateinit var contentPane: Div
    private val root = ui {
        appLayout {
            isDrawerOpened = false

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
                    routerLink(VaadinIcon.COG, "Admin", AdminView::class)
                    button("Log Out", VaadinIcon.SIGN_OUT.create()) {
                        onLeftClick { Session.loginManager.logout() }
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
