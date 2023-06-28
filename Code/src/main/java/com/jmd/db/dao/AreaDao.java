package com.jmd.db.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jmd.model.district.Area;

@Mapper
public interface AreaDao {

	int insert(Area area);

	int isExist(String adcode);

	Area getByAdcode(String adcode);

}
