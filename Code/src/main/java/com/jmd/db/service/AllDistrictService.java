package com.jmd.db.service;

import java.util.List;

import com.jmd.entity.district.Area;
import com.jmd.entity.district.City;
import com.jmd.entity.district.District;
import com.jmd.entity.district.Province;

public interface AllDistrictService {

	List<Province> getAllProvinces();

	List<City> getCitysByProvinceAdcode(String adcode);

	List<District> getDistrictsByCityAdcode(String adcode);
	
	Area getAreaByAdcode(String adcode);

}
