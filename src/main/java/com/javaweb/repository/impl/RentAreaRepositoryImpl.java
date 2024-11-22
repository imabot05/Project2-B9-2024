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
import com.javaweb.utils.ConnectionUtil;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository{
	
//	@Override
//	public List<RentAreaEntity> getValueByBuildingId(Long buildingId) {
//		String sql = "SELECT * FROM rentarea WHERE rentarea.buildingid = " + buildingId;
//		List<RentAreaEntity> rentAreas = new ArrayList<>();
//		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//				Statement stmt = conn.createStatement();
//				ResultSet rs = stmt.executeQuery(sql)
//				) {
//			while (rs.next()) {
//				RentAreaEntity rentAreaEntity = new RentAreaEntity();
//				rentAreaEntity.setValue(rs.getString("rentarea.value"));
//				rentAreas.add(rentAreaEntity);
//			}
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//		}
//		return rentAreas;
//	}
    
    @Override
    public List<RentAreaEntity> findByBuildingId(Long buildingId){
    	String sql = "SELECT * FROM rentarea WHERE 1 = 1 AND buildingid = " + buildingId;
    	List<RentAreaEntity> results = new ArrayList<>();
    	try (Connection conn = ConnectionUtil.getConnection();
    			Statement stm = conn.createStatement();
    			ResultSet rs = stm.executeQuery(sql);
    			) {
    		while (rs.next()) {
    			RentAreaEntity rentAreaEntity = new RentAreaEntity();
    			rentAreaEntity.setBuildingId(rs.getLong("buildingid"));
    			rentAreaEntity.setId(rs.getLong("id"));
    			rentAreaEntity.setValue(rs.getLong("value"));
    			results.add(rentAreaEntity);
    		}
	    } catch (SQLException ex) {
	    	ex.printStackTrace();
	    	}
    	return results;
	    }
}
