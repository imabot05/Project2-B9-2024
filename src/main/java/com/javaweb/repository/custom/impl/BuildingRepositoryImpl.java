package com.javaweb.repository.custom.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.custom.BuildingRepositoryCustom;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
@Primary
public class BuildingRepositoryImpl implements BuildingRepositoryCustom{
	@PersistenceContext
	private EntityManager entityManager;
	
	public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			sql.append(" INNER JOIN assignmentbuilding on b.id = assignmentbuilding.buildingid ");
		}
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() != 0) {
			sql.append(" INNER JOIN buildingrenttype on b.id = buildingrenttype.buildingid ");
			sql.append(" INNER JOIN renttype on renttype.id = buildingrenttype.renttypeid ");
		} 
//		String rentAreaTo = (String) params.get("areaTo");
//		String rentAreaFrom = (String) params.get("areaFrom");
//		if (StringUtil.checkString(rentAreaFrom) == true || StringUtil.checkString(rentAreaFrom) == true) {
//			sql.append(" INNER JOIN rentarea ON rentarea.buildingid = b.id ");
//		}
	}
	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			where.append(" AND assignmentbuilding.staffId = " + staffId);
		}
		Long rentAreaTo = buildingSearchBuilder.getAreaTo();
		Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
		if (rentAreaFrom != null || rentAreaTo != null) {
			where.append(" AND EXISTS (SELECT * FROM rentarea r WHERE b.id = r.buildingId ");
			if (rentAreaFrom != null) {
				where.append(" AND r.value >= " + rentAreaFrom);
			}
			if (rentAreaTo != null) {
				where.append(" AND r.value <= " + rentAreaTo);
			}
			where.append(") ");
		}
		Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
		Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
		if (rentPriceFrom != null || rentPriceTo != null) {
			if (rentPriceFrom != null) {
				where.append(" AND b.rentPrice >= " + rentPriceFrom);
			}
			if (rentPriceTo != null) {
				where.append(" AND b.rentPrice <= " + rentPriceTo);
			}
		}
//		if (typeCode != null && typeCode.size() > 0) {
//			List<String> code = new ArrayList<>();
//			for (String item: typeCode) {
//				code.add("'" + item + "'");
//			}
//			where.append(" AND renttype.code IN(" + String.join(",", code) + ") ");
//		}
		// JAVA 8
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() > 0) {
			where.append(" AND( ");
			String sql = typeCode.stream().map(it -> "renttype.code LIKE " + "'%" + it + "%'").collect(Collectors.joining(" OR "));
			where.append(sql);
			where.append(" ) ");
		}
	}
	public static void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
//		for (Map.Entry<String, Object> it: params.entrySet()) {
//			if (!it.getKey().equals("staffId") && !it.getKey().equals("typeCode") && !it.getKey().startsWith("area") && !it.getKey().startsWith("rentPrice")) {
//				String value = it.getValue().toString();
//				if (StringUtil.checkString(value)) {
//					if (NumberUtil.isNumber(value) == true) {
//						where.append(" AND b. " + it.getKey() + " = " + value);
//					} else {
//						where.append(" AND b. " + it.getKey() + " LIKE '%" + value + "%' ");
//					}
//				}
//			}
//		}
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field item: fields) {
				item.setAccessible(true);
				String fieldName = item.getName();
				if (fieldName.equals("districtId")) {
					Object value = item.get(buildingSearchBuilder);
					if (value != null) {
						if (item.getName().equals("districtid")) {
							where.append(" AND b.districtid = " + value);
						}
					}
				}
				if (!fieldName.equals("staffId") && !fieldName.equals("typeCode") && !fieldName.startsWith("area") && !fieldName.startsWith("rentPrice")) {
					Object value = item.get(buildingSearchBuilder);
					if (value != null) {
						if (item.getType().getName().equals("java.lang.Long") || item.getType().getName().equals("java.lang.Integer") || item.getType().getName().equals("java.lang.Float")){
							where.append(" AND b. " + fieldName + " = " + value);
						} else if (item.getType().getName().equalsIgnoreCase("java.lang.String")){
							where.append(" AND b. " + fieldName + " LIKE '%" + value + "%' ");
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder){
		List<BuildingEntity> result = new ArrayList<>();
			System.out.println("Connection success");
			StringBuilder sql = new StringBuilder("SELECT b.* FROM building b ");
			joinTable(buildingSearchBuilder, sql);
			StringBuilder where = new StringBuilder("WHERE 1 = 1 ");
			queryNormal(buildingSearchBuilder, where);
			querySpecial(buildingSearchBuilder, where);
			where.append(" GROUP BY b.id; ");
			sql.append(where);
//			Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
//			System.out.println(sql.toString());
//			return query.getResultList();
			Connection conn = ConnectionJDBCUtil.getConnection();
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());
				System.out.println(sql.toString());
				while (rs.next()) {
					BuildingEntity buildingEntity = new BuildingEntity();
					buildingEntity.setId(rs.getLong("b.id"));
					buildingEntity.setName(rs.getString("b.name"));
					buildingEntity.setWard(rs.getString("b.ward"));
					buildingEntity.setNumberOfBasement(rs.getLong("b.numberOfBasement"));
					Long districtId = rs.getLong("b.districtid");
				    DistrictEntity districtEntity = findDistrictById(districtId);
				    buildingEntity.setDistrict(districtEntity);
					buildingEntity.setStreet(rs.getString("b.street"));
					buildingEntity.setFloorArea(rs.getLong("b.floorarea"));
					buildingEntity.setRentPrice(rs.getLong("b.rentprice"));
					buildingEntity.setServiceFee(rs.getString("b.serviceFee"));
					buildingEntity.setBrokerageFee(rs.getLong("b.brokerageFee"));
					buildingEntity.setManagerName(rs.getString("b.managerName"));
					buildingEntity.setManagerPhoneNumber(rs.getString("b.managerphonenumber"));
					result.add(buildingEntity);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return result;
	}
	public void DeleteById(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	public DistrictEntity findDistrictById(Long id) {
	    String sql = "SELECT * FROM district WHERE id = ?";
	    try (Connection conn = ConnectionJDBCUtil.getConnection();
	        PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setLong(1, id);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            DistrictEntity district = new DistrictEntity();
	            district.setId(rs.getLong("id"));
	            district.setCode(rs.getString("code"));
	            district.setName(rs.getString("name"));
	            return district;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	
}
