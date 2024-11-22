package com.javaweb.service;

import java.util.List;
import java.util.Map;

import com.javaweb.dto.BuildingDTO;
import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.entity.BuildingEntity;

public interface BuildingService {
	List<BuildingResponseDTO> findAll(Map<String, Object> params, List<String> typeCodes);
	BuildingEntity createBuilding(BuildingDTO buildingDTO);
	BuildingEntity updateBuilding(BuildingDTO buildingDTO);
	void deleteById(List<Long> ids);
}
