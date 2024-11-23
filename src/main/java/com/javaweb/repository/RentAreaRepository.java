package com.javaweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.RentAreaEntity;

public interface RentAreaRepository extends JpaRepository<RentAreaEntity, Long>{
	void deleteByBuilding(BuildingEntity buildingEntity);
	void deleteByBuilding_IdIn(List<Long> buildingIds);
}
