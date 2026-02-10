package pt.ipp.isep.dei.controller.acountRelated;

import pt.ipp.isep.dei.domain.User;
import pt.ipp.isep.dei.data.repository.Repositories;
import pt.ipp.isep.dei.data.repository.UserRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Controller responsible for user registration operations.
 * <p>
 * Handles email and password validation, account existence checks,
 * user creation, and persistence to a CSV file.
 */
public class RegisterController {

    /** Minimum password length requirement. */
    private static final int MIN_NUMBER_OF_DIGITS = 8;

    /** Repository for managing user entities. */
    private UserRepository userRepository;

    /** File path for storing user data in CSV format. */
    private static final String FILE_PATH = "data/userData.csv";

    /**
     * Default constructor that initializes the user repository.
     */
    public RegisterController() {
        userRepository = Repositories.getInstance().getUserRepository();
    }

    /**
     * Verifies if an email address follows a valid format.
     *
     * @param mail the email address to verify
     * @return true if the email is valid, false otherwise
     */
    public boolean verifyMail(String mail) {
        if (mail == null || mail.isEmpty()) {
            return false;
        }

        if (mail.contains(" ")) {
            return false;
        }

        int atIndex = mail.indexOf('@');
        if (atIndex < 1 || atIndex != mail.lastIndexOf('@')) {
            return false;
        }

        String localPart = mail.substring(0, atIndex);
        String domainPart = mail.substring(atIndex + 1);

        if (localPart.isEmpty() || domainPart.isEmpty()) {
            return false;
        }

        if (!domainPart.contains(".")) {
            return false;
        }

        if (mail.startsWith(".") || mail.endsWith(".")) {
            return false;
        }

        return true;
    }

    /**
     * Checks if an account with the given email already exists.
     *
     * @param mail the email to verify
     * @return true if the account exists, false otherwise
     */
    public boolean verifyIsAccountAlreadyExists(String mail) {
        return userRepository.exists(mail);
    }

    /**
     * Verifies if a password has the required minimum length.
     *
     * @param password the password to verify
     * @return true if the password meets the length requirement, false otherwise
     */
    public boolean verifyPasswordDigitNumber(String password) {
        if (password == null) return false;
        return password.length() >= MIN_NUMBER_OF_DIGITS;
    }

    /**
     * Verifies if a password includes at least one uppercase letter.
     *
     * @param password the password to verify
     * @return true if the password contains an uppercase letter, false otherwise
     */
    public boolean verifyPasswordIncludesCapitalLetter(String password) {
        if (password == null) return false;
        return password.matches(".*[A-Z].*");
    }

    /**
     * Verifies if a password contains at least one numeric character.
     *
     * @param password the password to verify
     * @return true if the password contains a number, false otherwise
     */
    public boolean verifyOneNumber(String password) {
        if (password == null) return false;
        return password.matches(".*[0-9].*");
    }

    /**
     * Verifies if a password includes at least one special character.
     *
     * @param password the password to verify
     * @return true if the password contains a special character, false otherwise
     */
    public boolean verifySpecialCharacter(String password) {
        if (password == null) return false;
        return password.matches(".*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/\\\\|`~].*");
    }

    /**
     * Creates a new user and stores it in the repository and CSV file.
     *
     * @param text the email of the new user
     * @param text1 the password of the new user
     */
    public void createUser(String text, String text1) {
        int id = userRepository.count() + 1;
        User newUser = new User(id, text, text1);
        userRepository.add(newUser);
        saveUserToCSV(newUser);
    }

    /**
     * Saves a user record to the CSV file. If the file does not exist, creates it with headers.
     *
     * @param user the user to be saved
     */
    private void saveUserToCSV(User user) {
        boolean fileExists = Files.exists(Paths.get(FILE_PATH));

        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            if (!fileExists) {
                pw.println("ID,Mail,Password,Admin,Picker,Planner,QualityOperator,TerminalOperator,TrafficDispatcher,WarehousePlanner,DataEngineer");
            }

            pw.println(user.getId() + "," + user.getEmail() + "," + user.getPassword() + ",0,0,0,0,0,0,0,0");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
