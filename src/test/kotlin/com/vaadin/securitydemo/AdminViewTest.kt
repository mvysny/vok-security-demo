package com.vaadin.securitydemo

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectThrows
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.router.NotFoundException
import eu.vaadinonkotlin.vaadin.Session

/**
 * Mocks the UI and logs in given user.
 */
fun login(username: String) {
    Session.loginManager.login(username, username)
    // check that there is no LoginForm and everything is prepared
    _expectNone<LoginForm>()
    // in fact, by default the WelcomeView should be displayed
    _expectOne<WelcomeView>()
}

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class AdminViewTest : DynaTest({
    usingApp()

    test("logged out user should not be able to see AdminView") {
        navigateTo<AdminView>()
        _expectOne<LoginView>()
    }

    test("Admin should see AdminView properly") {
        login("admin")
        navigateTo<AdminView>()
        _expectOne<AdminView>()
    }

    test("User should not see AdminView") {
        login("user")
        // this should not be allowed for security reasons:
        expectThrows<NotFoundException>("No route found for 'admin': No route found for 'admin'") {
            navigateTo<AdminView>()
        }
    }
})

private val routes = Routes().autoDiscoverViews("com.vaadin.securitydemo")

@DynaTestDsl
fun DynaNodeGroup.usingApp() {
    beforeGroup {
        Bootstrap().contextInitialized(null)
    }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup(routes) }
    afterEach { MockVaadin.tearDown() }
}
