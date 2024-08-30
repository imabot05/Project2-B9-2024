package com.javaweb.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.custom.BuildingRepositoryCustom;
import com.javaweb.repository.entity.BuildingEntity;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long>, BuildingRepositoryCustom {
//	List<BuildingEntity> findAll(BuildingSearchBuilder buildingSeachBuilder);
	void DeleteById(Long id);
	void deleteByIdIn(Long[] ids);
	List<BuildingEntity> findByNameContaining(String name);
	List<BuildingEntity> findByNameContainingAndStreet(String name, String street);
}
