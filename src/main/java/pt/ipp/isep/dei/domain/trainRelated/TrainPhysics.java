package pt.ipp.isep.dei.domain.trainRelated;

import pt.ipp.isep.dei.data.memory.LocomotiveStoreInMemory;
import pt.ipp.isep.dei.domain.trackRelated.RailwayLineSegment;
import pt.ipp.isep.dei.domain.trackRelated.SegmentRelated;

import java.time.Duration;
import java.util.List;

/**
 * Utility class responsible for train physics calculations.
 * Provides methods to compute allowed speed, travel time on segments
 * and total travel time along a path.
 */
public class TrainPhysics {

    /** Coefficient used to relate mass and speed */
    private static final double MASS_SPEED_COEFFICIENT = 0.0035;
    /** Conversion constant from hours to milliseconds */
    private static final long HOUR_TO_MILLIS = 3600000L;
    /** Number of meters per kilometer */
    private static final int METERS_PER_KM = 1000;

    /**
     * Private constructor to prevent instantiation.
     */
    private TrainPhysics() {}

    // ------------------------
    // Velocidade permitida no segmento
    // ------------------------

    /**
     * Computes the maximum allowed speed for a train on a given segment.
     * The allowed speed is the minimum between:
     * the segment speed limit, the train speed limit and the physics-based limit.
     *
     * @param train train to evaluate
     * @param segment railway segment
     * @return allowed speed in km/h
     */
    public static double computeAllowedSpeed(Train train, SegmentRelated segment) {
        double viaLimit   = segment.getSpeedLimit();         // km/h
        double trainLimit = train.getMaxAllowedSpeed();      // km/h
        double physics    = computeMassSpeedLimit(train);    // km/h
        return Math.min(viaLimit, Math.min(trainLimit, physics));
    }

    /**
     * Computes the maximum speed based on train power and total weight.
     *
     * @param train train to evaluate
     * @return physics-based speed limit in km/h
     */
    private static double computeMassSpeedLimit(Train train) {
        double totalWeight = train.getTotalWeigh();
        if (totalWeight <= 0) return train.getMaxAllowedSpeed(); // fallback
        return train.getTotalPower() / (totalWeight * MASS_SPEED_COEFFICIENT);
    }

    /**
     * Returns the maximum acceleration among all locomotives of the train.
     *
     * @param train train to evaluate
     * @return maximum acceleration in m/s²
     */
    private static double getMaxAcceleration(Train train) {
        return train.getLocomotives().stream()
                .mapToDouble(l -> l.getLocomotiveModel().getAcceleration())
                .max()
                .orElse(0);
    }

    // ------------------------
    // Tempo para percorrer um segmento
    // ------------------------

    /**
     * Computes the travel time for a train to traverse a given segment.
     * Considers acceleration when available, otherwise assumes constant speed.
     *
     * @param train train to evaluate
     * @param segment railway segment
     * @return travel time as a Duration
     */
    public static Duration computeTravelTime(Train train, SegmentRelated segment) {
        double vMax = computeAllowedSpeed(train, segment); // km/h
        double distance = segment.getLength();             // metros
        double accel = getMaxAcceleration(train);         // m/s²

        if (accel <= 0) {
            // Sem aceleração conhecida → velocidade constante
            double speedMS = vMax * 1000 / 3600; // km/h → m/s
            long timeMillis = (long) ((distance / speedMS) * 1000);
            return Duration.ofMillis(timeMillis);
        }

        // Velocidade máxima em m/s
        double vMaxMS = vMax * 1000 / 3600;
        double sToMaxSpeed = 0.5 * vMaxMS * vMaxMS / accel;

        if (sToMaxSpeed >= distance) {
            // Não atinge vMax no segmento
            double timeSec = Math.sqrt(2 * distance / accel);
            return Duration.ofMillis((long) (timeSec * 1000));
        } else {
            // Acelera até vMax, depois segue constante
            double tAccel = vMaxMS / accel;
            double sConst = distance - sToMaxSpeed;
            double tConst = sConst / vMaxMS;
            return Duration.ofMillis((long) ((tAccel + tConst) * 1000));
        }
    }

    // ------------------------
    // Tempo total para todo o path
    // ------------------------

    /**
     * Computes the total travel time for a train along a sequence of segments.
     *
     * @param train train to evaluate
     * @param path list of railway line segments
     * @return total travel time as a Duration
     */
    public static Duration computeTotalTravelTime(Train train, List<RailwayLineSegment> path) {
        Duration total = Duration.ZERO;
        for (RailwayLineSegment segment : path) {
            total = total.plus(computeTravelTime(train, segment));
        }
        return total;
    }

}
