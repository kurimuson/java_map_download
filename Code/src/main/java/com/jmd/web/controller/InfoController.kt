package com.jmd.web.controller

import com.jmd.ApplicationPort
import com.jmd.web.common.RESTfulResult
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/info")
class InfoController {

    @RequestMapping(value = ["/getWsPath"], method = [RequestMethod.GET])
    @ResponseBody
    fun getWsPath(): RESTfulResult<String> {
        val result = RESTfulResult<String>()
        result.code = 200
        result.message = "ok"
        result.success = true
        result.data = "ws://localhost:${ApplicationPort.startPort}/websocket/map"
        return result
    }

}