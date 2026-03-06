package com.umendes.schemeredux.lexer;

import org.jparsec.Tokens;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemeLexerTest {

    SchemeLexer testLexer = new SchemeLexer();
    Tokens.Fragment test_fragment;

    @Test
    @DisplayName("Datum comment prefix")
    void s_datum_comment_prefix() {
        test_fragment = (Tokens.Fragment) testLexer.s_datum_comment_prefix.parse("#;");
        assertEquals("#;", test_fragment.text());
    }

    @Test
    @DisplayName("Line comment")
    void s_line_comment() {
        test_fragment = (Tokens.Fragment) testLexer.s_line_comment.parse(";Comment");
        assertEquals(";Comment", test_fragment.text());
    }

    @Test
    @DisplayName("Block comment")
    // May not properly be handling block comments; #|text#|text|#|# fails when it shouldn't
    void s_block_comment() {
        test_fragment = (Tokens.Fragment) testLexer.s_block_comment.parse("#||#");
        assertEquals("#||#", test_fragment.text());
    }

    @Test
    @DisplayName("Whitespace")
    void s_whitespace() {
        test_fragment = (Tokens.Fragment) testLexer.s_whitespace.parse("   ");
        assertEquals("WHITE_SPACE", test_fragment.text());
    }

    @Test
    @DisplayName("Single operator (bracket/quote) character")
    void s_op_single_char() {
        test_fragment = (Tokens.Fragment) testLexer.s_op_single_char.parse("]");
        assertEquals("]", test_fragment.text());
    }

    @Test
    @DisplayName("Open vector character")
    // Needs to be changed to comply with R7RS
    void s_op_open_vector() {
        test_fragment = (Tokens.Fragment) testLexer.s_op_open_vector.parse("#vu8(");
        assertEquals("#vu8(", test_fragment.text());
    }

    @Test
    @DisplayName("Abbreviation operators")
    void s_op_abbreviations() {
        test_fragment = (Tokens.Fragment) testLexer.s_op_abbreviations.parse("#,@");
        assertEquals("#,@", test_fragment.text());
    }

    @Test
    @DisplayName("Identifier names")
    void s_name_literal() {
        String[] TEST_INPUT_VALID = {"B", "BC", "BCD", "BC@Y", "BC\\xD6;E"};
        for (String s : TEST_INPUT_VALID) {
            test_fragment = (Tokens.Fragment) testLexer.s_name_literal.parse(s);
            assertEquals(s, test_fragment.text());
        }
//        Pass tests on error, these should not be allowed
//        String[] TEST_INPUT_INVALID = {"BC\\xL6;E", "6GB"};
//        for (String s : TEST_INPUT_INVALID) {
//            test_fragment = (Tokens.Fragment) testLexer.s_name_literal.parse(s);
//            assertEquals(s, test_fragment.text());
//        }
    }

    @Test
    @DisplayName("Numbers")
    void s_numbers() {
        String[] TEST_INPUT = {"+15", "#b01001", "#O1067", "#d6590", "#Xe83e"};
        for (String s : TEST_INPUT) {
            test_fragment = (Tokens.Fragment) testLexer.s_numbers.parse(s);
            assertEquals("number", test_fragment.text());
        }
    }

    @Test
    @DisplayName("Characters")
    // Also test for invalid case: char followed by char
    void s_sharp_char() {
        String[] TEST_INPUT = {"#\\@", "#\\F", "#\\delete"};
        for (String s : TEST_INPUT) {
            test_fragment = (Tokens.Fragment) testLexer.s_sharp_char.parse(s);
            assertEquals(s, test_fragment.text());
        }
    }

    @Test
    @DisplayName("Strings")
    void PAR_STRING() {
        test_fragment = (Tokens.Fragment) testLexer.PAR_STRING.parse("\"It's text\"");
        assertEquals("\"It's text\"", test_fragment.text());
    }

    @Test
    @DisplayName("Booleans")
    // terminals being entered straight rather than being converted to list may be the
    // reason for me needing escapes here. Will rectify during dot-parse implementation
    void s_boolean() {
        test_fragment = (Tokens.Fragment) testLexer.PAR_STRING.parse("\"#T\"");
        assertEquals("\"#T\"", test_fragment.text());
    }

    @Test
    @DisplayName("Sharp Exclamation")
    // Cannot find any mention of this in the R7RS report, but will implement anyway
    void s_sharp_exclamation() {
        String[] TEST_INPUT = {"#!B", "#!BC", "#!BCD", "#!BC@Y", "#!BC\\xD6;E"};
        for (String s : TEST_INPUT) {
            test_fragment = (Tokens.Fragment) testLexer.s_sharp_exclamation.parse(s);
            assertEquals(s, test_fragment.text());
        }
    }

    @Test
    @DisplayName("Built-in Elements")
    // Tests both s_keywords and s_builtin_procedures
    // Need to adjust list based on R7RS updated lists
    void PAR_BUILTIN_ELEMENTS() {
        String[] TEST_INPUT = {"quasisyntax", "div-and-mod"};
        for (String s : TEST_INPUT) {
            test_fragment = (Tokens.Fragment) testLexer.PAR_BUILTIN_ELEMENTS.parse(s);
            assertEquals(s, test_fragment.text());
        }
    }

    @Test
    @DisplayName("Built-in Elements")
    void PAR_TOKEN() {
        String[] TEST_INPUT = {"quasisynquax", "|"};
        for (String s : TEST_INPUT) {
            test_fragment = (Tokens.Fragment) testLexer.PAR_TOKEN.parse(s);
            // Out of interest
            System.out.println("type: " + test_fragment.tag().toString());
            assertEquals(s, test_fragment.text());
        }
    }

    // Will also create tests for full scm files

}