package com.jmd.web.service;

import com.jmd.entity.controller.WebDownloadSubmitVo;

public interface WebSubmitService {
	
	void blockDownload(WebDownloadSubmitVo vo);
	
	void worldDownload(WebDownloadSubmitVo vo);

}
