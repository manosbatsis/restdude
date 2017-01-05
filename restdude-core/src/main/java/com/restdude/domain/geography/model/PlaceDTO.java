/**
 * Restdude
 * -------------------------------------------------------------------
 * Module restdude-core, https://manosbatsis.github.io/restdude/restdude-core
 * <p>
 * Full stack, high level framework for horizontal, model-driven application hackers.
 * <p>
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.geography.model;

/**
 * Created by manos on 4/12/2016.
 */
public class PlaceDTO {
    /*

    administrative_area_level_1
    administrative_area_level_2
    administrative_area_level_3
    administrative_area_level_4
    administrative_area_level_5
    colloquial_area indicates a commonly-used alternative name for the entity.
    locality
    ward indicates a specific type of Japanese locality, to facilitate distinction between multiple locality components within a Japanese address.
    sublocality indicates a first-order civil entity below a locality. For some locations may receive one of the additional types: sublocality_level_1 to sublocality_level_5. Each sublocality level is a civil entity. Larger numbers indicate a smaller geographic area.
    neighborhood indicates a named neighborhood
    premise indicates a named location, usually a building or collection of buildings with a common name
    subpremise indicates a first-order entity below a named location, usually a singular building within a collection of buildings with a common name
    postal_code indicates a postal code as used to address postal mail within the country.
    natural_feature indicates a prominent natural feature.
    airport indicates an airport.
    park indicates a named park.
    point_of_interest indicates a named point of interest. Typically, these "POI"s are prominent local entities that don't easily fit in another category, such as "Empire State Building" or "Statue of Liberty."

    * */

    // street address
    /**
     * indicates a precise street address
     */
    private String street_address;
    /**
     * indicates a named route (such as "US 101")
     */
    private String route;
    /////////////////////////////////////////////////
    /**
     * indicates a named location, usually a building or collection of buildings with a common name
     */
    private String premise;
    /**
     * indicates a first-order entity below a named location, usually a singular building within a collection of buildings with a common name
     */
    private String subpremise;
    ////////////////////////////////////////////////
    /**
     * indicates a prominent natural feature.
     */
    private String natural_feature;
    ////////////////////////////////////////////////
    /**
     * indicates an airport.
     */
    private String airport;
    ////////////////////////////////////////////////
    /**
     * indicates a named park.
     */
    private String park;
    ////////////////////////////////////////////////
    /**
     * indicates a named point of interest. Typically, these "POI"s are prominent local entities that don't easily fit in another category, such as "Empire State Building" or "Statue of Liberty."
     */
    private String point_of_interest;


    private String postal_code;


    /**
     * indicates an incorporated city or town political entity.
     */
    private String locality;
    /**
     * indicates a specific type of Japanese locality, to facilitate distinction between multiple locality components within a Japanese address.
     */
    private String ward;
    /**
     * indicates a first-order civil entity below a locality. For some locations may receive one of the additional types: sublocality_level_1 to sublocality_level_5. Each sublocality level is a civil entity. Larger numbers indicate a smaller geographic area.
     */
    private String sublocality;

    //

    /**
     * indicates the national political entity,
     */
    private String country;
    /**
     * indicates a first-order civil entity below the country level. Within the United States, these administrative levels are states. Not all nations exhibit these administrative levels. In most cases, administrative_area_level_1 short names will closely match ISO 3166-2 subdivisions and other widely circulated lists; however this is not guaranteed as our geocoding results are based on a variety of signals and location data.
     */
    private String administrative_area_level_1;
    /**
     * indicates a second-order civil entity below the country level. Within the United States, these administrative levels are counties. Not all nations exhibit these administrative levels.
     */
    private String administrative_area_level_2;
    /**
     * indicates a third-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
     */
    private String administrative_area_level_3;
    /**
     * indicates a fourth-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
     */
    private String administrative_area_level_4;
    /**
     * indicates a fifth-order civil entity below the country level. This type indicates a minor civil division. Not all nations exhibit these administrative levels.
     */
    private String administrative_area_level_5;
    private String administrative_area_level_6;
}
