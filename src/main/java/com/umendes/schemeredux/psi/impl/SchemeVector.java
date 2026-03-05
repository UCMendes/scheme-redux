package com.umendes.schemeredux.psi.impl;

import com.intellij.lang.ASTNode;
import com.umendes.schemeredux.psi.api.SchemeBraced;


public class SchemeVector extends SchemePsiElementBase implements SchemeBraced
{
  public SchemeVector(ASTNode node)
  {
    super(node, "SchemeVector");
  }
}
