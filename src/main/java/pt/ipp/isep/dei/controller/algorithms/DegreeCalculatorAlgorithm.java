package pt.ipp.isep.dei.controller.algorithms;

import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint3.RailwayLineEsinfRepository;
import pt.ipp.isep.dei.domain.ESINF.RailwayLineEsinf;
import pt.ipp.isep.dei.domain.ESINF.StationEsinf;

public class DegreeCalculatorAlgorithm {

    private RailwayLineEsinfRepository railwayLineEsinfRepository;

    public DegreeCalculatorAlgorithm(){
        this.railwayLineEsinfRepository = Repositories.getInstance().getRailwayLineEsinfRepository();
    }

    // Complexity O(E)
    // E = edges = number of lines
    public int calculateDegree(StationEsinf calculatingFacility){
        int degreeCounter = 0;

        for (RailwayLineEsinf line : railwayLineEsinfRepository.findAll()) {
            if (line.getArrivalStation().getId() == calculatingFacility.getId() || line.getDepartureStation().getId() == calculatingFacility.getId()) {
                degreeCounter++;
            }
        }

        return degreeCounter;
    }
}
