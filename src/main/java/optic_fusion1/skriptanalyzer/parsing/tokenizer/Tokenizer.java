package optic_fusion1.skriptanalyzer.parsing.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Implement proper support for things like lists {something} or {something.somethingelse}
public class Tokenizer {

    private static final List<Object[]> spec = new ArrayList<>();

    static {
        // Whitespace:
        spec.add(new Object[]{Pattern.compile("^\\s+"), null});

        // Comments:
        // Skip comments:
        spec.add(new Object[]{Pattern.compile("^#.*"), null});

        // Symbols, delimiters:
        spec.add(new Object[]{Pattern.compile("^;"), ';'});
        spec.add(new Object[]{Pattern.compile("^\\{"), "{"});
        spec.add(new Object[]{Pattern.compile("^\\}"), "}"});
        spec.add(new Object[]{Pattern.compile("^\\("), "("});
        spec.add(new Object[]{Pattern.compile("^\\)"), ")"});
        spec.add(new Object[]{Pattern.compile("^,"), ","});
        spec.add(new Object[]{Pattern.compile("^\\."), "."});
        spec.add(new Object[]{Pattern.compile("^\\["), "["});
        spec.add(new Object[]{Pattern.compile("^\\]"), "]"});
        spec.add(new Object[]{Pattern.compile("^\\:"), ":"});
        spec.add(new Object[]{Pattern.compile("^/"), "/"});
        spec.add(new Object[]{Pattern.compile("^%"), "%"});
        spec.add(new Object[]{Pattern.compile("^'"), "'"});

        // Keywords:
        // TODO: Better handle commands
        spec.add(new Object[]{Pattern.compile("^\\bcommand\\b"), "command"});
        spec.add(new Object[]{Pattern.compile("^\\bIP-ban\\b"), "ip-ban"});
        // TODO: Implement full set of keywords

        // Numbers:
        spec.add(new Object[]{Pattern.compile("^\\d+"), "NUMBER"});

        // Identifiers:
        spec.add(new Object[]{Pattern.compile("^\\w+"), "IDENTIFIER"});

        // Equality operators: ==, !=
        spec.add(new Object[]{Pattern.compile("^[=!]="), "EQUALITY_OPERATOR"});

        // Assignment operators: =, *=, /=, +=, -=,
        spec.add(new Object[]{Pattern.compile("^="), "SIMPLE_ASSIGN"});
        spec.add(new Object[]{Pattern.compile("^[*/+-]="), "COMPLEX_ASSIGN"});

        // Math operators: +, -, *, /
        spec.add(new Object[]{Pattern.compile("^[+\\-]"), "ADDITIVE_OPERATOR"});
        spec.add(new Object[]{Pattern.compile("^[*/]"), "MULTIPLICATIVE_OPERATOR"});

        // Relational operators: >, >=, <, <=
        spec.add(new Object[]{Pattern.compile("^[><]=?"), "RELATIONAL_OPERATOR"});

        // Logical Operators: &&, ||
        spec.add(new Object[]{Pattern.compile("^&&"), "LOGICAL_AND"});
        spec.add(new Object[]{Pattern.compile("^\\|\\|"), "LOGICAL_OR"});
        spec.add(new Object[]{Pattern.compile("^!"), "LOGICAL_NOT"});

        // Strings:
        spec.add(new Object[]{Pattern.compile("^\"[^\"]*\""), "STRING"});
        // TODO: Re-implement this
//        spec.add(new Object[]{Pattern.compile("^'[^']*'"), "STRING"});
    }

    private String string;
    private int cursor;

    public void init(String inputString) {
        this.string = inputString;
        this.cursor = 0;
    }

    public boolean isEOF() {
        return this.cursor == this.string.length();
    }

    public boolean hasMoreTokens() {
        return this.cursor < this.string.length();
    }

    public Token getNextToken() {
        if (!this.hasMoreTokens()) {
            return null;
        }

        String remainingString = this.string.substring(this.cursor);

        for (Object[] entry : spec) {
            Pattern pattern = (Pattern) entry[0];
            Object tokenType = entry[1];

            String tokenValue = match(pattern, remainingString);

            if (tokenValue == null) {
                continue;
            }

            if (tokenType == null) {
                return getNextToken();
            }
            return new Token((String) tokenType, tokenValue);
        }
        System.out.println("Unexpected token " + remainingString.charAt(0));
        return null;
    }

    private String match(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String matched = matcher.group(0);
            this.cursor += matched.length();
            return matched;
        }
        return null;
    }

    public class Token {

        private String type;
        private String value;

        public Token(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

    }

}
