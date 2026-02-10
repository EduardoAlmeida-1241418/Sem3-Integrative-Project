package pt.ipp.isep.dei.domain;

/**
 * Enumeration of the major locomotive types by propulsion method.
 * <p>
 * These reflect common classifications used in rail industry literature:
 * steam, diesel (internal-combustion), and electric. :contentReference[oaicite:0]{index=0}
 * </p>
 */
public enum LocomotiveType {
    /** Powered exclusively by onboard electric supply (e.g., from overhead wire or third rail). */
    ELECTRIC,

    /** Powered by a diesel engine (often driving generators for traction motors). */
    DIESEL,

    /** Powered by steam (historical propulsion method). */
    STEAM
}
