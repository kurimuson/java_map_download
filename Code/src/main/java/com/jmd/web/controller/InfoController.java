package com.jmd.web.controller;

import com.jmd.ApplicationConfig;
import com.jmd.web.common.RESTfulResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/info")
public class InfoController {

    @RequestMapping(value = "getWsPath", method = RequestMethod.GET)
    @ResponseBody
    public RESTfulResult<String> getPort() {
        RESTfulResult<String> result = new RESTfulResult<>();
        result.setCode(200);
        result.setMessage("ok");
        result.setSuccess(true);
        result.setData("ws://localhost:" + ApplicationConfig.startPort + "/websocket/map");
        return result;
    }

}
