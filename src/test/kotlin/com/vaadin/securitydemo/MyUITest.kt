package com.vaadin.securitydemo

import com.github.karibu.testing.*
import com.github.mvysny.dynatest.DynaTest
import com.github.vok.framework.flow.Session
import com.github.vokorm.deleteAll
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import kotlin.test.expect

/**
 * Uses the [Karibu-Testing](https://github.com/mvysny/karibu-testing) library to test Vaadin-based apps.
 */
class MyUITest : DynaTest({
    beforeGroup { Bootstrap().contextInitialized(null) }
    afterGroup { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    beforeEach { MockVaadin.setup(routes) }

    test("unsuccessful login") {
        _get<LoginView>() // check that initially the LoginView is displayed
        _get<TextField> { caption = "Username" }._value = "invaliduser"
        _get<PasswordField> { caption = "Password" }._value = "invalidpass"
        _get<Button> { caption = "Login" }._click()
        expect(false) { Session.loginManager.isLoggedIn }
        expect("No such user") { _get<TextField> { caption = "Username" }.errorMessage }
    }

    test("successful login") {
        _get<LoginView>() // check that initially the LoginView is displayed
        _get<TextField> { caption = "Username" }._value = "user"
        _get<PasswordField> { caption = "Password" }._value = "user"
        _get<Button> { caption = "Login" }._click()
        expect(true) { Session.loginManager.isLoggedIn }
        _expectNone<LoginView>()
        // after successful login the WelcomeView should be displayed
        _get<WelcomeView>()
    }
})
