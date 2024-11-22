package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.converter.BuildingConverter;
import com.javaweb.converter.BuildingSearchBuilderConverter;
import com.javaweb.dto.BuildingDTO;
import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.service.BuildingService;

@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private BuildingConverter buildingConverter;
	
	@Autowired
	private BuildingSearchBuilderConverter buildingSearchBuilderConverter;
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
	
	@Override
	public BuildingEntity createBuilding(BuildingDTO building) {
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setName(building.getName());
		buildingEntity.setStreet(building.getStreet());
		buildingEntity.setWard(building.getWard());
		buildingEntity.setNumberOfBasement(building.getNumberOfBasement());
		buildingEntity.setRentPrice(16L);
		
		DistrictEntity districtEntity = entityManager.find(DistrictEntity.class, building.getDistrictId());
		buildingEntity.setDistrict(districtEntity);
		
		entityManager.persist(buildingEntity);
		return buildingEntity;
	}
	
	@Override
	public BuildingEntity updateBuilding(BuildingDTO building) {
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setId(building.getId());
		buildingEntity.setName(building.getName());
		buildingEntity.setStreet(building.getStreet());
		buildingEntity.setWard(building.getWard());
		buildingEntity.setNumberOfBasement(building.getNumberOfBasement());
		buildingEntity.setRentPrice(building.getRentPrice());
		
		DistrictEntity districtEntity = entityManager.find(DistrictEntity.class, building.getDistrictId());
		buildingEntity.setDistrict(districtEntity);
		
		entityManager.merge(buildingEntity);
		return null;
	}
	
	@Override
	public void deleteById(List<Long> ids) {
		for (Long id: ids) {
			BuildingEntity building = entityManager.find(BuildingEntity.class, id);
			entityManager.remove(building);
		}
	}
}
