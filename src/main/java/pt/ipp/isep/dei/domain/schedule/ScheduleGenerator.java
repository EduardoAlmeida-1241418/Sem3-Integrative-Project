package pt.ipp.isep.dei.domain.schedule;

import pt.ipp.isep.dei.data.memory.GeneralScheduleStoreInMemory;
import pt.ipp.isep.dei.data.memory.RailwayLineStoreInMemory;
import pt.ipp.isep.dei.domain.DateTime;
import pt.ipp.isep.dei.domain.Facility;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.TimeInterval;
import pt.ipp.isep.dei.domain.trackRelated.*;
import pt.ipp.isep.dei.domain.trainRelated.Train;
import pt.ipp.isep.dei.domain.trainRelated.TrainPhysics;
import pt.ipp.isep.dei.domain.transportationRelated.Freight;
import pt.ipp.isep.dei.domain.wagonRelated.Wagon;

import java.time.Duration;
import java.util.*;

public class ScheduleGenerator {

    private static final int SINGLE_TACK_NUMBER = 1;    // Number of tracks in a single track segment

    private GeneralSchedule newGeneralSchedule = new GeneralSchedule();

    private GeneralScheduleStoreInMemory generalScheduleStoreInMemory = new GeneralScheduleStoreInMemory();
    private RailwayLineStoreInMemory railwayLineStoreInMemory = new RailwayLineStoreInMemory();

    private PriorityQueue<ScheduleAction> scheduleActionPriorityQueue = new PriorityQueue<>();
    private Map<Train, List<TrackLocation>> trainTrackLocations = new HashMap<>();
    private Map<Train, Integer> trainPathIndex = new HashMap<>();                      // Representa a localização atual

    private List<Train> trainList = new ArrayList<>();

    public ScheduleGenerator(List<Train> trainList) {
        for (Train train: trainList){

            if (!train.isDispatched()) {
                this.trainList.add(train);
            }
        }
    }

    public GeneralSchedule generateNewSchedule(){
        // 1ª Copiar Schedules de Trains Dispatched
        copyScheduleDispatchedInfo();

        // 2º Gerar os Paths
        generatePaths();

        // 3ª Criar lista de eventos
        populateSchedulePriorityQueue();

        // 4º Criar os Schedules
        createSchedules();

        return newGeneralSchedule;
    }


    // ############################################
    // # 1ª Copiar Schedules de Trains Dispatched #
    // ############################################

    private void copyScheduleDispatchedInfo(){
        GeneralSchedule oldGeneralSchedule = generalScheduleStoreInMemory.findLatest() ;

        // Significa que este Schedule é novo
        if (oldGeneralSchedule == null){
            return;
        }

        for (TrainSchedule trainSchedule: oldGeneralSchedule.getScheduleForTrainList()){
            // Apenas Adiciona os eventos dos que estiverem já despatched
            if (!trainSchedule.getTrain().isDispatched()){
                continue;
            }

            for (ScheduleEvent event: trainSchedule.getScheduleEvents()){
                newGeneralSchedule.addEvent(new ScheduleEvent(event));
            }
        }
    }

    // #####################
    // # 2ª Gerar os Paths #
    // #####################

    private void generatePaths() {
        List<TrackLocation> trackLocationList = new ArrayList<>();

        for (Train train: trainList){

            if (train.isDispatched()){
                continue;
            }

            trackLocationList.clear();

            for (int i = 0; i < train.getRoute().getPath().getFacilities().size() - 1; i++) {
                Facility origin = train.getRoute().getPath().getFacilities().get(i);
                Facility destination = train.getRoute().getPath().getFacilities().get(i + 1);

                trackLocationList.add(origin);
                for(RailwayLine railwayLine: railwayLineStoreInMemory.findAll()){

                    // Skip se for a linha errada
                    if (!railwayLine.correctRailwayLine(origin, destination)){
                        continue;
                    }

                    // Adicionar conforme origem e destino
                    for (RailwayLineSegment trackLocation: railwayLine.getSegmentsInOrder(origin, destination)){

                        if (trackLocation.hasSiding()){
                            
                            divideSingleTrack(trackLocation, railwayLine.getDirection(origin,destination), trackLocationList);
                            
                            
                            continue;
                        }
                        trackLocationList.add(trackLocation);
                    }
                    
                    break;
                }

                // Juntar RailwayLines Que forem Single Track sem Sidings
                concatenateJointSingleTracks(trackLocationList);

                trackLocationList.add(destination);
            }
            trainTrackLocations.put(train, new ArrayList<>(trackLocationList));

            loadItemsForPathIndex(train);
        }
    }

