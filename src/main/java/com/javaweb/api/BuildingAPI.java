package com.javaweb.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {
//	@GetMapping("/api/building")
//	private void getBuildings(@RequestParam(name = "name",  required = false) String name,
//			@RequestParam(name = "ward", required = false) String ward,
//			@RequestParam(name = "districtid", required = false) Long districtId,
//			@RequestParam(name = "typeCode", required = false) List<String> typeCode
//			) {
//		System.out.println("OKAY " + name + " " + ward );
//	}
	@Autowired
	private BuildingService buildingService; 
	
	@GetMapping("/api/buildings")
    public List<BuildingResponseDTO> getBuildings(
            @RequestParam Map<String, Object> params,
            @RequestParam(name = "typeCode", required = false) List<String> typeCodes) {

        System.out.println("Received params: " + params);
        return buildingService.findAll(params, typeCodes);
    }
	
//	@PostMapping("/api/building")
//	private Object createBuilding(
//			@RequestBody BuildingDTO building
//			) {
//		validate(building);
//		return building;
//	}
	
	
	
	@DeleteMapping("/api/building/{ids}")
	private void deleteBuilding(@PathVariable Long[] ids) {
		System.out.println(ids);
	}
}
