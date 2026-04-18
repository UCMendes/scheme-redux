package com.umendes.schemeredux.lexer;


import com.google.common.labs.parse.Parser;
import com.google.mu.util.CharPredicate;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;

// dot parse doesn't have token functionality yet(?) so this stays
import org.jetbrains.annotations.NotNull;
import org.jparsec.Tokens;

// For testing
import java.io.FileWriter;
import java.io.IOException;

import static com.google.common.labs.parse.Parser.*;
import static com.google.mu.util.CharPredicate.isNot;
import static com.umendes.schemeredux.lexer.SchemeLexerHelper.*;
import static com.umendes.schemeredux.lexer.SchemeSpecialWordList.*;
import static java.util.stream.Collectors.joining;


public class SchemeLexer extends LexerBase
{
  private CharSequence buffer;
  private int lex_start_pos;
  private int lexed_end_pos;
  private int token_length;
  private Tokens.Fragment token_frag;
  private int bufferEnd;
  protected IElementType type;

  enum Tag {
    TAG_LINE_COMMENT,
    TAG_BLOCK_COMMENT,
    TAG_DATUM_COMMENT_PREFIX,
    TAG_WHITE_SPACE,
    TAG_OP_SINGLE_CHAR,
    TAG_OP_OPEN_VECTOR,
    TAG_OP_ABBREVIATIONS,
    TAG_NUMBER,
    TAG_DATUM_LABEL,
    TAG_DATUM_REFERENCE,
    TAG_BOOLEAN,
    TAG_QUOTE_STRING,
    TAG_SHARP_CHAR,
    TAG_SHARP_EXCLAMATION,
    TAG_NAME_LITERAL,
    TAG_BAD_CHARACTER,

    TAG_KEYWORD,
    TAG_PROCEDURE
  }

