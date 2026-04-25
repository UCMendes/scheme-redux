package com.umendes.schemeredux.settings.colorScheme;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.umendes.schemeredux.SchemeIcons;
import com.umendes.schemeredux.highlighter.SchemeSyntaxHighlighter;
import com.umendes.schemeredux.utils.SchemeResourceUtil;

import javax.swing.Icon;
import java.util.HashMap;
import java.util.Map;

public class SchemeColorsAndFontsPage implements ColorSettingsPage
{
  @NotNull
  public String getDisplayName()
  {
    return "Scheme";
  }

  @Nullable
  public Icon getIcon()
  {
    return SchemeIcons.SCHEME_ICON;
  }

  @NotNull
  public AttributesDescriptor @NotNull [] getAttributeDescriptors()
  {
    return ATTRS;
  }

  private final SchemeSyntaxHighlighter syntaxHighlighter;
  private final AttributesDescriptor[] ATTRS;

  public SchemeColorsAndFontsPage() {
    this.syntaxHighlighter = new SchemeSyntaxHighlighter();
    this.ATTRS =
        new AttributesDescriptor[]{desc(SchemeSyntaxHighlighter.IDENTIFIER_ID, syntaxHighlighter.IDENTIFIER),
            desc(SchemeSyntaxHighlighter.COMMENT_ID, syntaxHighlighter.COMMENT),
//                               desc(SchemeSyntaxHighlighter.BLOCK_COMMENT_ID, SchemeSyntaxHighlighter.BLOCK_COMMENT),
//                               desc(SchemeSyntaxHighlighter.DATUM_COMMENT_ID, SchemeSyntaxHighlighter.DATUM_COMMENT),
            desc(SchemeSyntaxHighlighter.NUMBER_ID, syntaxHighlighter.NUMBER),
            desc(SchemeSyntaxHighlighter.STRING_ID, syntaxHighlighter.STRING),
            desc(SchemeSyntaxHighlighter.BRACES_ID, syntaxHighlighter.BRACE),
            desc(SchemeSyntaxHighlighter.PAREN_ID, syntaxHighlighter.PAREN),
            desc(SchemeSyntaxHighlighter.BAD_CHARACTER_ID, syntaxHighlighter.BAD_CHARACTER),
            desc(SchemeSyntaxHighlighter.CHAR_ID, syntaxHighlighter.CHAR),
            desc(SchemeSyntaxHighlighter.DATUM_LABEL_ID, syntaxHighlighter.DATUM_LABEL),
            desc(SchemeSyntaxHighlighter.DATUM_REFERENCE_ID, syntaxHighlighter.DATUM_REFERENCE),
            desc(SchemeSyntaxHighlighter.LITERAL_ID, syntaxHighlighter.LITERAL),
            desc(SchemeSyntaxHighlighter.KEYWORD_ID, syntaxHighlighter.KEYWORD),
            desc(SchemeSyntaxHighlighter.PROCEDURE_ID, syntaxHighlighter.PROCEDURE),
            desc(SchemeSyntaxHighlighter.SPECIAL_ID, syntaxHighlighter.SPECIAL),
            desc(SchemeSyntaxHighlighter.QUOTED_TEXT_ID, syntaxHighlighter.QUOTED_TEXT),
            desc(SchemeSyntaxHighlighter.QUOTED_STRING_ID, syntaxHighlighter.QUOTED_STRING),
            desc(SchemeSyntaxHighlighter.QUOTED_NUMBER_ID, syntaxHighlighter.QUOTED_NUMBER),
        };
  }

  private static AttributesDescriptor desc(String displayName, TextAttributesKey key)
  {
    return new AttributesDescriptor(displayName, key);
  }

  @NotNull
  public ColorDescriptor @NotNull [] getColorDescriptors()
  {
    return new ColorDescriptor[0];
  }

  @NotNull
  public SyntaxHighlighter getHighlighter()
  {
    return new SchemeSyntaxHighlighter();
  }

  @NonNls
  @NotNull
  public String getDemoText()
  {
    String content = SchemeResourceUtil.readResourceAsString("/scheme/sample-code.scm");
    if (content != null) {
      return content;
    }

    // Fallback to default demo text
    return """
            ;; Test highlighting
            
            (define string "Some string")
            
            (define quoted '(my quoted 3 items "with quoted string"))
            
            (define char #\\c)
            
            (define special #!eof)
            
            (let ((x '(#1=(a b) #1#))) (eq? (car x) (cadr x)))
            
            (let ((x '(1 3 5 7 9)))
              (do ((x x (cdr x))
                   (sum 0 (+ sum (car x))))
                  ((null? x) sum)))""";
  }

  @Nullable
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
  {
    Map<String, TextAttributesKey> map = new HashMap<>();
    map.put("def", syntaxHighlighter.IDENTIFIER);
    return map;
  }
}
