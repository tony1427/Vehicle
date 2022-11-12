package com.walnutvw.vehicle.entity.converters

import org.apache.commons.beanutils.Converter

class LocalDateConverter : Converter {
    //    override fun convert(type: Class<*>?, value: Any): LocalDate? {
//        return if (value == null) {
//            null
//        } else {
//            try {
//                LocalDate.parse(value as String, DateTimeFormatter.ofPattern("yyyy-MM-DD"))
//            } catch (e: Exception) {
//                throw RuntimeException(e)
//            }
//        }
//    }
//
//    @JvmName("convert1")
//    override fun <T : Any?> convert(clazz: Class<T>?, value: Any?): LocalDate? {
//        return if(value == null) {
//            null
//        } else {
//            try {
//                LocalDate.parse(value as String, DateTimeFormatter.ofPattern("yyyy-MM-DD"))
//            } catch (e: Exception) {
//                throw RuntimeException(e)
//            }
//        }
//
//    }
    override fun <T : Any?> convert(p0: Class<T>?, p1: Any?): T {
        TODO("Not yet implemented")
    }
}