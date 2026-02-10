package pt.ipp.isep.dei.controller.qualityOperator;

import pt.ipp.isep.dei.data.repository.*;
import pt.ipp.isep.dei.data.repository.sprint1.BayRepository;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.sprint1.ReturnRepository;
import pt.ipp.isep.dei.data.repository.sprint1.WarehouseRepository;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.ui.console.UIUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the quarantine inspection workflow
 * performed by the Quality Operator.
 * <p>
 * This class automates the classification and handling of returned goods based
 * on their return reason (restock, discard, or manual review). It creates boxes
 * for restocked items, updates warehouse storage, removes processed returns,
 * and logs all related actions for auditing purposes.
 */
public class QuarantineReturnController {

    private ReturnRepository returnRepo;
    private BoxRepository boxRepo;
    private BayRepository bayRepo;
    private WarehouseRepository whRepo;
    private LogRepository logRepo;

    /**
     * Constructs a new controller and initializes all required repositories.
     */
    public QuarantineReturnController() {
        Repositories repos = Repositories.getInstance();
        this.returnRepo = repos.getReturnRepository();
        this.boxRepo = repos.getBoxRepository();
        this.bayRepo = repos.getBayRepository();
        this.whRepo = repos.getWarehouseRepository();
        this.logRepo = repos.getLogRepository();
    }

    /**
     * Pads a given value with spaces up to a specified width, ensuring
     * left-aligned text formatting (used primarily for audit log entries).
     *
     * @param v     the value to pad
     * @param width the desired total width of the formatted string
     * @return the padded string value
     */
    private static String pad(Object v, int width) {
        return String.format("%-" + width + "s", v == null ? "-" : v.toString());
    }

    /**
     * Retrieves all return records currently under quarantine, sorted
     * by date or predefined priority.
     *
     * @return a list of quarantined {@link Return} objects
     */
    public List<Return> listQuarantine() {
        return returnRepo.findAllSorted();
    }

    /**
     * Automatically inspects a return item and determines the appropriate action
     * based on its {@link ReturnReason}.
     * <ul>
     *     <li>{@code CUSTOMER_REMORSE} → Item is restocked.</li>
     *     <li>{@code DAMAGED} or {@code EXPIRED} → Item is discarded.</li>
     *     <li>{@code CYCLE_COUNT} → Requires manual operator decision.</li>
     * </ul>
     *
     * @param returnId the ID of the return to inspect
     * @return a string indicating the inspection result:
     *         "RESTOCKED", "DISCARDED", "DISCARDED_FAILED_RESTOCK", or "NEEDS_DECISION"
     */
    public String inspectAuto(String returnId) {
        Return r = require(returnRepo.findById(returnId), "Return not found: " + returnId);
        ReturnReason reason = r.getReturnReason();

        switch (reason) {
            case CUSTOMER_REMORSE:
                boolean success = restock(returnId, r.getQuantity());
                if (success) {
                    return "RESTOCKED";
                }
                return "DISCARDED_FAILED_RESTOCK";
            case DAMAGED:
            case EXPIRED:
                discard(returnId);
                return "DISCARDED";
            case CYCLE_COUNT:
            default:
                return "NEEDS_DECISION";
        }
    }

