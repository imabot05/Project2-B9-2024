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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.context.annotation.Primary;
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
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
        sql.append(where).append(" GROUP by b.id");
        Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
        return query.getResultList();
    }


    @Override
    public Object delete(List<Long> ids) {
        return null;
    }
}
