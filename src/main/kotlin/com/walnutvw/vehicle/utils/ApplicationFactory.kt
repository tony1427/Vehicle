package com.walnutvw.vehicle.utils

import org.springframework.stereotype.Component
import java.util.stream.Collectors
import kotlin.reflect.full.declaredMemberProperties

@Component
class ApplicationFactory {
    /*
    Started with the link below to see if I can be a generic function that can be used for any class passed in.
    https://stackoverflow.com/questions/43569835/generics-on-kproperty1-getdelegate-too-restrictive
    Below seems to work

     */

    final inline fun <reified T : Any> getProperties(): List<String> {
        return T::class.declaredMemberProperties.stream().map { f -> f.name }.collect(Collectors.toList())
    }
}