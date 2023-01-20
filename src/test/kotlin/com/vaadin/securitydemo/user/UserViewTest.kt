package com.vaadin.securitydemo.user

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.securitydemo.admin.usingApp
import com.vaadin.securitydemo.login
import com.vaadin.securitydemo.login.LoginView

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class UserViewTest : DynaTest({
    usingApp()

    test("logged out user should not be able to see UserView") {
        navigateTo<UserView>()
        _expectOne<LoginView>()
    }

    test("User should see UserView") {
        login("user")
        navigateTo<UserView>()
        _expectOne<UserView>()
    }

    test("admin should see UserView") {
        login("admin")
        navigateTo<UserView>()
        _expectOne<UserView>()
    }
})