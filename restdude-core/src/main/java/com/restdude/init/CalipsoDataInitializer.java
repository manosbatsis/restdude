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
package com.restdude.init;

import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.geography.model.Continent;
import com.restdude.domain.geography.model.Country;
import com.restdude.domain.geography.repository.ContinentRepository;
import com.restdude.domain.geography.repository.CountryRepository;
import com.restdude.domain.users.model.*;
import com.restdude.domain.users.repository.RoleRepository;
import com.restdude.domain.users.repository.UserRegistrationCodeBatchRepository;
import com.restdude.domain.users.repository.UserRegistrationCodeRepository;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.domain.users.service.UserService;
import com.restdude.domain.util.email.service.EmailService;
import com.restdude.util.ConfigurationFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.resthub.common.util.PostInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class CalipsoDataInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalipsoDataInitializer.class);

	private UserService userService;
	private EmailService emailService;
	private ContinentRepository continentRepository;;
	private CountryRepository countryRepository;
	private RoleRepository roleRepository;
	private UserRepository userRepository;
	private ModelRepository<UserCredentials, String> credentialsRepository;
	private UserRegistrationCodeRepository userRegistrationCodeRepository;
	private UserRegistrationCodeBatchRepository userRegistrationCodeBatchRepository;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Autowired
	public void setContinentRepository(ContinentRepository continentRepository) {
		this.continentRepository = continentRepository;
	}

	@Autowired
	public void setCountryRepository(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setCredentialsRepository(ModelRepository<UserCredentials, String> credentialsRepository) {
		this.credentialsRepository = credentialsRepository;
	}

	@Autowired
	public void setUserRegistrationCodeRepository(UserRegistrationCodeRepository userRegistrationCodeRepository) {
		this.userRegistrationCodeRepository = userRegistrationCodeRepository;
	}

	@Autowired
	public void setUserRegistrationCodeBatchRepository(UserRegistrationCodeBatchRepository userRegistrationCodeBatchRepository) {
		this.userRegistrationCodeBatchRepository = userRegistrationCodeBatchRepository;
	}
	

	@PostInitialize(order = 10)
	@Transactional(readOnly = false)
	public void postInitialize(){
		
		Configuration config = ConfigurationFactory.getConfiguration();
		boolean initData = config.getBoolean(ConfigurationFactory.INIT_DATA, true);

		if (initData && this.userRepository.count() == 0) {
			

			this.initContinentsAndCountries();
			this.initRoles();
			
			
			Role adminRole = this.roleRepository.findByIdOrName(Role.ROLE_ADMIN);
			Role operatorRole = this.roleRepository.findByIdOrName(Role.ROLE_SITE_OPERATOR);
			
			Date now = new Date();

			User system = new User();
			system.setEmail("system@abiss.gr");
			system.setFirstName("System");
			system.setLastName("User");
            system.setCredentials(new UserCredentials.Builder().active(false).username("system").password("system").build());
            system.setLastVisit(now);
			system = userService.createTest(system);

			// login
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(system, system.getCredentials().getPassword(), system.getRoles()));

			User adminUser = new User();
			adminUser.setEmail("info@abiss.gr");
			adminUser.setFirstName("Admin");
			adminUser.setLastName("User");
			adminUser.setLastVisit(now);
			adminUser.addRole(adminRole);
            adminUser.setCredentials(new UserCredentials.Builder().active(true).username("admin").password("admin").build());
//			adminUser.setCreatedBy(system);
			adminUser = userService.createTest(adminUser);

			User opUser = new User();
			opUser.setEmail("operator@abiss.gr");
			opUser.setFirstName("Operator");
			opUser.setLastName("User");
            opUser.setCredentials(new UserCredentials.Builder().active(true).username("operator").password("operator").build());
            opUser.setLastVisit(now);
            opUser.addRole(operatorRole);
//			opUser.setCreatedBy(system);
			opUser = userService.createTest(opUser);

			int usersMax =  10;
			int usersCreated = 0;
			while(usersCreated < usersMax){
				for (String fullName : this.getTenNames()) {
					String userName = fullName.toLowerCase().replace(" ", "") + usersCreated;
					User u = new User();
					u.setEmail(userName + "@abiss.gr");
					u.setFirstName(fullName.substring(0, fullName.indexOf(" ")));
					u.setLastName(fullName.substring(fullName.indexOf(" ") + 1));
                    u.setCredentials(new UserCredentials.Builder().active(true).username(userName).password(userName).build());
                    u.setLastVisit(now);
//					u.setCreatedBy(system);
					u = userService.createTest(u);
					
					usersCreated++;
					LOGGER.info("Created user: " + u);
					if(usersCreated >= usersMax){
						break;
					}
				}
			}

			// sample registration codes batch
			UserRegistrationCodeBatch batch = new UserRegistrationCodeBatch();
			batch.setCreatedBy(system);
			batch.setBatchSize(20);
			batch.setName("INITDATATEST01");
			batch.setDescription("Sample batch created as initial test data");
			batch.setExpirationDate(DateUtils.addMonths(batch.getCreatedDate(), 1));
			batch = this.userRegistrationCodeBatchRepository.save(batch);
			// create codes
			List<UserRegistrationCode> codes = new ArrayList<UserRegistrationCode>(batch.getBatchSize());
			for (int i = 0; i < batch.getBatchSize(); i++) {
				codes.add(new UserRegistrationCode(batch));
			}
			this.userRegistrationCodeRepository.save(codes);



		}
		
		// send test email?
		if(config.getBoolean(ConfigurationFactory.TEST_EMAIL_ENABLE, false)){
			String testEmailUsername = config.getString(ConfigurationFactory.TEST_EMAIL_USER, "system");
			if(StringUtils.isNotBlank(testEmailUsername)){
                User u = this.userService.findOneByUserNameOrEmail(testEmailUsername);
                this.emailService.sendTest(u);
			}
		}
		

	}


	private void initRoles() {
		if (this.roleRepository.count() == 0) {
			Role adminRole = new Role(Role.ROLE_ADMIN, "System Administrator.");
			adminRole = this.roleRepository.save(adminRole);
			Role siteAdminRole = new Role(Role.ROLE_SITE_OPERATOR, "Site Operator.");
			siteAdminRole = this.roleRepository.save(siteAdminRole);
			// this is added to users by user service, just creating it
			Role userRole = new Role(Role.ROLE_USER, "Logged in user");
			userRole = this.roleRepository.save(userRole);
		}
	}

	protected void initContinentsAndCountries() {

		if (continentRepository.count() == 0) {
			Continent AF = continentRepository.save(new Continent("AF", "Africa"));
			Continent AN = continentRepository.save(new Continent("AN", "Antarctica"));
			Continent AS = continentRepository.save(new Continent("AS", "Asia"));
			Continent EU = continentRepository.save(new Continent("EU", "Europe"));
			Continent NA = continentRepository.save(new Continent("NA", "North America"));
			Continent OC = continentRepository.save(new Continent("OC", "Oceania"));
			Continent SA = continentRepository.save(new Continent("SA", "South America"));

			countryRepository.save(new Country("AD", "Andorra", "Andorra", "376", EU, "Andorra la Vella", "EUR", "ca"));
			countryRepository.save(new Country("AE", "United Arab Emirates", "دولة الإمارات العربية المتحدة", "971", AS,
					"Abu Dhabi", "AED", "ar"));
			countryRepository.save(new Country("AF", "Afghanistan", "افغانستان", "93", AS, "Kabul", "AFN", "ps,uz,tk"));
			countryRepository.save(new Country("AG", "Antigua and Barbuda", "Antigua and Barbuda", "1268", NA,
					"Saint John's", "XCD", "en"));
			countryRepository.save(new Country("AI", "Anguilla", "Anguilla", "1264", NA, "The Valley", "XCD", "en"));
			countryRepository.save(new Country("AL", "Albania", "Shqipëria", "355", EU, "Tirana", "ALL", "sq"));
			countryRepository.save(new Country("AM", "Armenia", "Հայաստան", "374", AS, "Yerevan", "AMD", "hy,ru"));
			countryRepository.save(new Country("AO", "Angola", "Angola", "244", AF, "Luanda", "AOA", "pt"));
			countryRepository.save(new Country("AQ", "Antarctica", null, null, AN, null, null, null));
			countryRepository
					.save(new Country("AR", "Argentina", "Argentina", "54", SA, "Buenos Aires", "ARS", "es,gn"));
			countryRepository.save(
					new Country("AS", "American Samoa", "American Samoa", "1684", OC, "Pago Pago", "USD", "en,sm"));
			countryRepository.save(new Country("AT", "Austria", "Österreich", "43", EU, "Vienna", "EUR", "de"));
			countryRepository.save(new Country("AU", "Australia", "Australia", "61", OC, "Canberra", "AUD", "en"));
			countryRepository.save(new Country("AW", "Aruba", "Aruba", "297", NA, "Oranjestad", "AWG", "nl,pa"));
			countryRepository.save(new Country("AX", "Åland", "Åland", "358", EU, "Mariehamn", "EUR", "sv"));
			countryRepository.save(new Country("AZ", "Azerbaijan", "Azərbaycan", "994", AS, "Baku", "AZN", "az,hy"));
			countryRepository.save(new Country("BA", "Bosnia and Herzegovina", "Bosna i Hercegovina", "387", EU,
					"Sarajevo", "BAM", "bs,hr,sr"));
			countryRepository.save(new Country("BB", "Barbados", "Barbados", "1246", NA, "Bridgetown", "BBD", "en"));
			countryRepository.save(new Country("BD", "Bangladesh", "Bangladesh", "880", AS, "Dhaka", "BDT", "bn"));
			countryRepository.save(new Country("BE", "Belgium", "België", "32", EU, "Brussels", "EUR", "nl,fr,de"));
			countryRepository
					.save(new Country("BF", "Burkina Faso", "Burkina Faso", "226", AF, "Ouagadougou", "XOF", "fr,ff"));
			countryRepository.save(new Country("BG", "Bulgaria", "България", "359", EU, "Sofia", "BGN", "bg"));
			countryRepository.save(new Country("BH", "Bahrain", "‏البحرين", "973", AS, "Manama", "BHD", "ar"));
			countryRepository.save(new Country("BI", "Burundi", "Burundi", "257", AF, "Bujumbura", "BIF", "fr,rn"));
			countryRepository.save(new Country("BJ", "Benin", "Bénin", "229", AF, "Porto-Novo", "XOF", "fr"));
			countryRepository.save(
					new Country("BL", "Saint Barthélemy", "Saint-Barthélemy", "590", NA, "Gustavia", "EUR", "fr"));
			countryRepository.save(new Country("BM", "Bermuda", "Bermuda", "1441", NA, "Hamilton", "BMD", "en"));
			countryRepository.save(new Country("BN", "Brunei", "Negara Brunei Darussalam", "673", AS,
					"Bandar Seri Begawan", "BND", "ms"));
			countryRepository.save(new Country("BO", "Bolivia", "Bolivia", "591", SA, "Sucre", "BOB,BOV", "es,ay,qu"));
			countryRepository.save(new Country("BQ", "Bonaire", "Bonaire", "5997", NA, "Kralendijk", "USD", "nl"));
			countryRepository.save(new Country("BR", "Brazil", "Brasil", "55", SA, "Brasília", "BRL", "pt"));
			countryRepository.save(new Country("BS", "Bahamas", "Bahamas", "1242", NA, "Nassau", "BSD", "en"));
			countryRepository.save(new Country("BT", "Bhutan", "ʼbrug-yul", "975", AS, "Thimphu", "BTN,INR", "dz"));
			countryRepository.save(new Country("BV", "Bouvet Island", "Bouvetøya", null, AN, null, "NOK", null));
			countryRepository.save(new Country("BW", "Botswana", "Botswana", "267", AF, "Gaborone", "BWP", "en,tn"));
			countryRepository.save(new Country("BY", "Belarus", "Белару́сь", "375", EU, "Minsk", "BYR", "be,ru"));
			countryRepository.save(new Country("BZ", "Belize", "Belize", "501", NA, "Belmopan", "BZD", "en,es"));
			countryRepository.save(new Country("CA", "Canada", "Canada", "1", NA, "Ottawa", "CAD", "en,fr"));
			countryRepository.save(new Country("CC", "Cocos [Keeling] Islands", "Cocos (Keeling) Islands", "61", AS,
					"West Island", "AUD", "en"));
			countryRepository.save(new Country("CD", "Democratic Republic of the Congo",
					"République démocratique du Congo", "243", AF, "Kinshasa", "CDF", "fr,ln,kg,sw,lu"));
			countryRepository.save(new Country("CF", "Central African Republic", "Ködörösêse tî Bêafrîka", "236", AF,
					"Bangui", "XAF", "fr,sg"));
			countryRepository.save(new Country("CG", "Republic of the Congo", "République du Congo", "242", AF,
					"Brazzaville", "XAF", "fr,ln"));
			countryRepository
					.save(new Country("CH", "Switzerland", "Schweiz", "41", EU, "Bern", "CHE,CHF,CHW", "de,fr,it"));
			countryRepository
					.save(new Country("CI", "Ivory Coast", "Côte d'Ivoire", "225", AF, "Yamoussoukro", "XOF", "fr"));
			countryRepository.save(new Country("CK", "Cook Islands", "Cook Islands", "682", OC, "Avarua", "NZD", "en"));
			countryRepository.save(new Country("CL", "Chile", "Chile", "56", SA, "Santiago", "CLF,CLP", "es"));
			countryRepository.save(new Country("CM", "Cameroon", "Cameroon", "237", AF, "Yaoundé", "XAF", "en,fr"));
			countryRepository.save(new Country("CN", "China", "中国", "86", AS, "Beijing", "CNY", "zh"));
			countryRepository.save(new Country("CO", "Colombia", "Colombia", "57", SA, "Bogotá", "COP", "es"));
			countryRepository.save(new Country("CR", "Costa Rica", "Costa Rica", "506", NA, "San José", "CRC", "es"));
			countryRepository.save(new Country("CU", "Cuba", "Cuba", "53", NA, "Havana", "CUC,CUP", "es"));
			countryRepository.save(new Country("CV", "Cape Verde", "Cabo Verde", "238", AF, "Praia", "CVE", "pt"));
			countryRepository
					.save(new Country("CW", "Curacao", "Curaçao", "5999", NA, "Willemstad", "ANG", "nl,pa,en"));
			countryRepository.save(new Country("CX", "Christmas Island", "Christmas Island", "61", AS,
					"Flying Fish Cove", "AUD", "en"));
			countryRepository.save(new Country("CY", "Cyprus", "Κύπρος", "357", EU, "Nicosia", "EUR", "el,tr,hy"));
			countryRepository
					.save(new Country("CZ", "Czechia", "Česká republika", "420", EU, "Prague", "CZK", "cs,sk"));
			countryRepository.save(new Country("DE", "Germany", "Deutschland", "49", EU, "Berlin", "EUR", "de"));
			countryRepository.save(new Country("DJ", "Djibouti", "Djibouti", "253", AF, "Djibouti", "DJF", "fr,ar"));
			countryRepository.save(new Country("DK", "Denmark", "Danmark", "45", EU, "Copenhagen", "DKK", "da"));
			countryRepository.save(new Country("DM", "Dominica", "Dominica", "1767", NA, "Roseau", "XCD", "en"));
			countryRepository.save(new Country("DO", "Dominican Republic", "República Dominicana", "1809,1829,1849", NA,
					"Santo Domingo", "DOP", "es"));
			countryRepository.save(new Country("DZ", "Algeria", "الجزائر", "213", AF, "Algiers", "DZD", "ar"));
			countryRepository.save(new Country("EC", "Ecuador", "Ecuador", "593", SA, "Quito", "USD", "es"));
			countryRepository.save(new Country("EE", "Estonia", "Eesti", "372", EU, "Tallinn", "EUR", "et"));
			countryRepository.save(new Country("EG", "Egypt", "مصر‎", "20", AF, "Cairo", "EGP", "ar"));
			countryRepository.save(
					new Country("EH", "Western Sahara", "الصحراء الغربية", "212", AF, "El Aaiún", "MAD,DZD,MRO", "es"));
			countryRepository.save(new Country("ER", "Eritrea", "ኤርትራ", "291", AF, "Asmara", "ERN", "ti,ar,en"));
			countryRepository.save(new Country("ES", "Spain", "España", "34", EU, "Madrid", "EUR", "es,eu,ca,gl,oc"));
			countryRepository.save(new Country("ET", "Ethiopia", "ኢትዮጵያ", "251", AF, "Addis Ababa", "ETB", "am"));
			countryRepository.save(new Country("FI", "Finland", "Suomi", "358", EU, "Helsinki", "EUR", "fi,sv"));
			countryRepository.save(new Country("FJ", "Fiji", "Fiji", "679", OC, "Suva", "FJD", "en,fj,hi,ur"));
			countryRepository
					.save(new Country("FK", "Falkland Islands", "Falkland Islands", "500", SA, "Stanley", "FKP", "en"));
			countryRepository.save(new Country("FM", "Micronesia", "Micronesia", "691", OC, "Palikir", "USD", "en"));
			countryRepository.save(new Country("FO", "Faroe Islands", "Føroyar", "298", EU, "Tórshavn", "DKK", "fo"));
			countryRepository.save(new Country("FR", "France", "France", "33", EU, "Paris", "EUR", "fr"));
			countryRepository.save(new Country("GA", "Gabon", "Gabon", "241", AF, "Libreville", "XAF", "fr"));
			countryRepository
					.save(new Country("GB", "United Kingdom", "United Kingdom", "44", EU, "London", "GBP", "en"));
			countryRepository.save(new Country("GD", "Grenada", "Grenada", "1473", NA, "St. George's", "XCD", "en"));
			countryRepository.save(new Country("GE", "Georgia", "საქართველო", "995", AS, "Tbilisi", "GEL", "ka"));
			countryRepository
					.save(new Country("GF", "French Guiana", "Guyane française", "594", SA, "Cayenne", "EUR", "fr"));
			countryRepository
					.save(new Country("GG", "Guernsey", "Guernsey", "44", EU, "St. Peter Port", "GBP", "en,fr"));
			countryRepository.save(new Country("GH", "Ghana", "Ghana", "233", AF, "Accra", "GHS", "en"));
			countryRepository.save(new Country("GI", "Gibraltar", "Gibraltar", "350", EU, "Gibraltar", "GIP", "en"));
			countryRepository.save(new Country("GL", "Greenland", "Kalaallit Nunaat", "299", NA, "Nuuk", "DKK", "kl"));
			countryRepository.save(new Country("GM", "Gambia", "Gambia", "220", AF, "Banjul", "GMD", "en"));
			countryRepository.save(new Country("GN", "Guinea", "Guinée", "224", AF, "Conakry", "GNF", "fr,ff"));
			countryRepository
					.save(new Country("GP", "Guadeloupe", "Guadeloupe", "590", NA, "Basse-Terre", "EUR", "fr"));
			countryRepository.save(
					new Country("GQ", "Equatorial Guinea", "Guinea Ecuatorial", "240", AF, "Malabo", "XAF", "es,fr"));
			countryRepository.save(new Country("GR", "Greece", "Ελλάδα", "30", EU, "Athens", "EUR", "el"));
			countryRepository.save(new Country("GS", "South Georgia and the South Sandwich Islands", "South Georgia",
					"500", AN, "King Edward Point", "GBP", "en"));
			countryRepository
					.save(new Country("GT", "Guatemala", "Guatemala", "502", NA, "Guatemala City", "GTQ", "es"));
			countryRepository.save(new Country("GU", "Guam", "Guam", "1671", OC, "Hagåtña", "USD", "en,ch,es"));
			countryRepository
					.save(new Country("GW", "Guinea-Bissau", "Guiné-Bissau", "245", AF, "Bissau", "XOF", "pt"));
			countryRepository.save(new Country("GY", "Guyana", "Guyana", "592", SA, "Georgetown", "GYD", "en"));
			countryRepository.save(new Country("HK", "Hong Kong", "香港", "852", AS, "City of Victoria", "HKD", "zh,en"));
			countryRepository.save(new Country("HM", "Heard Island and McDonald Islands",
					"Heard Island and McDonald Islands", null, AN, null, "AUD", "en"));
			countryRepository.save(new Country("HN", "Honduras", "Honduras", "504", NA, "Tegucigalpa", "HNL", "es"));
			countryRepository.save(new Country("HR", "Croatia", "Hrvatska", "385", EU, "Zagreb", "HRK", "hr"));
			countryRepository
					.save(new Country("HT", "Haiti", "Haïti", "509", NA, "Port-au-Prince", "HTG,USD", "fr,ht"));
			countryRepository.save(new Country("HU", "Hungary", "Magyarország", "36", EU, "Budapest", "HUF", "hu"));
			countryRepository.save(new Country("ID", "Indonesia", "Indonesia", "62", AS, "Jakarta", "IDR", "id"));
			countryRepository.save(new Country("IE", "Ireland", "Éire", "353", EU, "Dublin", "EUR", "ga,en"));
			countryRepository.save(new Country("IL", "Israel", "יִשְׂרָאֵל", "972", AS, "Jerusalem", "ILS", "he,ar"));
			countryRepository
					.save(new Country("IM", "Isle of Man", "Isle of Man", "44", EU, "Douglas", "GBP", "en,gv"));
			countryRepository.save(new Country("IN", "India", "भारत", "91", AS, "New Delhi", "INR", "hi,en"));
			countryRepository.save(new Country("IO", "British Indian Ocean Territory", "British Indian Ocean Territory",
					"246", AS, "Diego Garcia", "USD", "en"));
			countryRepository.save(new Country("IQ", "Iraq", "العراق", "964", AS, "Baghdad", "IQD", "ar,ku"));
			countryRepository.save(new Country("IR", "Iran", "Irān", "98", AS, "Tehran", "IRR", "fa"));
			countryRepository.save(new Country("IS", "Iceland", "Ísland", "354", EU, "Reykjavik", "ISK", "is"));
			countryRepository.save(new Country("IT", "Italy", "Italia", "39", EU, "Rome", "EUR", "it"));
			countryRepository.save(new Country("JE", "Jersey", "Jersey", "44", EU, "Saint Helier", "GBP", "en,fr"));
			countryRepository.save(new Country("JM", "Jamaica", "Jamaica", "1876", NA, "Kingston", "JMD", "en"));
			countryRepository.save(new Country("JO", "Jordan", "الأردن", "962", AS, "Amman", "JOD", "ar"));
			countryRepository.save(new Country("JP", "Japan", "日本", "81", AS, "Tokyo", "JPY", "ja"));
			countryRepository.save(new Country("KE", "Kenya", "Kenya", "254", AF, "Nairobi", "KES", "en,sw"));
			countryRepository.save(new Country("KG", "Kyrgyzstan", "Кыргызстан", "996", AS, "Bishkek", "KGS", "ky,ru"));
			countryRepository.save(new Country("KH", "Cambodia", "Kâmpŭchéa", "855", AS, "Phnom Penh", "KHR", "km"));
			countryRepository.save(new Country("KI", "Kiribati", "Kiribati", "686", OC, "South Tarawa", "AUD", "en"));
			countryRepository.save(new Country("KM", "Comoros", "Komori", "269", AF, "Moroni", "KMF", "ar,fr"));
			countryRepository.save(new Country("KN", "Saint Kitts and Nevis", "Saint Kitts and Nevis", "1869", NA,
					"Basseterre", "XCD", "en"));
			countryRepository.save(new Country("KP", "North Korea", "북한", "850", AS, "Pyongyang", "KPW", "ko"));
			countryRepository.save(new Country("KR", "South Korea", "대한민국", "82", AS, "Seoul", "KRW", "ko"));
			countryRepository.save(new Country("KW", "Kuwait", "الكويت", "965", AS, "Kuwait City", "KWD", "ar"));
			countryRepository.save(
					new Country("KY", "Cayman Islands", "Cayman Islands", "1345", NA, "George Town", "KYD", "en"));
			countryRepository.save(new Country("KZ", "Kazakhstan", "Қазақстан", "76,77", AS, "Astana", "KZT", "kk,ru"));
			countryRepository.save(new Country("LA", "Laos", "ສປປລາວ", "856", AS, "Vientiane", "LAK", "lo"));
			countryRepository.save(new Country("LB", "Lebanon", "لبنان", "961", AS, "Beirut", "LBP", "ar,fr"));
			countryRepository
					.save(new Country("LC", "Saint Lucia", "Saint Lucia", "1758", NA, "Castries", "XCD", "en"));
			countryRepository
					.save(new Country("LI", "Liechtenstein", "Liechtenstein", "423", EU, "Vaduz", "CHF", "de"));
			countryRepository.save(new Country("LK", "Sri Lanka", "śrī laṃkāva", "94", AS, "Colombo", "LKR", "si,ta"));
			countryRepository.save(new Country("LR", "Liberia", "Liberia", "231", AF, "Monrovia", "LRD", "en"));
			countryRepository.save(new Country("LS", "Lesotho", "Lesotho", "266", AF, "Maseru", "LSL,ZAR", "en,st"));
			countryRepository.save(new Country("LT", "Lithuania", "Lietuva", "370", EU, "Vilnius", "LTL", "lt"));
			countryRepository
					.save(new Country("LU", "Luxembourg", "Luxembourg", "352", EU, "Luxembourg", "EUR", "fr,de,lb"));
			countryRepository.save(new Country("LV", "Latvia", "Latvija", "371", EU, "Riga", "EUR", "lv"));
			countryRepository.save(new Country("LY", "Libya", "‏ليبيا", "218", AF, "Tripoli", "LYD", "ar"));
			countryRepository.save(new Country("MA", "Morocco", "المغرب", "212", AF, "Rabat", "MAD", "ar"));
			countryRepository.save(new Country("MC", "Monaco", "Monaco", "377", EU, "Monaco", "EUR", "fr"));
			countryRepository.save(new Country("MD", "Moldova", "Moldova", "373", EU, "Chișinău", "MDL", "ro"));
			countryRepository
					.save(new Country("ME", "Montenegro", "Црна Гора", "382", EU, "Podgorica", "EUR", "sr,bs,sq,hr"));
			countryRepository
					.save(new Country("MF", "Saint Martin", "Saint-Martin", "590", NA, "Marigot", "EUR", "en,fr,nl"));
			countryRepository
					.save(new Country("MG", "Madagascar", "Madagasikara", "261", AF, "Antananarivo", "MGA", "fr,mg"));
			countryRepository
					.save(new Country("MH", "Marshall Islands", "M̧ajeļ", "692", OC, "Majuro", "USD", "en,mh"));
			countryRepository.save(new Country("MK", "Macedonia", "Македонија", "389", EU, "Skopje", "MKD", "mk"));
			countryRepository.save(new Country("ML", "Mali", "Mali", "223", AF, "Bamako", "XOF", "fr"));
			countryRepository.save(new Country("MM", "Myanmar [Burma]", "Myanma", "95", AS, "Naypyidaw", "MMK", "my"));
			countryRepository.save(new Country("MN", "Mongolia", "Монгол улс", "976", AS, "Ulan Bator", "MNT", "mn"));
			countryRepository.save(new Country("MO", "Macao", "澳門", "853", AS, null, "MOP", "zh,pt"));
			countryRepository.save(new Country("MP", "Northern Mariana Islands", "Northern Mariana Islands", "1670", OC,
					"Saipan", "USD", "en,ch"));
			countryRepository
					.save(new Country("MQ", "Martinique", "Martinique", "596", NA, "Fort-de-France", "EUR", "fr"));
			countryRepository.save(new Country("MR", "Mauritania", "موريتانيا", "222", AF, "Nouakchott", "MRO", "ar"));
			countryRepository.save(new Country("MS", "Montserrat", "Montserrat", "1664", NA, "Plymouth", "XCD", "en"));
			countryRepository.save(new Country("MT", "Malta", "Malta", "356", EU, "Valletta", "EUR", "mt,en"));
			countryRepository.save(new Country("MU", "Mauritius", "Maurice", "230", AF, "Port Louis", "MUR", "en"));
			countryRepository.save(new Country("MV", "Maldives", "Maldives", "960", AS, "Malé", "MVR", "dv"));
			countryRepository.save(new Country("MW", "Malawi", "Malawi", "265", AF, "Lilongwe", "MWK", "en,ny"));
			countryRepository.save(new Country("MX", "Mexico", "México", "52", NA, "Mexico City", "MXN", "es"));
			countryRepository.save(new Country("MY", "Malaysia", "Malaysia", "60", AS, "Kuala Lumpur", "MYR", null));
			countryRepository.save(new Country("MZ", "Mozambique", "Moçambique", "258", AF, "Maputo", "MZN", "pt"));
			countryRepository.save(new Country("NA", "Namibia", "Namibia", "264", AF, "Windhoek", "NAD,ZAR", "en,af"));
			countryRepository
					.save(new Country("NC", "New Caledonia", "Nouvelle-Calédonie", "687", OC, "Nouméa", "XPF", "fr"));
			countryRepository.save(new Country("NE", "Niger", "Niger", "227", AF, "Niamey", "XOF", "fr"));
			countryRepository
					.save(new Country("NF", "Norfolk Island", "Norfolk Island", "672", OC, "Kingston", "AUD", "en"));
			countryRepository.save(new Country("NG", "Nigeria", "Nigeria", "234", AF, "Abuja", "NGN", "en"));
			countryRepository.save(new Country("NI", "Nicaragua", "Nicaragua", "505", NA, "Managua", "NIO", "es"));
			countryRepository.save(new Country("NL", "Netherlands", "Nederland", "31", EU, "Amsterdam", "EUR", "nl"));
			countryRepository.save(new Country("NO", "Norway", "Norge", "47", EU, "Oslo", "NOK", "no,nb,nn"));
			countryRepository.save(new Country("NP", "Nepal", "नपल", "977", AS, "Kathmandu", "NPR", "ne"));
			countryRepository.save(new Country("NR", "Nauru", "Nauru", "674", OC, "Yaren", "AUD", "en,na"));
			countryRepository.save(new Country("NU", "Niue", "Niuē", "683", OC, "Alofi", "NZD", "en"));
			countryRepository
					.save(new Country("NZ", "New Zealand", "New Zealand", "64", OC, "Wellington", "NZD", "en,mi"));
			countryRepository.save(new Country("OM", "Oman", "عمان", "968", AS, "Muscat", "OMR", "ar"));
			countryRepository.save(new Country("PA", "Panama", "Panamá", "507", NA, "Panama City", "PAB,USD", "es"));
			countryRepository.save(new Country("PE", "Peru", "Perú", "51", SA, "Lima", "PEN", "es"));
			countryRepository.save(
					new Country("PF", "French Polynesia", "Polynésie française", "689", OC, "Papeetē", "XPF", "fr"));
			countryRepository.save(
					new Country("PG", "Papua New Guinea", "Papua Niugini", "675", OC, "Port Moresby", "PGK", "en"));
			countryRepository.save(new Country("PH", "Philippines", "Pilipinas", "63", AS, "Manila", "PHP", "en"));
			countryRepository.save(new Country("PK", "Pakistan", "Pakistan", "92", AS, "Islamabad", "PKR", "en,ur"));
			countryRepository.save(new Country("PL", "Poland", "Polska", "48", EU, "Warsaw", "PLN", "pl"));
			countryRepository.save(new Country("PM", "Saint Pierre and Miquelon", "Saint-Pierre-et-Miquelon", "508", NA,
					"Saint-Pierre", "EUR", "fr"));
			countryRepository.save(
					new Country("PN", "Pitcairn Islands", "Pitcairn Islands", "64", OC, "Adamstown", "NZD", "en"));
			countryRepository
					.save(new Country("PR", "Puerto Rico", "Puerto Rico", "1787,1939", NA, "San Juan", "USD", "es,en"));
			countryRepository.save(new Country("PS", "Palestine", "فلسطين", "970", AS, "Ramallah", "ILS", "ar"));
			countryRepository.save(new Country("PT", "Portugal", "Portugal", "351", EU, "Lisbon", "EUR", "pt"));
			countryRepository.save(new Country("PW", "Palau", "Palau", "680", OC, "Ngerulmud", "USD", "en"));
			countryRepository.save(new Country("PY", "Paraguay", "Paraguay", "595", SA, "Asunción", "PYG", "es,gn"));
			countryRepository.save(new Country("QA", "Qatar", "قطر", "974", AS, "Doha", "QAR", "ar"));
			countryRepository.save(new Country("RE", "Réunion", "La Réunion", "262", AF, "Saint-Denis", "EUR", "fr"));
			countryRepository.save(new Country("RO", "Romania", "România", "40", EU, "Bucharest", "RON", "ro"));
			countryRepository.save(new Country("RS", "Serbia", "Србија", "381", EU, "Belgrade", "RSD", "sr"));
			countryRepository.save(new Country("RU", "Russia", "Россия", "7", EU, "Moscow", "RUB", "ru"));
			countryRepository.save(new Country("RW", "Rwanda", "Rwanda", "250", AF, "Kigali", "RWF", "rw,en,fr"));
			countryRepository
					.save(new Country("SA", "Saudi Arabia", "العربية السعودية", "966", AS, "Riyadh", "SAR", "ar"));
			countryRepository
					.save(new Country("SB", "Solomon Islands", "Solomon Islands", "677", OC, "Honiara", "SDB", "en"));
			countryRepository
					.save(new Country("SC", "Seychelles", "Seychelles", "248", AF, "Victoria", "SCR", "fr,en"));
			countryRepository.save(new Country("SD", "Sudan", "السودان", "249", AF, "Khartoum", "SDG", "ar,en"));
			countryRepository.save(new Country("SE", "Sweden", "Sverige", "46", EU, "Stockholm", "SEK", "sv"));
			countryRepository
					.save(new Country("SG", "Singapore", "Singapore", "65", AS, "Singapore", "SGD", "en,ms,ta,zh"));
			countryRepository
					.save(new Country("SH", "Saint Helena", "Saint Helena", "290", AF, "Jamestown", "SHP", "en"));
			countryRepository.save(new Country("SI", "Slovenia", "Slovenija", "386", EU, "Ljubljana", "EUR", "sl"));
			countryRepository.save(new Country("SJ", "Svalbard and Jan Mayen", "Svalbard og Jan Mayen", "4779", EU,
					"Longyearbyen", "NOK", "no"));
			countryRepository.save(new Country("SK", "Slovakia", "Slovensko", "421", EU, "Bratislava", "EUR", "sk"));
			countryRepository
					.save(new Country("SL", "Sierra Leone", "Sierra Leone", "232", AF, "Freetown", "SLL", "en"));
			countryRepository
					.save(new Country("SM", "San Marino", "San Marino", "378", EU, "City of San Marino", "EUR", "it"));
			countryRepository.save(new Country("SN", "Senegal", "Sénégal", "221", AF, "Dakar", "XOF", "fr"));
			countryRepository.save(new Country("SO", "Somalia", "Soomaaliya", "252", AF, "Mogadishu", "SOS", "so,ar"));
			countryRepository.save(new Country("SR", "Suriname", "Suriname", "597", SA, "Paramaribo", "SRD", "nl"));
			countryRepository.save(new Country("SS", "South Sudan", "South Sudan", "211", AF, "Juba", "SSP", "en"));
			countryRepository.save(new Country("ST", "São Tomé and Príncipe", "São Tomé e Príncipe", "239", AF,
					"São Tomé", "STD", "pt"));
			countryRepository
					.save(new Country("SV", "El Salvador", "El Salvador", "503", NA, "San Salvador", "SVC,USD", "es"));
			countryRepository
					.save(new Country("SX", "Sint Maarten", "Sint Maarten", "1721", NA, "Philipsburg", "ANG", "nl,en"));
			countryRepository.save(new Country("SY", "Syria", "سوريا", "963", AS, "Damascus", "SYP", "ar"));
			countryRepository.save(new Country("SZ", "Swaziland", "Swaziland", "268", AF, "Lobamba", "SZL", "en,ss"));
			countryRepository.save(new Country("TC", "Turks and Caicos Islands", "Turks and Caicos Islands", "1649", NA,
					"Cockburn Town", "USD", "en"));
			countryRepository.save(new Country("TD", "Chad", "Tchad", "235", AF, "N'Djamena", "XAF", "fr,ar"));
			countryRepository.save(new Country("TF", "French Southern Territories",
					"Territoire des Terres australes et antarctiques fr", null, AN, "Port-aux-Français", "EUR", "fr"));
			countryRepository.save(new Country("TG", "Togo", "Togo", "228", AF, "Lomé", "XOF", "fr"));
			countryRepository.save(new Country("TH", "Thailand", "ประเทศไทย", "66", AS, "Bangkok", "THB", "th"));
			countryRepository
					.save(new Country("TJ", "Tajikistan", "Тоҷикистон", "992", AS, "Dushanbe", "TJS", "tg,ru"));
			countryRepository.save(new Country("TK", "Tokelau", "Tokelau", "690", OC, "Fakaofo", "NZD", "en"));
			countryRepository.save(new Country("TL", "East Timor", "Timor-Leste", "670", OC, "Dili", "USD", "pt"));
			countryRepository
					.save(new Country("TM", "Turkmenistan", "Türkmenistan", "993", AS, "Ashgabat", "TMT", "tk,ru"));
			countryRepository.save(new Country("TN", "Tunisia", "تونس", "216", AF, "Tunis", "TND", "ar"));
			countryRepository.save(new Country("TO", "Tonga", "Tonga", "676", OC, "Nuku'alofa", "TOP", "en,to"));
			countryRepository.save(new Country("TR", "Turkey", "Türkiye", "90", AS, "Ankara", "TRY", "tr"));
			countryRepository.save(new Country("TT", "Trinidad and Tobago", "Trinidad and Tobago", "1868", NA,
					"Port of Spain", "TTD", "en"));
			countryRepository.save(new Country("TV", "Tuvalu", "Tuvalu", "688", OC, "Funafuti", "AUD", "en"));
			countryRepository.save(new Country("TW", "Taiwan", "臺灣", "886", AS, "Taipei", "TWD", "zh"));
			countryRepository.save(new Country("TZ", "Tanzania", "Tanzania", "255", AF, "Dodoma", "TZS", "sw,en"));
			countryRepository.save(new Country("UA", "Ukraine", "Україна", "380", EU, "Kiev", "UAH", "uk"));
			countryRepository.save(new Country("UG", "Uganda", "Uganda", "256", AF, "Kampala", "UGX", "en,sw"));
			countryRepository.save(new Country("UM", "U.S. Minor Outlying Islands",
					"United States Minor Outlying Islands", null, OC, null, "USD", "en"));
			countryRepository.save(new Country("US", "United States", "United States", "1", NA, "Washington D.C.",
					"USD,USN,USS", "en"));
			countryRepository.save(new Country("UY", "Uruguay", "Uruguay", "598", SA, "Montevideo", "UYI,UYU", "es"));
			countryRepository
					.save(new Country("UZ", "Uzbekistan", "O‘zbekiston", "998", AS, "Tashkent", "UZS", "uz,ru"));
			countryRepository.save(
					new Country("VA", "Vatican City", "Vaticano", "39066,379", EU, "Vatican City", "EUR", "it,la"));
			countryRepository.save(new Country("VC", "Saint Vincent and the Grenadines",
					"Saint Vincent and the Grenadines", "1784", NA, "Kingstown", "XCD", "en"));
			countryRepository.save(new Country("VE", "Venezuela", "Venezuela", "58", SA, "Caracas", "VEF", "es"));
			countryRepository.save(new Country("VG", "British Virgin Islands", "British Virgin Islands", "1284", NA,
					"Road Town", "USD", "en"));
			countryRepository.save(new Country("VI", "U.S. Virgin Islands", "United States Virgin Islands", "1340", NA,
					"Charlotte Amalie", "USD", "en"));
			countryRepository.save(new Country("VN", "Vietnam", "Việt Nam", "84", AS, "Hanoi", "VND", "vi"));
			countryRepository.save(new Country("VU", "Vanuatu", "Vanuatu", "678", OC, "Port Vila", "VUV", "bi,en,fr"));
			countryRepository.save(
					new Country("WF", "Wallis and Futuna", "Wallis et Futuna", "681", OC, "Mata-Utu", "XPF", "fr"));
			countryRepository.save(new Country("WS", "Samoa", "Samoa", "685", OC, "Apia", "WST", "sm,en"));
			countryRepository.save(
					new Country("XK", "Kosovo", "Republika e Kosovës", "377,381,386", EU, "Pristina", "EUR", "sq,sr"));
			countryRepository.save(new Country("YE", "Yemen", "اليَمَن", "967", AS, "Sana'a", "YER", "ar"));
			countryRepository.save(new Country("YT", "Mayotte", "Mayotte", "262", AF, "Mamoudzou", "EUR", "fr"));
			countryRepository.save(new Country("ZA", "South Africa", "South Africa", "27", AF, "Pretoria", "ZAR",
					"af,en,nr,st,ss,tn,ts,ve,xh,zu"));
			countryRepository.save(new Country("ZM", "Zambia", "Zambia", "260", AF, "Lusaka", "ZMK", "en"));
			countryRepository.save(new Country("ZW", "Zimbabwe", "Zimbabwe", "263", AF, "Harare", "ZWL", "en,sn,nd"));
		}
	}

	private String[] getTenNames() {
		try {
			URL namey = new URL("http://namey.muffinlabs.com/name.json?count=10&with_surname=true");
			URLConnection yc = namey.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String[] names = in.readLine().replace("[", "").replace("]", "").replace("\"", "").split(",");
			return names;
		} catch (Exception e) {
			String[] names = { "Linda Hernandez", "David Ellis", "Nancy Morgan", "Elizabeth White", "Richard Collins",
					"David Sanchez", "Michael Cox", "Karen Moore", "John Gray", "Carol Garcia" };
			return names;
		}
	}
}
