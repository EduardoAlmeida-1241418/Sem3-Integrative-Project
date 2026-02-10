package pt.ipp.isep.dei.comparator;

import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.Comparator;

/**
 * Comparator for boxes using the FIFO (First In, First Out) principle.
 * <p>
 * Compares boxes by their received date and time. Boxes received earlier
 * are ordered before those received later.
 */
public class FifoBoxComparator implements Comparator<String> {

    /**
     * Compares two box identifiers according to their received date and time.
     * <p>
     * If the received dates are equal, the comparison falls back to received time.
     * If both are identical, box identifiers are compared lexicographically.
     *
     * @param idBox1 the first box identifier
     * @param idBox2 the second box identifier
     * @return a negative integer, zero, or a positive integer if the first box
     *         was received earlier, at the same time, or later than the second box
     * @throws IllegalArgumentException if one or both boxes are not found in the repository
     */
    @Override
    public int compare(String idBox1, String idBox2) {
        BoxRepository boxRepository = Repositories.getInstance().getBoxRepository();
        Box box1 = boxRepository.findById(idBox1);
        Box box2 = boxRepository.findById(idBox2);

        if (box1 == null || box2 == null) {
            throw new IllegalArgumentException("One or both boxes not found in repository.");
        }

        Date receivedDate1 = box1.getReceivedDate();
        Date receivedDate2 = box2.getReceivedDate();
        Time receivedTime1 = box1.getReceivedTime();
        Time receivedTime2 = box2.getReceivedTime();

        int dateComparison = receivedDate1.compareTo(receivedDate2);
        if (dateComparison != 0) {
            return dateComparison;
        }

        int timeComparison = receivedTime1.compareTo(receivedTime2);
        if (timeComparison != 0) {
            return timeComparison;
        }

        return idBox1.compareTo(idBox2);
    }
}
