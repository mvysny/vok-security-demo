package com.vaadin.securitydemo

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.text
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import eu.vaadinonkotlin.security.AccessRejectedException
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletResponse

@PageTitle("Access Denied")
class AccessDeniedView : KComposite(), HasErrorParameter<AccessRejectedException> {
    private val root = ui {
        verticalLayout()
    }
    override fun setErrorParameter(event: BeforeEnterEvent, parameter: ErrorParameter<AccessRejectedException>): Int {
        log.error("Access denied", parameter.exception)
        root.text("Access denied: ${parameter.exception.message}")
        return HttpServletResponse.SC_FORBIDDEN
    }
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(AccessDeniedView::class.java)
    }
}
