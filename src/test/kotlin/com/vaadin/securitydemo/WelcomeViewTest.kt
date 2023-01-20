package com.vaadin.securitydemo

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.securitydemo.admin.login
import com.vaadin.securitydemo.admin.usingApp

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class WelcomeViewTest : DynaTest({
    usingApp()

    test("logged out user should not be able to see WelcomeView") {
        navigateTo<WelcomeView>()
        _expectOne<LoginView>()
    }

    test("User should see WelcomeView") {
        login("user")
        navigateTo<WelcomeView>()
        _expectOne<WelcomeView>()
    }

    test("admin should see WelcomeView") {
        login("admin")
        navigateTo<WelcomeView>()
        _expectOne<WelcomeView>()
    }
})
