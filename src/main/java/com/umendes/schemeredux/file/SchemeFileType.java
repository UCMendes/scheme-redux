package com.umendes.schemeredux.file;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import com.umendes.schemeredux.SchemeIcons;
import com.umendes.schemeredux.SchemeLanguage;

import javax.swing.Icon;

public class SchemeFileType extends LanguageFileType
{
  public static final SchemeFileType SCHEME_FILE_TYPE = new SchemeFileType();
  public static final Language SCHEME_LANGUAGE = SCHEME_FILE_TYPE.getLanguage();

  public SchemeFileType()
  {
    super(SchemeLanguage.INSTANCE);
  }

  @NotNull
  public String getName()
  {
    return "Scheme";
  }

  @NotNull
  public String getDescription()
  {
    return "Scheme com.umendes.schemeredux.schemeredux.file";
  }

  @NotNull
  public String getDefaultExtension()
  {
    return "ss";
  }

  public Icon getIcon()
  {
    return SchemeIcons.SCHEME_ICON;
  }
}
