package pt.ipp.isep.dei.data.store;

import pt.ipp.isep.dei.data.config.DatabaseConnection;
import pt.ipp.isep.dei.data.memory.RailwayLineStoreInMemory;
import pt.ipp.isep.dei.data.memory.RailwayLineSegmentStoreInMemory;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLine;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;

import java.util.List;

public class RailwayTopologyBuilder {

    public static void build(DatabaseConnection db) {

        RailwayLineStoreInMemory lineMem = new RailwayLineStoreInMemory();
        RailwayLineSegmentStoreInMemory segMem = new RailwayLineSegmentStoreInMemory();

        RailwayLineStore lineStore = new RailwayLineStore();
        RailwayLineSegmentStore segStore = new RailwayLineSegmentStore();

        List<RailwayLine> lines = lineMem.findAll();
        List<RailwayLineSegment> segments = segMem.findAll();

        for (RailwayLine line : lines) {
            List<Integer> segIds = lineStore.findSegmentIdsForLine(db, line.getId());
            for (int id : segIds) {
                RailwayLineSegment seg = segMem.findById(db, String.valueOf(id));
                line.addSegment(seg,-1);
            }
        }

        for (RailwayLineSegment seg : segments) {
            List<Integer> lineIds = segStore.findLineIdsForSegment(db, seg.getId());
            for (int id : lineIds) {
                RailwayLine line = lineMem.findById(db, String.valueOf(id));
                seg.addRailwayLine(line);
            }
        }
    }
}
