package com.jmd.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jmd.model.district.Province;

@Mapper
public interface ProvinceDao {

	List<Province> queryAll();

}
