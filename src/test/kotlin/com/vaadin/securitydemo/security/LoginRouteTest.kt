package com.vaadin.securitydemo.security

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.securitydemo.admin.usingApp
import com.vaadin.securitydemo.welcome.WelcomeRoute
import eu.vaadinonkotlin.vaadin.Session
import kotlin.test.expect

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class LoginRouteTest : DynaTest({
    usingApp()

    test("unsuccessful login") {
        _expectOne<LoginRoute>() // check that initially the LoginView is displayed
        _get<LoginForm>()._login("invaliduser", "invaliduser")
        expect(false) { Session.loginService.isLoggedIn }
        expect(true) { _get<LoginForm>().isError }
    }

    test("successful login") {
        _expectOne<LoginRoute>() // check that initially the LoginView is displayed
        _get<LoginForm>()._login("user", "user")
        expect(true) { Session.loginService.isLoggedIn }
        _expectNone<LoginRoute>()
        // after successful login the WelcomeView should be displayed
        _expectOne<WelcomeRoute>()
    }

    test("error route not hijacked by the LoginView") {
        UI.getCurrent().addBeforeEnterListener { e ->
            e.rerouteToError(RuntimeException("Simulated"), "Simulated")
        }
        navigateTo(WelcomeRoute::class)
        _expectInternalServerError("Simulated")
    }
})