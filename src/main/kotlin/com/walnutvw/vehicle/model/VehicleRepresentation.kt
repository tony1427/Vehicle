package com.walnutvw.vehicle.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.walnutvw.vehicle.config.NoArgConstructor
import com.walnutvw.vehicle.entity.VehicleEntity

import java.time.LocalDate
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@NoArgConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
data class VehicleRepresentation(
    @field:Size(max =17)
    var vin: String,
    @field:NotNull
    var make: String,
    @field:NotNull
    var model: String,
    @field:NotNull
    var purchaseDate: LocalDate

) : BaseRepresentation() {
    fun toEntity(): VehicleEntity = VehicleEntity(vin, make, model, purchaseDate)
}