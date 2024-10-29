package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private DistrictRepository districtRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;
	
	@Override
	public List<BuildingResponseDTO> findAll(Map<String, Object> params, List<String> typeCodes) {
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(params, typeCodes);
		List<BuildingResponseDTO> results = new ArrayList<>();
		for (BuildingEntity building: buildingEntities) {
			BuildingResponseDTO buildingResponseDTO = new BuildingResponseDTO();
			buildingResponseDTO.setId(building.getId());
			buildingResponseDTO.setName(building.getName());
			
			DistrictEntity districtEntity = districtRepository.findNameById((Long)building.getDistrictId());
			buildingResponseDTO.setAddress(building.getStreet() + "," + building.getWard() + "," + districtEntity.getName());
			
			List<RentAreaEntity> rentAreaEntities = rentAreaRepository.getValueByBuildingId((Long) building.getDistrictId());
			List<String> listRentArea = new ArrayList<>();
			for (RentAreaEntity rentAreaEntity : rentAreaEntities) {
				String rentArea = rentAreaEntity.getValue();
				listRentArea.add(rentArea);
			}
			String rentAreaString = "";
			for (int i = 0; i < listRentArea.size(); i++) {
				rentAreaString += listRentArea.get(i) + " ";
			}
			rentAreaString = rentAreaString.trim();
			buildingResponseDTO.setRentArea(rentAreaString);
			
			buildingResponseDTO.setManagerName(building.getManagerName());
			buildingResponseDTO.setManagerPhoneNumber(building.getManagerPhoneNumber());
			buildingResponseDTO.setDirection(building.getDirection());
			buildingResponseDTO.setRentPrice(building.getRentPrice());
			buildingResponseDTO.setFloorArea(building.getFloorArea());
			buildingResponseDTO.setNumberOfBasement(building.getNumberOfBasement());
			results.add(buildingResponseDTO);
		}
		return results;
	}
}
