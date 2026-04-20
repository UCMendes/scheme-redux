package com.umendes.schemeredux.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import com.umendes.schemeredux.lexer.SchemeLexer;
import com.umendes.schemeredux.lexer.SchemeTokens;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SchemeSyntaxHighlighter extends SyntaxHighlighterBase implements SchemeTokens
{
  private static final Map<IElementType, TextAttributesKey[]> ATTRIBUTES = new HashMap<IElementType, TextAttributesKey[]>();

  @NotNull
  public Lexer getHighlightingLexer()
  {
    return new SchemeLexer();
  }

  public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType)
  {
    TextAttributesKey[] Keys = ATTRIBUTES.get(tokenType);
    if (null == Keys) {
//      System.out.println("tokenType: " + tokenType.toString());
      return EMPTY_KEYS;
    } else {
      return Keys;
    }
  }

  public static final String COMMENT_ID = "Scheme Comment";
  public static final String IDENTIFIER_ID = "Scheme Identifier";
  public static final String NUMBER_ID = "Scheme Numbers";
  public static final String STRING_ID = "Scheme String";
  public static final String STRING_ESCAPE_ID = "Scheme String Escape";
  public static final String BAD_CHARACTER_ID = "Scheme Bad character";
  public static final String BRACES_ID = "Scheme Braces";
  public static final String PAREN_ID = "Scheme Parentheses";
  public static final String LITERAL_ID = "Scheme Literal";
  public static final String CHAR_ID = "Scheme Character";
  public static final String DATUM_LABEL_ID = "Scheme Datum Label";
  public static final String DATUM_REFERENCE_ID = "Scheme Datum Reference";
  public static final String KEYWORD_ID = "Scheme Keyword";
  public static final String PROCEDURE_ID = "Scheme Procedure";
  public static final String SPECIAL_ID = "Scheme Special";
  public static final String QUOTED_TEXT_ID = "Scheme Quoted text";
  public static final String QUOTED_STRING_ID = "Scheme Quoted string";
  public static final String QUOTED_NUMBER_ID = "Scheme Quoted number";
  public static final String DOT_ID = "Scheme Dot";
  public static final String ABBREVIATION_ID = "Scheme Abbreviation";

  public TextAttributesKey COMMENT = createTextAttributesKey(COMMENT_ID, DefaultLanguageHighlighterColors.LINE_COMMENT);
  public TextAttributesKey IDENTIFIER = createTextAttributesKey(IDENTIFIER_ID, DefaultLanguageHighlighterColors.IDENTIFIER);
  public TextAttributesKey NUMBER = createTextAttributesKey(NUMBER_ID, DefaultLanguageHighlighterColors.NUMBER);
  public TextAttributesKey STRING = createTextAttributesKey(STRING_ID, DefaultLanguageHighlighterColors.STRING);
  public TextAttributesKey STRING_ESCAPE = createTextAttributesKey(STRING_ESCAPE_ID, DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE);
  public TextAttributesKey BRACE = createTextAttributesKey(BRACES_ID, DefaultLanguageHighlighterColors.BRACES);
  public TextAttributesKey PAREN = createTextAttributesKey(PAREN_ID, DefaultLanguageHighlighterColors.PARENTHESES);
  public TextAttributesKey LITERAL = createTextAttributesKey(LITERAL_ID, HighlighterColors.TEXT);
  public TextAttributesKey CHAR = createTextAttributesKey(CHAR_ID, DefaultLanguageHighlighterColors.STRING);
  public TextAttributesKey BAD_CHARACTER = createTextAttributesKey(BAD_CHARACTER_ID, HighlighterColors.BAD_CHARACTER);
  public TextAttributesKey DATUM_LABEL = createTextAttributesKey(DATUM_LABEL_ID, DefaultLanguageHighlighterColors.LABEL);
  public TextAttributesKey DATUM_REFERENCE = createTextAttributesKey(DATUM_REFERENCE_ID, DefaultLanguageHighlighterColors.IDENTIFIER);
  public TextAttributesKey KEYWORD = createTextAttributesKey(KEYWORD_ID, DefaultLanguageHighlighterColors.KEYWORD);
  public TextAttributesKey PROCEDURE = createTextAttributesKey(PROCEDURE_ID, DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
  public TextAttributesKey SPECIAL = createTextAttributesKey(SPECIAL_ID, DefaultLanguageHighlighterColors.KEYWORD);
  public TextAttributesKey ABBREVIATION = createTextAttributesKey(ABBREVIATION_ID, DefaultLanguageHighlighterColors.KEYWORD);

  // Currently not being used in any significantly meaningful way
  public TextAttributesKey QUOTED_TEXT = createTextAttributesKey(QUOTED_TEXT_ID, brighter(HighlighterColors.TEXT));
  public TextAttributesKey QUOTED_STRING = createTextAttributesKey(QUOTED_STRING_ID, brighter(DefaultLanguageHighlighterColors.STRING));
  public TextAttributesKey QUOTED_NUMBER = createTextAttributesKey(QUOTED_NUMBER_ID, brighter(DefaultLanguageHighlighterColors.NUMBER));
  public TextAttributesKey DOT = createTextAttributesKey(DOT_ID, brighter(DefaultLanguageHighlighterColors.DOT));

  public static TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

  {
    newFillMap(pack(COMMENT),
            SchemeTokens.LINE_COMMENT, SchemeTokens.BLOCK_COMMENT, SchemeTokens.DATUM_COMMENT);
    newFillMap(pack(NUMBER), SchemeTokens.NUMBER_LITERAL);
    newFillMap(pack(STRING), SchemeTokens.STRING_LITERAL);
    newFillMap(pack(BRACE),
            SchemeTokens.LEFT_SQUARE, SchemeTokens.RIGHT_SQUARE, SchemeTokens.LEFT_CURLY, SchemeTokens.RIGHT_CURLY);
    newFillMap(pack(PAREN), SchemeTokens.LEFT_PAREN, SchemeTokens.RIGHT_PAREN);
    newFillMap(pack(CHAR), SchemeTokens.CHAR_LITERAL);
    newFillMap(pack(SPECIAL), SchemeTokens.SPECIAL);
    newFillMap(pack(DATUM_LABEL), SchemeTokens.DATUM_LABEL);
    newFillMap(pack(DATUM_REFERENCE), SchemeTokens.DATUM_REFERENCE);
    newFillMap(pack(KEYWORD), SchemeTokens.KEYWORD, SchemeTokens.BOOLEAN_LITERAL);
    newFillMap(pack(PROCEDURE), SchemeTokens.PROCEDURE);
    newFillMap(pack(DOT), SchemeTokens.DOT);
    newFillMap(pack(ABBREVIATION),
            SchemeTokens.QUOTE, SchemeTokens.QUASIQUOTE, SchemeTokens.UNQUOTE, SchemeTokens.UNQUOTE_SPLICING,
            SchemeTokens.SYNTAX, SchemeTokens.QUASISYNTAX, SchemeTokens.UNSYNTAX, SchemeTokens.UNSYNTAX_SPLICING);
  }

  protected void newFillMap(TextAttributesKey[] value, @NotNull IElementType... types) {
      for (IElementType type : types) {
          SchemeSyntaxHighlighter.ATTRIBUTES.put(type, value);
      }
  }

  private TextAttributes brighter(TextAttributesKey key)
  {
    TextAttributes attributes = key.getDefaultAttributes().clone();
    Color foregroundColor = attributes.getForegroundColor();
    if (foregroundColor != null)
    {
      attributes.setForegroundColor(foregroundColor.brighter());
    }
    else
    {
      attributes.setForegroundColor(JBColor.DARK_GRAY);
    }
    return attributes;
  }
}
