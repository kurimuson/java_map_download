package com.jmd.web.controller

import com.jmd.model.controller.WebDownloadSubmitVo
import com.jmd.web.common.RESTfulResult
import com.jmd.web.service.WebSubmitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/submit")
class WebSubmitController {

    @Autowired
    private val webSubmitService: WebSubmitService? = null

    @RequestMapping(value = ["/blockDownload"], method = [RequestMethod.POST])
    @ResponseBody
    fun blockDownload(@RequestBody vo: WebDownloadSubmitVo?): RESTfulResult<*> {
        val result: RESTfulResult<*> = RESTfulResult<Any>()
        webSubmitService!!.blockDownload(vo)
        result.code = 200
        result.message = "ok"
        result.success = true
        return result
    }

    @RequestMapping(value = ["worldDownload"], method = [RequestMethod.POST])
    @ResponseBody
    fun worldDownload(@RequestBody vo: WebDownloadSubmitVo?): RESTfulResult<*> {
        val result: RESTfulResult<*> = RESTfulResult<Any>()
        webSubmitService!!.worldDownload(vo)
        result.code = 200
        result.message = "ok"
        result.success = true
        return result
    }
}