package com.umendes.schemeredux.schemeredux.scheme;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.umendes.schemeredux.schemeredux.parser.SchemePsiCreator;

public interface Scheme
{
  // Parsing customisations
  Lexer getLexer();

  PsiParser getParser();

  SchemePsiCreator getPsiCreator();
}
