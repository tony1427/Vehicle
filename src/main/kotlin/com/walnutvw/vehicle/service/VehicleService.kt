package com.walnutvw.vehicle.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.walnutvw.vehicle.entity.VehicleEntity
import com.walnutvw.vehicle.exception.BadActionException
import com.walnutvw.vehicle.exception.NotFoundException
import com.walnutvw.vehicle.model.VehicleRepresentation
import com.walnutvw.vehicle.repository.VehicleRepository
import com.walnutvw.vehicle.utils.ApplicationFactory
import com.walnutvw.vehicle.utils.toUUID
import org.modelmapper.ModelMapper
import org.modelmapper.internal.util.ToStringBuilder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.OffsetDateTime

@Service
class VehicleService(
    private val vehicleRepository: VehicleRepository,
    private val modelMapper: ModelMapper,
    private val applicationFactory: ApplicationFactory,
    private val vehicleServiceHelper: VehicleServiceHelper,
    private val objectMapper: ObjectMapper
) {

    fun getVehicle(id: String): VehicleRepresentation {
        return vehicleRepository.findById(id.toUUID())
            .map { modelMapper.map(it, VehicleRepresentation::class.java) }
            .orElseThrow { NotFoundException() }
    }

    fun getVehicles(): List<VehicleRepresentation> {
        return vehicleRepository.findAll()
                .map { modelMapper.map(it, VehicleRepresentation::class.java) }
    }

    fun createVehicle(vehicleRepresentation: VehicleRepresentation): VehicleRepresentation {
        return vehicleRepository.save(vehicleRepresentation.toEntity()).toRepresentation()
    }

    fun updateVehicle(id: String, vehicleRepresentation: VehicleRepresentation): VehicleRepresentation {
        val vehicle = vehicleRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }

        vehicle.apply {
            vin = vehicleRepresentation.vin
            make = vehicleRepresentation.make
            model = vehicleRepresentation.model
            purchaseDate = vehicleRepresentation.purchaseDate
        }

        return vehicleRepository.save(vehicle).toRepresentation()
    }

    fun updateVehicle(id: String, map: Map<String, String>): VehicleRepresentation {

        if (map.isEmpty()) {
            throw BadActionException("Map request is empty")
        }

        val vehicle = vehicleRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }

        val properties = applicationFactory.getProperties<VehicleRepresentation>()
        val validateMapFields = vehicleServiceHelper.validateRequest(map, properties)

        if (validateMapFields.isNotEmpty()) {
            throw BadActionException("Fields don't exist $validateMapFields")
        }

        var purchaseDateFromMap = map["purchaseDate"]


        vehicle.apply {
            vin = map["vin"] ?: vehicle.vin
            make = map["make"] ?: vehicle.make
            model = map["model"] ?: vehicle.model
            purchaseDate = if(purchaseDateFromMap != null) {
                LocalDate.parse(purchaseDateFromMap)
            } else {
                vehicle.purchaseDate
            }
        }
        return vehicleRepository.save(vehicle).toRepresentation()
    }

    fun patchVehicle(id: String, patch: JsonPatch){
        val vehicle = vehicleRepository.findById(id.toUUID()).orElseThrow { NotFoundException() }

        val patchedVehicle = applyPatchToVehicle(vehicle, patch)
        patchedVehicle.updatedAt = OffsetDateTime.now()

        vehicleRepository.save(patchedVehicle)
    }

    private fun applyPatchToVehicle(vehicle: VehicleEntity, patch: JsonPatch): VehicleEntity {

        val patched = patch.apply(
            objectMapper.convertValue(
                vehicle,
                JsonNode::class.java
            )
        )

        return objectMapper.treeToValue(patched, VehicleEntity::class.java)
    }


}

