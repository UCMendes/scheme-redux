package com.umendes.schemeredux.schemeredux.scheme.impl;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.umendes.schemeredux.schemeredux.lexer.SchemeLexer;
import com.umendes.schemeredux.schemeredux.parser.SchemeParser;
import com.umendes.schemeredux.schemeredux.parser.SchemePsiCreator;
import com.umendes.schemeredux.schemeredux.scheme.Scheme;

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
