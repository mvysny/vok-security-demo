package com.vaadin.securitydemo.welcome

import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.securitydemo.AbstractAppTest
import com.vaadin.securitydemo.login
import com.vaadin.securitydemo.security.LoginRoute
import org.junit.jupiter.api.Test

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class WelcomeRouteTest : AbstractAppTest() {
    @Test fun `logged out user should not be able to see WelcomeView`() {
        navigateTo<WelcomeRoute>()
        _expectOne<LoginRoute>()
    }

    @Test fun `User should see WelcomeView`() {
        login("user")
        navigateTo<WelcomeRoute>()
        _expectOne<WelcomeRoute>()
    }

    @Test fun `admin should see WelcomeView`() {
        login("admin")
        navigateTo<WelcomeRoute>()
        _expectOne<WelcomeRoute>()
    }
}
