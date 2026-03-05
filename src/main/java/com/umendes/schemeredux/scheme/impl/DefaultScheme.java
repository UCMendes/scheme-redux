package com.umendes.schemeredux.scheme.impl;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.umendes.schemeredux.lexer.SchemeLexer;
import com.umendes.schemeredux.parser.SchemeParser;
import com.umendes.schemeredux.parser.SchemePsiCreator;
import com.umendes.schemeredux.scheme.Scheme;

public class DefaultScheme implements Scheme
{
  @Override
  public Lexer getLexer()
  {
    return new SchemeLexer();
  }

  @Override
  public PsiParser getParser()
  {
    return new SchemeParser();
  }

  @Override
  public SchemePsiCreator getPsiCreator()
  {
    return new SchemePsiCreator();
  }
}
