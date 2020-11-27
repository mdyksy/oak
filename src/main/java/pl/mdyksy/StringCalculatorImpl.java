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

    @Override
    public int add(final String numbers) {
        if (numbers == null || numbers.isBlank()) {
            return 0;
        }

        String delimiter = DEFAULT_DELIMITER;
        String numbersPart = numbers;

        if (numbers.startsWith(CUSTOM_DELIMITER_PREFIX)) {
            delimiter = prepareCustomDelimiter(numbers);
            numbersPart = StringUtils.substringAfter(numbers, NEW_LINE_SIGN);
        }

        final List<Integer> intNumbers = Arrays
                .stream(numbersPart.split(delimiter))
                .map(Integer::parseInt)
                .collect(toUnmodifiableList());

        final Optional<String> negativeNumbers = getNegatives(intNumbers);
        if (negativeNumbers.isPresent()) {
            throw new NegativeNumberException("Negative numbers are not allowed: " + negativeNumbers.get());
        }

        return intNumbers.stream()
                .mapToInt(number -> number)
                .filter(number -> number < MAX_VALUE)
                .sum();
    }

    private String prepareCustomDelimiter(final String numbers) {
        String delimiter = StringUtils.substringBetween(numbers, CUSTOM_DELIMITER_PREFIX, NEW_LINE_SIGN);
        if (delimiter.startsWith("[") && delimiter.endsWith("]")) {
            final String[] delimiteres = StringUtils.substringsBetween(delimiter, "[", "]");
            delimiter = "";
            for (final String d : delimiteres) {
                if (d.length() > 1) {
                    for (final String dPart : d.split("")) {
                        delimiter = delimiter.concat("[" + dPart + "]");
                    }
                } else {
                    delimiter = delimiter.concat("[" + d + "]");
                }
                delimiter = delimiter.concat("|");
            }
            delimiter = StringUtils.removeEnd(delimiter, "|");
        }
        return delimiter;
    }

    private Optional<String> getNegatives(final List<Integer> numbers) {
        return numbers.stream()
                .filter(value -> value < 0)
                .map(String::valueOf)
                .reduce((number1, number2) -> String.join(", ", number1, number2));
    }
}
