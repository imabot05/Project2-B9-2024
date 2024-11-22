package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.dto.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionUtil;
import com.javaweb.utils.DataUtil;
import com.javaweb.utils.NumberUtil;


@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "";
    
    private void sqlJoin(BuildingSearchBuilder builder, StringBuilder join) {
    	Long staffId = builder.getStaffId();
    	if (DataUtil.checkData(staffId)) {
    		join.append(" JOIN assignmentbuilding ab ON b.id = ab.buildingid ");
    	}
    	Long rentAreaFrom = builder.getAreaFrom();
    	Long rentAreaTo = builder.getAreaTo();
    	if (DataUtil.checkData(rentAreaFrom) || DataUtil.checkData(rentAreaTo)) {
    		join.append(" JOIN rentarea rt ON b.id = rt.buildingid ");
    	}
    	List<String> typeCode = builder.getTypeCode();
    	if (typeCode != null && typeCode.size() != 0) {
    		join.append(" JOIN buildingrenttype bt ON b.id = bt.buildingid ");
    		join.append(" JOIN renttype ON renttype.id = bt.renttypeid ");
    	}
    }
    
    private void sqlWhereNormal(BuildingSearchBuilder builder, StringBuilder where) {
//    	for (Map.Entry<String, Object> it: params.entrySet()) {
//    		String key = it.getKey();
//    		if (!key.equals("staffId") && !key.equals("typeCode") && !key.startsWith("rentArea") && !key.startsWith("rentPrice")){
//    			String value = it.getValue().toString();
//    			if (!NumberUtil.checkNumber(key)) {
//    				where.append(" AND b." + key + " LIKE '%" + value + "%'");
//    			} else {
//    				where.append(" AND b." + key + " = " + value);
//    			}
//    		}
//    	}
    	try {
    		Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
    		for (Field it: fields) {
    			// Cho phep truy cap vao cac truong thong tin
    			it.setAccessible(true);
    			String fieldName = it.getName();
    			if (!fieldName.equals("staffId") && !fieldName.equals("typeCode") && !fieldName.startsWith("area") && !fieldName.startsWith("rentPrice")) {
    				Object value = it.get(builder);
    				if (value != null) {
    					if (it.getType().getName().equals("java.lang.String")) {
    						where.append(" AND b." + fieldName + " LIKE '%" + value + "%'");
    					}
    					else if (it.getType().getName().equals("java.lang.Long") || it.getType().getName().equals("java.lang.Integer")) {
    						where.append(" AND b." + fieldName + " = " + value);
    					}
    				}
    			}
    		}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    private void sqlWhereSpecial(BuildingSearchBuilder builder, StringBuilder where) {
    	Long staffId = builder.getStaffId();
    	if (DataUtil.checkData(staffId)) {
    		where.append(" AND ab.staffId = " + staffId);
    	}
    	Long rentAreaFrom = builder.getAreaFrom();
    	Long rentAreaTo = builder.getAreaTo();
    	if (DataUtil.checkData(rentAreaFrom)) {
    		where.append(" AND rt.value >= " + rentAreaFrom);
    	}
    	if (DataUtil.checkData(rentAreaTo)) {
    		where.append(" AND rt.value <= " + rentAreaTo);
    	}
    	
    	Long rentPriceFrom = builder.getRentPriceFrom();
    	Long rentPriceTo = builder.getRentPriceTo();
    	if (DataUtil.checkData(rentPriceFrom)) {
    		where.append(" AND b.rentprice >= " + rentPriceFrom);
    	}
    	if (DataUtil.checkData(rentPriceTo)) {
    		where.append(" AND b.rentprice <= " + rentPriceTo);
    	}
    	List<String> typeCode = builder.getTypeCode();
    	if (typeCode != null && typeCode.size() != 0) {
    		where.append(" AND renttype.code IN(" + typeCode.stream().map(i -> "'" + i + "'").collect(Collectors.joining(",")) + ")");
    	}
    }
    
    @Override
    public List<BuildingEntity> findAll(BuildingSearchBuilder builder) {
        StringBuilder sql = new StringBuilder("SELECT b.* FROM building b ");
        sqlJoin(builder, sql);
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        sqlWhereNormal(builder, where);
        sqlWhereSpecial(builder, where);
        where.append(" GROUP by b.id");
        sql.append(where);
        
        List<BuildingEntity> results = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection();
        		Statement stm = conn.createStatement();
        		ResultSet rs = stm.executeQuery(sql.toString());
        		) {
            System.out.println(sql);
            while (rs.next()) {
            	BuildingEntity buildingEntity = new BuildingEntity();
            	buildingEntity.setId(rs.getLong("b.id"));
            	buildingEntity.setName(rs.getString("b.name"));
            	buildingEntity.setDistrictId(rs.getLong("b.districtid"));
            	buildingEntity.setWard(rs.getString("b.ward"));
            	buildingEntity.setStreet(rs.getString("b.street"));
            	buildingEntity.setDirection(rs.getString("b.direction"));
            	buildingEntity.setNumberOfBasement(rs.getLong("b.numberofbasement"));
            	buildingEntity.setFloorArea(rs.getLong("b.floorarea"));
            	buildingEntity.setRentPrice(rs.getLong("b.rentprice"));
            	buildingEntity.setManagerName(rs.getString("b.managername"));
            	buildingEntity.setManagerPhoneNumber(rs.getString("b.managerphonenumber"));
            	buildingEntity.setBrokerageFee(rs.getLong("b.brokeragefee"));
            	buildingEntity.setServiceFee(rs.getString("b.servicefee"));
            	results.add(buildingEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }


    @Override
    public Object delete(List<Long> ids) {
        return null;
    }
}
