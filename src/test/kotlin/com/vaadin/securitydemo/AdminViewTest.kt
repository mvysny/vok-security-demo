package com.vaadin.securitydemo

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributools.navigateTo
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.login.LoginForm
import eu.vaadinonkotlin.vaadin10.Session
import kotlin.test.expect

/**
 * Mocks the UI and logs in given user.
 */
fun login(username: String) {
    expect(true) { Session.loginManager.login(username, username) }
    UI.getCurrent().page.reload()
    // check that there is no LoginForm and everything is prepared
    _expectNone<LoginForm>()
    // in fact, by default the WelcomeView should be displayed
    _get<WelcomeView>()
}

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class AdminViewTest : DynaTest({
    usingApp()

    test("Admin should see AdminView properly") {
        login("admin")
        navigateTo<AdminView>()
        _get<AdminView>()
    }

    test("User should not see AdminView") {
        login("user")
        // this should not be allowed for security reasons:
        navigateTo<AdminView>()
        // expect that an AccessDeniedView is shown
        _get<AccessDeniedView>()._get<Text> { text = "Access denied: Route AdminView: Can not access AdminView, you are not admin" }
    }
})

fun DynaNodeGroup.usingApp() {
    lateinit var routes: Routes
    beforeGroup {
        routes = Routes().autoDiscoverViews("com.vaadin.securitydemo")
        Bootstrap().contextInitialized(null)
    }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup(routes) }
    afterEach { MockVaadin.tearDown() }
}