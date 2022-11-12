package com.walnutvw.vehicle.controller

import com.walnutvw.vehicle.controller.ApplicationConstants.FORWARD_SLASH
import com.walnutvw.vehicle.utils.formattedUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URI

fun entityCreatedResponse(identifier: String): ResponseEntity<Unit> {
    val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

    return ResponseEntity.created(
        URI.create(("${request.requestURL}$FORWARD_SLASH$identifier")
            .formattedUrl())).build()
}

fun entityNoContentResponse(): ResponseEntity <Unit>{
    return ResponseEntity.noContent().build()
}