package com.vaadin.securitydemo

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.securitydemo.security.User
import com.vaadin.securitydemo.security.loginService
import com.vaadin.securitydemo.welcome.WelcomeRoute
import eu.vaadinonkotlin.vaadin.Session

/**
 * Mocks the UI and logs in given user.
 */
fun login(username: String) {
    Session.loginService.login(username, username)
    // check that there is no LoginForm and everything is prepared
    _expectNone<LoginForm>()
    // in fact, by default the WelcomeView should be displayed
    _expectOne<WelcomeRoute>()
}

private val routes = Routes().autoDiscoverViews("com.vaadin.securitydemo")

@DynaTestDsl
fun DynaNodeGroup.usingApp() {
    beforeGroup { Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup(routes) }
    afterEach { MockVaadin.tearDown() }
}
