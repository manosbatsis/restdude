/**
 *
 * Restdude
 * -------------------------------------------------------------------
 *
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.init;

import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.details.contact.model.ContactDetails;
import com.restdude.domain.details.contact.model.EmailDetail;
import com.restdude.domain.geography.model.Continent;
import com.restdude.domain.geography.model.Country;
import com.restdude.domain.geography.service.ContinentService;
import com.restdude.domain.geography.service.CountryService;
import com.restdude.domain.users.model.Role;
import com.restdude.domain.users.model.Roles;
import com.restdude.domain.users.model.User;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.repository.UserRegistrationCodeBatchRepository;
import com.restdude.domain.users.repository.UserRegistrationCodeRepository;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.domain.users.service.RoleService;
import com.restdude.domain.users.service.UserService;
import com.restdude.util.ConfigurationFactory;
import com.restdude.util.email.service.EmailService;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Creates initial roles users, countrries etc.
 */
public abstract class DataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    protected UserService userService;
    protected EmailService emailService;
    protected ContinentService continentService;
    protected CountryService countryService;
    protected RoleService roleService;
    protected UserRepository userRepository;
    protected ModelRepository<UserCredentials, String> credentialsRepository;

    protected UserRegistrationCodeRepository userRegistrationCodeRepository;
    protected UserRegistrationCodeBatchRepository userRegistrationCodeBatchRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setContinentService(ContinentService continentService) {
        this.continentService = continentService;
    }

    @Autowired
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
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

    protected abstract String getTestEmailDomain();

    public void run() {


        Configuration config = ConfigurationFactory.getConfiguration();
        boolean initData = config.getBoolean(ConfigurationFactory.INIT_DATA, true);
        String testEmailDomain = getTestEmailDomain();
        LOGGER.debug("run, testEmailDomain: {}", testEmailDomain);
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken(this.getClass().getName(), this.getClass().getName(),
                        Arrays.asList(new Role[]{new Role(Roles.ROLE_USER, "Logged in user"), new Role(Roles.ROLE_ADMIN, "System Administrator.")})));

        if (initData && this.userRepository.count() == 0) {
            LOGGER.debug("postInitialize, creating data");


            this.initContinentsAndCountries();
            this.initRoles();

            List<User> users = new LinkedList<User>();


            Role adminRole = this.roleService.findByIdOrName(Roles.ROLE_ADMIN);
            Role operatorRole = this.roleService.findByIdOrName(Roles.ROLE_SITE_OPERATOR);

            LocalDateTime now = LocalDateTime.now();

            User system = new User();
            system.setUsername("system");
            system.setFirstName("System");
            system.setLastName("User");
            system.setCredentials(new UserCredentials.Builder().password("system").build());
            system.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("system@" + testEmailDomain)).build());
            system.setLastVisit(now);
            system = userService.createAsConfirmed(system);
            users.add(system);

            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setLastVisit(now);
            adminUser.addRole(adminRole);
            adminUser.setCredentials(new UserCredentials.Builder().password("admin").build());
            adminUser.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("ehadmin@" + testEmailDomain)).build());
//			adminUser.setCreatedBy(system);
            adminUser = userService.createAsConfirmed(adminUser);
            users.add(adminUser);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(system, system.getCredentials().getPassword(), system.getRoles()));


            User opUser = new User();
            opUser.setUsername("operator");
            opUser.setFirstName("Operator");
            opUser.setLastName("User");
            opUser.setCredentials(new UserCredentials.Builder().password("operator").build());
            opUser.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("operator@" + testEmailDomain)).build());
            opUser.setLastVisit(now);
            opUser.addRole(operatorRole);
//			opUser.setCreatedBy(system);
            opUser = userService.createAsConfirmed(opUser);
            users.add(opUser);

            User simpleUser = new User();
            simpleUser.setUsername("simple");
            simpleUser.setFirstName("Simple");
            simpleUser.setLastName("User");
            simpleUser.setCredentials(new UserCredentials.Builder().password("simple").build());
            simpleUser.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail("simple@" + testEmailDomain)).build());
            simpleUser.setLastVisit(now);
