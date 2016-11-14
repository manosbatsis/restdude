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
package com.restdude.domain.misc.model;

import com.restdude.domain.cms.model.Resource;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "license")
public class License extends Resource{
	
	// Basic information common to every license
	//private String name;             	// A unique name for this license
	private String short_name;       	// A short textual representation of the license name
	private String full_text;        	// A URI to the full text of this license
	private String logo_uri;         	// An optional logo for this license
	private String extra_rdf;        	// A buffer for additional RDF outside the scope of the primitives
	// Permissions for the license
	private boolean reproduction;     	// Do we allow the work to be reproduced?
	private boolean distribution;     	// Do we allow public distribution of the work?
	private boolean derivatives;      	// Do we allow derivative works?
	// Restrictions for the license
	private boolean noncommercial;    	// Can others profit from the work?
	// Requirements of the license
	private boolean notice;           	// Do we require that copyright notices be maintained?
	private boolean attribution;      	// Do we require attribution?
	private boolean share_alike;        // Do we require that derivative works use the same license?

}
