package com.jmd.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jmd.entity.district.City;

@Mapper
public interface CityDao {

	List<City> queryAll();

	List<City> queryByPadcode(String padcode);

}
