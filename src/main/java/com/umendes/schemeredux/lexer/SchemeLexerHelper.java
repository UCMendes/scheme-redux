package com.umendes.schemeredux.lexer;

import com.google.common.labs.parse.Parser;
import com.google.mu.util.CharPredicate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.google.common.labs.parse.Parser.*;

// Functions/Parsers created to assist SchemeLexer
// May include code taken from jparsec

public class SchemeLexerHelper {

    // Check for one matching string out of a list
    // The order here matters, longer words should be placed earlier
    public static Parser<String> multiword (List<String> terminals) {
        if (terminals.size() == 2) {
            return Parser.anyOf(word(terminals.getFirst()), word(terminals.getLast()));
        } else {
            return Parser.anyOf(word(terminals.getFirst()), multiword(terminals.subList(1, terminals.size())));
        }
    }

    // Look for one character out of a given string
    // Created just to reduce repeated typing
    public static Parser<?> oneOf (String chars, String name) {
        return Parser.single(CharPredicate.anyOf(chars), name);
    }

    // Next few Parsers are ports from jparsec's Patterns/Parsers classes
    // A to F or 0 to 9
    public static final CharPredicate HEX = CharPredicate.range('a', 'f').orRange('A', 'F')
            .orRange('0', '9');

    public static final Parser<?> DEC_INTEGER = oneOf("0123456789", "1 to 9").then(
            digits().zeroOrMore());

    public static final Parser<?> DECIMAL = anyOf(single(CharPredicate.range('0', '9'), "digit")
                    .then(string("."))
                    .then(digits().zeroOrMore()),
            string(".")
                    .then(digits()));

    public static final Parser<?> SCIENTIFIC_NOTATION = DECIMAL
            .then(single(CharPredicate.anyOf("eE"), "plus or minus")
                    .then(single(CharPredicate.anyOf("+-"), "plus or minus").optional()).then(digits()));

    // Adapted from jparsec
    // Sort array of strings by the longer string first
    // Current function sorts as if there are no equal elements
    private static final Comparator<String> LONGER_STRING_FIRST = (a, b) -> (b.length() - a.length() < 0 ? -1 : 1);
    public static List<String> terminalSort(String[] names) {
        Arrays.sort(names, LONGER_STRING_FIRST);
        return List.of(names);
    }
}
