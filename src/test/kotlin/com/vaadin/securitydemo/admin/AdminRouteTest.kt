package com.vaadin.securitydemo.admin

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.router.NotFoundException
import com.vaadin.securitydemo.login
import com.vaadin.securitydemo.security.LoginRoute
import com.vaadin.securitydemo.usingApp

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class AdminRouteTest : DynaTest({
    usingApp()

    test("logged out user should not be able to see AdminView") {
        navigateTo<AdminRoute>()
        _expectOne<LoginRoute>()
    }

    test("Admin should see AdminView properly") {
        login("admin")
        navigateTo<AdminRoute>()
        _expectOne<AdminRoute>()
    }

    test("User should not see AdminView") {
        login("user")
        // this should not be allowed for security reasons:
        expectThrows<RuntimeException>("Exceptions handled by HasErrorParameter views are") {
            navigateTo<AdminRoute>()
        }
    }
})
