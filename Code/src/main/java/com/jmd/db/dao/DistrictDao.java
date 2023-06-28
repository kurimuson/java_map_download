package com.jmd.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jmd.model.district.District;

@Mapper
public interface DistrictDao {

	int insert(District district);

	List<District> queryAll();

	District queryByAdcode(String adcode);

	List<District> queryByPadcode(String padcode);

}
