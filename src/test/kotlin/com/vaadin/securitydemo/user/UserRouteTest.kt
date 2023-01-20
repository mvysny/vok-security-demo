package com.vaadin.securitydemo.user

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.securitydemo.login
import com.vaadin.securitydemo.security.LoginRoute
import com.vaadin.securitydemo.usingApp

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class UserRouteTest : DynaTest({
    usingApp()

    test("logged out user should not be able to see UserView") {
        navigateTo<UserRoute>()
        _expectOne<LoginRoute>()
    }

    test("User should see UserView") {
        login("user")
        navigateTo<UserRoute>()
        _expectOne<UserRoute>()
    }

    test("admin should see UserView") {
        login("admin")
        navigateTo<UserRoute>()
        _expectOne<UserRoute>()
    }
})