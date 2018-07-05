package ru.emi.util;


import java.util.HashMap;
import java.util.Map;

public class Ajax {

    private static final String STATUS = "status";
    private static final String DATA = "data";
    private static final String MESSAGE = "message";

    private Ajax() {
    }

    public static Map<String, Object> successResponse(Object object) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, "success");
        response.put(DATA, object);
        return response;
    }

    public static Map<String, Object> emptyResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, "success");
        return response;
    }

    public static Map<String, Object> errorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, "error");
        response.put(MESSAGE, errorMessage);
        return response;
    }
}

