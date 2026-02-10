package pt.ipp.isep.dei.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Utility class providing common operations and helper methods.
 * Contains static methods for various operations like string manipulation,
 * list operations, mathematical calculations and data validation.
 */
public class Utils {

    /**
     * Clamps a value between a minimum and maximum range.
     *
     * @param value the value to clamp
     * @param min   the minimum allowed value
     * @param max   the maximum allowed value
     * @return the clamped value between min and max
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Checks if a number is even.
     *
     * @param number the number to check
     * @return true if the number is even, false otherwise
     */
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    /**
     * Checks if a number is odd.
     *
     * @param number the number to check
     * @return true if the number is odd, false otherwise
     */
    public static boolean isOdd(int number) {
        return number % 2 != 0;
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a list is null or empty.
     *
     * @param list the list to check
     * @return true if the list is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Reverses a string.
     *
     * @param str the string to reverse
     * @return the reversed string, or null if input is null
     */
    public static String reverse(String str) {
        if (str == null) return null;
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Capitalizes the first character of a string.
     *
     * @param str the string to capitalize
     * @return the capitalized string, or original string if null or empty
     */
    public static String capitalize(String str) {
        if (isNullOrEmpty(str)) return str;
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }

    /**
     * Truncates a string to a maximum length.
     *
     * @param str       the string to truncate
     * @param maxLength the maximum length allowed
     * @return the truncated string, or null if input is null
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) return null;
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }

    /**
     * Repeats a string n times.
     *
     * @param str the string to repeat
     * @param n   number of times to repeat
     * @return the repeated string, or null if input is null
     */
    public static String repeat(String str, int n) {
        if (str == null) return null;
        return str.repeat(Math.max(0, n));
    }

    /**
     * Swaps two elements in a list.
     *
     * @param list the list containing elements to swap
     * @param i    index of first element
     * @param j    index of second element
     * @param <T>  type of elements in the list
     * @throws IllegalArgumentException if list is null
     */
    public static <T> void swap(List<T> list, int i, int j) {
        if (list == null) throw new IllegalArgumentException("List cannot be null");
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     * Creates a shallow copy of a list.
     *
     * @param list the list to copy
     * @param <T>  type of elements in the list
     * @return a new list containing the same elements, or null if input is null
     */
    public static <T> List<T> copyList(List<T> list) {
        if (list == null) return null;
        return new ArrayList<>(list);
    }

    /**
     * Calculates the sum of all integers in a list.
     *
     * @param list the list of integers
     * @return the sum of all numbers, or 0 if list is null
     */
    public static int sum(List<Integer> list) {
        if (list == null) return 0;
        int total = 0;
        for (int i : list) total += i;
        return total;
    }

    /**
     * Calculates the average of integers in a list.
     *
     * @param list the list of integers
     * @return the average value, or 0 if list is null or empty
     */
    public static double average(List<Integer> list) {
        if (list == null || list.isEmpty()) return 0;
        return (double) sum(list) / list.size();
    }

    /**
     * Checks if a list contains a specific element.
     *
     * @param list    the list to check
     * @param element the element to look for
     * @param <T>     type of elements in the list
     * @return true if element is found, false otherwise or if list is null
     */
    public static <T> boolean contains(List<T> list, T element) {
        return list != null && list.contains(element);
    }

    /**
     * Finds the maximum element in a list of comparable elements.
     *
     * @param list the list to search
     * @param <T>  type of elements that must implement Comparable
     * @return the maximum element, or null if list is null or empty
     */
    public static <T extends Comparable<T>> T max(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        T max = list.get(0);
        for (T item : list) if (item.compareTo(max) > 0) max = item;
        return max;
    }

    /**
     * Finds the minimum element in a list of comparable elements.
     *
     * @param list the list to search
     * @param <T>  type of elements that must implement Comparable
     * @return the minimum element, or null if list is null or empty
     */
    public static <T extends Comparable<T>> T min(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        T min = list.get(0);
        for (T item : list) if (item.compareTo(min) < 0) min = item;
        return min;
    }

    /**
     * Reverses the order of elements in a list.
     *
     * @param list the list to reverse
     * @param <T>  type of elements in the list
     */
    public static <T> void reverseList(List<T> list) {
        if (list != null) Collections.reverse(list);
    }

    /**
     * Sorts a list of comparable elements.
     *
     * @param list the list to sort
     * @param <T>  type of elements in the list
     */
    public static <T> void sortList(List<T> list) {
        if (list != null && !list.isEmpty() && list.get(0) instanceof Comparable) {
            Collections.sort((List) list);
        }
    }

    /**
     * Joins a list of strings into a single string with a separator.
     *
     * @param list      the list of strings to join
     * @param separator the separator to use between strings
     * @return the joined string, or empty string if list is null or empty
     */
    public static String join(List<String> list, String separator) {
        if (list == null || list.isEmpty()) return "";
        return String.join(separator, list);
    }

    /**
     * Checks if a string is a palindrome.
     *
     * @param str the string to check
     * @return true if the string is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String str) {
        if (str == null) return false;
        String clean = str.replaceAll("\\s+","").toLowerCase();
        return clean.equals(reverse(clean));
    }

    /**
     * Checks if a string starts with a given prefix, case-insensitively.
     *
     * @param str    the string to check
     * @param prefix the prefix to look for
     * @return true if the string starts with the prefix, false otherwise
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) return false;
        return str.toLowerCase().startsWith(prefix.toLowerCase());
    }

    /**
     * Checks if a string ends with a given suffix, case-insensitively.
     *
     * @param str    the string to check
     * @param suffix the suffix to look for
     * @return true if the string ends with the suffix, false otherwise
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) return false;
        return str.toLowerCase().endsWith(suffix.toLowerCase());
    }

    /**
     * Removes all whitespace characters from a string.
     *
     * @param str the string to process
     * @return the string without whitespace, or null if input is null
     */
    public static String removeWhitespace(String str) {
        if (str == null) return null;
        return str.replaceAll("\\s+", "");
    }

    /**
     * Calculates the sum of an array of integers.
     *
     * @param arr the array of integers
     * @return the sum of all numbers, or 0 if array is null
     */
    public static int sumArray(int[] arr) {
        if (arr == null) return 0;
        int total = 0;
        for (int i : arr) total += i;
        return total;
    }

    /**
     * Calculates the average of an array of integers.
     *
     * @param arr the array of integers
     * @return the average value, or 0 if array is null or empty
     */
    public static double averageArray(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        return (double) sumArray(arr) / arr.length;
    }

    /**
     * Finds the maximum value in an array of integers.
     *
     * @param arr the array to search
     * @return the maximum value
     * @throws IllegalArgumentException if array is null or empty
     */
    public static int maxArray(int[] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Array is empty");
        int max = arr[0];
        for (int i : arr) if (i > max) max = i;
        return max;
    }

    /**
     * Finds the minimum value in an array of integers.
     *
     * @param arr the array to search
     * @return the minimum value
     * @throws IllegalArgumentException if array is null or empty
     */
    public static int minArray(int[] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Array is empty");
        int min = arr[0];
        for (int i : arr) if (i < min) min = i;
        return min;
    }
}
