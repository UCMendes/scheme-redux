package com.umendes.schemeredux.settings.codeStyle;

import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import com.umendes.schemeredux.SchemeLanguage;


public class SchemeCodeStyleSettingsProvider extends CodeStyleSettingsProvider
{
  @Override
  public @NotNull CodeStyleConfigurable createConfigurable(
          @NotNull CodeStyleSettings settings, @NotNull CodeStyleSettings originalSettings)
  {
    return new SchemeFormatConfigurable(settings, originalSettings);
  }

  @Override
  public CustomCodeStyleSettings createCustomSettings(@NotNull CodeStyleSettings settings)
  {
    return new SchemeCodeStyleSettings(settings);
  }

  @Override
  public String getConfigurableDisplayName() {
    return "Scheme";
  }

  @NotNull
  @Override
  public Language getLanguage() {
    return SchemeLanguage.INSTANCE;
  }
}
