package com.walnutvw.vehicle.entity

import com.walnutvw.vehicle.model.VehicleRepresentation
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "motorcycle")
class VehicleEntity(


    @Column(name = "vin")
    var vin: String,

    var make: String,

    var model: String,

    @Column(name = "purchase_date")
    var purchaseDate: LocalDate,

): UpdatableEntity(){
    fun toRepresentation(): VehicleRepresentation = let {
        VehicleRepresentation(
            vin = vin,
            make = make,
            model = model ,
            purchaseDate
        )
            .apply { id = it.id.toString() }
    }
}