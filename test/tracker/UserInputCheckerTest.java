package tracker;

import org.junit.Ignore;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tracker.utils.UserInputChecker;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserInputCheckerTest {

//    @Ignore
//    @ParameterizedTest(name= "{index} => testNameValidation({0}, {1}) == {2}")
//    @CsvSource({"J., false", "J-, true", "Stanisław, false", "陳 港 生, false", "John, true", "O'Neill, true", "Jean-Claude, true", "Jemison Van de Graaff, true"})
//    public void testNameValidation(String name, boolean expectedResult) {
//        boolean actualResult = UserInputChecker.isValidName(name);
//
//        assertEquals(expectedResult, actualResult);
//    }

//    @Ignore
//    @ParameterizedTest
//    @CsvSource({"john.doe_email.com, false", "@email.com, false", "email, false", "test@email., false", "jdoe@yahoo.com, true", "john_doe@gmail.com, true", "jc@google.it, true"})
//    public void testEmailValidation(String email, boolean expectedResult) {
//        boolean actualResult = UserInputChecker.isValidEmail(email);
//
//        assertEquals(expectedResult, actualResult);
//    }

}
