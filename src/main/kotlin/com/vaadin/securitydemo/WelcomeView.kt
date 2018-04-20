package com.vaadin.securitydemo

import com.github.vok.karibudsl.flow.*
import com.github.vok.security.AllowAllUsers
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.Version
import org.intellij.lang.annotations.Language
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode

/**
 * The welcome view of the app, visible to all users. It is a vertical layout which lays out the child components vertically.
 *
 * The UI is defined by the means of so-called DSL; see [Karibu-DSL examples](https://github.com/mvysny/karibu-dsl#how-to-write-dsls-for-vaadin-8-and-vaadin8-v7-compat)
 * for more examples.
 *
 * Note that the `@Route` annotation defines the main layout with which this view is wrapped in. See [MainLayout] for details on how to
 * create an app-wide layout which hosts views.
 */
@Route("", layout = MainLayout::class)
@AllowAllUsers
class WelcomeView : VerticalLayout() {
    init {
        setSizeFull(); isPadding = false
        content { align(center, middle) }

        verticalLayout {
            content { align(center, middle) }
            isMargin = false; isSpacing = true
            h1("Yay! You're on Vaadin-on-Kotlin!")
            text("This is a welcome view for all users; all logged-in users can see this content")
            div { html("<strong>Vaadin version: </strong> ${Version.getFullVersion()}") }
            div { html("<strong>Kotlin version: </strong> ${KotlinVersion.CURRENT}") }
            div { html("<strong>JVM version: </strong> $jvmVersion") }
        }
    }
}

val jvmVersion: String get() = System.getProperty("java.version")
fun (@VaadinDsl HasComponents).html(@Language("html") html: String) {
    val doc: Element = Jsoup.parse(html).body()
    for (childNode in doc.childNodes()) {
        when(childNode) {
            is TextNode -> text(childNode.text())
            is Element -> add(Html(childNode.outerHtml()))
        }
    }
}
