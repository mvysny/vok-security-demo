package com.vaadin.securitydemo.user

import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.securitydemo.AbstractAppTest
import com.vaadin.securitydemo.login
import com.vaadin.securitydemo.security.LoginRoute
import org.junit.jupiter.api.Test

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class UserRouteTest : AbstractAppTest() {
    @Test fun `logged out user should not be able to see UserView`() {
        navigateTo<UserRoute>()
        _expectOne<LoginRoute>()
    }

    @Test fun `User should see UserView`() {
        login("user")
        navigateTo<UserRoute>()
        _expectOne<UserRoute>()
    }

    @Test fun `admin should see UserView`() {
        login("admin")
        navigateTo<UserRoute>()
        _expectOne<UserRoute>()
    }
}