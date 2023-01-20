package com.vaadin.securitydemo.components

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.PageTitle
import kotlin.reflect.KClass

/**
 * Taken from the [AppLayout](https://vaadin.com/docs/latest/components/app-layout) component documentation.
 */
class NavMenuBar : Tabs(), AfterNavigationObserver {
    /**
     * Maps route class to its tab.
     */
    private val tabs = mutableMapOf<KClass<*>, Tab>()

    init {
        orientation = Orientation.VERTICAL
    }

    override fun afterNavigation(event: AfterNavigationEvent) {
        val routeClass = event.activeChain[0].javaClass.kotlin
        selectedTab = tabs[routeClass]
    }

    fun addRoute(icon: VaadinIcon, routeClass: KClass<out Component>, title: String = getRouteTitle(routeClass)) {
        val tab = tab {
            routerLink(viewType = routeClass) {
                navIcon(icon)
                span(title)
            }
        }
        tabs[routeClass] = tab
    }

    fun addButton(icon: VaadinIcon, title: String, clickListener: () -> Unit) {
        tab {
            div {
                navIcon(icon)
                span(title)
                addClickListener { clickListener() }
            }
        }
    }

    @VaadinDsl
    private fun (@VaadinDsl HasComponents).navIcon(icon: VaadinIcon): Icon = icon(icon) {
        style.apply {
            set("box-sizing", "border-box")
            set("margin-inline-end", "var(--lumo-space-m)")
            set("margin-inline-start", "var(--lumo-space-xs)")
            set("padding", "var(--lumo-space-xs)")
        }
    }

    companion object {
        fun getRouteTitle(routeClass: KClass<out Component>): String =
            routeClass.java.getAnnotation(PageTitle::class.java)?.value ?: routeClass.java.simpleName
    }
}

@VaadinDsl
fun (@VaadinDsl HasComponents).navMenuBar(
    block: (@VaadinDsl NavMenuBar).() -> Unit = {}
): NavMenuBar = init(NavMenuBar(), block)
