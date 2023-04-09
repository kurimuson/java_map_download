package com.jmd.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jmd.web.common.RESTfulResult;
import com.jmd.entity.controller.WebDownloadSubmitVo;
import com.jmd.web.service.WebSubmitService;

@CrossOrigin
@RestController
@RequestMapping("/submit")
public class WebSubmitController {
	
	@Autowired
	private WebSubmitService webSubmitService;

	@RequestMapping(value = "blockDownload", method = RequestMethod.POST)
	@ResponseBody
	public RESTfulResult<?> blockDownload(@RequestBody WebDownloadSubmitVo vo) {
		RESTfulResult<?> result = new RESTfulResult<>();
		webSubmitService.blockDownload(vo);
		result.setCode(200);
		result.setMessage("ok");
		result.setSuccess(true);
		return result;
	}

	@RequestMapping(value = "worldDownload", method = RequestMethod.POST)
	@ResponseBody
	public RESTfulResult<?> worldDownload(@RequestBody WebDownloadSubmitVo vo) {
		RESTfulResult<?> result = new RESTfulResult<>();
		webSubmitService.worldDownload(vo);
		result.setCode(200);
		result.setMessage("ok");
		result.setSuccess(true);
		return result;
	}

}
