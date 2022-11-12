package com.walnutvw.vehicle.utils

import com.walnutvw.vehicle.config.Logging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.reflect.full.declaredMemberProperties

fun <T : Logging> T.logger(): Logger = LoggerFactory.getLogger(javaClass)
/*
Started with the link below to see if I can be a generic function that can be used for any class passed in.
https://stackoverflow.com/questions/43569835/generics-on-kproperty1-getdelegate-too-restrictive
Below seems to work
However this wouldn't be considered an extension function.
Might remove
 */
inline fun <reified T: Any> getProperties2(): List<String> = T::class.declaredMemberProperties.stream().map{ f -> f.name }.collect(Collectors.toList())
