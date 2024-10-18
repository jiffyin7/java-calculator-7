package calculator.stringAdder.numberParser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author : jiffyin7@gmail.com
 * @since : 24. 10. 17.
 */
class NumberParserTest {

  private NumberParser numberParser;

  @BeforeEach
  void setUp(){
    numberParser = new NumberParser();
  }

  @Test
  void getNumbers_defaultDelimiters_success() {
    String input = String.valueOf(Long.MAX_VALUE)+",1,2:3";
    String[] delimiters = {",", ":"};
    String[] expected = {String.valueOf(Long.MAX_VALUE), "1", "2", "3"};

    assertArrayEquals(expected, numberParser.getNumbers(input, delimiters));
  }

  @Test
  void getNumbers_customDelimiters_success() {
    String input = "//;\\n"+String.valueOf(Long.MAX_VALUE)+",2;3";
    String[] delimiters = {",", ":",";"};
    String[] expected = {String.valueOf(Long.MAX_VALUE), "2", "3"};

    assertArrayEquals(expected, numberParser.getNumbers(input, delimiters));
  }

  @Test
  void getNumbers_exceedLongRange_success() {
    String input = "//;\\n"+String.valueOf(Long.MAX_VALUE)+"9474747474723459237"+",2;3";
    String[] delimiters = {",", ":",";"};
    String[] expected = {String.valueOf(Long.MAX_VALUE)+"9474747474723459237", "2", "3"};

    assertArrayEquals(expected, numberParser.getNumbers(input, delimiters));
  }

  @Test
  void getNumbers_negativeNumber_fail() {
    String input = String.valueOf(Long.MAX_VALUE)+",-1,2,3";
    String[] delimiters = {",", ":","$"};

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      numberParser.getNumbers(input, delimiters);
    });

    String expectedMessage = "NumberParser: 음수는 허용되지 않습니다: -1";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getNumbers_zeroNumber_fail() {
    String input = String.valueOf(Long.MAX_VALUE)+",0,2,3";
    String[] delimiters = {",", ":","$"};

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      numberParser.getNumbers(input, delimiters);
    });

    String expectedMessage = "NumberParser: 0은 허용되지 않습니다: 0";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void getNumbers_DecimalNumber_fail() {
    String input = String.valueOf(Long.MAX_VALUE)+",3.5,2,3";
    String[] delimiters = {",", ":","$"};

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      numberParser.getNumbers(input, delimiters);
    });

    String expectedMessage = "NumberParser: 소수는 허용되지 않습니다: 3.5";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @ParameterizedTest
  @ValueSource(strings = {"1a,2,3", "1,2b,3", "1,2,3c"})
  void getNumbers_invalidNumberFormat_fail(String input) {
    String[] delimiters = {",", ":"};

    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        numberParser.getNumbers(input, delimiters)
    );
    assertTrue(exception.getMessage().contains("유효하지 않은 숫자 형식 입니다"));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidInputs")
  void testGetNumbersWithInvalidInput(String input, String[] delimiters) {
    assertThrows(IllegalArgumentException.class, () -> {
      numberParser.getNumbers(input, delimiters);
    });
  }

  private static Stream<Arguments> provideInvalidInputs() {
    return Stream.of(
        Arguments.of("1,2,-3,4", new String[]{",", ":"}),
        Arguments.of("//;\n1;-2;3", new String[]{",", ":", ";"}),
        Arguments.of("1:2:-3", new String[]{",", ":"})
    );
  }
}