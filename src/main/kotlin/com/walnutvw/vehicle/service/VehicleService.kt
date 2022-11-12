package com.walnutvw.vehicle.service

import com.github.fge.jsonpatch.JsonPatch
import com.walnutvw.vehicle.exception.BadActionException
import com.walnutvw.vehicle.exception.NotFoundException
import com.walnutvw.vehicle.model.VehicleRepresentation
import com.walnutvw.vehicle.repository.VehicleRepository
import com.walnutvw.vehicle.utils.ApplicationFactory
import com.walnutvw.vehicle.utils.toUUID
import org.apache.commons.beanutils.BeanUtilsBean
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class VehicleService(
    private val vehicleRepository: VehicleRepository,
    private val modelMapper: ModelMapper,
    private val beanUtilsBean: BeanUtilsBean,
    private val applicationFactory: ApplicationFactory,
    private val vehicleServiceHelper: VehicleServiceHelper
) {

    fun getVehicle(id: String): VehicleRepresentation {
        return vehicleRepository.findById(id.toUUID())
            .map { modelMapper.map(it, VehicleRepresentation::class.java) }
            .orElseThrow { NotFoundException() }
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

        if(purchaseDateFromMap != null){
            LocalDate.parse(purchaseDateFromMap)
        }

        vehicle.apply {
            vin = map["vin"] ?: vehicle.vin
            make = map["make"] ?: vehicle.make
            model = map["model"] ?: vehicle.model
            if(purchaseDateFromMap != null) {
                purchaseDate = LocalDate.parse(purchaseDateFromMap)
            } else {
                purchaseDate = vehicle.purchaseDate
            }
        }

        //populate the opp entity and save

        return vehicleRepository.save(vehicle).toRepresentation()
    }

    fun patchVehicle(id: String, request: JsonPatch){

    }


}


//fun main() {
//
//    println("Local Date: ${LocalDate.parse(null) ?: "Hey"}")


//    val map = mapOf(
//        "1" to "This",
//        "2" to "That",
//        "3" to "the other"
//    )
//
//    println("Keys ${map.keys}")
//
//    val props = VehicleRepresentation::class.declaredMemberProperties.stream().map { f -> f.name }
//        .collect(Collectors.toList())
//
//    println("prop: $props")
//    val list = listOf("make1", "model2")
//
//    val difference = props.filterNot { list.contains(it) }
////    https://www.techiedelight.com/difference-between-two-lists-kotlin/
//    val difference2 = list.filterNot { props.contains(it) }// <- use this
//
//    println("Difference: $difference")
//    println("Difference: $difference2")
//
//}
