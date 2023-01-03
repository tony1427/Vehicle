package com.walnutvw.vehicle.config


import com.walnutvw.vehicle.entity.converters.EnumConverter
import org.apache.commons.beanutils.BeanUtilsBean
import org.apache.commons.beanutils.ConvertUtilsBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

//@Configuration
class BeanUtilsConfig {

    @Bean(name = ["beansUtilBean"])
    fun beanUtilsBean(): BeanUtilsBean {
        val beanUtilsBean = BeanUtilsBean(object: ConvertUtilsBean(){
            override fun convert(value: String, clazz: Class<*>): Any{
                return if(clazz.isEnum){
                    return EnumConverter()
                        .convert(clazz, value)
                } else {
                    super.convert(value, clazz)
                }
            }
        })
//        beanUtilsBean.convertUtils.register(LocalDateTimeConverter(), LocalDateTime::class.java)

        return beanUtilsBean

    }
}