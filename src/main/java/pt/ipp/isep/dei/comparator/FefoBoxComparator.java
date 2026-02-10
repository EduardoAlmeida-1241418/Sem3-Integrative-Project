package pt.ipp.isep.dei.comparator;

import pt.ipp.isep.dei.domain.BoxRelated.Box;
import pt.ipp.isep.dei.data.repository.sprint1.BoxRepository;
import pt.ipp.isep.dei.data.repository.Repositories;

import java.util.Comparator;

/**
 * Comparator for boxes using the FEFO (First Expired, First Out) principle.
 * <p>
 * Compares boxes based on their expiry dates. Boxes with earlier expiry dates
 * are ordered before those with later ones.
 */
public class FefoBoxComparator implements Comparator<String> {

    /**
     * Compares two box identifiers according to their expiry dates.
     * <p>
     * Boxes without expiry dates are considered to have lower priority.
     *
     * @param idBox1 the first box identifier
     * @param idBox2 the second box identifier
     * @return a negative integer, zero, or a positive integer if the first box
     *         expires earlier, at the same time, or later than the second box
     * @throws IllegalArgumentException if any box ID does not exist
     */
    @Override
    public int compare(String idBox1, String idBox2) {
        BoxRepository boxRepository = Repositories.getInstance().getBoxRepository();
        Box box1 = boxRepository.findById(idBox1);
        Box box2 = boxRepository.findById(idBox2);

        if (box1 == null || box2 == null) {
            throw new IllegalArgumentException("One or both box IDs do not exist: " + idBox1 + ", " + idBox2);
        }

        if (box1.getExpiryDate() != null && box2.getExpiryDate() != null) {
            int result = box1.getExpiryDate().compareTo(box2.getExpiryDate());
            if (result != 0) return result;
        } else if (box1.getExpiryDate() == null && box2.getExpiryDate() != null) {
            return 1;
        } else if (box1.getExpiryDate() != null && box2.getExpiryDate() == null) {
            return -1;
        }
        return 0;
    }
}
