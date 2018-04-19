package com.vaadin.pwademo

import com.github.vok.framework.VaadinOnKotlin
import com.github.vok.framework.flow.Session
import com.github.vok.karibudsl.flow.div
import com.github.vok.karibudsl.flow.onLeftClick
import com.github.vok.security.AllowAllUsers
import com.github.vok.security.loggedInUserResolver
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcons
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import com.vaadin.pwademo.components.*

/**
 * The main layout. It uses the app-layout component which makes the app look like an Android Material app. See [AppHeaderLayout]
 * for more details.
 */
@BodySize(width = "100vw", height = "100vh")
@HtmlImport("frontend://styles.html")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@Theme(Lumo::class)
@AllowAllUsers
class MainLayout : AppHeaderLayout(), RouterLayout, BeforeEnterObserver {
    override fun beforeEnter(event: BeforeEnterEvent) {
        if (!Session.loginManager.isLoggedIn) {
            event.rerouteTo(LoginView::class.java)
        } else {
            // @todo move this into the vok-framework-v10 as VokSecurity.install(), and register the listener into the UI.
            // the code is not important, what's more important is the documentation
            // also it may be necessary to check the entire Router's layout chain? Probably not - the parent layouts can not be shown
            // on their own. Or can they? Perhaps evaluate all @Route-annotated parent layouts? Can this happen?
            VaadinOnKotlin.loggedInUserResolver!!.checkPermissionsOnClass(event.navigationTarget)
        }
    }

    private val content: Div
    init {
        appHeader {
            appToolbar {
                title.text = "Vaadin Kotlin PWA Demo"
                paperIconButton(VaadinIcons.FILE_REMOVE) {
                    addClickListener {
                        Notification.show("A toast!", 3000, Notification.Position.BOTTOM_CENTER)
                    }
                }
            }
        }
        appDrawer {
            navItem(VaadinIcons.NEWSPAPER, "Welcome", WelcomeView::class.java)
            navItem(VaadinIcons.LIST, "User", UserView::class.java)
            navItem(VaadinIcons.COG, "Admin", AdminView::class.java)
            navItemClickable(VaadinIcons.SIGN_OUT, "Log Out") {
                onLeftClick { Session.loginManager.logout() }
            }
        }
        content = div {
            setSizeFull(); classNames.add("app-content")
        }
    }

    override fun showRouterLayoutContent(content: HasElement) {
        this.content.element.appendChild(content.element)
    }
}
