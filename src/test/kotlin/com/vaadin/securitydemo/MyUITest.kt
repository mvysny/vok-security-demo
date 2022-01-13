package com.vaadin.securitydemo

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.login.LoginForm
import eu.vaadinonkotlin.vaadin.Session
import kotlin.test.expect

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class MyUITest : DynaTest({
    usingApp()

    test("unsuccessful login") {
        _expectOne<LoginView>() // check that initially the LoginView is displayed
        _get<LoginForm>()._login("invaliduser", "invaliduser")
        expect(false) { Session.loginManager.isLoggedIn }
        expect(true) { _get<LoginForm>().isError }
    }

    test("successful login") {
        _expectOne<LoginView>() // check that initially the LoginView is displayed
        _get<LoginForm>()._login("user", "user")
        expect(true) { Session.loginManager.isLoggedIn }
        _expectNone<LoginView>()
        // after successful login the WelcomeView should be displayed
        _get<WelcomeView>()
    }

    test("error route not hijacked by the LoginView") {
        UI.getCurrent().addBeforeEnterListener { e ->
            e.rerouteToError(RuntimeException("Simulated"), "Simulated")
        }
        navigateTo(WelcomeView::class)
        _expectInternalServerError("Simulated")
    }
})
