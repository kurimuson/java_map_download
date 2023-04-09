package com.jmd.db.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jmd.entity.district.Area;

@Mapper
public interface AreaDao {

	int insert(Area area);

	int isExist(String adcode);

	Area getByAdcode(String adcode);

}