    /**
     * Restocks a returned item into the warehouse. A new {@link Box} is created,
     * assigned to a compatible bay, and the corresponding return record is removed.
     * <p>
     * If no suitable bay is found, or the box insertion fails, the operation is logged
     * as a failed restock attempt.
     *
     * @param returnId the ID of the return to restock
     * @param quantity the quantity of items to restock
     * @return {@code true} if restocking was successful; {@code false} otherwise
     */
    public boolean restock(String returnId, int quantity) {
        Return r = require(returnRepo.findById(returnId), "Return not found: " + returnId);

        String id = r.getReturnId();
        String sku = r.getSkuItem();
        int discardedQty = r.getQuantity() - quantity;

        LocalDateTime now = LocalDateTime.now();
        Date nowD = new Date(now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        Time nowT = new Time(now.getHour(), now.getMinute(), now.getSecond());

        String newBoxId = "RET-" + id;
        Box newBox = new Box(newBoxId, sku, quantity, r.getExpiryDate(), nowD, nowT);
        boxRepo.add(newBox);

        String targetBayId = findCompatibleBayIdForBox(newBoxId);

        if (targetBayId != null) {
            Bay target = bayRepo.findByKey(targetBayId);
            boolean inserted = target != null && target.addBox(newBoxId, RoleType.QUALITY_OPERATOR);
            if (inserted) {

                String quantityStr;
                if (discardedQty > 0) {
                    quantityStr = String.format("qtyRestocked=%s | qtyDiscarded=%s", pad(quantity, 2), pad(discardedQty, 2));
                } else {
                    quantityStr = String.format("qty=%s", pad(quantity, 2));
                }

                UIUtils.addLog(
                        String.format(
                                "%s %s | returnId=%s | sku=%s | action=%s | %s | boxId=%s | allocatedBay=%s",
                                nowD, nowT,
                                pad(id, 8),
                                pad(sku, 7),
                                pad("Restocked", 9),
                                pad(quantityStr, 2),
                                pad(newBoxId, 12),
                                pad(targetBayId, 6)
                        ),
                        LogType.INSPECTION, RoleType.QUALITY_OPERATOR
                );
                returnRepo.remove(id);
                return true;
            } else {
                UIUtils.addLog(
                        String.format(
                                "%s %s | returnId=%s | sku=%s | action=%s | qty=%s | boxId=%s | allocation=%s",
                                nowD, nowT,
                                pad(id, 8),
                                pad(sku, 7),
                                pad("Restocked", 9),
                                pad(quantity, 2),
                                pad(newBoxId, 12),
                                "FAILED_ADD_BOX"
                        ),
                        LogType.INSPECTION, RoleType.QUALITY_OPERATOR
                );
                returnRepo.remove(id);
                return false;
            }
        } else {
            UIUtils.addLog(
                    String.format(
                            "%s %s | returnId=%s | sku=%s | action=%s | qty=%s | boxId=%s | allocation=%s",
                            nowD, nowT,
                            pad(id, 8),
                            pad(sku, 7),
                            pad("Restocked", 9),
                            pad(quantity, 2),
                            pad(newBoxId, 12),
                            "FAILED_NO_FREE_BAY"
                    ),
                    LogType.INSPECTION, RoleType.QUALITY_OPERATOR
            );
            returnRepo.remove(id);
            return false;
        }
    }

    /**
     * Discards a returned item that cannot be restocked.
     * <p>
     * Removes the return record and logs the discard action.
     *
     * @param returnId the ID of the return to discard
     */
    public void discard(String returnId) {
        Return r = require(returnRepo.findById(returnId), "Return not found: " + returnId);

        String id = r.getReturnId();
        String sku = r.getSkuItem();

        LocalDateTime now = LocalDateTime.now();
        Date nowD = new Date(now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        Time nowT = new Time(now.getHour(), now.getMinute(), now.getSecond());

        UIUtils.addLog(
                String.format(
                        "%s %s | returnId=%s | sku=%s | action=%s",
                        nowD, nowT,
                        pad(id, 8),
                        pad(sku, 7),
                        "Discarded"
                ),
                LogType.INSPECTION, RoleType.QUALITY_OPERATOR
        );

        returnRepo.remove(id);
    }

    /**
     * Retrieves a list of formatted inspection log entries
     * associated with the quarantine return process.
     *
     * @return a list of audit log messages
     */
    public List<String> listAuditLines() {
        List<String> out = new ArrayList<>();
        for (Log log : logRepo.findAll()) {
            if (log.getLogType() == LogType.INSPECTION) {
                out.add(log.getMessage());
            }
        }
        return out;
    }

    /**
     * Finds a compatible storage bay for a given box ID.
     * <p>
     * The method searches through all warehouses and returns the
     * first available bay that can accommodate the specified box.
     *
     * @param boxId the ID of the box to allocate
     * @return the ID of a suitable bay, or {@code null} if none are available
     */
    private String findCompatibleBayIdForBox(String boxId) {
        var warehouses = whRepo.findAll();
        if (warehouses == null || warehouses.isEmpty()) return null;
        var wh = warehouses.iterator().next();
        return wh.getFreeBayWithItem(boxId);
    }

    /**
     * Returns the quantity of items associated with a specific return.
     *
     * @param returnId the ID of the return
     * @return the quantity of returned items
     */
    public int getReturnQuantity(String returnId) {
        Return r = require(returnRepo.findById(returnId), "Return not found: " + returnId);
        return r.getQuantity();
    }

    /**
     * Ensures that a given object is not {@code null}.
     * <p>
     * If the object is {@code null}, an {@link IllegalArgumentException}
     * is thrown with the provided message.
     *
     * @param v   the object to validate
     * @param msg the exception message if validation fails
     * @param <T> the type of the validated object
     * @return the validated non-null object
     * @throws IllegalArgumentException if {@code v} is {@code null}
     */
    private static <T> T require(T v, String msg) {
        if (v == null) throw new IllegalArgumentException(msg);
        return v;
    }
}