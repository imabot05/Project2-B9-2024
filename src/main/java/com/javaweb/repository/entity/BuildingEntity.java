package com.javaweb.repository.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "building")
public class BuildingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "numberofbasement")
	private Long numberOfBasement;
	
	@Column(name = "ward")
	private String ward;
	
	@Column(name = "street")
	private String street;
	
	@Column(name = "managername")
	private String managerName;
	
	@Column(name = "managerphonenumber")
	private String managerPhoneNumber;
	
	@Column(name = "floorarea")
	private Long floorArea;
	
	@Column(name = "direction")
	private String direction;
	
	@Column(name = "rentprice")
	private Long rentPrice;
	
	@Column(name = "servicefee")
	private String serviceFee;
	
	@Column(name = "brokeragefee")
	private Long brokerageFee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "districtid")
	private DistrictEntity district;
	
	@OneToMany(mappedBy = "buildingEntity", fetch = FetchType.LAZY)
	private List<RentAreaEntity> rentareas = new ArrayList<>();
	
	public List<RentAreaEntity> getRentareas() {
		return rentareas;
	}
	public void setRentareas(List<RentAreaEntity> rentareas) {
		this.rentareas = rentareas;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerPhoneNumber() {
		return managerPhoneNumber;
	}
	public void setManagerPhoneNumber(String managerPhoneNumber) {
		this.managerPhoneNumber = managerPhoneNumber;
	}
	public Long getFloorArea() {
		return floorArea;
	}
	public void setFloorArea(Long floorArea) {
		this.floorArea = floorArea;
	}
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Long getRentPrice() {
		return rentPrice;
	}
	public void setRentPrice(Long rentPrice) {
		this.rentPrice = rentPrice;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNumberOfBasement() {
		return numberOfBasement;
	}
	public void setNumberOfBasement(Long numberOfBasement) {
		this.numberOfBasement = numberOfBasement;
	}
	
	public DistrictEntity getDistrict() {
		return district;
	}
	
	public void setDistrict(DistrictEntity district) {
		this.district = district;
	}
	
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	public Long getBrokerageFee() {
		return brokerageFee;
	}
	public void setBrokerageFee(Long brokerageFee) {
		this.brokerageFee = brokerageFee;
	}
	
}
