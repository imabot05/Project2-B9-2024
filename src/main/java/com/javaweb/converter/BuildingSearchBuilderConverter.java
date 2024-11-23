package com.javaweb.converter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.utils.MapUtil;

@Component
public class BuildingSearchBuilderConverter {
	
	public BuildingSearchBuilder toBuildingSearchBuilder(Map<String, Object> params, List<String> typeCode) {
		BuildingSearchBuilder buildingSearchBuilder = new BuildingSearchBuilder.Builder()
													.setName(MapUtil.getObject(params, "name", String.class))
													.setDistrictId(MapUtil.getObject(params, "districtId", Long.class))
													.setAreaFrom(MapUtil.getObject(params, "rentAreaFrom", Long.class))
													.setAreaTo(MapUtil.getObject(params, "rentAreaTo", Long.class))
													.setFloorArea(MapUtil.getObject(params, "floorArea", Long.class))
													.setManagerName(MapUtil.getObject(params, "managerName", String.class))
													.setManagerPhoneNumber(MapUtil.getObject(params, "managerPhoneNumber", String.class))
													.setTypeCode(typeCode)
													.setRentPriceFrom(MapUtil.getObject(params, "rentPriceFrom", Long.class))
													.setRentPriceTo(MapUtil.getObject(params, "rentPriceTo", Long.class))
													.setNumberOfBasement(MapUtil.getObject(params, "numberOfBasement", Long.class))
													.setStaffId(MapUtil.getObject(params, "staffId", Long.class))
													.setStreet(MapUtil.getObject(params, "street", String.class))
													.setWard(MapUtil.getObject(params, "ward", String.class))
													.build();
		return buildingSearchBuilder;
	}
}
