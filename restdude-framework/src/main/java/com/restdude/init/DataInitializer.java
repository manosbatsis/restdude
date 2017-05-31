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

import com.restdude.domain.Roles;
import com.restdude.domain.error.service.ErrorLogService;
import com.restdude.domain.error.service.SystemErrorService;
import com.restdude.domain.geography.model.Continent;
import com.restdude.domain.geography.model.Country;
import com.restdude.domain.geography.service.ContinentService;
import com.restdude.domain.geography.service.CountryService;
import com.restdude.domain.users.model.UserCredentials;
import com.restdude.domain.users.repository.UserRegistrationCodeBatchRepository;
import com.restdude.domain.users.repository.UserRegistrationCodeRepository;
import com.restdude.domain.users.repository.UserRepository;
import com.restdude.domain.users.service.RoleService;
import com.restdude.domain.users.service.UserService;
import com.restdude.mdd.repository.ModelRepository;
import com.restdude.util.ConfigurationFactory;
import com.restdude.util.email.service.EmailService;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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

    protected SystemErrorService systemErrorService;
    protected ErrorLogService stackTraceService;

    @Autowired
    public void setSystemErrorService(SystemErrorService systemErrorService) {
        this.systemErrorService = systemErrorService;
    }

    @Autowired
    public void setStackTraceService(ErrorLogService stackTraceService) {
        this.stackTraceService = stackTraceService;
    }
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
                        Arrays.asList(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority(Roles.ROLE_USER), new SimpleGrantedAuthority(Roles.ROLE_ADMIN)})));

        if (this.countryService.count() == 0) {
            this.initContinentsAndCountries();
        } else {

            LOGGER.debug("run, skipping data init");
        }
    }



    protected void initContinentsAndCountries() {

        LOGGER.debug("#initContinentsAndCountries, creating");
        if (continentService.count() == 0) {
            Continent AF = continentService.create(new Continent("AF", "Africa"));
            Continent AN = continentService.create(new Continent("AN", "Antarctica"));
            Continent AS = continentService.create(new Continent("AS", "Asia"));
            Continent EU = continentService.create(new Continent("EU", "Europe"));
            Continent NA = continentService.create(new Continent("NA", "North America"));
            Continent OC = continentService.create(new Continent("OC", "Oceania"));
            Continent SA = continentService.create(new Continent("SA", "South America"));
            List<Country> countries = new LinkedList<>();
            countries.add(new Country("AD", "Andorra", "Andorra", "376", EU, "Andorra la Vella", "EUR", "ca"));
            countries.add(new Country("AE", "United Arab Emirates", "دولة الإمارات العربية المتحدة", "971", AS,
                    "Abu Dhabi", "AED", "ar"));
            countries.add(new Country("AF", "Afghanistan", "افغانستان", "93", AS, "Kabul", "AFN", "ps,uz,tk"));
            countries.add(new Country("AG", "Antigua and Barbuda", "Antigua and Barbuda", "1268", NA,
                    "Saint John's", "XCD", "en"));
            countries.add(new Country("AI", "Anguilla", "Anguilla", "1264", NA, "The Valley", "XCD", "en"));
            countries.add(new Country("AL", "Albania", "Shqipëria", "355", EU, "Tirana", "ALL", "sq"));
            countries.add(new Country("AM", "Armenia", "Հայաստան", "374", AS, "Yerevan", "AMD", "hy,ru"));
            countries.add(new Country("AO", "Angola", "Angola", "244", AF, "Luanda", "AOA", "pt"));
            countries.add(new Country("AQ", "Antarctica", null, null, AN, null, null, null));
            countries.add(new Country("AR", "Argentina", "Argentina", "54", SA, "Buenos Aires", "ARS", "es,gn"));
            countries.add(
                    new Country("AS", "American Samoa", "American Samoa", "1684", OC, "Pago Pago", "USD", "en,sm"));
            countries.add(new Country("AT", "Austria", "Österreich", "43", EU, "Vienna", "EUR", "de"));
            countries.add(new Country("AU", "Australia", "Australia", "61", OC, "Canberra", "AUD", "en"));
            countries.add(new Country("AW", "Aruba", "Aruba", "297", NA, "Oranjestad", "AWG", "nl,pa"));
            countries.add(new Country("AX", "Åland", "Åland", "358", EU, "Mariehamn", "EUR", "sv"));
            countries.add(new Country("AZ", "Azerbaijan", "Azərbaycan", "994", AS, "Baku", "AZN", "az,hy"));
            countries.add(new Country("BA", "Bosnia and Herzegovina", "Bosna i Hercegovina", "387", EU,
                    "Sarajevo", "BAM", "bs,hr,sr"));
            countries.add(new Country("BB", "Barbados", "Barbados", "1246", NA, "Bridgetown", "BBD", "en"));
            countries.add(new Country("BD", "Bangladesh", "Bangladesh", "880", AS, "Dhaka", "BDT", "bn"));
            countries.add(new Country("BE", "Belgium", "België", "32", EU, "Brussels", "EUR", "nl,fr,de"));
            countries.add(new Country("BF", "Burkina Faso", "Burkina Faso", "226", AF, "Ouagadougou", "XOF", "fr,ff"));
            countries.add(new Country("BG", "Bulgaria", "България", "359", EU, "Sofia", "BGN", "bg"));
            countries.add(new Country("BH", "Bahrain", "‏البحرين", "973", AS, "Manama", "BHD", "ar"));
            countries.add(new Country("BI", "Burundi", "Burundi", "257", AF, "Bujumbura", "BIF", "fr,rn"));
            countries.add(new Country("BJ", "Benin", "Bénin", "229", AF, "Porto-Novo", "XOF", "fr"));
            countries.add(new Country("BL", "Saint Barthélemy", "Saint-Barthélemy", "590", NA, "Gustavia", "EUR", "fr"));
            countries.add(new Country("BM", "Bermuda", "Bermuda", "1441", NA, "Hamilton", "BMD", "en"));
            countries.add(new Country("BN", "Brunei", "Negara Brunei Darussalam", "673", AS,
                    "Bandar Seri Begawan", "BND", "ms"));
            countries.add(new Country("BO", "Bolivia", "Bolivia", "591", SA, "Sucre", "BOB,BOV", "es,ay,qu"));
            countries.add(new Country("BQ", "Bonaire", "Bonaire", "5997", NA, "Kralendijk", "USD", "nl"));
            countries.add(new Country("BR", "Brazil", "Brasil", "55", SA, "Brasília", "BRL", "pt"));
            countries.add(new Country("BS", "Bahamas", "Bahamas", "1242", NA, "Nassau", "BSD", "en"));
            countries.add(new Country("BT", "Bhutan", "ʼbrug-yul", "975", AS, "Thimphu", "BTN,INR", "dz"));
            countries.add(new Country("BV", "Bouvet Island", "Bouvetøya", null, AN, null, "NOK", null));
            countries.add(new Country("BW", "Botswana", "Botswana", "267", AF, "Gaborone", "BWP", "en,tn"));
            countries.add(new Country("BY", "Belarus", "Белару́сь", "375", EU, "Minsk", "BYR", "be,ru"));
            countries.add(new Country("BZ", "Belize", "Belize", "501", NA, "Belmopan", "BZD", "en,es"));
            countries.add(new Country("CA", "Canada", "Canada", "1", NA, "Ottawa", "CAD", "en,fr"));
            countries.add(new Country("CC", "Cocos [Keeling] Islands", "Cocos (Keeling) Islands", "61", AS,
                    "West Island", "AUD", "en"));
            countries.add(new Country("CD", "Democratic Republic of the Congo",
                    "République démocratique du Congo", "243", AF, "Kinshasa", "CDF", "fr,ln,kg,sw,lu"));
            countries.add(new Country("CF", "Central African Republic", "Ködörösêse tî Bêafrîka", "236", AF,
                    "Bangui", "XAF", "fr,sg"));
            countries.add(new Country("CG", "Republic of the Congo", "République du Congo", "242", AF,
                    "Brazzaville", "XAF", "fr,ln"));
            countries.add(new Country("CH", "Switzerland", "Schweiz", "41", EU, "Bern", "CHE,CHF,CHW", "de,fr,it"));
            countries.add(new Country("CI", "Ivory Coast", "Côte d'Ivoire", "225", AF, "Yamoussoukro", "XOF", "fr"));
            countries.add(new Country("CK", "Cook Islands", "Cook Islands", "682", OC, "Avarua", "NZD", "en"));
            countries.add(new Country("CL", "Chile", "Chile", "56", SA, "Santiago", "CLF,CLP", "es"));
            countries.add(new Country("CM", "Cameroon", "Cameroon", "237", AF, "Yaoundé", "XAF", "en,fr"));
            countries.add(new Country("CN", "China", "中国", "86", AS, "Beijing", "CNY", "zh"));
            countries.add(new Country("CO", "Colombia", "Colombia", "57", SA, "Bogotá", "COP", "es"));
            countries.add(new Country("CR", "Costa Rica", "Costa Rica", "506", NA, "San José", "CRC", "es"));
            countries.add(new Country("CU", "Cuba", "Cuba", "53", NA, "Havana", "CUC,CUP", "es"));
            countries.add(new Country("CV", "Cape Verde", "Cabo Verde", "238", AF, "Praia", "CVE", "pt"));
            countries.add(new Country("CW", "Curacao", "Curaçao", "5999", NA, "Willemstad", "ANG", "nl,pa,en"));
            countries.add(new Country("CX", "Christmas Island", "Christmas Island", "61", AS,
                    "Flying Fish Cove", "AUD", "en"));
            countries.add(new Country("CY", "Cyprus", "Κύπρος", "357", EU, "Nicosia", "EUR", "el,tr,hy"));
            countries.add(new Country("CZ", "Czechia", "Česká republika", "420", EU, "Prague", "CZK", "cs,sk"));
            countries.add(new Country("DE", "Germany", "Deutschland", "49", EU, "Berlin", "EUR", "de"));
            countries.add(new Country("DJ", "Djibouti", "Djibouti", "253", AF, "Djibouti", "DJF", "fr,ar"));
            countries.add(new Country("DK", "Denmark", "Danmark", "45", EU, "Copenhagen", "DKK", "da"));
            countries.add(new Country("DM", "Dominica", "Dominica", "1767", NA, "Roseau", "XCD", "en"));
            countries.add(new Country("DO", "Dominican Republic", "República Dominicana", "1809,1829,1849", NA,
                    "Santo Domingo", "DOP", "es"));
            countries.add(new Country("DZ", "Algeria", "الجزائر", "213", AF, "Algiers", "DZD", "ar"));
            countries.add(new Country("EC", "Ecuador", "Ecuador", "593", SA, "Quito", "USD", "es"));
            countries.add(new Country("EE", "Estonia", "Eesti", "372", EU, "Tallinn", "EUR", "et"));
            countries.add(new Country("EG", "Egypt", "مصر‎", "20", AF, "Cairo", "EGP", "ar"));
            countries.add(
                    new Country("EH", "Western Sahara", "الصحراء الغربية", "212", AF, "El Aaiún", "MAD,DZD,MRO", "es"));
            countries.add(new Country("ER", "Eritrea", "ኤርትራ", "291", AF, "Asmara", "ERN", "ti,ar,en"));
            countries.add(new Country("ES", "Spain", "España", "34", EU, "Madrid", "EUR", "es,eu,ca,gl,oc"));
            countries.add(new Country("ET", "Ethiopia", "ኢትዮጵያ", "251", AF, "Addis Ababa", "ETB", "am"));
            countries.add(new Country("FI", "Finland", "Suomi", "358", EU, "Helsinki", "EUR", "fi,sv"));
            countries.add(new Country("FJ", "Fiji", "Fiji", "679", OC, "Suva", "FJD", "en,fj,hi,ur"));
            countries.add(new Country("FK", "Falkland Islands", "Falkland Islands", "500", SA, "Stanley", "FKP", "en"));
            countries.add(new Country("FM", "Micronesia", "Micronesia", "691", OC, "Palikir", "USD", "en"));
            countries.add(new Country("FO", "Faroe Islands", "Føroyar", "298", EU, "Tórshavn", "DKK", "fo"));
            countries.add(new Country("FR", "France", "France", "33", EU, "Paris", "EUR", "fr"));
            countries.add(new Country("GA", "Gabon", "Gabon", "241", AF, "Libreville", "XAF", "fr"));
            countries.add(new Country("GB", "United Kingdom", "United Kingdom", "44", EU, "London", "GBP", "en"));
            countries.add(new Country("GD", "Grenada", "Grenada", "1473", NA, "St. George's", "XCD", "en"));
            countries.add(new Country("GE", "Georgia", "საქართველო", "995", AS, "Tbilisi", "GEL", "ka"));
            countries.add(new Country("GF", "French Guiana", "Guyane française", "594", SA, "Cayenne", "EUR", "fr"));
            countries.add(new Country("GG", "Guernsey", "Guernsey", "44", EU, "St. Peter Port", "GBP", "en,fr"));
            countries.add(new Country("GH", "Ghana", "Ghana", "233", AF, "Accra", "GHS", "en"));
            countries.add(new Country("GI", "Gibraltar", "Gibraltar", "350", EU, "Gibraltar", "GIP", "en"));
            countries.add(new Country("GL", "Greenland", "Kalaallit Nunaat", "299", NA, "Nuuk", "DKK", "kl"));
            countries.add(new Country("GM", "Gambia", "Gambia", "220", AF, "Banjul", "GMD", "en"));
            countries.add(new Country("GN", "Guinea", "Guinée", "224", AF, "Conakry", "GNF", "fr,ff"));
            countries.add(new Country("GP", "Guadeloupe", "Guadeloupe", "590", NA, "Basse-Terre", "EUR", "fr"));
            countries.add(
                    new Country("GQ", "Equatorial Guinea", "Guinea Ecuatorial", "240", AF, "Malabo", "XAF", "es,fr"));
            countries.add(new Country("GR", "Greece", "Ελλάδα", "30", EU, "Athens", "EUR", "el"));
            countries.add(new Country("GS", "South Georgia and the South Sandwich Islands", "South Georgia",
                    "500", AN, "King Edward Point", "GBP", "en"));
            countries.add(new Country("GT", "Guatemala", "Guatemala", "502", NA, "Guatemala City", "GTQ", "es"));
            countries.add(new Country("GU", "Guam", "Guam", "1671", OC, "Hagåtña", "USD", "en,ch,es"));
            countries.add(new Country("GW", "Guinea-Bissau", "Guiné-Bissau", "245", AF, "Bissau", "XOF", "pt"));
            countries.add(new Country("GY", "Guyana", "Guyana", "592", SA, "Georgetown", "GYD", "en"));
            countries.add(new Country("HK", "Hong Kong", "香港", "852", AS, "City of Victoria", "HKD", "zh,en"));
            countries.add(new Country("HM", "Heard Island and McDonald Islands",
                    "Heard Island and McDonald Islands", null, AN, null, "AUD", "en"));
            countries.add(new Country("HN", "Honduras", "Honduras", "504", NA, "Tegucigalpa", "HNL", "es"));
            countries.add(new Country("HR", "Croatia", "Hrvatska", "385", EU, "Zagreb", "HRK", "hr"));
            countries
                    .add(new Country("HT", "Haiti", "Haïti", "509", NA, "Port-au-Prince", "HTG,USD", "fr,ht"));
            countries.add(new Country("HU", "Hungary", "Magyarország", "36", EU, "Budapest", "HUF", "hu"));
            countries.add(new Country("ID", "Indonesia", "Indonesia", "62", AS, "Jakarta", "IDR", "id"));
            countries.add(new Country("IE", "Ireland", "Éire", "353", EU, "Dublin", "EUR", "ga,en"));
            countries.add(new Country("IL", "Israel", "יִשְׂרָאֵל", "972", AS, "Jerusalem", "ILS", "he,ar"));
            countries
                    .add(new Country("IM", "Isle of Man", "Isle of Man", "44", EU, "Douglas", "GBP", "en,gv"));
            countries.add(new Country("IN", "India", "भारत", "91", AS, "New Delhi", "INR", "hi,en"));
            countries.add(new Country("IO", "British Indian Ocean Territory", "British Indian Ocean Territory",
                    "246", AS, "Diego Garcia", "USD", "en"));
            countries.add(new Country("IQ", "Iraq", "العراق", "964", AS, "Baghdad", "IQD", "ar,ku"));
            countries.add(new Country("IR", "Iran", "Irān", "98", AS, "Tehran", "IRR", "fa"));
            countries.add(new Country("IS", "Iceland", "Ísland", "354", EU, "Reykjavik", "ISK", "is"));
            countries.add(new Country("IT", "Italy", "Italia", "39", EU, "Rome", "EUR", "it"));
            countries.add(new Country("JE", "Jersey", "Jersey", "44", EU, "Saint Helier", "GBP", "en,fr"));
            countries.add(new Country("JM", "Jamaica", "Jamaica", "1876", NA, "Kingston", "JMD", "en"));
            countries.add(new Country("JO", "Jordan", "الأردن", "962", AS, "Amman", "JOD", "ar"));
            countries.add(new Country("JP", "Japan", "日本", "81", AS, "Tokyo", "JPY", "ja"));
            countries.add(new Country("KE", "Kenya", "Kenya", "254", AF, "Nairobi", "KES", "en,sw"));
            countries.add(new Country("KG", "Kyrgyzstan", "Кыргызстан", "996", AS, "Bishkek", "KGS", "ky,ru"));
            countries.add(new Country("KH", "Cambodia", "Kâmpŭchéa", "855", AS, "Phnom Penh", "KHR", "km"));
            countries.add(new Country("KI", "Kiribati", "Kiribati", "686", OC, "South Tarawa", "AUD", "en"));
            countries.add(new Country("KM", "Comoros", "Komori", "269", AF, "Moroni", "KMF", "ar,fr"));
            countries.add(new Country("KN", "Saint Kitts and Nevis", "Saint Kitts and Nevis", "1869", NA,
                    "Basseterre", "XCD", "en"));
            countries.add(new Country("KP", "North Korea", "북한", "850", AS, "Pyongyang", "KPW", "ko"));
            countries.add(new Country("KR", "South Korea", "대한민국", "82", AS, "Seoul", "KRW", "ko"));
            countries.add(new Country("KW", "Kuwait", "الكويت", "965", AS, "Kuwait City", "KWD", "ar"));
            countries.add(
                    new Country("KY", "Cayman Islands", "Cayman Islands", "1345", NA, "George Town", "KYD", "en"));
            countries.add(new Country("KZ", "Kazakhstan", "Қазақстан", "76,77", AS, "Astana", "KZT", "kk,ru"));
            countries.add(new Country("LA", "Laos", "ສປປລາວ", "856", AS, "Vientiane", "LAK", "lo"));
            countries.add(new Country("LB", "Lebanon", "لبنان", "961", AS, "Beirut", "LBP", "ar,fr"));
            countries
                    .add(new Country("LC", "Saint Lucia", "Saint Lucia", "1758", NA, "Castries", "XCD", "en"));
            countries
                    .add(new Country("LI", "Liechtenstein", "Liechtenstein", "423", EU, "Vaduz", "CHF", "de"));
            countries.add(new Country("LK", "Sri Lanka", "śrī laṃkāva", "94", AS, "Colombo", "LKR", "si,ta"));
            countries.add(new Country("LR", "Liberia", "Liberia", "231", AF, "Monrovia", "LRD", "en"));
            countries.add(new Country("LS", "Lesotho", "Lesotho", "266", AF, "Maseru", "LSL,ZAR", "en,st"));
            countries.add(new Country("LT", "Lithuania", "Lietuva", "370", EU, "Vilnius", "LTL", "lt"));
            countries
                    .add(new Country("LU", "Luxembourg", "Luxembourg", "352", EU, "Luxembourg", "EUR", "fr,de,lb"));
            countries.add(new Country("LV", "Latvia", "Latvija", "371", EU, "Riga", "EUR", "lv"));
            countries.add(new Country("LY", "Libya", "‏ليبيا", "218", AF, "Tripoli", "LYD", "ar"));
            countries.add(new Country("MA", "Morocco", "المغرب", "212", AF, "Rabat", "MAD", "ar"));
            countries.add(new Country("MC", "Monaco", "Monaco", "377", EU, "Monaco", "EUR", "fr"));
            countries.add(new Country("MD", "Moldova", "Moldova", "373", EU, "Chișinău", "MDL", "ro"));
            countries
                    .add(new Country("ME", "Montenegro", "Црна Гора", "382", EU, "Podgorica", "EUR", "sr,bs,sq,hr"));
            countries
                    .add(new Country("MF", "Saint Martin", "Saint-Martin", "590", NA, "Marigot", "EUR", "en,fr,nl"));
            countries
                    .add(new Country("MG", "Madagascar", "Madagasikara", "261", AF, "Antananarivo", "MGA", "fr,mg"));
            countries
                    .add(new Country("MH", "Marshall Islands", "M̧ajeļ", "692", OC, "Majuro", "USD", "en,mh"));
            countries.add(new Country("MK", "Macedonia", "Македонија", "389", EU, "Skopje", "MKD", "mk"));
            countries.add(new Country("ML", "Mali", "Mali", "223", AF, "Bamako", "XOF", "fr"));
            countries.add(new Country("MM", "Myanmar [Burma]", "Myanma", "95", AS, "Naypyidaw", "MMK", "my"));
            countries.add(new Country("MN", "Mongolia", "Монгол улс", "976", AS, "Ulan Bator", "MNT", "mn"));
            countries.add(new Country("MO", "Macao", "澳門", "853", AS, null, "MOP", "zh,pt"));
            countries.add(new Country("MP", "Northern Mariana Islands", "Northern Mariana Islands", "1670", OC,
                    "Saipan", "USD", "en,ch"));
            countries
                    .add(new Country("MQ", "Martinique", "Martinique", "596", NA, "Fort-de-France", "EUR", "fr"));
            countries.add(new Country("MR", "Mauritania", "موريتانيا", "222", AF, "Nouakchott", "MRO", "ar"));
            countries.add(new Country("MS", "Montserrat", "Montserrat", "1664", NA, "Plymouth", "XCD", "en"));
            countries.add(new Country("MT", "Malta", "Malta", "356", EU, "Valletta", "EUR", "mt,en"));
            countries.add(new Country("MU", "Mauritius", "Maurice", "230", AF, "Port Louis", "MUR", "en"));
            countries.add(new Country("MV", "Maldives", "Maldives", "960", AS, "Malé", "MVR", "dv"));
            countries.add(new Country("MW", "Malawi", "Malawi", "265", AF, "Lilongwe", "MWK", "en,ny"));
            countries.add(new Country("MX", "Mexico", "México", "52", NA, "Mexico City", "MXN", "es"));
            countries.add(new Country("MY", "Malaysia", "Malaysia", "60", AS, "Kuala Lumpur", "MYR", null));
            countries.add(new Country("MZ", "Mozambique", "Moçambique", "258", AF, "Maputo", "MZN", "pt"));
            countries.add(new Country("NA", "Namibia", "Namibia", "264", AF, "Windhoek", "NAD,ZAR", "en,af"));
            countries
                    .add(new Country("NC", "New Caledonia", "Nouvelle-Calédonie", "687", OC, "Nouméa", "XPF", "fr"));
            countries.add(new Country("NE", "Niger", "Niger", "227", AF, "Niamey", "XOF", "fr"));
            countries
                    .add(new Country("NF", "Norfolk Island", "Norfolk Island", "672", OC, "Kingston", "AUD", "en"));
            countries.add(new Country("NG", "Nigeria", "Nigeria", "234", AF, "Abuja", "NGN", "en"));
            countries.add(new Country("NI", "Nicaragua", "Nicaragua", "505", NA, "Managua", "NIO", "es"));
            countries.add(new Country("NL", "Netherlands", "Nederland", "31", EU, "Amsterdam", "EUR", "nl"));
            countries.add(new Country("NO", "Norway", "Norge", "47", EU, "Oslo", "NOK", "no,nb,nn"));
            countries.add(new Country("NP", "Nepal", "नपल", "977", AS, "Kathmandu", "NPR", "ne"));
            countries.add(new Country("NR", "Nauru", "Nauru", "674", OC, "Yaren", "AUD", "en,na"));
            countries.add(new Country("NU", "Niue", "Niuē", "683", OC, "Alofi", "NZD", "en"));
            countries
                    .add(new Country("NZ", "New Zealand", "New Zealand", "64", OC, "Wellington", "NZD", "en,mi"));
            countries.add(new Country("OM", "Oman", "عمان", "968", AS, "Muscat", "OMR", "ar"));
            countries.add(new Country("PA", "Panama", "Panamá", "507", NA, "Panama City", "PAB,USD", "es"));
            countries.add(new Country("PE", "Peru", "Perú", "51", SA, "Lima", "PEN", "es"));
            countries.add(
                    new Country("PF", "French Polynesia", "Polynésie française", "689", OC, "Papeetē", "XPF", "fr"));
            countries.add(
                    new Country("PG", "Papua New Guinea", "Papua Niugini", "675", OC, "Port Moresby", "PGK", "en"));
            countries.add(new Country("PH", "Philippines", "Pilipinas", "63", AS, "Manila", "PHP", "en"));
            countries.add(new Country("PK", "Pakistan", "Pakistan", "92", AS, "Islamabad", "PKR", "en,ur"));
            countries.add(new Country("PL", "Poland", "Polska", "48", EU, "Warsaw", "PLN", "pl"));
            countries.add(new Country("PM", "Saint Pierre and Miquelon", "Saint-Pierre-et-Miquelon", "508", NA,
                    "Saint-Pierre", "EUR", "fr"));
            countries.add(
                    new Country("PN", "Pitcairn Islands", "Pitcairn Islands", "64", OC, "Adamstown", "NZD", "en"));
            countries
                    .add(new Country("PR", "Puerto Rico", "Puerto Rico", "1787,1939", NA, "San Juan", "USD", "es,en"));
            countries.add(new Country("PS", "Palestine", "فلسطين", "970", AS, "Ramallah", "ILS", "ar"));
            countries.add(new Country("PT", "Portugal", "Portugal", "351", EU, "Lisbon", "EUR", "pt"));
            countries.add(new Country("PW", "Palau", "Palau", "680", OC, "Ngerulmud", "USD", "en"));
            countries.add(new Country("PY", "Paraguay", "Paraguay", "595", SA, "Asunción", "PYG", "es,gn"));
            countries.add(new Country("QA", "Qatar", "قطر", "974", AS, "Doha", "QAR", "ar"));
            countries.add(new Country("RE", "Réunion", "La Réunion", "262", AF, "Saint-Denis", "EUR", "fr"));
            countries.add(new Country("RO", "Romania", "România", "40", EU, "Bucharest", "RON", "ro"));
            countries.add(new Country("RS", "Serbia", "Србија", "381", EU, "Belgrade", "RSD", "sr"));
            countries.add(new Country("RU", "Russia", "Россия", "7", EU, "Moscow", "RUB", "ru"));
            countries.add(new Country("RW", "Rwanda", "Rwanda", "250", AF, "Kigali", "RWF", "rw,en,fr"));
            countries
                    .add(new Country("SA", "Saudi Arabia", "العربية السعودية", "966", AS, "Riyadh", "SAR", "ar"));
            countries
                    .add(new Country("SB", "Solomon Islands", "Solomon Islands", "677", OC, "Honiara", "SDB", "en"));
            countries
                    .add(new Country("SC", "Seychelles", "Seychelles", "248", AF, "Victoria", "SCR", "fr,en"));
            countries.add(new Country("SD", "Sudan", "السودان", "249", AF, "Khartoum", "SDG", "ar,en"));
            countries.add(new Country("SE", "Sweden", "Sverige", "46", EU, "Stockholm", "SEK", "sv"));
            countries
                    .add(new Country("SG", "Singapore", "Singapore", "65", AS, "Singapore", "SGD", "en,ms,ta,zh"));
            countries
                    .add(new Country("SH", "Saint Helena", "Saint Helena", "290", AF, "Jamestown", "SHP", "en"));
            countries.add(new Country("SI", "Slovenia", "Slovenija", "386", EU, "Ljubljana", "EUR", "sl"));
            countries.add(new Country("SJ", "Svalbard and Jan Mayen", "Svalbard og Jan Mayen", "4779", EU,
                    "Longyearbyen", "NOK", "no"));
            countries.add(new Country("SK", "Slovakia", "Slovensko", "421", EU, "Bratislava", "EUR", "sk"));
            countries
                    .add(new Country("SL", "Sierra Leone", "Sierra Leone", "232", AF, "Freetown", "SLL", "en"));
            countries
                    .add(new Country("SM", "San Marino", "San Marino", "378", EU, "City of San Marino", "EUR", "it"));
            countries.add(new Country("SN", "Senegal", "Sénégal", "221", AF, "Dakar", "XOF", "fr"));
            countries.add(new Country("SO", "Somalia", "Soomaaliya", "252", AF, "Mogadishu", "SOS", "so,ar"));
            countries.add(new Country("SR", "Suriname", "Suriname", "597", SA, "Paramaribo", "SRD", "nl"));
            countries.add(new Country("SS", "South Sudan", "South Sudan", "211", AF, "Juba", "SSP", "en"));
            countries.add(new Country("ST", "São Tomé and Príncipe", "São Tomé e Príncipe", "239", AF,
                    "São Tomé", "STD", "pt"));
            countries
                    .add(new Country("SV", "El Salvador", "El Salvador", "503", NA, "San Salvador", "SVC,USD", "es"));
            countries
                    .add(new Country("SX", "Sint Maarten", "Sint Maarten", "1721", NA, "Philipsburg", "ANG", "nl,en"));
            countries.add(new Country("SY", "Syria", "سوريا", "963", AS, "Damascus", "SYP", "ar"));
            countries.add(new Country("SZ", "Swaziland", "Swaziland", "268", AF, "Lobamba", "SZL", "en,ss"));
            countries.add(new Country("TC", "Turks and Caicos Islands", "Turks and Caicos Islands", "1649", NA,
                    "Cockburn Town", "USD", "en"));
            countries.add(new Country("TD", "Chad", "Tchad", "235", AF, "N'Djamena", "XAF", "fr,ar"));
            countries.add(new Country("TF", "French Southern Territories",
                    "Territoire des Terres australes et antarctiques fr", null, AN, "Port-aux-Français", "EUR", "fr"));
            countries.add(new Country("TG", "Togo", "Togo", "228", AF, "Lomé", "XOF", "fr"));
            countries.add(new Country("TH", "Thailand", "ประเทศไทย", "66", AS, "Bangkok", "THB", "th"));
            countries
                    .add(new Country("TJ", "Tajikistan", "Тоҷикистон", "992", AS, "Dushanbe", "TJS", "tg,ru"));
            countries.add(new Country("TK", "Tokelau", "Tokelau", "690", OC, "Fakaofo", "NZD", "en"));
            countries.add(new Country("TL", "East Timor", "Timor-Leste", "670", OC, "Dili", "USD", "pt"));
            countries
                    .add(new Country("TM", "Turkmenistan", "Türkmenistan", "993", AS, "Ashgabat", "TMT", "tk,ru"));
            countries.add(new Country("TN", "Tunisia", "تونس", "216", AF, "Tunis", "TND", "ar"));
            countries.add(new Country("TO", "Tonga", "Tonga", "676", OC, "Nuku'alofa", "TOP", "en,to"));
            countries.add(new Country("TR", "Turkey", "Türkiye", "90", AS, "Ankara", "TRY", "tr"));
            countries.add(new Country("TT", "Trinidad and Tobago", "Trinidad and Tobago", "1868", NA,
                    "Port of Spain", "TTD", "en"));
            countries.add(new Country("TV", "Tuvalu", "Tuvalu", "688", OC, "Funafuti", "AUD", "en"));
            countries.add(new Country("TW", "Taiwan", "臺灣", "886", AS, "Taipei", "TWD", "zh"));
            countries.add(new Country("TZ", "Tanzania", "Tanzania", "255", AF, "Dodoma", "TZS", "sw,en"));
            countries.add(new Country("UA", "Ukraine", "Україна", "380", EU, "Kiev", "UAH", "uk"));
            countries.add(new Country("UG", "Uganda", "Uganda", "256", AF, "Kampala", "UGX", "en,sw"));
            countries.add(new Country("UM", "U.S. Minor Outlying Islands",
                    "United States Minor Outlying Islands", null, OC, null, "USD", "en"));
            countries.add(new Country("US", "United States", "United States", "1", NA, "Washington D.C.",
                    "USD,USN,USS", "en"));
            countries.add(new Country("UY", "Uruguay", "Uruguay", "598", SA, "Montevideo", "UYI,UYU", "es"));
            countries
                    .add(new Country("UZ", "Uzbekistan", "O‘zbekiston", "998", AS, "Tashkent", "UZS", "uz,ru"));
            countries.add(
                    new Country("VA", "Vatican City", "Vaticano", "39066,379", EU, "Vatican City", "EUR", "it,la"));
            countries.add(new Country("VC", "Saint Vincent and the Grenadines",
                    "Saint Vincent and the Grenadines", "1784", NA, "Kingstown", "XCD", "en"));
            countries.add(new Country("VE", "Venezuela", "Venezuela", "58", SA, "Caracas", "VEF", "es"));
            countries.add(new Country("VG", "British Virgin Islands", "British Virgin Islands", "1284", NA,
                    "Road Town", "USD", "en"));
            countries.add(new Country("VI", "U.S. Virgin Islands", "United States Virgin Islands", "1340", NA,
                    "Charlotte Amalie", "USD", "en"));
            countries.add(new Country("VN", "Vietnam", "Việt Nam", "84", AS, "Hanoi", "VND", "vi"));
            countries.add(new Country("VU", "Vanuatu", "Vanuatu", "678", OC, "Port Vila", "VUV", "bi,en,fr"));
            countries.add(
                    new Country("WF", "Wallis and Futuna", "Wallis et Futuna", "681", OC, "Mata-Utu", "XPF", "fr"));
            countries.add(new Country("WS", "Samoa", "Samoa", "685", OC, "Apia", "WST", "sm,en"));
            countries.add(
                    new Country("XK", "Kosovo", "Republika e Kosovës", "377,381,386", EU, "Pristina", "EUR", "sq,sr"));
            countries.add(new Country("YE", "Yemen", "اليَمَن", "967", AS, "Sana'a", "YER", "ar"));
            countries.add(new Country("YT", "Mayotte", "Mayotte", "262", AF, "Mamoudzou", "EUR", "fr"));
            countries.add(new Country("ZA", "South Africa", "South Africa", "27", AF, "Pretoria", "ZAR",
                    "af,en,nr,st,ss,tn,ts,ve,xh,zu"));
            countries.add(new Country("ZM", "Zambia", "Zambia", "260", AF, "Lusaka", "ZMK", "en"));
            countries.add(new Country("ZW", "Zimbabwe", "Zimbabwe", "263", AF, "Harare", "ZWL", "en,sn,nd"));
            countryService.create(countries);
        } else {

            LOGGER.debug("#initContinentsAndCountries, skipping");
        }
    }



}
