package com.vaadin.securitydemo.welcome

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.VaadinVersion
import com.vaadin.flow.router.Route
import com.vaadin.securitydemo.MainLayout
import javax.annotation.security.PermitAll

/**
 * The welcome view of the app, visible to all users. It is a vertical layout which lays out the child components vertically.
 *
 * The UI is defined by the means of so-called DSL; see [Karibu-DSL examples](https://github.com/mvysny/karibu-dsl#how-to-write-dsls-for-vaadin-8-and-vaadin8-v7-compat)
 * for more examples.
 *
 * Note that the [Route] annotation defines the main layout with which this view is wrapped in. See [MainLayout] for details on how to
 * create an app-wide layout which hosts views.
 */
@Route("", layout = MainLayout::class)
@PermitAll
class WelcomeRoute : KComposite() {
    private val root = ui {
        verticalLayout {
            setSizeFull(); isPadding = false
            content { align(center, middle) }

            h1("Yay! You're on Vaadin-on-Kotlin!")
            text("This is a welcome view for all users; all logged-in users can see this content")
            div { html("<strong>Vaadin version: </strong> ${VaadinVersion.get}") }
            div { html("<strong>Kotlin version: </strong> ${KotlinVersion.CURRENT}") }
            div { html("<strong>JVM version: </strong> $jvmVersion") }
        }
    }
}

val jvmVersion: String get() = System.getProperty("java.version")
