package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.converter.BuildingConverter;
import com.javaweb.converter.BuildingSearchBuilderConverter;
import com.javaweb.dto.BuildingDTO;
import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;
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
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
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
	public BuildingEntity createOrUpdateBuilding(BuildingDTO building) {
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setId(building.getId());
		buildingEntity.setName(building.getName());
		buildingEntity.setStreet(building.getStreet());
		buildingEntity.setWard(building.getWard());
		buildingEntity.setManagerName(building.getManagerName());
		buildingEntity.setManagerPhoneNumber(building.getManagerPhoneNumber());
		buildingEntity.setNumberOfBasement(building.getNumberOfBasement());
		DistrictEntity district = districtRepository.findById(building.getDistrictId()).get();
		buildingEntity.setDistrict(district);
		buildingEntity.setRentPrice(building.getRentPrice());
		buildingRepository.save(buildingEntity);
		
		if (buildingEntity.getId() != null) rentAreaRepository.deleteByBuilding(buildingEntity);
		List<RentAreaEntity> rentAreaEntities = new ArrayList<>();
		RentAreaEntity rentArea1 = new RentAreaEntity();
		rentArea1.setBuilding(buildingEntity);
		rentArea1.setValue(350L);
		
		
		RentAreaEntity rentArea2 = new RentAreaEntity();
		rentArea2.setBuilding(buildingEntity);
		rentArea2.setValue(420L);
		rentAreaEntities.add(rentArea1);
		rentAreaEntities.add(rentArea2);
		rentAreaRepository.saveAll(rentAreaEntities);
		
		return null;
	}
	
	@Override
	public void deleteById(List<Long> ids) {
		rentAreaRepository.deleteByBuilding_IdIn(ids);
		buildingRepository.deleteByIdIn(ids);
		
	}
}