    private void concatenateJointSingleTracks(List<TrackLocation> trackLocationList) {

        int i = 0;

        while (i < trackLocationList.size()) {

            if (trackLocationList.get(i) instanceof RailwayLineSegment segment
                    && segment.getNumberTracks() == 1
                    && segment.getSiding() == null) {

                List<RailwayLineSegment> jointSegments = new ArrayList<>();
                jointSegments.add(segment);

                int j = i + 1;

                while (j < trackLocationList.size()
                        && trackLocationList.get(j) instanceof RailwayLineSegment next
                        && next.getNumberTracks() == 1
                        && next.getSiding() == null) {

                    jointSegments.add(next);
                    j++;
                }

                if (jointSegments.size() > 1) {
                    JointRailwayLineSegments joint =
                            new JointRailwayLineSegments(jointSegments);

                    // remove os segmentos antigos
                    for (int k = 0; k < jointSegments.size(); k++) {
                        trackLocationList.remove(i);
                    }

                    // insere o conjunto
                    trackLocationList.add(i, joint);
                }

                i++; // avança após o bloco
            } else {
                i++;
            }
        }
    }


    private void divideSingleTrack(RailwayLineSegment trackLocation, boolean direction, List<TrackLocation> trackLocationList) {

        SegmentLineDivided first;
        SegmentLineDivided second;

        if (direction){ // correct direction
            first = new SegmentLineDivided(1, trackLocation, trackLocation.getSiding().getPosition());
            second = new SegmentLineDivided(2, trackLocation, trackLocation.getLength() - trackLocation.getSiding().getPosition() + trackLocation.getSiding().getLength());

        } else {        // Reversed
            first = new SegmentLineDivided(2, trackLocation, trackLocation.getLength() - trackLocation.getSiding().getPosition() + trackLocation.getSiding().getLength());
            second   = new SegmentLineDivided(1, trackLocation, trackLocation.getSiding().getPosition());

        }

        trackLocationList.add(first);
        // Duplicar os sidings para que seja possível movimentos dentro do siding
        trackLocationList.add(trackLocation.getSiding());
        trackLocationList.add(trackLocation.getSiding());
        trackLocationList.add(second);
    }

    private void loadItemsForPathIndex(Train train) {
        trainPathIndex.put(train, 1); // inicia na segunda position (end do primeiro caso)
    }

    // #############################
    // # 3ª Criar lista de eventos #
    // #############################

    private void populateSchedulePriorityQueue() {

        for (Train train: trainList){

            // Se ja estiver Dispatched -> Skip
            if (train.isDispatched()){
                continue;
            }

            DateTime dateTimeToUse = viewKickoffDate(train);

            // Adicionar evento de espera por Assemble


            if (!dateTimeToUse.equals(train.getDateTime())) {
                ScheduleEvent scheduleEvent = new ScheduleEvent(new TimeInterval(train.getDateTime(), dateTimeToUse), trainTrackLocations.get(train).getFirst(), trainTrackLocations.get(train).getFirst(), train, ScheduleEventType.WAITING_FOR_ASSEMBLE);
                newGeneralSchedule.addEvent(scheduleEvent);
            }


            scheduleActionPriorityQueue.add(new ScheduleAction(train, trainTrackLocations.get(train).getFirst(), trainTrackLocations.get(train).get(1), dateTimeToUse));
        }


    }

