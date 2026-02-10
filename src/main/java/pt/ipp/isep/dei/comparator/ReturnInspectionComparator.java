package pt.ipp.isep.dei.comparator;

import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Return;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.sprint1.ReturnRepository;

import java.util.Comparator;

/**
 * Comparator for return inspections.
 * <p>
 * Orders return identifiers by inspection date and time in descending order (most recent first).
 * If both are identical, ties are resolved by comparing return IDs lexicographically.
 */
public class ReturnInspectionComparator implements Comparator<String> {

    /**
     * Compares two return identifiers based on their inspection timestamps.
     * <p>
     * Comparison order:
     * <ol>
     *   <li>Date (descending)</li>
     *   <li>Time (descending)</li>
     *   <li>Return ID (lexicographical)</li>
     * </ol>
     *
     * @param idReturn1 the first return identifier
     * @param idReturn2 the second return identifier
     * @return a negative integer, zero, or a positive integer depending on which return
     *         is more recent or lexicographically smaller
     * @throws IllegalArgumentException if one or both return records are not found
     */
    @Override
    public int compare(String idReturn1, String idReturn2) {
        ReturnRepository returnRepository = Repositories.getInstance().getReturnRepository();
        Return return1 = returnRepository.findById(idReturn1);
        Return return2 = returnRepository.findById(idReturn2);

        if (return1 == null || return2 == null) {
            throw new IllegalArgumentException("One or both returns not found in repository.");
        }

        Date date1 = return1.getDateStamp();
        Date date2 = return2.getDateStamp();
        Time time1 = return1.getTimeStamp();
        Time time2 = return2.getTimeStamp();

        // DESC order (most recent first)
        int dateComparison = date2.compareTo(date1);
        if (dateComparison != 0) return dateComparison;

        int timeComparison = time2.compareTo(time1);
        if (timeComparison != 0) return timeComparison;

        // Tie-breaker by ID
        return idReturn1.compareTo(idReturn2);
    }
}
