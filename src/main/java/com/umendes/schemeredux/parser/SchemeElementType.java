package com.umendes.schemeredux.parser;

import com.intellij.psi.tree.IElementType;
import com.umendes.schemeredux.file.SchemeFileType;

// Token type definition, for creating new tokens ones for use with scheme specifically
public class SchemeElementType extends IElementType
{
  private final String name;

  public SchemeElementType(String debugName)
  {
    super(debugName, SchemeFileType.SCHEME_LANGUAGE);
    name = debugName;
  }

  public String getName()
  {
    return name;
  }
}
