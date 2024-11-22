package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Component
public class BuildingConverter {
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingResponseDTO toBuildingResponseDTO(BuildingEntity building) {
		BuildingResponseDTO buildingResponseDTO = modelMapper.map(building, BuildingResponseDTO.class);
//		buildingResponseDTO.setId(building.getId());
//		buildingResponseDTO.setName(building.getName());
		
		DistrictEntity districtEntity = districtRepository.findNameById((Long)building.getDistrictId());
		buildingResponseDTO.setAddress(building.getStreet() + "," + building.getWard() + "," + districtEntity.getName());
		
		List<RentAreaEntity> rentAreaEntities = rentAreaRepository.findByBuildingId(building.getId());
		String rentArea = rentAreaEntities.stream()
								.map(it -> it.getValue().toString()).collect(Collectors.joining(","));
		
		buildingResponseDTO.setRentArea(rentArea);
//		buildingResponseDTO.setManagerName(building.getManagerName());
//		buildingResponseDTO.setManagerPhoneNumber(building.getManagerPhoneNumber());
//		buildingResponseDTO.setDirection(building.getDirection());
//		buildingResponseDTO.setRentPrice(building.getRentPrice());
//		buildingResponseDTO.setFloorArea(building.getFloorArea());
//		buildingResponseDTO.setNumberOfBasement(building.getNumberOfBasement());
		return buildingResponseDTO;
	}
}
