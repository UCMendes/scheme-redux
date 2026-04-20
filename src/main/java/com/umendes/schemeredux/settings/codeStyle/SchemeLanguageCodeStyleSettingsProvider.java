package com.umendes.schemeredux.settings.codeStyle;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.*;
import org.jetbrains.annotations.NotNull;
import com.umendes.schemeredux.SchemeLanguage;
import com.umendes.schemeredux.utils.SchemeResourceUtil;


public class SchemeLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    @NotNull
    @Override
    public Language getLanguage() {
        return SchemeLanguage.INSTANCE;
    }

    @Override
    public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions("SPACE_AROUND_ASSIGNMENT_OPERATORS");
            consumer.renameStandardOption("SPACE_AROUND_ASSIGNMENT_OPERATORS", "Separator");
        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE");
        }
    }

    @Override
    public @NotNull CommonCodeStyleSettings getDefaultCommonSettings() {
        CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(SchemeLanguage.INSTANCE);
        defaultSettings.initIndentOptions();
        CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.getIndentOptions();
        if (null != indentOptions) {
            indentOptions.INDENT_SIZE = 2;
            indentOptions.TAB_SIZE = 2;
            indentOptions.CONTINUATION_INDENT_SIZE = 4;
        }
        return defaultSettings;
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        String content = SchemeResourceUtil.readResourceAsString("/scheme/sample-code.scm");
        if (content != null) {
            return content;
        }

        // Fallback to default code sample
        return """
                (f f)
                
                ((lambda (f) x) (lambda (f) x))
                
                (define Y (lambda (f) (f f)))
                
                ((lambda (f) (lambda (x) ((f f) (g x))))
                  (lambda (f) (lambda (x) ((f f) (g x)))))
                \s
                ((lambda (g)
                    ((lambda (f) (lambda (x) ((f f) (g x))))
                      (lambda (f) (lambda (x) ((f f) (g x))))))
                  cdr)
                
                ((lambda (g)
                   ((lambda (f) (f f))
                     (lambda (f) (lambda (x) ((f f) (g x))))))
                  g)""";
    }

    @Override
    public IndentOptionsEditor getIndentOptionsEditor() {
        return new SmartIndentOptionsEditor();
    }
}
