package com.example.final_project.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class Convert {
    public static String convertListMapToString(List<Map<String, Object>> data){
        // convert List<Map<String, Object>> into String format
        String newString;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            newString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle exception
            return null;
        }
        return newString;
    }
}
