package com.jmd.web.service;

import com.jmd.model.controller.WebDownloadSubmitVo;

public interface WebSubmitService {
	
	void blockDownload(WebDownloadSubmitVo vo);
	
	void worldDownload(WebDownloadSubmitVo vo);

}
