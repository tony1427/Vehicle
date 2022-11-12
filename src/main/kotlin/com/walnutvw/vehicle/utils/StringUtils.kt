package com.walnutvw.vehicle.utils


import com.walnutvw.vehicle.controller.ApplicationConstants.FORWARD_SLASH
import java.lang.IllegalArgumentException
import java.rmi.RemoteException
import java.util.*


val SLASH_REMOVING_REGEX = "(?<!(http:|https:))[//]+".toRegex()

fun String.formattedUrl(): String = this.replace(SLASH_REMOVING_REGEX, FORWARD_SLASH)

fun String.toUUID(): UUID = try {
    UUID.fromString(this)
} catch (e: IllegalArgumentException) {
    throw RemoteException()
}