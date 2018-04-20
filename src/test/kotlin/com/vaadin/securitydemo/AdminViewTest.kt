package com.vaadin.securitydemo

import com.github.karibu.testing.MockVaadin
import com.github.karibu.testing.Routes
import com.github.karibu.testing._expectNone
import com.github.karibu.testing._get
import com.github.mvysny.dynatest.DynaTest
import com.github.vok.framework.flow.LoginForm
import com.github.vok.framework.flow.Session
import com.github.vok.karibudsl.flow.navigateToView
import com.github.vokorm.deleteAll
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI

val routes = Routes().autoDiscoverViews("com.vaadin.securitydemo").addErrorRoutes(AccessDeniedView::class.java)

/**
 * Mocks the UI and logs in given user.
 */
fun login(username: String) {
    MockVaadin.setup(routes)
    Session.loginManager.login(User.findByUsername(username)!!)
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
    beforeGroup { Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }

    test("Admin should see AdminView properly") {
        login("admin")
        navigateToView<AdminView>()
        _get<AdminView>()
    }

    test("User should not see AdminView") {
        login("user")
        // this should not be allowed for security reasons:
        navigateToView<AdminView>()
        // expect that an AccessDeniedView is shown
        _get<AccessDeniedView>()._get<Text> { text = "Access denied: Can not access AdminView, you are not admin" }
    }
})
