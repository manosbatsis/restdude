/**
 * calipso-hub-utilities - A full stack, high level framework for lazy application hackers.
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
package com.restdude.util;

import org.apache.commons.configuration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a configuration based on calipso.defaults.properties and calipso.properties
 * @author manos
 *
 */
public class ConfigurationFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationFactory.class);
	
	public static final String APP_NAME = "calipso.appName";
	public static final String APP_VERSION = "calipso.appVersion";
    public static final String FORCE_CODES = "calipso.registration.forceCodes";

    public static final String BASE_URL = "calipso.baseurl";
    public static final String INIT_DATA = "calipso.initData";
	public static final String FILES_DIR = "calipso.files.dir";
	

	public static final String CONTACT_NAME = "calipso.contact.name";
	public static final String CONTACT_URL = "calipso.contact.url";
	public static final String CONTACT_EMAIL = "calipso.contact.email";
	

	public static final String LICENSE_NAME = "calipso.license.name";
	public static final String LICENSE_URL = "calipso.license.url";

	public static final String TEST_EMAIL_ENABLE = "mail.test.enable";
	public static final String TEST_EMAIL_USER = "mail.test.user";
	
	public static final String FS_IMPL_CLASS = "fs.filePersistenceService";


    private static CompositeConfiguration config = new CompositeConfiguration();
	
	static {
		// add default and custom calipso properties
		String[] propertyFiles = {"calipso.properties", "calipso.defaults.properties"};
		for(String propFile : propertyFiles){
			addConfiguration(propFile);
		}
		// add system level properties
		config.addConfiguration(new SystemConfiguration());
		// add environment level properties
		config.addConfiguration(new EnvironmentConfiguration());
		
	}

	public static void addConfiguration(String propFile) {
		PropertiesConfiguration properties = loadClasspathResource(propFile);

		if(properties != null){
			config.addConfiguration(properties);
		}
	}

	public static PropertiesConfiguration loadClasspathResource(String propFile) {
		PropertiesConfiguration properties = null;
		try {
			properties = new PropertiesConfiguration(propFile);
			LOGGER.debug("Added configuration file {}", properties.getPath());
		} catch (ConfigurationException ex) {
			try {
				properties = new PropertiesConfiguration("classpath:/" + propFile);
				LOGGER.debug("Added configuration file {}", properties.getPath());
			} catch (ConfigurationException e) {
				LOGGER.warn("Failed to load configuration file {}", propFile);
			}
		}
		return properties;
	}

	public static Configuration getConfiguration() {
		return config;
	}
}