    private DateTime viewKickoffDate(Train train) {
        DateTime latestDateTime = train.getDateTime();

        // Verificar ultima data de Locomotivas
        for (Locomotive locomotive: train.getLocomotives()){

            for (TrainSchedule trainSchedule: newGeneralSchedule.getScheduleForTrainList()){
                if (trainSchedule.getTrain().getLocomotives().contains(locomotive)){
                    if (latestDateTime.compareTo(trainSchedule.getScheduleEvents().getLast().getTimeInterval().getFinalDateTime()) == -1){
                        latestDateTime = trainSchedule.getScheduleEvents().getLast().getTimeInterval().getFinalDateTime();
                    }
                }
            }
        }

        // Verificar ultima data de Wagons
        for (Freight freight : train.getRoute().getFreights()) {
            for (Wagon wagon : freight.getWagons()) {
                for (TrainSchedule trainSchedule : newGeneralSchedule.getScheduleForTrainList()) {
                    for (Freight f : trainSchedule.getTrain().getRoute().getFreights()) {
                        if (f.getWagons().contains(wagon)) {
                            DateTime dt = trainSchedule.getScheduleEvents()
                                    .getLast().getTimeInterval().getFinalDateTime();
                            if (latestDateTime.isBefore(dt)) {
                                latestDateTime = dt;
                            }
                        }
                    }
                }
            }
        }

        return latestDateTime;
    }

    // #########################
    // # 4ª Generate Schedules #
    // #########################

    private void createSchedules() {

        while (!scheduleActionPriorityQueue.isEmpty()){
            ScheduleAction scheduleAction = scheduleActionPriorityQueue.poll();
            createEvent(scheduleAction);

        }

    }

    private void createEvent(ScheduleAction scheduleAction) {

        // 4.1º Movement Station to Beginning Line Segment (instantaneous)
        if (scheduleAction.getStartPosition() instanceof Facility && (scheduleAction.getEndPosition() instanceof SegmentRelated)){

            movementStationSegment(scheduleAction);

            return;
        }

        // 4.2º Movement Segment to Segment (calculated time)
        if (scheduleAction.getStartPosition() instanceof SegmentRelated && scheduleAction.getEndPosition() instanceof SegmentRelated){

            movementSegmentSegment(scheduleAction);

            return;
        }

        // 4.3º Movement Segment Initial to Siding (Calculated Time)
        if (scheduleAction.getStartPosition() instanceof SegmentRelated && scheduleAction.getEndPosition() instanceof Siding){

            movementSegmentSiding(scheduleAction);

            return;
        }

        // 4.4º Movement Segment Initial to Station (Calculated Time)
        if (scheduleAction.getStartPosition() instanceof SegmentRelated && scheduleAction.getEndPosition() instanceof Facility){

            movementSegmentStation(scheduleAction);

            return;
        }

        // 4.5º Movement Siding to Siding (Calculated Time)
        if (scheduleAction.getStartPosition() instanceof Siding && scheduleAction.getEndPosition() instanceof Siding){

            movementSidingSiding(scheduleAction);

            return;
        }

        // 4.6º Movement Siding to Segment (Instantaneous)
        if (scheduleAction.getStartPosition() instanceof Siding && scheduleAction.getEndPosition() instanceof SegmentRelated){

            movementSidingSegment(scheduleAction);

            return;
        }
    }


    // 4.1º Movement Station to Beginning Line Segment (instantaneous)
    private void movementStationSegment(ScheduleAction scheduleAction) {

        SegmentRelated segment = (SegmentRelated) scheduleAction.getEndPosition();

        Duration timeNeeded = TrainPhysics.computeTravelTime(scheduleAction.getTrain(), segment);

        TimeInterval interval = new TimeInterval(scheduleAction.getDateTime(), scheduleAction.getDateTime().plusSeconds(timeNeeded.getSeconds()));

        interval = isSegmentOccupied(segment, interval, scheduleAction.getTrain(), scheduleAction);

        if (interval != null) {
            createDelayedEvent(interval, scheduleAction);
            return;
        }

        ScheduleEvent event = new ScheduleEvent( scheduleAction, scheduleAction.getDateTime(), ScheduleEventType.MOVEMENT_TO_SEGMENT);

        createEvents(event, scheduleAction);
    }

