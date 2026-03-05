package com.umendes.schemeredux.lexer;

import com.intellij.psi.tree.TokenSet;
import com.umendes.schemeredux.parser.AST;


public class TokenSets implements SchemeTokens
{
  // TODO CMF
  public static final TokenSet REFERENCE_NAMES = TokenSet.create(AST.AST_BASIC_ELE_SYMBOL);
}
