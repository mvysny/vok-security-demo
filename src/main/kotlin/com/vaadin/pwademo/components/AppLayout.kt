package com.vaadin.pwademo.components

import com.github.vok.karibudsl.flow.VaadinDsl
import com.github.vok.karibudsl.flow.div
import com.github.vok.karibudsl.flow.init
import com.github.vok.karibudsl.flow.themes
import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcons

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

// @todo when the button is clicked, hide the app-drawer!
class NavMenuItem(icon: VaadinIcons, caption: String) : Button(caption, Icon(icon)) {
    init {
        classNames.add("navmenuitem")
        themes.add("large")
    }
    var selected: Boolean
        get() = classNames.contains("selected")
        set(value) { classNames.set("selected", value) }
}

// @todo replace with RouterLink: make it navigate to particular view and auto-select based on URL
fun (@VaadinDsl HasComponents).navMenuItem(icon: VaadinIcons, caption: String, block: (@VaadinDsl NavMenuItem).() -> Unit = {}) = init(NavMenuItem(icon, caption), block)

// @todo extract this into a stand-alone project with two modules: the component itself, and the sample web app. The web app should be live on Heroku.
// @todo then refactor this project and also vaadin-kotlin-pwa