    private TimeInterval isSegmentOccupied(SegmentRelated segment, TimeInterval interval, Train currentTrain, ScheduleAction scheduleAction) {
        for (ScheduleEvent event : newGeneralSchedule.getAllEvents()) {

            // ignora o próprio comboio
            if (event.getTrain().equals(currentTrain)) {
                continue;
            }

            // só eventos de movimento no segmento
            if (event.getScheduleEventType() != ScheduleEventType.MOVEMENT_IN_SEGMENT) {
                continue;
            }

            // só eventos que ocupam segmento
            if (!(event.getStartPosition() instanceof SegmentRelated)) {
                continue;
            }

            // só se a linha for single
            if (segment.getNumberTracks() != SINGLE_TACK_NUMBER) {
                continue;
            }

            // mesmo recurso físico
            if (!event.getStartPosition().equals(segment)) {
                continue;
            }

            boolean oppositeDirections = event.getEndPosition().equals(scheduleAction.getStartPosition());
            if (!oppositeDirections){
                continue;
            }

            // overlap temporal
            if (event.getTimeInterval().overlaps(interval)) {
                return new TimeInterval(interval.getInitialDateTime(), event.getTimeInterval().getFinalDateTime());
            }
        }
        return null;
    }



    // 4.2º Movement Segment Initial to Beginning Segment Secondary (Calculated Time)
    private void movementSegmentSegment(ScheduleAction scheduleAction) {

        Duration timeNeeded = TrainPhysics.computeTravelTime(scheduleAction.getTrain(), (SegmentRelated) scheduleAction.getStartPosition());
        TimeInterval generatedTimeInterval = new TimeInterval(scheduleAction.getDateTime(), scheduleAction.getDateTime().plusSeconds(timeNeeded.getSeconds()));

        ScheduleEvent scheduleEvent = new ScheduleEvent(generatedTimeInterval, scheduleAction.getStartPosition(), scheduleAction.getEndPosition(), scheduleAction.getTrain(), ScheduleEventType.MOVEMENT_IN_SEGMENT);

        TimeInterval collisionTimeInterval = verifyFrontalCollision(scheduleEvent.getEndPosition(), scheduleEvent);

        // If no collisions are expected
        if (collisionTimeInterval == null){

            createEvents(scheduleEvent, scheduleAction);

            return;
        }

        // If collisions are expected
        createDelayedEvent(collisionTimeInterval, scheduleAction);

    }

    // 4.3º Movement Segment Initial to Siding (Calculated Time)
    private void movementSegmentSiding(ScheduleAction scheduleAction) {
        movementSegmentSegment(scheduleAction);
    }

    // 4.4º Movement Segment Initial to Station (Calculated Time)
    private void movementSegmentStation(ScheduleAction scheduleAction){
        movementSegmentSegment(scheduleAction);
    }

    // 4.5º Movement Siding to Siding (Calculated Time)
    private void movementSidingSiding(ScheduleAction scheduleAction) {
        SegmentLineDivided segmentLineDivided = (SegmentLineDivided) trainTrackLocations.get(scheduleAction.getTrain()).get(trainPathIndex.get(scheduleAction.getTrain()) + 1);
        RailwayLineSegment sidingClonedSegment = new RailwayLineSegment(segmentLineDivided, ((Siding) scheduleAction.getEndPosition()).getLength());

        Duration timeNeeded = TrainPhysics.computeTravelTime(scheduleAction.getTrain(), sidingClonedSegment);

        ScheduleEvent scheduleEvent = new ScheduleEvent(scheduleAction, scheduleAction.getDateTime().plusSeconds(timeNeeded.getSeconds()), ScheduleEventType.MOVEMENT_IN_SIDING);

        createEvents(scheduleEvent, scheduleAction);
    }

