package com.example.config;

import com.example.dto.grid.SortPair;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NameToEnumDeserializer extends JsonDeserializer<SortPair.Order> {
    @Override
    public SortPair.Order deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        final String param = jsonParser.getText();//jsonParser.getIntValue();// 1
        final JsonStreamContext parsingContext = jsonParser.getParsingContext();// 2
        final String currentName = parsingContext.getCurrentName();// 3
        final Object currentValue = parsingContext.getCurrentValue();// 4
        try {
            final Field declaredField = currentValue.getClass().getDeclaredField(currentName);// 5
            final Class<?> targetType = declaredField.getType();// 6
            final Method valuesMethod = targetType.getDeclaredMethod("values");// 7
            SortPair.Order[] enums = (SortPair.Order[]) valuesMethod.invoke(null);
            for (SortPair.Order anEnum : enums) {
                if(anEnum.name().equalsIgnoreCase(param)){
                    return anEnum;
                }
            }
            throw new RuntimeException("不匹配");
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
            throw new RuntimeException("不匹配",e);
        }
    }
}
