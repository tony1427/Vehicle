package com.walnutvw.vehicle.controller


import com.github.fge.jsonpatch.JsonPatch
import com.walnutvw.vehicle.model.VehicleRepresentation
import com.walnutvw.vehicle.service.VehicleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.DeleteMapping




@RestController
@RequestMapping("vehicles")
class VehicleController(
    private val vehicleService: VehicleService
) {

    @GetMapping("/{id}")
    fun getVehicles(@PathVariable id: String): VehicleRepresentation {
        return vehicleService.getVehicle(id)
    }

    @GetMapping
    fun getAllVehicles(): List<VehicleRepresentation> {
        return vehicleService.getVehicles()
    }

    @PostMapping
    fun createVehicles(@RequestBody vehicleRepresentation: VehicleRepresentation): ResponseEntity<Unit> {
        println(vehicleRepresentation)
        val vehicle = vehicleService.createVehicle(vehicleRepresentation)
        return entityCreatedResponse(vehicle.id!!)
    }

    @PutMapping("/{id}")
    fun updateVehicle(
        @PathVariable id: String,
        @RequestBody vehicleRepresentation: VehicleRepresentation
    ): ResponseEntity<Unit> {
        println(vehicleRepresentation)

        vehicleService.updateVehicle(id, vehicleRepresentation)
        return entityNoContentResponse()
    }

    @PatchMapping("/{id}")
    fun updateVehicle(@PathVariable id: String,@RequestBody map: Map<String, String>): ResponseEntity<Unit> {
        vehicleService.updateVehicle(id, map)

        return entityNoContentResponse()
    }

    @PatchMapping("/patch/{id}", consumes = ["application/json-patch+json"])
    fun patchVehicle(@PathVariable id: String, @RequestBody patch: JsonPatch): ResponseEntity<Unit> {
        println(patch)
        vehicleService.patchVehicle(id, patch)

        return entityNoContentResponse()
    }

    @DeleteMapping("/{id}")
    fun deleteVehicles() {
        //todo implement
    }
}