package pl.mdyksy;

import org.apache.commons.lang3.StringUtils;

class DelimiterService {

    private static final String CUSTOM_DELIMITER_PREFIX = "//";
    private static final String NEW_LINE_SIGN = "\n";
    private static final String MULTIPLE_CHAR_DELIMITER_PREFIX = "[";
    private static final String MULTIPLE_CHAR_DELIMITER_SUFFIX = "]";
    private static final String REGEX_OR_SIGN = "|";

    String prepareCustomDelimiter(final String input) {
        final String delimitersPart = StringUtils.substringBetween(input, CUSTOM_DELIMITER_PREFIX, NEW_LINE_SIGN);
        final DelimiterHelper delimiterHelper = new DelimiterHelper();
        if (delimiterHasMoreCharsThan1(delimitersPart)) {
            final String[] delimiters = StringUtils.substringsBetween(delimitersPart, MULTIPLE_CHAR_DELIMITER_PREFIX, MULTIPLE_CHAR_DELIMITER_SUFFIX);
            for (final String delimiter : delimiters) {
                if (delimiter.length() > 1) {
                    for (final String delimiterPart : delimiter.split("")) {
                        delimiterHelper.add(delimiterPart);
                    }
                } else {
                    delimiterHelper.add(delimiter);
                }
                delimiterHelper.addOR();
            }
            return delimiterHelper.getDelimiter();
        } else {
            return delimitersPart;
        }
    }

    private boolean delimiterHasMoreCharsThan1(final String delimiter) {
        return delimiter.startsWith(MULTIPLE_CHAR_DELIMITER_PREFIX) && delimiter.endsWith(MULTIPLE_CHAR_DELIMITER_SUFFIX);
    }

    private static class DelimiterHelper {
        private final StringBuilder delimiter;

        private DelimiterHelper() {
            this.delimiter = new StringBuilder();
        }

        private void add(final String delimiter) {
            this.delimiter
                    .append(MULTIPLE_CHAR_DELIMITER_PREFIX)
                    .append(delimiter)
                    .append(MULTIPLE_CHAR_DELIMITER_SUFFIX);
        }

        private void addOR() {
            this.delimiter.append(REGEX_OR_SIGN);
        }

        private String getDelimiter() {
            return StringUtils.removeEnd(delimiter.toString(), REGEX_OR_SIGN);
        }
    }
}
