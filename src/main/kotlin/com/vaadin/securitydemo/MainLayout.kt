package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.div
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.vaadin.flow.component.HasElement
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
import com.vaadin.securitydemo.components.*
import eu.vaadinonkotlin.vaadin10.Session
import eu.vaadinonkotlin.vaadin10.VokSecurity

/**
 * The main layout. It uses the app-layout component which makes the app look like an Android Material app. See [AppHeaderLayout]
 * for more details.
 */
@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
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

    private lateinit var content: Div
    private val root = ui {
        appHeaderLayout {
            appHeader {
                appToolbar {
                    title.text = "Vaadin Kotlin Security Demo"
                    paperIconButton(VaadinIcon.FILE_REMOVE) {
                        addClickListener {
                            Notification.show("A toast!", 3000, Notification.Position.BOTTOM_CENTER)
                        }
                    }
                }
            }
            appDrawer {
                navItem(VaadinIcon.NEWSPAPER, "Welcome", WelcomeView::class)
                navItem(VaadinIcon.LIST, "User", UserView::class)
                navItem(VaadinIcon.COG, "Admin", AdminView::class)
                navItemClickable(VaadinIcon.SIGN_OUT, "Log Out") {
                    onLeftClick { Session.loginManager.logout() }
                }
            }
            content = div {
                setSizeFull(); classNames.add("app-content")
            }
        }
    }

    override fun showRouterLayoutContent(content: HasElement) {
        this.content.element.appendChild(content.element)
    }
}
