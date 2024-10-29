package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.dto.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "";

    @Override
    public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typeCodes) {
        StringBuilder sql = new StringBuilder("SELECT building.id, building.name, building.districtid, building.street, building.ward, building.numberofbasement, building.floorarea,"
            + "building.managername, building.direction, building.managerphonenumber, building.rentprice, building.servicefee, building.brokeragefee FROM building ");
        mergeTable(params, typeCodes, sql);
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        queryInBuildingTable(params, sql);
        queryOutOfBuildingTable(params, typeCodes, where);
        where.append(" GROUP by building.id");
        sql.append(where);
        
        List<BuildingEntity> results = new ArrayList<>();
        try (
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql.toString())
        ) {
            System.out.println(sql);
            while (rs.next()) {
            	BuildingEntity buildingEntity = new BuildingEntity();
            	buildingEntity.setId(rs.getLong("building.id"));
            	buildingEntity.setName(rs.getString("building.name"));
            	buildingEntity.setDistrictId(rs.getLong("building.districtid"));
            	buildingEntity.setWard(rs.getString("building.ward"));
            	buildingEntity.setStreet(rs.getString("building.street"));
            	buildingEntity.setDirection(rs.getString("building.direction"));
            	buildingEntity.setNumberOfBasement(rs.getLong("building.numberofbasement"));
            	buildingEntity.setFloorArea(rs.getLong("building.floorarea"));
            	buildingEntity.setRentPrice(rs.getLong("building.rentprice"));
            	buildingEntity.setManagerName(rs.getString("building.managername"));
            	buildingEntity.setManagerPhoneNumber(rs.getString("building.managerphonenumber"));
            	buildingEntity.setBrokerageFee(rs.getLong("building.brokeragefee"));
            	buildingEntity.setServiceFee(rs.getString("building.servicefee"));
            	results.add(buildingEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }

    public StringBuilder mergeTable(Map<String, Object> params, List<String> typeCodes, StringBuilder sql) {
        String staffId = (String) params.get("staffId");
        if (staffId != null && !staffId.isEmpty()) {
            sql.append(" INNER JOIN assignmentbuilding ON building.id = assignmentbuilding.buildingid ");
        }
        if (typeCodes != null && !typeCodes.isEmpty()) {
            sql.append(" INNER JOIN buildingrenttype ON building.id = buildingrenttype.buildingid ")
               .append(" INNER JOIN renttype ON renttype.id = buildingrenttype.renttypeid ");
        }
        String rentAreaFrom = (String) params.get("areaFrom");
        String rentAreaTo = (String) params.get("areaTo");
        if ((rentAreaFrom != null && !rentAreaFrom.isEmpty()) || (rentAreaTo != null && !rentAreaTo.isEmpty())) {
            sql.append(" INNER JOIN rentarea ON rentarea.buildingid = building.id ");
        }
        return sql;
    }
    
    public static void queryInBuildingTable(Map<String, Object> params, StringBuilder where) {
    	for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            if (key.equals("staffId") || key.equals("typeCodes") || key.startsWith("area") || key.startsWith("rentPrice")) {
                continue;
            }
            if (isLongNumber(value)) {
                where.append(" AND building.").append(key).append(" = ").append(value);
            } else {
                where.append(" AND building.").append(key).append(" LIKE '%").append(value).append("%' ");
            }
        }
    }
    
    public static void queryOutOfBuildingTable(Map<String, Object> params, List<String> typeCodes, StringBuilder where) {
    	String staffId = (String) params.get("staffId");
    	if (isLongNumber(staffId)) {
    		where.append(" AND assignmentbuilding.staffid = " + staffId);
    	}
    	
    	String rentAreaFrom = (String) params.get("areaFrom");
    	String rentAreaTo = (String) params.get("areaTo");
    	if (isLongNumber(rentAreaFrom)) {
    		where.append(" AND rentarea.value >= " + rentAreaFrom);
    	}
    	if (isLongNumber(rentAreaTo)) {
    		where.append(" AND rentarea.value <= " + rentAreaTo);
    	}
    	
    	String rentPriceFrom = (String) params.get("rentPriceFrom");
    	String rentPriceTo = (String) params.get("rentPriceTo");
    	if (isLongNumber(rentPriceFrom)) {
    		where.append(" AND building.rentprice >= " + rentPriceFrom);
    	}
    	if (isLongNumber(rentPriceTo)) {
    		where.append(" AND building.rentprice <= " + rentPriceTo);
    	}
    	
    	if (typeCodes != null && !typeCodes.isEmpty()) {
    		where.append(" AND renttype.code IN " + String.join(",", typeCodes) + ") ");
    	}
    }
    
    private static boolean isLongNumber(String value) {
        try {
            Long number = Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    

    @Override
    public Object delete(List<Long> ids) {
        return null;
    }
}
