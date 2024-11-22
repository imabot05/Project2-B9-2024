package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Component
public class BuildingConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingResponseDTO toBuildingResponseDTO(BuildingEntity building) {
		BuildingResponseDTO buildingResponseDTO = modelMapper.map(building, BuildingResponseDTO.class);
		DistrictEntity districtEntity = building.getDistrict();
		buildingResponseDTO.setAddress(building.getStreet() + "," + building.getWard() + "," + districtEntity.getName());
		
		List<RentAreaEntity> rentAreaEntities = building.getRentareas();
		String rentArea = rentAreaEntities.stream()
								.map(it -> it.getValue().toString()).collect(Collectors.joining(","));
		
		buildingResponseDTO.setRentArea(rentArea);
		return buildingResponseDTO;
	}
}
