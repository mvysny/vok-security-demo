package com.vaadin.securitydemo

import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.login.LoginForm
import eu.vaadinonkotlin.vaadin.Session

/**
 * Mocks the UI and logs in given user.
 */
fun login(username: String) {
    Session.loginService.login(username, username)
    // check that there is no LoginForm and everything is prepared
    _expectNone<LoginForm>()
    // in fact, by default the WelcomeView should be displayed
    _expectOne<WelcomeView>()
}
