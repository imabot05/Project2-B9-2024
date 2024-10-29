package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository{
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "";
	
	@Override
	public List<RentAreaEntity> getValueByBuildingId(Long buildingId) {
		String sql = "SELECT * FROM rentarea WHERE rentarea.buildingid = " + buildingId;
		List<RentAreaEntity> rentAreas = new ArrayList<>();
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)
				) {
			while (rs.next()) {
				RentAreaEntity rentAreaEntity = new RentAreaEntity();
				rentAreaEntity.setValue(rs.getString("rentarea.value"));
				rentAreas.add(rentAreaEntity);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return rentAreas;
	}
}
