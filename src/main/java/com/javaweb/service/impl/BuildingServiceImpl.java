package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.converter.BuildingConverter;
import com.javaweb.converter.BuildingSearchBuilderConverter;
import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private BuildingConverter buildingConverter;
	
	@Autowired
	private BuildingSearchBuilderConverter buildingSearchBuilderConverter;
	
	@Override
	public List<BuildingResponseDTO> findAll(Map<String, Object> params, List<String> typeCodes) {
		BuildingSearchBuilder builder = buildingSearchBuilderConverter.toBuildingSearchBuilder(params, typeCodes);
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(builder);
		List<BuildingResponseDTO> results = new ArrayList<>();
		for (BuildingEntity building: buildingEntities) {
			BuildingResponseDTO buildingResponseDTO = buildingConverter.toBuildingResponseDTO(building);
			results.add(buildingResponseDTO);
		}
		return results;
	}
}
