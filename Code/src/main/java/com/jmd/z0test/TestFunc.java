package com.jmd.z0test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jmd.db.dao.AreaDao;
import com.jmd.db.dao.CityDao;
import com.jmd.db.dao.DistrictDao;
import com.jmd.db.dao.ProvinceDao;
import com.jmd.entity.district.Area;
import com.jmd.entity.district.City;
import com.jmd.entity.district.District;
import com.jmd.entity.district.Province;
import com.jmd.entity.district.WebAPIResult;
import com.jmd.http.HttpClient;

@Component
public class TestFunc {

	private final String WEB_API_URL = "https://restapi.amap.com/v3/config/district?keywords={adcode}&subdistrict=0&extensions=all&key=84fe8781d6effe901187090b738a4f96";

	@Autowired
	private HttpClient http;
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private DistrictDao districtDao;
	@Autowired
	private ProvinceDao provinceDao;

	public void run() {
		// writeProvinceArea();
		// writeCityArea();
		// writeDistrictArea();
		// writeDistrict();
	}

	@SuppressWarnings("unused")
	private void writeProvinceArea() {
		List<Province> provinces = provinceDao.queryAll();
		for (Province province : provinces) {
			if (areaDao.isExist(province.getAdcode()) == 0) {
				String url = WEB_API_URL.replaceAll("\\{adcode\\}", province.getAdcode());
				String result = http.doGet(url);
				System.out.println(province.getAdcode());
				JSONObject jsonObj = JSON.parseObject(result);
				List<WebAPIResult> webApiResults = JSON.parseArray(jsonObj.get("districts").toString(),
						WebAPIResult.class);
				String line = webApiResults.get(0).getPolyline();
				String code = webApiResults.get(0).getAdcode();
				Area area = new Area();
				area.setAdcode(code);
				area.setPolyline(line);
				areaDao.insert(area);
			}
		}
		System.out.println("finish!");
	}

	@SuppressWarnings("unused")
	private void writeDistrict() {
		String padc = "810000";
		String url = "https://restapi.amap.com/v3/config/district?keywords=" + padc
				+ "&subdistrict=1&key=84fe8781d6effe901187090b738a4f96";
		String result = http.doGet(url);
		JSONObject jsonObj = JSON.parseObject(result);
		List<WebAPIResult> webApiResults = JSON.parseArray(jsonObj.get("districts").toString(), WebAPIResult.class);
		for (WebAPIResult each : webApiResults.get(0).getDistricts()) {
			System.out.println(each);
			if (districtDao.queryByAdcode(each.getAdcode()) == null) {
				System.out.println(123);
				District district = new District();
				district.setName(each.getName());
				district.setCenter(each.getCenter());
				district.setCitycode(each.getCitycode());
				district.setAdcode(each.getAdcode());
				district.setPadcode(padc);
				districtDao.insert(district);
			}
		}
	}

	@SuppressWarnings("unused")
	private void writeCityArea() {
		List<City> cities = cityDao.queryAll();
		for (City city : cities) {
			if (areaDao.isExist(city.getAdcode()) == 0) {
				String url = WEB_API_URL.replaceAll("\\{adcode\\}", city.getAdcode());
				String result = http.doGet(url);
				System.out.println(city.getAdcode());
				JSONObject jsonObj = JSON.parseObject(result);
				List<WebAPIResult> webApiResults = JSON.parseArray(jsonObj.get("districts").toString(),
						WebAPIResult.class);
				String line = webApiResults.get(0).getPolyline();
				String code = webApiResults.get(0).getAdcode();
				Area area = new Area();
				area.setAdcode(code);
				area.setPolyline(line);
				areaDao.insert(area);
			}
		}
		System.out.println("finish!");
	}

	@SuppressWarnings("unused")
	private void writeDistrictArea() {
		List<District> districts = districtDao.queryAll();
		for (District district : districts) {
			if (areaDao.isExist(district.getAdcode()) == 0) {
				String url = WEB_API_URL.replaceAll("\\{adcode\\}", district.getAdcode());
				String result = http.doGet(url);
				System.out.println(district.getAdcode());
				JSONObject jsonObj = JSON.parseObject(result);
				List<WebAPIResult> webApiResults = JSON.parseArray(jsonObj.get("districts").toString(),
						WebAPIResult.class);
				String line = webApiResults.get(0).getPolyline();
				String code = webApiResults.get(0).getAdcode();
				Area area = new Area();
				area.setAdcode(code);
				area.setPolyline(line);
				areaDao.insert(area);
			}
		}
		System.out.println("finish!");
	}

}
