package pt.ipp.isep.dei.domain;

/**
 * Enumeration representing the possible reasons for a product return
 * in the quarantine inspection process (USEI05).
 *
 * <p>Each value has:
 * <ul>
 *   <li>An internal numeric identifier ({@code id}) — useful if the reason
 *       needs to be persisted or mapped from external data.</li>
 * </ul>
 * </p>
 *
 * <p>Expected mappings:
 * <ul>
 *   <li>{@code "customer-remorse"} → {@link #CUSTOMER_REMORSE}</li>
 *   <li>{@code "damaged"} → {@link #DAMAGED}</li>
 *   <li>{@code "expired"} → {@link #EXPIRED}</li>
 *   <li>{@code "cycle-count"} → {@link #CYCLE_COUNT}</li>
 * </ul>
 * </p>
 */
public enum ReturnReason {

    /** Product returned in good condition (e.g., unwanted or wrong item). */
    CUSTOMER_REMORSE(1, "customer-remorse"),

    /** Product is physically broken or unusable. */
    DAMAGED(2, "damaged"),

    /** Product has passed its expiry date. */
    EXPIRED(3, "expired"),

    /** Discrepancy found during a stock audit (needs recheck). */
    CYCLE_COUNT(4, "cycle-count");

    private final int id;
    private final String reason;

    ReturnReason(int id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    /** @return the numeric identifier for this return reason */
    public int getId() {
        return id;
    }

    /** @return the lowercase string label for this return reason */
    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return reason;
    }
}
