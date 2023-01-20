package com.vaadin.securitydemo

import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import com.vaadin.securitydemo.security.LoginRoute
import eu.vaadinonkotlin.security.VokViewAccessChecker

/**
 * Checks security and redirects to the LoginView if need be.
 */
class AppServiceInitListener : VaadinServiceInitListener {
    private val checker = VokViewAccessChecker()
    init {
        checker.setLoginView(LoginRoute::class.java)
    }

    override fun serviceInit(e: ServiceInitEvent) {
        e.source.addUIInitListener { uiInitEvent -> uiInitEvent.ui.addBeforeEnterListener(checker) }
    }
}
