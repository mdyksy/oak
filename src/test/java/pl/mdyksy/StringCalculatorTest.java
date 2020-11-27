package pl.mdyksy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class StringCalculatorTest {

    private StringCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new StringCalculatorImpl();
    }

    @DisplayName("when input is empty, null or blank then should return 0")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "      ")
    void emptyInput(final String numbers) {
        final int result = calculator.add(numbers);

        assertThat(result).isZero();
    }

    @DisplayName("when input is single number then should return it")
    @Test
    void singleArgument() {
        final int result = calculator.add("5");

        assertThat(result).isEqualTo(5);
    }

    @DisplayName("when there are 2 and more input numbers then should return sum")
    @Test
    void twoArguments() {
        final int result = calculator.add("5,6,8");

        assertThat(result).isEqualTo(19);
    }

    @DisplayName("when there are mixed \n and , delimiters then should return sum")
    @Test
    void differentDelimiters() {
        final int result = calculator.add("5,6\n1");

        assertThat(result).isEqualTo(12);
    }

    @DisplayName("when input starts with //[delimiter]\n then should treat it as delimiter")
    @Test
    void customDelimiter() {
        final int result = calculator.add("//;\n5;6");

        assertThat(result).isEqualTo(11);
    }

    @DisplayName("when input number is negative then should throw exception with those numbers")
    @Test
    void negativeNumbers() {
        final NegativeNumberException exception = catchThrowableOfType(() -> calculator.add("-5,6,-9"),
                NegativeNumberException.class);

        assertThat(exception).hasMessage("Negative numbers are not allowed: -5, -9");
    }

    @DisplayName("when input contains numbers greater than 1000 then should ignore those ones")
    @Test
    void thousandIgnored() {
        final int result = calculator.add("5,1000,1001,999");

        assertThat(result).isEqualTo(1004);
    }

    @DisplayName("when input starts with //[d2]\n then should treat it as delimiter with more than 1 sign")
    @Test
    void customDelimiterLength() {
        final int result = calculator.add("//[...]\n5...8");

        assertThat(result).isEqualTo(13);
    }

    @DisplayName("when input starts with //[d1][d2]\n then should treat those as delimiters")
    @Test
    void customDelimitersNumber() {
        final int result = calculator.add("//[,][.]\n5,6.8");

        assertThat(result).isEqualTo(19);
    }

    @DisplayName("when input starts with //[d1d1][d2d2]\n then should treat those as delimiters")
    @Test
    void customDelimitersLength() {
        final int result = calculator.add("//[,,,][.]\n5,,,6.8");

        assertThat(result).isEqualTo(19);
    }
}
