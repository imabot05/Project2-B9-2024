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
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.customexceptions.InvalidDataException;
import com.javaweb.dto.BuildingDTO;
import com.javaweb.dto.ErrorDetailDTO;
import com.javaweb.dto.response.BuildingResponseDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.service.BuildingService;

@RestController
//@PropertySource("classpath:application.properties")
public class BuildingAPI {
	@Autowired
	private BuildingService buildingService; 
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping("/api/buildings")
    public List<BuildingResponseDTO> getBuildings(
            @RequestParam Map<String, Object> params,
            @RequestParam(name = "typeCode", required = false) List<String> typeCodes) {

        System.out.println("Received params: " + params);
        return buildingService.findAll(params, typeCodes);
    }
	
	private void validate(BuildingDTO building) {
		if (building.getName() == null || building.getName().equals("") || building.getNumberOfBasement() == null) {
			throw new InvalidDataException("Name or number of basements can't be empty");
		}
	}
	
	@PostMapping("/api/buildings")
	public Object createBuilding(@RequestBody BuildingDTO building) {
		validate(building);
		return buildingService.createBuilding(building);
	}
	
	@PutMapping("/api/buildings")
	public Object updateBuilding(@RequestBody BuildingDTO building) {
		if (building.getId() != null) {
			validate(building);
			buildingService.updateBuilding(building);
		} else {
			
		}
		return null;
	}
	
	@DeleteMapping("/api/buildings/{ids}")
	private void deleteBuilding(@PathVariable List<Long> ids) {
		if (ids.size() != 0) {
			buildingService.deleteById(ids);
		} 
	}
}
