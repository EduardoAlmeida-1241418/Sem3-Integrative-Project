package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Enumeration that represents supported time zones.
 * Each time zone is identified by its standard zone ID string.
 */
public enum TimeZone {

    EUROPE_PARIS("Europe/Paris"),
    EUROPE_ZURICH("Europe/Zurich"),
    EUROPE_BERLIN("Europe/Berlin"),
    EUROPE_BRUSSELS("Europe/Brussels"),
    EUROPE_MADRID("Europe/Madrid"),
    EUROPE_ROME("Europe/Rome"),
    EUROPE_ANDORRA("Europe/Andorra"),
    EUROPE_LONDON("Europe/London"),
    EUROPE_AMSTERDAM("Europe/Amsterdam"),
    EUROPE_VIENNA("Europe/Vienna"),
    EUROPE_LUXEMBOURG("Europe/Luxembourg"),
    EUROPE_LISBON("Europe/Lisbon"),
    EUROPE_WARSAW("Europe/Warsaw"),
    EUROPE_MOSCOW("Europe/Moscow"),
    EUROPE_MINSK("Europe/Minsk"),
    EUROPE_BUDAPEST("Europe/Budapest"),
    EUROPE_PRAGUE("Europe/Prague"),
    EUROPE_BRATISLAVA("Europe/Bratislava"),
    EUROPE_ZAGREB("Europe/Zagreb"),
    EUROPE_COPENHAGEN("Europe/Copenhagen"),
    EUROPE_STOCKHOLM("Europe/Stockholm"),
    EUROPE_LJUBLJANA("Europe/Ljubljana"),
    AFRICA_CASABLANCA("Africa/Casablanca"),
    EUROPE_DUBLIN("Europe/Dublin"),
    EUROPE_SOFIA("Europe/Sofia"),
    EUROPE_ATHENS("Europe/Athens"),
    EUROPE_VILNIUS("Europe/Vilnius"),
    EUROPE_RIGA("Europe/Riga"),
    EUROPE_SKOPJE("Europe/Skopje"),
    EUROPE_OSLO("Europe/Oslo"),
    EUROPE_BUCHAREST("Europe/Bucharest"),
    EUROPE_KIEV("Europe/Kiev"),
    EUROPE_ISTANBUL("Europe/Istanbul"),
    EUROPE_BELGRADE("Europe/Belgrade"),
    EUROPE_PODGORICA("Europe/Podgorica"),
    EUROPE_SARAJEVO("Europe/Sarajevo"),
    EUROPE_HELSINKI("Europe/Helsinki"),
    EUROPE_VADUZ("Europe/Vaduz"),
    EUROPE_TIRANE("Europe/Tirane"),
    EUROPE_MALTA("Europe/Malta"),
    EUROPE_CHISINAU("Europe/Chisinau"),
    EUROPE_TALLINN("Europe/Tallinn"),
    ASIA_NICOSIA("Asia/Nicosia");

    /** Standard zone identifier string */
    private final String zoneId;

    /**
     * Constructs a time zone with the given zone ID.
     *
     * @param zoneId standard time zone identifier
     */
    TimeZone(String zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * Returns the zone ID string.
     *
     * @return zone ID
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * Returns a list with all supported time zone identifiers.
     *
     * @return list of time zone IDs
     */
    public static List<String> getAllTimeZones() {
        return List.of(
                AFRICA_CASABLANCA.zoneId,
                ASIA_NICOSIA.zoneId,
                EUROPE_AMSTERDAM.zoneId,
                EUROPE_ANDORRA.zoneId,
                EUROPE_ATHENS.zoneId,
                EUROPE_BELGRADE.zoneId,
                EUROPE_BERLIN.zoneId,
                EUROPE_BRATISLAVA.zoneId,
                EUROPE_BRUSSELS.zoneId,
                EUROPE_BUCHAREST.zoneId,
                EUROPE_BUDAPEST.zoneId,
                EUROPE_CHISINAU.zoneId,
                EUROPE_COPENHAGEN.zoneId,
                EUROPE_DUBLIN.zoneId,
                EUROPE_HELSINKI.zoneId,
                EUROPE_ISTANBUL.zoneId,
                EUROPE_KIEV.zoneId,
                EUROPE_LISBON.zoneId,
                EUROPE_LJUBLJANA.zoneId,
                EUROPE_LONDON.zoneId,
                EUROPE_LUXEMBOURG.zoneId,
                EUROPE_MADRID.zoneId,
                EUROPE_MALTA.zoneId,
                EUROPE_MINSK.zoneId,
                EUROPE_MOSCOW.zoneId,
                EUROPE_OSLO.zoneId,
                EUROPE_PARIS.zoneId,
                EUROPE_PODGORICA.zoneId,
                EUROPE_PRAGUE.zoneId,
                EUROPE_RIGA.zoneId,
                EUROPE_ROME.zoneId,
                EUROPE_SARAJEVO.zoneId,
                EUROPE_SKOPJE.zoneId,
                EUROPE_SOFIA.zoneId,
                EUROPE_STOCKHOLM.zoneId,
                EUROPE_TALLINN.zoneId,
                EUROPE_TIRANE.zoneId,
                EUROPE_VADUZ.zoneId,
                EUROPE_VIENNA.zoneId,
                EUROPE_VILNIUS.zoneId,
                EUROPE_WARSAW.zoneId,
                EUROPE_ZAGREB.zoneId,
                EUROPE_ZURICH.zoneId
        );
    }
}
