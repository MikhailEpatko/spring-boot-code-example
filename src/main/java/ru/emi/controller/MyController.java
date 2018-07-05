package ru.emi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.emi.service.MyService;
import ru.emi.util.Ajax;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/path/here")
public class MyController {

    private MyService service;

    @Autowired
    public MyController(MyService service) {
        this.service = service;
    }

    @GetMapping("/html")
    public Map<String, Object> getView(@RequestHeader(value = "user-agent") String userAgent,
                                       @RequestHeader(value = "referer") String referer,
                                       @CookieValue  (value = "_id") String id,
                                       @CookieValue  (value = "_session") String ntSessionId,
                                       @RequestParam (value = "_fp") String ntFingerPrint,
                                       HttpServletRequest request) {
        String result = service.getHtmlByActivity(userAgent, referer, id, ntSessionId, ntFingerPrint, request.getRemoteAddr());
        if (result != null && !result.isEmpty()) {
            return Ajax.successResponse(result);
        }
        return Ajax.errorResponse("Something gone wrong: result is empty...");
    }

    @PutMapping("/tuple")
    public void updateTuple(@RequestParam(name = "id") String id) {
        service.updateTuple(id);
    }
}