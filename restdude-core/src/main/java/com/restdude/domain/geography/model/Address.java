/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.geography.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import org.javers.core.metamodel.annotation.ShallowReference;

import javax.persistence.*;


/**
 * An entity topersist postal addresses. 
 */
@ShallowReference
@Entity
@Table(name="postal_address")
public class Address extends AbstractSystemUuidPersistable  {
	
	private static final long serialVersionUID = 1L;

	private String address;

	private String address2;

	private String district;

	private String phone;

	@Column(name="postal_code")
	private String postalCode;

	@JsonProperty("lon")
	private Double longitude;
	
	@JsonProperty("lat")
	private Double latitude;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
	private LocalRegion city;


	public Address() {
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getAddress2() {
		return address2;
	}


	public void setAddress2(String address2) {
		this.address2 = address2;
	}


	public String getDistrict() {
		return district;
	}


	public void setDistrict(String district) {
		this.district = district;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getPostalCode() {
		return postalCode;
	}


	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public LocalRegion getCity() {
		return city;
	}


	public void setCity(LocalRegion city) {
		this.city = city;
	}
	
	

}