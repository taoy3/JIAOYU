/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taoy3.freight.netBean;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 *
 * @author wangliang
 */
public class BigDecimalGeoDeserializer implements JsonDeserializer<BigDecimal> {

         @Override
         public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                 throws JsonParseException {
                  try {
                           return BigDecimal.valueOf(Double.valueOf(json.getAsJsonPrimitive().getAsString()));
                  } catch (Exception ex) {
                           return null;
                  }
         }
}
