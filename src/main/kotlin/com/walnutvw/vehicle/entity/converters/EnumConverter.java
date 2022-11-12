package com.walnutvw.vehicle.entity.converters;

import org.apache.commons.beanutils.Converter;

public class EnumConverter implements Converter {

        public Object convert(Class pType, Object pValue){
            final Class<? extends Enum> type = pType;
            return Enum.valueOf(type, pValue.toString());

        }
}
