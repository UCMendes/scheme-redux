package com.umendes.schemeredux;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;

import javax.swing.Icon;

public interface SchemeIcons
{
  @NonNls
  Icon SCHEME_ICON = IconLoader.findIcon("/icons/lambda.png", SchemeIcons.class);

  Icon SYMBOL = IconLoader.findIcon("/icons/symbol.png", SchemeIcons.class);

}
