package com.jmd.db.service;

import java.util.List;

import com.jmd.model.district.Area;
import com.jmd.model.district.City;
import com.jmd.model.district.District;
import com.jmd.model.district.Province;

public interface AllDistrictService {

	List<Province> getAllProvinces();

	List<City> getCitysByProvinceAdcode(String adcode);

	List<District> getDistrictsByCityAdcode(String adcode);
	
	Area getAreaByAdcode(String adcode);

}
