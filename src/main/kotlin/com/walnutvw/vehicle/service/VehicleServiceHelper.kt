package com.walnutvw.vehicle.service

import org.springframework.stereotype.Component

@Component
class VehicleServiceHelper {
    fun validateRequest(requestMap: Map<String, String>, classFieldNames: List<String>): List<String> {

        val keys = requestMap.keys.toList()

        return keys.filterNot { classFieldNames.contains(it) }

//        return if (difference.isEmpty()) {
//            Pair(true, listOf())
//        } else {
//            Pair(false, difference)
//        }
    }
}