package com.vaadin.securitydemo

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import com.vaadin.flow.server.VaadinService
import com.vaadin.securitydemo.security.User
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach

private val routes = Routes().autoDiscoverViews("com.vaadin.securitydemo")

abstract class AbstractAppTest {
    companion object {
        @BeforeAll @JvmStatic fun startApp() { Bootstrap().contextInitialized(null) }
        @AfterAll @JvmStatic fun stopApp() { User.deleteAll(); Bootstrap().contextDestroyed(null) }
    }
    @BeforeEach fun setupVaadin() { MockVaadin.setup(routes) }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    protected val isProductionMode: Boolean get() = VaadinService.getCurrent().deploymentConfiguration.isProductionMode
}