//			simpleUser.setCreatedBy(system);
            simpleUser = userService.createAsConfirmed(simpleUser);
            users.add(simpleUser);

            int usersMax = 10;
            int usersCreated = 0;
            while (usersCreated < usersMax) {
                for (String fullName : this.getTenNames()) {
                    String userName = fullName.toLowerCase().replace(" ", "") + usersCreated;
                    User u = new User();
                    u.setUsername(userName);
                    u.setFirstName(fullName.substring(0, fullName.indexOf(" ")));
                    u.setLastName(fullName.substring(fullName.indexOf(" ") + 1));
                    u.setCredentials(new UserCredentials.Builder().password(userName).build());
                    u.setContactDetails(new ContactDetails.Builder().primaryEmail(new EmailDetail(userName + "@" + testEmailDomain)).build());
                    u.setLastVisit(now);
//					u.setCreatedBy(system);
                    u = userService.createAsConfirmed(u);
                    users.add(u);

                    usersCreated++;
                    LOGGER.info("Created user: " + u);
                    if (usersCreated >= usersMax) {
                        break;
                    }
                }
            }

        } else {

            LOGGER.debug("run, skipping data init");
        }
    }


    protected void initRoles() {
        if (this.roleService.count() == 0) {
            Role adminRole = new Role(Roles.ROLE_ADMIN, "System Administrator.");
            LOGGER.debug("#initRoles, creating");
            adminRole = this.roleService.create(adminRole);
            Role siteAdminRole = new Role(Roles.ROLE_SITE_OPERATOR, "Site Operator.");
            siteAdminRole = this.roleService.create(siteAdminRole);
            // this is added to users by user service, just creating it
            Role userRole = new Role(Roles.ROLE_USER, "Logged in user");
            userRole = this.roleService.create(userRole);
        } else {

            LOGGER.debug("#initRoles, skipping");
        }
    }

    protected void initContinentsAndCountries() {

        if (continentService.count() == 0) {
            LOGGER.debug("#initContinentsAndCountries, creating");
            Continent AF = continentService.create(new Continent("AF", "Africa"));
            Continent AN = continentService.create(new Continent("AN", "Antarctica"));
            Continent AS = continentService.create(new Continent("AS", "Asia"));
            Continent EU = continentService.create(new Continent("EU", "Europe"));
            Continent NA = continentService.create(new Continent("NA", "North America"));
            Continent OC = continentService.create(new Continent("OC", "Oceania"));
            Continent SA = continentService.create(new Continent("SA", "South America"));

            countryService.create(new Country("AD", "Andorra", "Andorra", "376", EU, "Andorra la Vella", "EUR", "ca"));
            countryService.create(new Country("AE", "United Arab Emirates", "دولة الإمارات العربية المتحدة", "971", AS,
                    "Abu Dhabi", "AED", "ar"));
            countryService.create(new Country("AF", "Afghanistan", "افغانستان", "93", AS, "Kabul", "AFN", "ps,uz,tk"));
            countryService.create(new Country("AG", "Antigua and Barbuda", "Antigua and Barbuda", "1268", NA,
                    "Saint John's", "XCD", "en"));
            countryService.create(new Country("AI", "Anguilla", "Anguilla", "1264", NA, "The Valley", "XCD", "en"));
            countryService.create(new Country("AL", "Albania", "Shqipëria", "355", EU, "Tirana", "ALL", "sq"));
            countryService.create(new Country("AM", "Armenia", "Հայաստան", "374", AS, "Yerevan", "AMD", "hy,ru"));
            countryService.create(new Country("AO", "Angola", "Angola", "244", AF, "Luanda", "AOA", "pt"));
            countryService.create(new Country("AQ", "Antarctica", null, null, AN, null, null, null));
            countryService
                    .create(new Country("AR", "Argentina", "Argentina", "54", SA, "Buenos Aires", "ARS", "es,gn"));
            countryService.create(
                    new Country("AS", "American Samoa", "American Samoa", "1684", OC, "Pago Pago", "USD", "en,sm"));
            countryService.create(new Country("AT", "Austria", "Österreich", "43", EU, "Vienna", "EUR", "de"));
            countryService.create(new Country("AU", "Australia", "Australia", "61", OC, "Canberra", "AUD", "en"));
            countryService.create(new Country("AW", "Aruba", "Aruba", "297", NA, "Oranjestad", "AWG", "nl,pa"));
            countryService.create(new Country("AX", "Åland", "Åland", "358", EU, "Mariehamn", "EUR", "sv"));
            countryService.create(new Country("AZ", "Azerbaijan", "Azərbaycan", "994", AS, "Baku", "AZN", "az,hy"));
            countryService.create(new Country("BA", "Bosnia and Herzegovina", "Bosna i Hercegovina", "387", EU,
                    "Sarajevo", "BAM", "bs,hr,sr"));
            countryService.create(new Country("BB", "Barbados", "Barbados", "1246", NA, "Bridgetown", "BBD", "en"));
            countryService.create(new Country("BD", "Bangladesh", "Bangladesh", "880", AS, "Dhaka", "BDT", "bn"));
            countryService.create(new Country("BE", "Belgium", "België", "32", EU, "Brussels", "EUR", "nl,fr,de"));
            countryService
                    .create(new Country("BF", "Burkina Faso", "Burkina Faso", "226", AF, "Ouagadougou", "XOF", "fr,ff"));
            countryService.create(new Country("BG", "Bulgaria", "България", "359", EU, "Sofia", "BGN", "bg"));
            countryService.create(new Country("BH", "Bahrain", "‏البحرين", "973", AS, "Manama", "BHD", "ar"));
            countryService.create(new Country("BI", "Burundi", "Burundi", "257", AF, "Bujumbura", "BIF", "fr,rn"));
            countryService.create(new Country("BJ", "Benin", "Bénin", "229", AF, "Porto-Novo", "XOF", "fr"));
            countryService.create(
                    new Country("BL", "Saint Barthélemy", "Saint-Barthélemy", "590", NA, "Gustavia", "EUR", "fr"));
            countryService.create(new Country("BM", "Bermuda", "Bermuda", "1441", NA, "Hamilton", "BMD", "en"));
            countryService.create(new Country("BN", "Brunei", "Negara Brunei Darussalam", "673", AS,
                    "Bandar Seri Begawan", "BND", "ms"));
            countryService.create(new Country("BO", "Bolivia", "Bolivia", "591", SA, "Sucre", "BOB,BOV", "es,ay,qu"));
            countryService.create(new Country("BQ", "Bonaire", "Bonaire", "5997", NA, "Kralendijk", "USD", "nl"));
            countryService.create(new Country("BR", "Brazil", "Brasil", "55", SA, "Brasília", "BRL", "pt"));
            countryService.create(new Country("BS", "Bahamas", "Bahamas", "1242", NA, "Nassau", "BSD", "en"));
            countryService.create(new Country("BT", "Bhutan", "ʼbrug-yul", "975", AS, "Thimphu", "BTN,INR", "dz"));
            countryService.create(new Country("BV", "Bouvet Island", "Bouvetøya", null, AN, null, "NOK", null));
            countryService.create(new Country("BW", "Botswana", "Botswana", "267", AF, "Gaborone", "BWP", "en,tn"));
            countryService.create(new Country("BY", "Belarus", "Белару́сь", "375", EU, "Minsk", "BYR", "be,ru"));
            countryService.create(new Country("BZ", "Belize", "Belize", "501", NA, "Belmopan", "BZD", "en,es"));
            countryService.create(new Country("CA", "Canada", "Canada", "1", NA, "Ottawa", "CAD", "en,fr"));
            countryService.create(new Country("CC", "Cocos [Keeling] Islands", "Cocos (Keeling) Islands", "61", AS,
                    "West Island", "AUD", "en"));
            countryService.create(new Country("CD", "Democratic Republic of the Congo",
                    "République démocratique du Congo", "243", AF, "Kinshasa", "CDF", "fr,ln,kg,sw,lu"));
            countryService.create(new Country("CF", "Central African Republic", "Ködörösêse tî Bêafrîka", "236", AF,
                    "Bangui", "XAF", "fr,sg"));
            countryService.create(new Country("CG", "Republic of the Congo", "République du Congo", "242", AF,
                    "Brazzaville", "XAF", "fr,ln"));
            countryService
                    .create(new Country("CH", "Switzerland", "Schweiz", "41", EU, "Bern", "CHE,CHF,CHW", "de,fr,it"));
            countryService
                    .create(new Country("CI", "Ivory Coast", "Côte d'Ivoire", "225", AF, "Yamoussoukro", "XOF", "fr"));
            countryService.create(new Country("CK", "Cook Islands", "Cook Islands", "682", OC, "Avarua", "NZD", "en"));
            countryService.create(new Country("CL", "Chile", "Chile", "56", SA, "Santiago", "CLF,CLP", "es"));
            countryService.create(new Country("CM", "Cameroon", "Cameroon", "237", AF, "Yaoundé", "XAF", "en,fr"));
            countryService.create(new Country("CN", "China", "中国", "86", AS, "Beijing", "CNY", "zh"));
            countryService.create(new Country("CO", "Colombia", "Colombia", "57", SA, "Bogotá", "COP", "es"));
            countryService.create(new Country("CR", "Costa Rica", "Costa Rica", "506", NA, "San José", "CRC", "es"));
            countryService.create(new Country("CU", "Cuba", "Cuba", "53", NA, "Havana", "CUC,CUP", "es"));
            countryService.create(new Country("CV", "Cape Verde", "Cabo Verde", "238", AF, "Praia", "CVE", "pt"));
            countryService
                    .create(new Country("CW", "Curacao", "Curaçao", "5999", NA, "Willemstad", "ANG", "nl,pa,en"));
            countryService.create(new Country("CX", "Christmas Island", "Christmas Island", "61", AS,
                    "Flying Fish Cove", "AUD", "en"));
            countryService.create(new Country("CY", "Cyprus", "Κύπρος", "357", EU, "Nicosia", "EUR", "el,tr,hy"));
            countryService
                    .create(new Country("CZ", "Czechia", "Česká republika", "420", EU, "Prague", "CZK", "cs,sk"));
            countryService.create(new Country("DE", "Germany", "Deutschland", "49", EU, "Berlin", "EUR", "de"));
            countryService.create(new Country("DJ", "Djibouti", "Djibouti", "253", AF, "Djibouti", "DJF", "fr,ar"));
            countryService.create(new Country("DK", "Denmark", "Danmark", "45", EU, "Copenhagen", "DKK", "da"));
            countryService.create(new Country("DM", "Dominica", "Dominica", "1767", NA, "Roseau", "XCD", "en"));
            countryService.create(new Country("DO", "Dominican Republic", "República Dominicana", "1809,1829,1849", NA,
                    "Santo Domingo", "DOP", "es"));
            countryService.create(new Country("DZ", "Algeria", "الجزائر", "213", AF, "Algiers", "DZD", "ar"));
            countryService.create(new Country("EC", "Ecuador", "Ecuador", "593", SA, "Quito", "USD", "es"));
            countryService.create(new Country("EE", "Estonia", "Eesti", "372", EU, "Tallinn", "EUR", "et"));
            countryService.create(new Country("EG", "Egypt", "مصر‎", "20", AF, "Cairo", "EGP", "ar"));
            countryService.create(
                    new Country("EH", "Western Sahara", "الصحراء الغربية", "212", AF, "El Aaiún", "MAD,DZD,MRO", "es"));
            countryService.create(new Country("ER", "Eritrea", "ኤርትራ", "291", AF, "Asmara", "ERN", "ti,ar,en"));
            countryService.create(new Country("ES", "Spain", "España", "34", EU, "Madrid", "EUR", "es,eu,ca,gl,oc"));
            countryService.create(new Country("ET", "Ethiopia", "ኢትዮጵያ", "251", AF, "Addis Ababa", "ETB", "am"));
            countryService.create(new Country("FI", "Finland", "Suomi", "358", EU, "Helsinki", "EUR", "fi,sv"));
            countryService.create(new Country("FJ", "Fiji", "Fiji", "679", OC, "Suva", "FJD", "en,fj,hi,ur"));
            countryService
                    .create(new Country("FK", "Falkland Islands", "Falkland Islands", "500", SA, "Stanley", "FKP", "en"));
            countryService.create(new Country("FM", "Micronesia", "Micronesia", "691", OC, "Palikir", "USD", "en"));
            countryService.create(new Country("FO", "Faroe Islands", "Føroyar", "298", EU, "Tórshavn", "DKK", "fo"));
            countryService.create(new Country("FR", "France", "France", "33", EU, "Paris", "EUR", "fr"));
            countryService.create(new Country("GA", "Gabon", "Gabon", "241", AF, "Libreville", "XAF", "fr"));
            countryService
                    .create(new Country("GB", "United Kingdom", "United Kingdom", "44", EU, "London", "GBP", "en"));
            countryService.create(new Country("GD", "Grenada", "Grenada", "1473", NA, "St. George's", "XCD", "en"));
            countryService.create(new Country("GE", "Georgia", "საქართველო", "995", AS, "Tbilisi", "GEL", "ka"));
            countryService
                    .create(new Country("GF", "French Guiana", "Guyane française", "594", SA, "Cayenne", "EUR", "fr"));
            countryService
                    .create(new Country("GG", "Guernsey", "Guernsey", "44", EU, "St. Peter Port", "GBP", "en,fr"));
            countryService.create(new Country("GH", "Ghana", "Ghana", "233", AF, "Accra", "GHS", "en"));
            countryService.create(new Country("GI", "Gibraltar", "Gibraltar", "350", EU, "Gibraltar", "GIP", "en"));
            countryService.create(new Country("GL", "Greenland", "Kalaallit Nunaat", "299", NA, "Nuuk", "DKK", "kl"));
            countryService.create(new Country("GM", "Gambia", "Gambia", "220", AF, "Banjul", "GMD", "en"));
            countryService.create(new Country("GN", "Guinea", "Guinée", "224", AF, "Conakry", "GNF", "fr,ff"));
            countryService
                    .create(new Country("GP", "Guadeloupe", "Guadeloupe", "590", NA, "Basse-Terre", "EUR", "fr"));
            countryService.create(
                    new Country("GQ", "Equatorial Guinea", "Guinea Ecuatorial", "240", AF, "Malabo", "XAF", "es,fr"));
            countryService.create(new Country("GR", "Greece", "Ελλάδα", "30", EU, "Athens", "EUR", "el"));
            countryService.create(new Country("GS", "South Georgia and the South Sandwich Islands", "South Georgia",
                    "500", AN, "King Edward Point", "GBP", "en"));
            countryService
                    .create(new Country("GT", "Guatemala", "Guatemala", "502", NA, "Guatemala City", "GTQ", "es"));
            countryService.create(new Country("GU", "Guam", "Guam", "1671", OC, "Hagåtña", "USD", "en,ch,es"));
            countryService
                    .create(new Country("GW", "Guinea-Bissau", "Guiné-Bissau", "245", AF, "Bissau", "XOF", "pt"));
            countryService.create(new Country("GY", "Guyana", "Guyana", "592", SA, "Georgetown", "GYD", "en"));
            countryService.create(new Country("HK", "Hong Kong", "香港", "852", AS, "City of Victoria", "HKD", "zh,en"));
            countryService.create(new Country("HM", "Heard Island and McDonald Islands",
                    "Heard Island and McDonald Islands", null, AN, null, "AUD", "en"));
            countryService.create(new Country("HN", "Honduras", "Honduras", "504", NA, "Tegucigalpa", "HNL", "es"));
            countryService.create(new Country("HR", "Croatia", "Hrvatska", "385", EU, "Zagreb", "HRK", "hr"));
            countryService
                    .create(new Country("HT", "Haiti", "Haïti", "509", NA, "Port-au-Prince", "HTG,USD", "fr,ht"));
            countryService.create(new Country("HU", "Hungary", "Magyarország", "36", EU, "Budapest", "HUF", "hu"));
            countryService.create(new Country("ID", "Indonesia", "Indonesia", "62", AS, "Jakarta", "IDR", "pk"));
            countryService.create(new Country("IE", "Ireland", "Éire", "353", EU, "Dublin", "EUR", "ga,en"));
            countryService.create(new Country("IL", "Israel", "יִשְׂרָאֵל", "972", AS, "Jerusalem", "ILS", "he,ar"));
            countryService
                    .create(new Country("IM", "Isle of Man", "Isle of Man", "44", EU, "Douglas", "GBP", "en,gv"));
            countryService.create(new Country("IN", "India", "भारत", "91", AS, "New Delhi", "INR", "hi,en"));
            countryService.create(new Country("IO", "British Indian Ocean Territory", "British Indian Ocean Territory",
                    "246", AS, "Diego Garcia", "USD", "en"));
            countryService.create(new Country("IQ", "Iraq", "العراق", "964", AS, "Baghdad", "IQD", "ar,ku"));
            countryService.create(new Country("IR", "Iran", "Irān", "98", AS, "Tehran", "IRR", "fa"));
            countryService.create(new Country("IS", "Iceland", "Ísland", "354", EU, "Reykjavik", "ISK", "is"));
            countryService.create(new Country("IT", "Italy", "Italia", "39", EU, "Rome", "EUR", "it"));
            countryService.create(new Country("JE", "Jersey", "Jersey", "44", EU, "Saint Helier", "GBP", "en,fr"));
            countryService.create(new Country("JM", "Jamaica", "Jamaica", "1876", NA, "Kingston", "JMD", "en"));
            countryService.create(new Country("JO", "Jordan", "الأردن", "962", AS, "Amman", "JOD", "ar"));
            countryService.create(new Country("JP", "Japan", "日本", "81", AS, "Tokyo", "JPY", "ja"));
            countryService.create(new Country("KE", "Kenya", "Kenya", "254", AF, "Nairobi", "KES", "en,sw"));
            countryService.create(new Country("KG", "Kyrgyzstan", "Кыргызстан", "996", AS, "Bishkek", "KGS", "ky,ru"));
            countryService.create(new Country("KH", "Cambodia", "Kâmpŭchéa", "855", AS, "Phnom Penh", "KHR", "km"));
            countryService.create(new Country("KI", "Kiribati", "Kiribati", "686", OC, "South Tarawa", "AUD", "en"));
            countryService.create(new Country("KM", "Comoros", "Komori", "269", AF, "Moroni", "KMF", "ar,fr"));
            countryService.create(new Country("KN", "Saint Kitts and Nevis", "Saint Kitts and Nevis", "1869", NA,
                    "Basseterre", "XCD", "en"));
            countryService.create(new Country("KP", "North Korea", "북한", "850", AS, "Pyongyang", "KPW", "ko"));
            countryService.create(new Country("KR", "South Korea", "대한민국", "82", AS, "Seoul", "KRW", "ko"));
            countryService.create(new Country("KW", "Kuwait", "الكويت", "965", AS, "Kuwait City", "KWD", "ar"));
            countryService.create(
                    new Country("KY", "Cayman Islands", "Cayman Islands", "1345", NA, "George Town", "KYD", "en"));
            countryService.create(new Country("KZ", "Kazakhstan", "Қазақстан", "76,77", AS, "Astana", "KZT", "kk,ru"));
            countryService.create(new Country("LA", "Laos", "ສປປລາວ", "856", AS, "Vientiane", "LAK", "lo"));
            countryService.create(new Country("LB", "Lebanon", "لبنان", "961", AS, "Beirut", "LBP", "ar,fr"));
            countryService
                    .create(new Country("LC", "Saint Lucia", "Saint Lucia", "1758", NA, "Castries", "XCD", "en"));
            countryService
                    .create(new Country("LI", "Liechtenstein", "Liechtenstein", "423", EU, "Vaduz", "CHF", "de"));
            countryService.create(new Country("LK", "Sri Lanka", "śrī laṃkāva", "94", AS, "Colombo", "LKR", "si,ta"));
            countryService.create(new Country("LR", "Liberia", "Liberia", "231", AF, "Monrovia", "LRD", "en"));
            countryService.create(new Country("LS", "Lesotho", "Lesotho", "266", AF, "Maseru", "LSL,ZAR", "en,st"));
            countryService.create(new Country("LT", "Lithuania", "Lietuva", "370", EU, "Vilnius", "LTL", "lt"));
            countryService
                    .create(new Country("LU", "Luxembourg", "Luxembourg", "352", EU, "Luxembourg", "EUR", "fr,de,lb"));
            countryService.create(new Country("LV", "Latvia", "Latvija", "371", EU, "Riga", "EUR", "lv"));
            countryService.create(new Country("LY", "Libya", "‏ليبيا", "218", AF, "Tripoli", "LYD", "ar"));
            countryService.create(new Country("MA", "Morocco", "المغرب", "212", AF, "Rabat", "MAD", "ar"));
            countryService.create(new Country("MC", "Monaco", "Monaco", "377", EU, "Monaco", "EUR", "fr"));
            countryService.create(new Country("MD", "Moldova", "Moldova", "373", EU, "Chișinău", "MDL", "ro"));
            countryService
                    .create(new Country("ME", "Montenegro", "Црна Гора", "382", EU, "Podgorica", "EUR", "sr,bs,sq,hr"));
            countryService
                    .create(new Country("MF", "Saint Martin", "Saint-Martin", "590", NA, "Marigot", "EUR", "en,fr,nl"));
            countryService
                    .create(new Country("MG", "Madagascar", "Madagasikara", "261", AF, "Antananarivo", "MGA", "fr,mg"));
            countryService
                    .create(new Country("MH", "Marshall Islands", "M̧ajeļ", "692", OC, "Majuro", "USD", "en,mh"));
            countryService.create(new Country("MK", "Macedonia", "Македонија", "389", EU, "Skopje", "MKD", "mk"));
            countryService.create(new Country("ML", "Mali", "Mali", "223", AF, "Bamako", "XOF", "fr"));
            countryService.create(new Country("MM", "Myanmar [Burma]", "Myanma", "95", AS, "Naypyidaw", "MMK", "my"));
            countryService.create(new Country("MN", "Mongolia", "Монгол улс", "976", AS, "Ulan Bator", "MNT", "mn"));
            countryService.create(new Country("MO", "Macao", "澳門", "853", AS, null, "MOP", "zh,pt"));
            countryService.create(new Country("MP", "Northern Mariana Islands", "Northern Mariana Islands", "1670", OC,
                    "Saipan", "USD", "en,ch"));
            countryService
                    .create(new Country("MQ", "Martinique", "Martinique", "596", NA, "Fort-de-France", "EUR", "fr"));
            countryService.create(new Country("MR", "Mauritania", "موريتانيا", "222", AF, "Nouakchott", "MRO", "ar"));
            countryService.create(new Country("MS", "Montserrat", "Montserrat", "1664", NA, "Plymouth", "XCD", "en"));
            countryService.create(new Country("MT", "Malta", "Malta", "356", EU, "Valletta", "EUR", "mt,en"));
            countryService.create(new Country("MU", "Mauritius", "Maurice", "230", AF, "Port Louis", "MUR", "en"));
            countryService.create(new Country("MV", "Maldives", "Maldives", "960", AS, "Malé", "MVR", "dv"));
            countryService.create(new Country("MW", "Malawi", "Malawi", "265", AF, "Lilongwe", "MWK", "en,ny"));
            countryService.create(new Country("MX", "Mexico", "México", "52", NA, "Mexico City", "MXN", "es"));
            countryService.create(new Country("MY", "Malaysia", "Malaysia", "60", AS, "Kuala Lumpur", "MYR", null));
            countryService.create(new Country("MZ", "Mozambique", "Moçambique", "258", AF, "Maputo", "MZN", "pt"));
            countryService.create(new Country("NA", "Namibia", "Namibia", "264", AF, "Windhoek", "NAD,ZAR", "en,af"));
            countryService
                    .create(new Country("NC", "New Caledonia", "Nouvelle-Calédonie", "687", OC, "Nouméa", "XPF", "fr"));
            countryService.create(new Country("NE", "Niger", "Niger", "227", AF, "Niamey", "XOF", "fr"));
            countryService
                    .create(new Country("NF", "Norfolk Island", "Norfolk Island", "672", OC, "Kingston", "AUD", "en"));
            countryService.create(new Country("NG", "Nigeria", "Nigeria", "234", AF, "Abuja", "NGN", "en"));
            countryService.create(new Country("NI", "Nicaragua", "Nicaragua", "505", NA, "Managua", "NIO", "es"));
            countryService.create(new Country("NL", "Netherlands", "Nederland", "31", EU, "Amsterdam", "EUR", "nl"));
            countryService.create(new Country("NO", "Norway", "Norge", "47", EU, "Oslo", "NOK", "no,nb,nn"));
            countryService.create(new Country("NP", "Nepal", "नपल", "977", AS, "Kathmandu", "NPR", "ne"));
            countryService.create(new Country("NR", "Nauru", "Nauru", "674", OC, "Yaren", "AUD", "en,na"));
            countryService.create(new Country("NU", "Niue", "Niuē", "683", OC, "Alofi", "NZD", "en"));
            countryService
                    .create(new Country("NZ", "New Zealand", "New Zealand", "64", OC, "Wellington", "NZD", "en,mi"));
            countryService.create(new Country("OM", "Oman", "عمان", "968", AS, "Muscat", "OMR", "ar"));
            countryService.create(new Country("PA", "Panama", "Panamá", "507", NA, "Panama City", "PAB,USD", "es"));
            countryService.create(new Country("PE", "Peru", "Perú", "51", SA, "Lima", "PEN", "es"));
            countryService.create(
                    new Country("PF", "French Polynesia", "Polynésie française", "689", OC, "Papeetē", "XPF", "fr"));
            countryService.create(
                    new Country("PG", "Papua New Guinea", "Papua Niugini", "675", OC, "Port Moresby", "PGK", "en"));
            countryService.create(new Country("PH", "Philippines", "Pilipinas", "63", AS, "Manila", "PHP", "en"));
            countryService.create(new Country("PK", "Pakistan", "Pakistan", "92", AS, "Islamabad", "PKR", "en,ur"));
            countryService.create(new Country("PL", "Poland", "Polska", "48", EU, "Warsaw", "PLN", "pl"));
            countryService.create(new Country("PM", "Saint Pierre and Miquelon", "Saint-Pierre-et-Miquelon", "508", NA,
                    "Saint-Pierre", "EUR", "fr"));
            countryService.create(
                    new Country("PN", "Pitcairn Islands", "Pitcairn Islands", "64", OC, "Adamstown", "NZD", "en"));
            countryService
                    .create(new Country("PR", "Puerto Rico", "Puerto Rico", "1787,1939", NA, "San Juan", "USD", "es,en"));
            countryService.create(new Country("PS", "Palestine", "فلسطين", "970", AS, "Ramallah", "ILS", "ar"));
            countryService.create(new Country("PT", "Portugal", "Portugal", "351", EU, "Lisbon", "EUR", "pt"));
            countryService.create(new Country("PW", "Palau", "Palau", "680", OC, "Ngerulmud", "USD", "en"));
            countryService.create(new Country("PY", "Paraguay", "Paraguay", "595", SA, "Asunción", "PYG", "es,gn"));
            countryService.create(new Country("QA", "Qatar", "قطر", "974", AS, "Doha", "QAR", "ar"));
            countryService.create(new Country("RE", "Réunion", "La Réunion", "262", AF, "Saint-Denis", "EUR", "fr"));
            countryService.create(new Country("RO", "Romania", "România", "40", EU, "Bucharest", "RON", "ro"));
            countryService.create(new Country("RS", "Serbia", "Србија", "381", EU, "Belgrade", "RSD", "sr"));
            countryService.create(new Country("RU", "Russia", "Россия", "7", EU, "Moscow", "RUB", "ru"));
            countryService.create(new Country("RW", "Rwanda", "Rwanda", "250", AF, "Kigali", "RWF", "rw,en,fr"));
            countryService
                    .create(new Country("SA", "Saudi Arabia", "العربية السعودية", "966", AS, "Riyadh", "SAR", "ar"));
            countryService
                    .create(new Country("SB", "Solomon Islands", "Solomon Islands", "677", OC, "Honiara", "SDB", "en"));
            countryService
                    .create(new Country("SC", "Seychelles", "Seychelles", "248", AF, "Victoria", "SCR", "fr,en"));
            countryService.create(new Country("SD", "Sudan", "السودان", "249", AF, "Khartoum", "SDG", "ar,en"));
            countryService.create(new Country("SE", "Sweden", "Sverige", "46", EU, "Stockholm", "SEK", "sv"));
            countryService
                    .create(new Country("SG", "Singapore", "Singapore", "65", AS, "Singapore", "SGD", "en,ms,ta,zh"));
            countryService
                    .create(new Country("SH", "Saint Helena", "Saint Helena", "290", AF, "Jamestown", "SHP", "en"));
            countryService.create(new Country("SI", "Slovenia", "Slovenija", "386", EU, "Ljubljana", "EUR", "sl"));
            countryService.create(new Country("SJ", "Svalbard and Jan Mayen", "Svalbard og Jan Mayen", "4779", EU,
                    "Longyearbyen", "NOK", "no"));
            countryService.create(new Country("SK", "Slovakia", "Slovensko", "421", EU, "Bratislava", "EUR", "sk"));
            countryService
                    .create(new Country("SL", "Sierra Leone", "Sierra Leone", "232", AF, "Freetown", "SLL", "en"));
            countryService
                    .create(new Country("SM", "San Marino", "San Marino", "378", EU, "City of San Marino", "EUR", "it"));
            countryService.create(new Country("SN", "Senegal", "Sénégal", "221", AF, "Dakar", "XOF", "fr"));
            countryService.create(new Country("SO", "Somalia", "Soomaaliya", "252", AF, "Mogadishu", "SOS", "so,ar"));
            countryService.create(new Country("SR", "Suriname", "Suriname", "597", SA, "Paramaribo", "SRD", "nl"));
            countryService.create(new Country("SS", "South Sudan", "South Sudan", "211", AF, "Juba", "SSP", "en"));
            countryService.create(new Country("ST", "São Tomé and Príncipe", "São Tomé e Príncipe", "239", AF,
                    "São Tomé", "STD", "pt"));
            countryService
                    .create(new Country("SV", "El Salvador", "El Salvador", "503", NA, "San Salvador", "SVC,USD", "es"));
            countryService
                    .create(new Country("SX", "Sint Maarten", "Sint Maarten", "1721", NA, "Philipsburg", "ANG", "nl,en"));
            countryService.create(new Country("SY", "Syria", "سوريا", "963", AS, "Damascus", "SYP", "ar"));
            countryService.create(new Country("SZ", "Swaziland", "Swaziland", "268", AF, "Lobamba", "SZL", "en,ss"));
            countryService.create(new Country("TC", "Turks and Caicos Islands", "Turks and Caicos Islands", "1649", NA,
                    "Cockburn Town", "USD", "en"));
            countryService.create(new Country("TD", "Chad", "Tchad", "235", AF, "N'Djamena", "XAF", "fr,ar"));
            countryService.create(new Country("TF", "French Southern Territories",
                    "Territoire des Terres australes et antarctiques fr", null, AN, "Port-aux-Français", "EUR", "fr"));
            countryService.create(new Country("TG", "Togo", "Togo", "228", AF, "Lomé", "XOF", "fr"));
            countryService.create(new Country("TH", "Thailand", "ประเทศไทย", "66", AS, "Bangkok", "THB", "th"));
            countryService
                    .create(new Country("TJ", "Tajikistan", "Тоҷикистон", "992", AS, "Dushanbe", "TJS", "tg,ru"));
            countryService.create(new Country("TK", "Tokelau", "Tokelau", "690", OC, "Fakaofo", "NZD", "en"));
            countryService.create(new Country("TL", "East Timor", "Timor-Leste", "670", OC, "Dili", "USD", "pt"));
            countryService
                    .create(new Country("TM", "Turkmenistan", "Türkmenistan", "993", AS, "Ashgabat", "TMT", "tk,ru"));
            countryService.create(new Country("TN", "Tunisia", "تونس", "216", AF, "Tunis", "TND", "ar"));
            countryService.create(new Country("TO", "Tonga", "Tonga", "676", OC, "Nuku'alofa", "TOP", "en,to"));
            countryService.create(new Country("TR", "Turkey", "Türkiye", "90", AS, "Ankara", "TRY", "tr"));
            countryService.create(new Country("TT", "Trinidad and Tobago", "Trinidad and Tobago", "1868", NA,
                    "Port of Spain", "TTD", "en"));
            countryService.create(new Country("TV", "Tuvalu", "Tuvalu", "688", OC, "Funafuti", "AUD", "en"));
            countryService.create(new Country("TW", "Taiwan", "臺灣", "886", AS, "Taipei", "TWD", "zh"));
            countryService.create(new Country("TZ", "Tanzania", "Tanzania", "255", AF, "Dodoma", "TZS", "sw,en"));
            countryService.create(new Country("UA", "Ukraine", "Україна", "380", EU, "Kiev", "UAH", "uk"));
            countryService.create(new Country("UG", "Uganda", "Uganda", "256", AF, "Kampala", "UGX", "en,sw"));
            countryService.create(new Country("UM", "U.S. Minor Outlying Islands",
                    "United States Minor Outlying Islands", null, OC, null, "USD", "en"));
            countryService.create(new Country("US", "United States", "United States", "1", NA, "Washington D.C.",
                    "USD,USN,USS", "en"));
            countryService.create(new Country("UY", "Uruguay", "Uruguay", "598", SA, "Montevideo", "UYI,UYU", "es"));
            countryService
                    .create(new Country("UZ", "Uzbekistan", "O‘zbekiston", "998", AS, "Tashkent", "UZS", "uz,ru"));
            countryService.create(
                    new Country("VA", "Vatican City", "Vaticano", "39066,379", EU, "Vatican City", "EUR", "it,la"));
            countryService.create(new Country("VC", "Saint Vincent and the Grenadines",
                    "Saint Vincent and the Grenadines", "1784", NA, "Kingstown", "XCD", "en"));
            countryService.create(new Country("VE", "Venezuela", "Venezuela", "58", SA, "Caracas", "VEF", "es"));
            countryService.create(new Country("VG", "British Virgin Islands", "British Virgin Islands", "1284", NA,
                    "Road Town", "USD", "en"));
            countryService.create(new Country("VI", "U.S. Virgin Islands", "United States Virgin Islands", "1340", NA,
                    "Charlotte Amalie", "USD", "en"));
            countryService.create(new Country("VN", "Vietnam", "Việt Nam", "84", AS, "Hanoi", "VND", "vi"));
            countryService.create(new Country("VU", "Vanuatu", "Vanuatu", "678", OC, "Port Vila", "VUV", "bi,en,fr"));
            countryService.create(
                    new Country("WF", "Wallis and Futuna", "Wallis et Futuna", "681", OC, "Mata-Utu", "XPF", "fr"));
            countryService.create(new Country("WS", "Samoa", "Samoa", "685", OC, "Apia", "WST", "sm,en"));
            countryService.create(
                    new Country("XK", "Kosovo", "Republika e Kosovës", "377,381,386", EU, "Pristina", "EUR", "sq,sr"));
            countryService.create(new Country("YE", "Yemen", "اليَمَن", "967", AS, "Sana'a", "YER", "ar"));
            countryService.create(new Country("YT", "Mayotte", "Mayotte", "262", AF, "Mamoudzou", "EUR", "fr"));
            countryService.create(new Country("ZA", "South Africa", "South Africa", "27", AF, "Pretoria", "ZAR",
                    "af,en,nr,st,ss,tn,ts,ve,xh,zu"));
            countryService.create(new Country("ZM", "Zambia", "Zambia", "260", AF, "Lusaka", "ZMK", "en"));
            countryService.create(new Country("ZW", "Zimbabwe", "Zimbabwe", "263", AF, "Harare", "ZWL", "en,sn,nd"));
        } else {

            LOGGER.debug("#initContinentsAndCountries, skipping");
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
            String[] names = {"Linda Hernandez", "David Ellis", "Nancy Morgan", "Elizabeth White", "Richard Collins",
                    "David Sanchez", "Michael Cox", "Karen Moore", "John Gray", "Carol Garcia"};
            return names;
        }
    }
}
