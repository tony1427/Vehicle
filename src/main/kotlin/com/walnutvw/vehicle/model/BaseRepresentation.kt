package com.walnutvw.vehicle.model

import java.time.Instant

abstract class BaseRepresentation(
    var id: String? = null,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
    var updatedBy: String? = null

)
