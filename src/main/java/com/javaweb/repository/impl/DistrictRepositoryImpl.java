package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.utils.ConnectionUtil;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository{
	
	@Override
	public DistrictEntity findNameById(Long id) {
		String sql = "SELECT district.code, district.name FROM district WHERE district.id = " + id + ";";
		DistrictEntity districtEntity = new DistrictEntity();
		try (Connection conn = ConnectionUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)
				){
			while(rs.next()) {
				districtEntity.setId(id);
				districtEntity.setCode(rs.getString("code"));
				districtEntity.setName(rs.getString("name"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return districtEntity;
	}
}
