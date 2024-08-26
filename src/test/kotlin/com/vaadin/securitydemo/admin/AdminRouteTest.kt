package com.vaadin.securitydemo.admin

import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.router.AccessDeniedException
import com.vaadin.securitydemo.AbstractAppTest
import com.vaadin.securitydemo.login
import com.vaadin.securitydemo.security.LoginRoute
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.expect

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class AdminRouteTest : AbstractAppTest() {
    @Test fun `logged out user should not be able to see AdminView`() {
        navigateTo<AdminRoute>()
        _expectOne<LoginRoute>()
    }

    @Test fun `Admin should see AdminView properly`() {
        login("admin")
        navigateTo<AdminRoute>()
        _expectOne<AdminRoute>()
    }

    @Test fun `User should not see AdminView`() {
        login("user")
        // this should not be allowed for security reasons:
        val ex = assertThrows<AccessDeniedException> {
            navigateTo<AdminRoute>()
        }
        // When Vaadin is in production mode, the error message is suppressed,
        // to not give potential attacker useful information.
        expect(if (isProductionMode) "" else "Access is denied by annotations on the view.") { ex.message }
    }
}