  /**
   * Tokens
   */
  // Blanks and Comments
  Parser<?> SCA_DATUM_COMMENT_PREFIX = string("#;");
  Parser<?> s_datum_comment_prefix = SCA_DATUM_COMMENT_PREFIX.source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_DATUM_COMMENT_PREFIX)));

  Parser<?> SCA_LINE_COMMENT = string(";").then(zeroOrMore(isNot('\n'), ";"));
  Parser<?> s_line_comment = SCA_LINE_COMMENT.source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_LINE_COMMENT)));

  // The commented must not be '#', or if it's '|', must not be followed by '#'
  Parser<String> SCA_BLOCK_COMMENT_CONTENT = anyOf(
          consecutive(isNot('|'), "|"),
          string("|").notFollowedBy("#"));
  Parser<String> s_block_comment = define(
          nested -> SCA_BLOCK_COMMENT_CONTENT.or(nested)
                  .zeroOrMore(joining())
                  .between("#|", "|#"));
  Parser<?> block_comment = s_block_comment.source().map((a) -> (Tokens.fragment(a, Tag.TAG_BLOCK_COMMENT)));

  Parser<?> PAR_COMMENT = anyOf(s_datum_comment_prefix, s_line_comment, block_comment);

  Parser<?> s_whitespace = consecutive(CharPredicate.anyOf(" \n\t"), "WHITE_SPACE")
          .map((a) -> (Tokens.fragment(a, Tag.TAG_WHITE_SPACE)));

  // Operators
  Parser<?> s_op_single_char = single(CharPredicate.anyOf("()[]'`,"), "operator").source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_OP_SINGLE_CHAR)));

  // #vu8( does not exist in R7RS, adjusting to suit new specification
  Parser<?> s_op_open_vector = anyOf(string("#("), string("#u8("))
          .source().map((a) -> (Tokens.fragment(a, Tag.TAG_OP_OPEN_VECTOR)));
  Parser<?> s_op_abbreviations = multiword(terminalSort(new String[]{",@", "#'", "#`", "#,@", "#,"})).source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_OP_ABBREVIATIONS)));
  Parser<?> PAR_OPERATORS = anyOf(s_op_abbreviations, s_op_open_vector, s_op_single_char);

  // Identifier
  Parser<?> PT_INLINE_HEX_ESCAPE = sequence(string("\\x"),
          consecutive(HEX, "hex character"), string(";"),
          (g1, g2, g3) -> g1+g2+g3);
  Parser<?> PAR_INLINE_HEX_ESCAPE = PT_INLINE_HEX_ESCAPE.source();
  Parser<?> PT_EXTENDED_ALPHABETIC_CHAR = oneOf("!$%&*+-./:<=>?@^_~","ext alpha");
  Parser<?> PT_NAME_LITERAL_CHAR_VALID = Parser
          .anyOf(single(CharPredicate.WORD, "letter or number"),
                  PT_EXTENDED_ALPHABETIC_CHAR, PT_INLINE_HEX_ESCAPE);
  Parser<?> PT_NAME_LITERAL_FIRST_CHAR = Parser
          .anyOf(single(CharPredicate.ALPHA, "letter"),
                  PT_EXTENDED_ALPHABETIC_CHAR, PT_INLINE_HEX_ESCAPE);
  Parser<?> PT_NAME_LITERAL = PT_NAME_LITERAL_FIRST_CHAR.then(PT_NAME_LITERAL_CHAR_VALID.zeroOrMore());
  Parser<String> PAR_NAME_LITERAL = PT_NAME_LITERAL.source();
  Parser<?> s_name_literal = PAR_NAME_LITERAL
          .map((a) -> (Tokens.fragment(a, Tag.TAG_NAME_LITERAL)));

  // Numbers
  Parser<?> PT_RIGHT_INTEGER = sequence(oneOf("+-", "plus-minus sign"), digits(),
          (g1, g2) -> g1+g2);
  Parser<String> PAR_RIGHT_INTEGER = PT_RIGHT_INTEGER.source();
  Parser<?> PAR_BIN_INTEGER = string("#b").or(string("#B"))
          .then(consecutive(CharPredicate.range('0', '1'), "binary")).source();
  Parser<?> PAR_OCT_INTEGER = string("#o").or(string("#O"))
          .then(consecutive(CharPredicate.range('0', '7'), "octonary")).source();
  Parser<?> PAR_DEC_INTEGER = string("#d").or(string("#D")).then(digits()).source();
  Parser<?> PAR_HEX_INTEGER = string("#x").or(string("#X"))
          .then(consecutive(HEX, "hex character")).source();
  Parser<?> s_numbers = anyOf(
                  PAR_RIGHT_INTEGER, DEC_INTEGER,
                  DECIMAL, SCIENTIFIC_NOTATION,
                  PAR_BIN_INTEGER, PAR_OCT_INTEGER, PAR_DEC_INTEGER, PAR_HEX_INTEGER).source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_NUMBER)));


  // Datum labels, creation and reference
  Parser<String> DATUM_LABEL = sequence(string("#"), digits(),
          string("="), (g1, g2, g3) -> g1+g2+g3)
          .notFollowedBy(s_whitespace, "whitespace");
  Parser<?> s_datum_label = DATUM_LABEL
          .map((a) -> (Tokens.fragment(a, Tag.TAG_DATUM_LABEL)));

  Parser<String> DATUM_REFERENCE = sequence(string("#"), digits(),
          string("#"), (g1, g2, g3) -> g1+g2+g3);
  Parser<?> s_datum_reference = DATUM_REFERENCE
          .map((a) -> (Tokens.fragment(a, Tag.TAG_DATUM_REFERENCE)));


  // Characters, see SchemeSpecialWordList.java
  Parser<?> PT_SINGLE_CHAR = single(CharPredicate.ANY, "any");
  Parser<?> PT_HEX_CHAR = oneOf("xX", "hex marker").then(consecutive(HEX, "hex digit"))
          .notFollowedBy(PT_NAME_LITERAL, "variable name");
  Parser<?> PT_CHAR_PREFIX = string("#\\");

  Parser<?> SCA_SPECIAL_CHAR_NAMES = multiword(terminalSort(STRS_SPECIAL_CHAR_NAME))
          .notFollowedBy(PT_NAME_LITERAL, "variable name");

  Parser<?> SCA_SINGLE_CHAR = PT_CHAR_PREFIX.then(PT_SINGLE_CHAR);
  Parser<?> SCA_HEX_CHAR = PT_CHAR_PREFIX.then(PT_HEX_CHAR);
  Parser<?> SCA_SPECIAL_CHAR = PT_CHAR_PREFIX.then(SCA_SPECIAL_CHAR_NAMES);
  Parser<?> s_sharp_char = anyOf(SCA_SPECIAL_CHAR, SCA_HEX_CHAR, SCA_SINGLE_CHAR).source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_SHARP_CHAR)));

  // String
  Parser<?> PAR_STRING = quotedByWithEscapes('"', '"', chars(1)).source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_QUOTE_STRING)));

  // Boolean
  String[] TERM_BOOLEAN = {"#t", "#f", "#true", "#false"};
  Parser<?> s_boolean = multiword(terminalSort(TERM_BOOLEAN)).source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_BOOLEAN)));

  // Sharp exclamation (#!eof, #!null, etc.)
  Parser<?> PT_SHARP_EXCLAMATION = string("#!").then(PT_NAME_LITERAL);
  Parser<?> s_sharp_exclamation = PT_SHARP_EXCLAMATION.source()
          .map((a) -> (Tokens.fragment(a, Tag.TAG_SHARP_EXCLAMATION)));

  // Values entered by the user
  Parser<?> PAR_LITERALS = anyOf(PAR_STRING, s_numbers, s_boolean, s_sharp_exclamation, s_sharp_char);

  /**
   * Built-in elements
   */
  // Keywords, see SchemeSpecialWordList.java
  Parser<?> s_keywords = multiword(terminalSort(STRS_KEYWORD))
          .notFollowedBy(PAR_NAME_LITERAL, "variable name").source()
        .map((a) -> (Tokens.fragment(a, Tag.TAG_KEYWORD)));

  // Built-in Procedures, see SchemeSpecialWordList.java
  Parser<?> s_builtin_procedures = multiword(terminalSort(STRS_BUILTIN_PROCEDURE)).source()
          .notFollowedBy(PAR_NAME_LITERAL, "variable name")
          .map((a) -> (Tokens.fragment(a, Tag.TAG_PROCEDURE)));

  Parser<?> PAR_BUILTIN_ELEMENTS = anyOf(s_keywords, s_builtin_procedures);

  // Bad elements
  Parser<?> PAR_ELEMENT = anyOf(s_whitespace, PAR_COMMENT,
          PAR_LITERALS, s_datum_label, s_datum_reference,
          PAR_BUILTIN_ELEMENTS, s_name_literal, PAR_OPERATORS);
  Parser<String> PAR_ANY_CHAR = single(CharPredicate.ANY, "any").source();
  Parser<?> s_bad_element = PAR_ANY_CHAR
          .map((a) -> (Tokens.fragment(a, Tag.TAG_BAD_CHARACTER)));

  Parser<Object> PAR_TOKEN = anyOf(PAR_ELEMENT, s_bad_element);

  // Run PAR_TOKEN and then check output using map
