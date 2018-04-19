package com.vaadin.pwademo.components

import com.github.vok.karibudsl.flow.*
import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcons
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLink

@Tag("app-header")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
open class AppHeader : Component(), HasComponents, HasSize {
    init {
        element.setAttribute("fixed", "")
    }
}

fun (@VaadinDsl HasComponents).appHeader(block: (@VaadinDsl AppHeader).() -> Unit = {}) = init(AppHeader(), block)

@Tag("app-toolbar")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
open class AppToolbar : Component(), HasComponents {
    val title: Div

    init {
        paperIconButton(VaadinIcons.MENU) {
            element.setAttribute("onclick", "drawer.toggle()")
        }
        title = div {
            element.setAttribute("main-title", "")
        }
    }
}

fun (@VaadinDsl HasComponents).appToolbar(block: (@VaadinDsl AppToolbar).() -> Unit = {}) = init(AppToolbar(), block)

@Tag("app-drawer")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
class AppDrawer : Component(), HasComponents {
    init {
        element.setAttribute("swipe-open", "")
        element.setAttribute("id", "drawer")
    }
}

fun (@VaadinDsl HasComponents).appDrawer(block: (@VaadinDsl AppDrawer).() -> Unit = {}) = init(AppDrawer(), block)

/**
 * A demo of how to use Polymer components. This particular component is the
 * [app-layout component](https://www.webcomponents.org/element/PolymerElements/app-layout).
 */
@Tag("app-header-layout")
@HtmlImport("frontend://bower_components/app-layout/app-layout.html")
open class AppHeaderLayout : Component(), HasComponents, HasSize {
    init {
        setSizeFull(); element.setAttribute("fullbleed", "")
    }
}

// @todo extract this into a stand-alone project with two modules: the component itself, and the sample web app. The web app should be live on Heroku.
// @todo then refactor this project and also vaadin-kotlin-pwa

class ClickableAnchor : Anchor(), ClickNotifier

fun (@VaadinDsl HasComponents).navItem(
    icon: VaadinIcons? = null, text: String? = null, viewType: Class<out Component>,
    block: (@VaadinDsl RouterLink).() -> Unit = {}
): RouterLink =
    routerLink(icon, text, viewType) {
        highlightCondition = HighlightConditions.sameLocation()
        addClassName("navmenuitem")
        element.setAttribute("onclick", "drawer.toggle()")
        block()
    }

fun (@VaadinDsl HasComponents).navItemClickable(
    icon: VaadinIcons? = null, text: String? = null,
    block: (@VaadinDsl ClickableAnchor).() -> Unit = {}
): ClickableAnchor {
    val link = ClickableAnchor().apply {
        addClassName("navmenuitem")
        element.setAttribute("onclick", "drawer.toggle()")
    }
    if (icon != null) link.icon(icon)
    if (text != null) link.text(text)
    init(link, block)
    return link
}
