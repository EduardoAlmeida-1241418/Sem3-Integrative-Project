package ESINF.sprint2.USEI06;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadDataESINFControllerCSVTest {

    private void cleanRepositories() {
        Repositories.getInstance().getCountryRepository().clear();
        Repositories.getInstance().getStationEsinf2Repository().clear();
    }

    @Test
    public void testCsv1ValidSingle() {
        /*
        cleanRepositories();

        LoadDataESINFController controller = new LoadDataESINFController();
        controller.setDATA_SET_SPRINT2_CSV_ESINF_PATH("src/test/java/ESINF/sprint2/USEI06/csvTests/csv1_invalid.csv");
        controller.run();

        List<Country> countries =
                List.copyOf(Repositories.getInstance().getCountryRepository().findAll());
        List<StationEsinf> stations =
                List.copyOf(Repositories.getInstance().getStationEsinfRepository().getStations().values());

        assertEquals(0, countries.size());
        assertEquals(0, stations.size());

         */
    }

    @Test
    public void testCsv2MultipleValid() {
        /*
        cleanRepositories();

        LoadDataESINFController controller = new LoadDataESINFController();
        controller.setDATA_SET_SPRINT2_CSV_ESINF_PATH("src/test/java/ESINF/sprint2/USEI06/csvTests/csv2_multiple_valid.csv");
        controller.run();

        List<Country> countries =
                List.copyOf(Repositories.getInstance().getCountryRepository().findAll());
        List<StationEsinf> stations =
                List.copyOf(Repositories.getInstance().getStationEsinfRepository().getStations().values());

        assertEquals(4, countries.size());
        as
        sertEquals(4, stations.size());
         */
    }

    @Test
    public void testCsv3InvalidLatitude() {
        /*
        cleanRepositories();

        LoadDataESINFController controller = new LoadDataESINFController();
        controller.setDATA_SET_SPRINT2_CSV_ESINF_PATH("src/test/java/ESINF/sprint2/USEI06/csvTests/csv3_invalid_latitude.csv");
        controller.run();

        List<Country> countries =
                List.copyOf(Repositories.getInstance().getCountryRepository().findAll());
        List<StationEsinf> stations =
                List.copyOf(Repositories.getInstance().getStationEsinfRepository().getStations().values());

        assertEquals(0, countries.size());
        assertEquals(0, stations.size());

         */
    }

    @Test
    public void testCsv4InvalidTimezone() {
        /*
        cleanRepositories();

        LoadDataESINFController controller = new LoadDataESINFController();
        controller.setDATA_SET_SPRINT2_CSV_ESINF_PATH("src/test/java/ESINF/sprint2/USEI06/csvTests/csv4_invalid_timezone.csv");
        controller.run();

        List<Country> countries =
                List.copyOf(Repositories.getInstance().getCountryRepository().findAll());
        List<StationEsinf> stations =
                List.copyOf(Repositories.getInstance().getStationEsinfRepository().getStations().values());

        assertEquals(0, countries.size());
        assertEquals(0, stations.size());

         */
    }

    @Test
    public void testCsv5MixedValidInvalid() {
        /*
        cleanRepositories();

        LoadDataESINFController controller = new LoadDataESINFController();
        controller.setDATA_SET_SPRINT2_CSV_ESINF_PATH("src/test/java/ESINF/sprint2/USEI06/csvTests/csv5_mixed.csv");
        controller.run();

        List<Country> countries =
                List.copyOf(Repositories.getInstance().getCountryRepository().findAll());
        List<StationEsinf> stations =
                List.copyOf(Repositories.getInstance().getStationEsinfRepository().getStations().values());

        assertEquals(2, countries.size());
        assertEquals(2, stations.size());

         */
    }
}
