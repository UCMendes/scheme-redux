package com.umendes.schemeredux.scheme;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.umendes.schemeredux.parser.SchemePsiCreator;

public interface Scheme
{
  // Parsing customisations
  Lexer getLexer();

  PsiParser getParser();

  SchemePsiCreator getPsiCreator();
}
