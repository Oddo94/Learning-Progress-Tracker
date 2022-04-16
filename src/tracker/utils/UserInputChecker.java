package tracker.utils;
import tracker.model.Student;

import java.util.Map;
import java.util.Objects;
import java.util.regex.*;

public class UserInputChecker {

    public static boolean isValidName(String name) {
        if(name == null || "".equals(name) || name.length() <= 1) {
            return false;
        }

        if(name.contains("-'") || name.contains("'-")) {
            return false;
        }

        Pattern specialCharactersConcatenation = Pattern.compile("[^\\w]{2,}");
        Matcher specialMatcher = specialCharactersConcatenation.matcher(name);

        Pattern namePattern = Pattern.compile("(?![^\\w])^[A-Za-z\\-'\\s]*$(?<![^\\w])");
        Matcher generalMatcher = namePattern.matcher(name);




        return generalMatcher.matches() && !specialMatcher.find();
    }

    public static boolean isValidEmail(String email) {
        if(email == null || "".equals(email)) {
            return false;
        }

        Pattern emailPattern = Pattern.compile("^[a-zA-z._\\-\\d]+@{1}\\w+\\.\\w+$");
        Matcher matcher = emailPattern.matcher(email);

        return matcher.matches();
    }

    public static boolean isAvailableEmail(String email, Map<String, Student> userRepository) {
        if(email == null || "".equals(email) || userRepository == null) {
            return false;
        }

        for (Map.Entry<String, Student> currentEntry : userRepository.entrySet()) {
            Student currentStudent = currentEntry.getValue();
            if (Objects.equals(email, currentStudent.getEmail())) {
                return false;
            }
        }

        return true;
    }

    public static boolean existsStudent(String uniqueIdentifier, Map<String, Student> userRepository) {
        if(uniqueIdentifier == null || userRepository == null) {
            return false;
        }

        Student student = userRepository.get(uniqueIdentifier);

        return student != null ? true : false;
    }

    public static boolean containsIllegalCharacters(String[] input) {
        if (input == null || input.length == 0) {
            return true;
        }

        for(String element : input) {
            try {
                int result = Integer.parseInt(element);
            } catch (NumberFormatException ex) {
                return true;
            }
        }
        return false;
    }
}
