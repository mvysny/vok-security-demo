package com.vaadin.securitydemo.welcome

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.securitydemo.admin.usingApp
import com.vaadin.securitydemo.login
import com.vaadin.securitydemo.security.LoginRoute

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class WelcomeRouteTest : DynaTest({
    usingApp()

    test("logged out user should not be able to see WelcomeView") {
        navigateTo<WelcomeRoute>()
        _expectOne<LoginRoute>()
    }

    test("User should see WelcomeView") {
        login("user")
        navigateTo<WelcomeRoute>()
        _expectOne<WelcomeRoute>()
    }

    test("admin should see WelcomeView") {
        login("admin")
        navigateTo<WelcomeRoute>()
        _expectOne<WelcomeRoute>()
    }
})