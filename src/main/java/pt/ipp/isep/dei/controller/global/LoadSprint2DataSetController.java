package pt.ipp.isep.dei.controller.global;

import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint2.CountryRepository;
import pt.ipp.isep.dei.data.repository.sprint2.StationEsinf2Repository;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.util.List;

/**
 * Controller responsável pelo carregamento do dataset CSV do Sprint 2,
 * criando países e estações ferroviárias ESINF.
 */
public class LoadSprint2DataSetController implements Runnable {

    /**
     * Caminho para o ficheiro CSV do dataset do Sprint 2.
     */
    private String DATA_SET_CSV_ESINF_PATH = "dataset/sprint2/train_stations_europe.csv";

    /**
     * Repositório de países.
     */
    private CountryRepository countryRepository;

    /**
     * Repositório de estações ESINF.
     */
    private StationEsinf2Repository stationEsinf2Repository;

    /**
     * Executa o carregamento do dataset e inicializa estruturas auxiliares.
     */
    @Override
    public void run() {
        Repositories repositories = Repositories.getInstance();

        countryRepository = repositories.getCountryRepository();
        stationEsinf2Repository = repositories.getStationEsinf2Repository();

        UIUtils.addLog("------------------------------ Starting CSV data sprint 2 loading... ------------------------------", LogType.INFO, RoleType.GLOBAL);
        loadDataSet();

        stationEsinf2Repository.createKDTree();
    }

