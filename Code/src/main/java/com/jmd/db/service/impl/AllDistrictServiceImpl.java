package com.jmd.db.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmd.db.dao.AreaDao;
import com.jmd.db.dao.CityDao;
import com.jmd.db.dao.DistrictDao;
import com.jmd.db.dao.ProvinceDao;
import com.jmd.db.service.AllDistrictService;
import com.jmd.model.district.Area;
import com.jmd.model.district.City;
import com.jmd.model.district.District;
import com.jmd.model.district.Province;

@Service
public class AllDistrictServiceImpl implements AllDistrictService {

	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private DistrictDao districtDao;
	@Autowired
	private AreaDao areaDao;

	@Override
	public List<Province> getAllProvinces() {
		return provinceDao.queryAll();
	}

	@Override
	public List<City> getCitysByProvinceAdcode(String adcode) {
		return cityDao.queryByPadcode(adcode);
	}

	@Override
	public List<District> getDistrictsByCityAdcode(String adcode) {
		return districtDao.queryByPadcode(adcode);
	}

	@Override
	public Area getAreaByAdcode(String adcode) {
		return areaDao.getByAdcode(adcode);
	}

}
