package com.vaadin.securitydemo

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.router.HasUrlParameter

// @todo remove when new Karibu-DSL is released

/**
 * Navigates to given view: `navigateToView<AdminView>()`
 */
inline fun <reified T: Component> navigateToView() {
    UI.getCurrent().apply {
        navigate(router.getUrl(T::class.java))
    }
}

/**
 * Navigates to given view with parameters: `navigateToView<DocumentView>(25L)`
 */
inline fun <C, reified T> navigateToView(vararg params: C) where T: Component, T: HasUrlParameter<C> {
    require(params.isNotEmpty()) { "No parameters passed in" }
    UI.getCurrent().apply {
        val url: String = if (params.size == 1) router.getUrl(T::class.java, params[0]) else router.getUrl(T::class.java, params.toList())
        navigate(url)
    }
}