    /**
     * Lê o ficheiro CSV e carrega os dados das estações ferroviárias,
     * validando campos e registando erros quando necessário.
     */
    private void loadDataSet() {
        List<String[]> stationsList = UIUtils.readCSV(DATA_SET_CSV_ESINF_PATH);

        for (String[] stationData : stationsList) {

            if (stationData.length != 10) continue;

            String country = stationData[0];
            if (country == null || country.isEmpty()) {
                UIUtils.addLog("Country is null or empty", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String timeZone = stationData[1];
            if (timeZone == null || timeZone.isEmpty()) {
                UIUtils.addLog("Time zone is null or empty", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String timeZoneGroup = stationData[3];
            if (timeZoneGroup == null || timeZoneGroup.isEmpty()) {
                UIUtils.addLog("Time zone group is null or empty", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String stationName = stationData[4];
            if (stationName == null || stationName.isEmpty()) {
                UIUtils.addLog("Station name is null or empty", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (stationData[5] == null || stationData[5].isEmpty()) {
                UIUtils.addLog("Latitude is null or empty", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            double latitude = Double.parseDouble(stationData[5]);
            if (latitude < -90 || latitude > 90) {
                UIUtils.addLog("Latitude out of bounds", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            if (stationData[6] == null || stationData[6].isEmpty()) {
                UIUtils.addLog("Longitude is null or empty", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            double longitude = Double.parseDouble(stationData[6]);
            if (longitude < -180 || longitude > 180) {
                UIUtils.addLog("Longitude out of bounds", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String isCity = stationData[7];
            if (isCity == null) {
                UIUtils.addLog("isCity is null", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            String cityNorm = isCity.trim().toLowerCase();
            if (!cityNorm.equals("true") && !cityNorm.equals("false")) {
                UIUtils.addLog("isCity is not 'true' or 'false'", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String isMainStation = stationData[8];
            if (isMainStation == null) {
                UIUtils.addLog("isMainStation is null", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            String mainNorm = isMainStation.trim().toLowerCase();
            if (!mainNorm.equals("true") && !mainNorm.equals("false")) {
                UIUtils.addLog("isMainStation is not 'true' or 'false'", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            String isAirport = stationData[9];
            if (isAirport == null) {
                UIUtils.addLog("isAirport is null", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }
            String airNorm = isAirport.trim().toLowerCase();
            if (!airNorm.equals("true") && !airNorm.equals("false")) {
                UIUtils.addLog("isAirport is not 'true' or 'false'", LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            timeZone = timeZone.replace("\"(", "").replace("'", "");

            TimeZone tz = getTimeZone(timeZone);
            if (tz == null) {
                UIUtils.addLog("Unrecognized time zone: " + timeZone, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            TimeZoneGroup tzGroup = getTimeZoneGroup(timeZoneGroup);
            if (tzGroup == null) {
                UIUtils.addLog("Unrecognized time zone group: " + timeZoneGroup, LogType.ERROR, RoleType.GLOBAL);
                continue;
            }

            boolean isCityBool = cityNorm.equals("true");
            boolean isMainStationBool = mainNorm.equals("true");
            boolean isAirportBool = airNorm.equals("true");

            Country countryObj;
            if (!countryRepository.existsByName(country)) {
                countryObj = new Country(country);
                countryRepository.add(countryObj);
                UIUtils.addLog("Added new country: " + country, LogType.INFO, RoleType.GLOBAL);
            } else {
                countryObj = countryRepository.findByName(country);
            }

            StationEsinf newStation = new StationEsinf(countryObj, tz, tzGroup, stationName, latitude, longitude, isCityBool, isMainStationBool, isAirportBool);

            if (!stationEsinf2Repository.existsById(newStation.getId())) {
                stationEsinf2Repository.addStation(newStation);
                UIUtils.addLog("Added new station: " + stationName + " in country: " + country, LogType.INFO, RoleType.GLOBAL);
            }
        }
    }

    /**
     * Mapeia uma string de fuso horário para o enum {@link TimeZone}.
     *
     * @param timeZone identificador textual do fuso horário
     * @return enum TimeZone correspondente ou null se não existir
     */
    private TimeZone getTimeZone(String timeZone) {
        if (timeZone == null) return null;

        switch (timeZone) {
            case "Europe/Paris": return TimeZone.EUROPE_PARIS;
            case "Europe/Zurich": return TimeZone.EUROPE_ZURICH;
            case "Europe/Berlin": return TimeZone.EUROPE_BERLIN;
            case "Europe/Brussels": return TimeZone.EUROPE_BRUSSELS;
            case "Europe/Madrid": return TimeZone.EUROPE_MADRID;
            case "Europe/Rome": return TimeZone.EUROPE_ROME;
            case "Europe/Andorra": return TimeZone.EUROPE_ANDORRA;
            case "Europe/London": return TimeZone.EUROPE_LONDON;
            case "Europe/Amsterdam": return TimeZone.EUROPE_AMSTERDAM;
            case "Europe/Vienna": return TimeZone.EUROPE_VIENNA;
            case "Europe/Luxembourg": return TimeZone.EUROPE_LUXEMBOURG;
            case "Europe/Lisbon": return TimeZone.EUROPE_LISBON;
            case "Europe/Warsaw": return TimeZone.EUROPE_WARSAW;
            case "Europe/Moscow": return TimeZone.EUROPE_MOSCOW;
            case "Europe/Minsk": return TimeZone.EUROPE_MINSK;
            case "Europe/Budapest": return TimeZone.EUROPE_BUDAPEST;
            case "Europe/Prague": return TimeZone.EUROPE_PRAGUE;
            case "Europe/Bratislava": return TimeZone.EUROPE_BRATISLAVA;
            case "Europe/Zagreb": return TimeZone.EUROPE_ZAGREB;
            case "Europe/Copenhagen": return TimeZone.EUROPE_COPENHAGEN;
            case "Europe/Stockholm": return TimeZone.EUROPE_STOCKHOLM;
            case "Europe/Ljubljana": return TimeZone.EUROPE_LJUBLJANA;
            case "Africa/Casablanca": return TimeZone.AFRICA_CASABLANCA;
            case "Europe/Dublin": return TimeZone.EUROPE_DUBLIN;
            case "Europe/Sofia": return TimeZone.EUROPE_SOFIA;
            case "Europe/Athens": return TimeZone.EUROPE_ATHENS;
            case "Europe/Vilnius": return TimeZone.EUROPE_VILNIUS;
            case "Europe/Riga": return TimeZone.EUROPE_RIGA;
            case "Europe/Skopje": return TimeZone.EUROPE_SKOPJE;
            case "Europe/Oslo": return TimeZone.EUROPE_OSLO;
            case "Europe/Bucharest": return TimeZone.EUROPE_BUCHAREST;
            case "Europe/Kiev": return TimeZone.EUROPE_KIEV;
            case "Europe/Istanbul": return TimeZone.EUROPE_ISTANBUL;
            case "Europe/Belgrade": return TimeZone.EUROPE_BELGRADE;
            case "Europe/Podgorica": return TimeZone.EUROPE_PODGORICA;
            case "Europe/Sarajevo": return TimeZone.EUROPE_SARAJEVO;
            case "Europe/Helsinki": return TimeZone.EUROPE_HELSINKI;
            case "Europe/Vaduz": return TimeZone.EUROPE_VADUZ;
            case "Europe/Tirane": return TimeZone.EUROPE_TIRANE;
            case "Europe/Malta": return TimeZone.EUROPE_MALTA;
            case "Europe/Chisinau": return TimeZone.EUROPE_CHISINAU;
            case "Europe/Tallinn": return TimeZone.EUROPE_TALLINN;
            case "Asia/Nicosia": return TimeZone.ASIA_NICOSIA;
            default: return null;
        }
    }

    /**
     * Mapeia uma string de grupo de fuso horário para o enum {@link TimeZoneGroup}.
     *
     * @param timeZoneGroup identificador do grupo de fuso horário
     * @return enum TimeZoneGroup correspondente ou null se não existir
     */
    private TimeZoneGroup getTimeZoneGroup(String timeZoneGroup) {
        if (timeZoneGroup == null) return null;

        switch (timeZoneGroup) {
            case "CET": return TimeZoneGroup.CET;
            case "EET": return TimeZoneGroup.EET;
            case "FET": return TimeZoneGroup.FET;
            case "WET/GMT": return TimeZoneGroup.WET_GMT;
            default: return null;
        }
    }
}
