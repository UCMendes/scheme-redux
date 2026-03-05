package com.umendes.schemeredux.lexer;

import org.jparsec.Tokens;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemeLexerTest {

    SchemeLexer testLexer = new SchemeLexer();

    @Test
    void SCA_DATUM_COMMENT_PREFIX() {
        Tokens.Fragment test_fragment = (Tokens.Fragment) testLexer.s_datum_comment_prefix.parse("#;");
        assertEquals("#;", test_fragment.text());
    }
}