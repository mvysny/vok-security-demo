package com.vaadin.pwademo

import com.github.vok.framework.VaadinOnKotlin
import com.github.vok.framework.flow.Session
import com.github.vok.karibudsl.flow.div
import com.github.vok.karibudsl.flow.onLeftClick
import com.github.vok.security.AllowAllUsers
import com.github.vok.security.loggedInUserResolver
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcons
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.router.*
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
            // also it may be necessary to check the entire Router's layout chain?
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
            navMenuItem(VaadinIcons.NEWSPAPER, "Welcome") {
                onLeftClick {
                    navigateTo<WelcomeView>()
                }
            }
            navMenuItem(VaadinIcons.LIST, "User") {
                onLeftClick {
                    navigateTo<UserView>()
                }
            }
            navMenuItem(VaadinIcons.COG, "Admin") {
                onLeftClick {
                    navigateTo<AdminView>()
                }
            }
            navMenuItem(VaadinIcons.SIGN_OUT, "Log Out") {
                onLeftClick {
                    Session.loginManager.logout()
                }
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

inline fun <reified T: Component> navigateTo() {
    UI.getCurrent().apply {
        navigate(router.getUrl(T::class.java))
    }
}
inline fun <C, reified T> navigateTo(vararg params: C) where T: Component, T: HasUrlParameter<C> {
    require(params.isNotEmpty()) { "No parameters passed in" }
    UI.getCurrent().apply {
        val url: String = if (params.size == 1) router.getUrl(T::class.java, params[0]) else router.getUrl(T::class.java, params.toList())
        navigate(url)
    }
}
