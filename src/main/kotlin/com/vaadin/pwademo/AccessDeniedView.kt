package com.vaadin.pwademo

import com.github.vok.karibudsl.flow.text
import com.github.vok.security.AccessRejectedException
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.ErrorParameter
import com.vaadin.flow.router.HasErrorParameter
import com.vaadin.flow.router.PageTitle
import javax.servlet.http.HttpServletResponse

@PageTitle("Access Denied")
class AccessDeniedView : VerticalLayout(), HasErrorParameter<AccessRejectedException> {
    override fun setErrorParameter(event: BeforeEnterEvent, parameter: ErrorParameter<AccessRejectedException>): Int {
        text("Access denied: ${parameter.exception.message}")
        return HttpServletResponse.SC_FORBIDDEN
    }
}
