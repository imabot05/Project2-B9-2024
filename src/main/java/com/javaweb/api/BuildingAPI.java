package com.javaweb.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.model.ErrorResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.service.BuildingService;

import customexception.FieldRequiredException;

@RestController
@Transactional
public class BuildingAPI {
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Value("${dev.nguyen}")
	private String data;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping(value="/api/building")
	public List<BuildingDTO> getBuilding(@RequestParam(required = false) Map<String, Object> params,
				@RequestParam(name="typeCode", required=false) List<String> typeCode
			){
		List<BuildingDTO> result = buildingService.findAll(params, typeCode);
		return result;
	}
	
	public void validate(BuildingDTO buildingDTO) throws FieldRequiredException{
		if (buildingDTO.getName() == null || buildingDTO.getName().equals("")) {
			throw new FieldRequiredException("Name or number basement is null");
		}
	}
	
//	@GetMapping(value="/api/building/{id}")
//	public BuildingDTO getBuildingById(@PathVariable Long id){
//		 BuildingDTO result = new BuildingDTO();
//		 BuildingEntity building = buildingRepository.findById(id).get();
//		 return result;
//		
//	}
	
//	@GetMapping(value="/api/building/{name}")
//	public BuildingDTO getBuildingByName(@PathVariable String name) {
//		BuildingDTO result = new BuildingDTO();
//		List<BuildingEntity> building = buildingRepository.findByNameContaining(name);
//		System.out.println(building.size());
//		return result;
//	}
	
	@GetMapping(value="/api/building/{name}/{street}")
	public BuildingDTO getBuildingById(@PathVariable String name, @PathVariable String street) {
		BuildingDTO result = new BuildingDTO();
		List<BuildingEntity> building = buildingRepository.findByNameContainingAndStreet(name, street);
		return result;
	}
	
	@PostMapping(value="/api/building/")
	public void createBuilding(@RequestBody BuildingRequestDTO buildingRequestDTO) {
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setName(buildingRequestDTO.getName());
		buildingEntity.setStreet(buildingRequestDTO.getStreet());
		
		buildingEntity.setWard(buildingRequestDTO.getWard());
		DistrictEntity districtEntity = new DistrictEntity();
		districtEntity.setId(buildingRequestDTO.getDistrictId());
		buildingEntity.setDistrict(districtEntity);
		entityManager.merge(districtEntity);
		System.out.println("OKE");
	}
	@PutMapping(value="/api/building/")
	public void updateBuilding(@RequestBody BuildingRequestDTO buildingRequestDTO) {
		BuildingEntity buildingEntity = buildingRepository.findById(buildingRequestDTO.getId()).get();
		buildingEntity.setName(buildingRequestDTO.getName());
		buildingEntity.setStreet(buildingRequestDTO.getStreet());
		buildingEntity.setWard(buildingRequestDTO.getWard());
		DistrictEntity districtEntity = new DistrictEntity();
		districtEntity.setId(buildingRequestDTO.getDistrictId());
		buildingEntity.setDistrict(districtEntity);
		buildingRepository.save(buildingEntity);
		System.out.println("OKE");
	}
	
	@DeleteMapping(value="/api/building/{ids}")
	public void deleteBuilding(@PathVariable Long[] ids) {
		buildingRepository.deleteByIdIn(ids);
	}
}