    // 4.6º Movement Siding to Segment (Instantaneous)
    private void movementSidingSegment(ScheduleAction scheduleAction) {

        Duration timeNeeded = TrainPhysics.computeTravelTime(scheduleAction.getTrain(), (SegmentRelated) scheduleAction.getEndPosition());

        ScheduleEvent scheduleEvent = new ScheduleEvent(scheduleAction, scheduleAction.getDateTime().plusSeconds(timeNeeded.getSeconds()), ScheduleEventType.IN_PROGRESS);

        TimeInterval timeInterval = verifyFrontalCollision(scheduleAction.getEndPosition(), scheduleEvent);
        if (timeInterval == null){
            scheduleEvent = new ScheduleEvent(scheduleAction, scheduleAction.getDateTime(), ScheduleEventType.MOVEMENT_TO_SEGMENT);
            createEvents(scheduleEvent, scheduleAction);
        }
        else{
            createDelayedEvent(timeInterval, scheduleAction);
        }
    }



    // Apoio
    private void createDelayedEvent(TimeInterval timeInterval, ScheduleAction scheduleAction){
        ScheduleEvent delayedEvent =  new ScheduleEvent(timeInterval, scheduleAction.getStartPosition(), scheduleAction.getEndPosition(), scheduleAction.getTrain(), ScheduleEventType.WAITING);

        newGeneralSchedule.addEvent(delayedEvent);
                                                                                                                                                            // Se tirar este "plusSecond(1)" o codigo parte
        ScheduleAction newScheduleAction = new ScheduleAction(scheduleAction.getTrain(), scheduleAction.getStartPosition(), scheduleAction.getEndPosition(), timeInterval.getFinalDateTime().plusSeconds(1));
        scheduleActionPriorityQueue.add(newScheduleAction);
    }

    // Apoio
    private void createEvents(ScheduleEvent scheduleEvent, ScheduleAction scheduleAction) {
        newGeneralSchedule.addEvent(scheduleEvent);

        TrackLocation nextPosition = findNextPosition(scheduleEvent.getTrain());

        // Fim do percurso
        if (nextPosition == null) {
            return;
        }

        ScheduleAction newScheduleAction = new ScheduleAction(scheduleAction.getTrain(), scheduleEvent.getEndPosition(), nextPosition, scheduleEvent.getTimeInterval().getFinalDateTime());
        scheduleActionPriorityQueue.add(newScheduleAction);
    }

    // Apoio
    private TrackLocation findNextPosition(Train train) {
        int nextPositionIndex = trainPathIndex.get(train) + 1;
        List<TrackLocation> path = trainTrackLocations.get(train);

        if (nextPositionIndex >= path.size()) {
            return null;
        }

        trainPathIndex.put(train, nextPositionIndex);
        return path.get(nextPositionIndex);
    }



    // ########################
    // # 5ª Analyze Collisions #
    // ########################

    private TimeInterval verifyFrontalCollision(TrackLocation movementPosition, ScheduleEvent givenScheduleEvent) {

        if (!(movementPosition instanceof SegmentRelated)) {
            return null;
        }

        if (((SegmentRelated) movementPosition).getNumberTracks() != SINGLE_TACK_NUMBER) {
            return null;
        }



        for (ScheduleEvent existing : newGeneralSchedule.findOverlaps(movementPosition, givenScheduleEvent.getTimeInterval())) {

            // ignora eventos do próprio comboio
            if (existing.getTrain().equals(givenScheduleEvent.getTrain())) {
                continue;
            }

            boolean oppositeDirections = ! existing.getEndPosition().equals(givenScheduleEvent.getStartPosition());

            if (oppositeDirections) {
                continue; // mesmo sentido → não é colisão
            }


            // QUALQUER overlap num single-track é conflito
            return new TimeInterval(
                    givenScheduleEvent.getTimeInterval().getInitialDateTime(),
                    existing.getTimeInterval().getFinalDateTime()
            );
        }

        return null;
    }

}