Parser<Object> s_token = PAR_TOKEN
          .map((a) -> {
            if (null != a) {
//              System.out.println("a: " + a);
              if (a.getClass() == Tokens.Fragment.class) {
                // If this is a valid token, get its type and text, else do the other stuff
                // Also, set global variable token-frag to token fragment a for later sorting
                token_frag = (Tokens.Fragment)a;
                token_length = ((Tokens.Fragment)a).text().length();

//                // For testing
//                System.out.println("type: " + ((Tokens.Fragment)a).tag().toString());
//                System.out.println("text: " + ((Tokens.Fragment)a).text());
//
//                try (FileWriter writer = new FileWriter("Absolute path to LexerTestDotParse.txt", true)) {
//                  writer.write("type: " + ((Tokens.Fragment)a).tag() + "\n");
//                  writer.write("text: " + ((Tokens.Fragment)a).text() + "\n" + "\n");
//
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }

              } else {
                token_frag = null;
//                System.out.println("type: " + a.getClass().getName());
              }
            } else {
              token_frag = null;
//              System.out.println("a: null");
            }
            return a;
          });

  @Override
  // The values that go in these are probably determined by intellij?
  public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState)
  {
//    System.out.println("startOffset: " + startOffset + ", endOffset: " + endOffset
//            + ". initialState: " + initialState);

    this.buffer = buffer;
    this.lex_start_pos = startOffset; // Back pointer
    this.lexed_end_pos = startOffset; // Forward pointer
    this.bufferEnd = endOffset;
    this.token_frag = null;
    decodeState(initialState);
    advance();
  }

  int cur_token_start = 0;
  int cur_token_end = 0;

  //Buffer = word to parse
  @Override
  public void advance()
  // Stop parsing if end of buffer (file)
  {
    if (lex_start_pos >= bufferEnd)
    {
      type = null;
      return;
    }

    // Same but bring back pointer to forward pointer first
    this.token_frag = null;
    if (lexed_end_pos >= bufferEnd)
    {
      lex_start_pos = lexed_end_pos;
      type = null;
      return;
    }

    // Bring back pointer up and create new buffer out of "there to eof"
    lex_start_pos = lexed_end_pos;
    cur_token_start = lex_start_pos;
    CharSequence myBuffer = buffer.subSequence(this.lex_start_pos, this.bufferEnd);

    try {
      s_token.parse(myBuffer.toString());

    } catch (Exception e) {
//      System.out.println("advance Exception: " + e.getMessage());

      if (null == token_frag) {
//        System.out.println("advance no valid token");
        type = null;
        return;
      }
    }

    // move forward pointer up
    lexed_end_pos = lex_start_pos + token_length;
    if (lexed_end_pos > bufferEnd) {
      lexed_end_pos = bufferEnd;
    }
    cur_token_end = lexed_end_pos;

    // Identify specific lexeme and sent to intellij
    switch ((Tag)token_frag.tag()) {

      case TAG_LINE_COMMENT:
        type = SchemeTokens.LINE_COMMENT;
        break;

      case TAG_BLOCK_COMMENT:
        type = SchemeTokens.BLOCK_COMMENT;
        break;

      case TAG_DATUM_COMMENT_PREFIX:
        type = SchemeTokens.DATUM_COMMENT_PRE;
        break;

      case TAG_WHITE_SPACE:
        type = SchemeTokens.WHITESPACE;
        break;

      case TAG_OP_SINGLE_CHAR: {
        String opStr = token_frag.text();
        switch (opStr) {
          case "(":
            type = SchemeTokens.LEFT_PAREN;

            break;
          case ")":
            type = SchemeTokens.RIGHT_PAREN;

            break;
          case "[":
            type = SchemeTokens.LEFT_SQUARE;

            break;
          case "]":
            type = SchemeTokens.RIGHT_SQUARE;

            break;
          case "'":
            type = SchemeTokens.QUOTE;

            break;
          case "`":
            type = SchemeTokens.QUASIQUOTE;

            break;
          case ",":
            type = SchemeTokens.UNQUOTE;

            break;
          default:
            type = SchemeTokens.SPECIAL;
            break;
        }

        break;
      }

      case TAG_OP_OPEN_VECTOR:
        type = SchemeTokens.OPEN_VECTOR;
        break;

      case TAG_OP_ABBREVIATIONS: {
        String opStr = token_frag.text();
        switch (opStr) {
          case ",@":
            type = SchemeTokens.UNQUOTE_SPLICING;
            break;

          case "#'":
            type = SchemeTokens.SYNTAX;
            break;

          case "#`":
            type = SchemeTokens.QUASISYNTAX;
            break;

          case "#,":
            type = SchemeTokens.UNSYNTAX;
            break;

          case "#,@":
            type = SchemeTokens.UNSYNTAX_SPLICING;
            break;

          default:
            type = SchemeTokens.SPECIAL;
            break;
        }

        break;
      }

      case TAG_NUMBER:
        type = SchemeTokens.NUMBER_LITERAL;
        break;

      case TAG_BOOLEAN:
        type = SchemeTokens.BOOLEAN_LITERAL;
        break;

      case TAG_SHARP_EXCLAMATION, TAG_SHARP_CHAR:
        type = SchemeTokens.CHAR_LITERAL;
        break;

      case TAG_QUOTE_STRING:
        type = SchemeTokens.STRING_LITERAL;
        return;

        case TAG_KEYWORD:
        type = SchemeTokens.KEYWORD;
        break;

      case TAG_PROCEDURE, TAG_DATUM_LABEL, TAG_DATUM_REFERENCE:
        type = SchemeTokens.PROCEDURE;
        break;

      case TAG_NAME_LITERAL:
        type = SchemeTokens.NAME_LITERAL;
        break;

      case TAG_BAD_CHARACTER:
        type = SchemeTokens.BAD_CHARACTER;
        break;

      default:
        type = null;
    }
  }

  @Override
  public int getState()
  {
    return encodeState();
  }

  @Override
  public IElementType getTokenType()
  {
    return type;
  }

  @Override
  public int getTokenStart()
  {
    return cur_token_start;
  }

  @Override
  public int getTokenEnd()
  {
    return cur_token_end;
  }

  @Override
  public @NotNull CharSequence getBufferSequence()
  {
    return buffer;
  }

  @Override
  public int getBufferEnd()
  {
    return bufferEnd;
  }

  private void decodeState(int state)
  {
  }

  private int encodeState()
  {
    return 0;
  }
}
