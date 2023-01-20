package com.vaadin.securitydemo

import com.github.mvysny.vaadinsimplesecurity.SimpleViewAccessChecker
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import com.vaadin.securitydemo.security.LoginRoute
import com.vaadin.securitydemo.security.loginService
import eu.vaadinonkotlin.vaadin.Session

/**
 * Checks security and redirects to the LoginView if need be.
 */
class AppServiceInitListener : VaadinServiceInitListener {
    private val checker = SimpleViewAccessChecker.usingService { Session.loginService }
    init {
        checker.setLoginView(LoginRoute::class.java)
    }

    override fun serviceInit(e: ServiceInitEvent) {
        e.source.addUIInitListener { uiInitEvent -> uiInitEvent.ui.addBeforeEnterListener(checker) }
    }
}
