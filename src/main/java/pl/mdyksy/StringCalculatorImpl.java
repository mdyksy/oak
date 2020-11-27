package pl.mdyksy;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;

class StringCalculatorImpl implements StringCalculator {

    private static final int MAX_VALUE = 1000;
    private static final String DEFAULT_DELIMITER = "[,]|[\\n]";
    private static final String CUSTOM_DELIMITER_PREFIX = "//";
    private static final String NEW_LINE_SIGN = "\n";

    private final DelimiterService delimiterService;

    public StringCalculatorImpl() {
        this.delimiterService = new DelimiterService();
    }

    @Override
    public int add(final String input) {
        if (input == null || input.isBlank()) {
            return 0;
        }

        final List<Integer> numbers = getAllNumbers(input);

        final Optional<String> negativeNumbers = getNegatives(numbers);
        if (negativeNumbers.isPresent()) {
            throw new NegativeNumberException("Negative numbers are not allowed: " + negativeNumbers.get());
        }

        return numbers.stream()
                .mapToInt(number -> number)
                .sum();
    }

    private List<Integer> getAllNumbers(final String input) {
        final boolean containsCustomDelimiter = input.startsWith(CUSTOM_DELIMITER_PREFIX);
        final String delimiter = getDelimiter(containsCustomDelimiter, input);
        final String numbers = getNumbers(containsCustomDelimiter, input);

        return Arrays
                .stream(numbers.split(delimiter))
                .map(Integer::parseInt)
                .filter(number -> number < MAX_VALUE)
                .collect(toUnmodifiableList());
    }

    private String getDelimiter(final boolean isCustom, final String input) {
        return isCustom ? delimiterService.prepareCustomDelimiter(input) : DEFAULT_DELIMITER;
    }

    private String getNumbers(final boolean isCustom, final String input) {
        return isCustom ? StringUtils.substringAfter(input, NEW_LINE_SIGN) : input;
    }

    private Optional<String> getNegatives(final List<Integer> numbers) {
        return numbers.stream()
                .filter(number -> number < 0)
                .map(String::valueOf)
                .reduce((number1, number2) -> String.join(", ", number1, number2));
    }
}
