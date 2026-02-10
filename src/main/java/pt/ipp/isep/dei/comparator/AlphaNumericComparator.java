package pt.ipp.isep.dei.comparator;

import java.util.Comparator;

/**
 * Comparator for alphanumeric strings.
 * <p>
 * Compares strings by treating consecutive digits as numeric values rather than characters.
 * Example: "A2" comes before "A10".
 */
public class AlphaNumericComparator implements Comparator<String> {

    /**
     * Compares two alphanumeric strings.
     * <p>
     * Digits are compared numerically, and letters or symbols are compared lexicographically.
     *
     * @param s1 the first string to compare
     * @param s2 the second string to compare
     * @return a negative integer, zero, or a positive integer if the first string
     *         is less than, equal to, or greater than the second string
     */
    @Override
    public int compare(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;

        int i = 0, j = 0;
        int len1 = s1.length(), len2 = s2.length();

        while (i < len1 && j < len2) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(j);

            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                long num1 = 0;
                while (i < len1 && Character.isDigit(s1.charAt(i))) {
                    num1 = num1 * 10 + (s1.charAt(i) - '0');
                    i++;
                }

                long num2 = 0;
                while (j < len2 && Character.isDigit(s2.charAt(j))) {
                    num2 = num2 * 10 + (s2.charAt(j) - '0');
                    j++;
                }

                if (num1 != num2) {
                    return Long.compare(num1, num2);
                }

            } else {
                if (c1 != c2) {
                    return c1 - c2;
                }
                i++;
                j++;
            }
        }

        return (len1 - i) - (len2 - j);
    }
}
