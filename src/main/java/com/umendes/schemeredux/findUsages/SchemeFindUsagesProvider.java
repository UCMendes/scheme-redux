package com.umendes.schemeredux.findUsages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.umendes.schemeredux.psi.impl.SchemeSymbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.umendes.schemeredux.lexer.SchemeLexer;
import com.umendes.schemeredux.lexer.SchemeTokens;


public class SchemeFindUsagesProvider implements FindUsagesProvider
{
  @Nullable
  public WordsScanner getWordsScanner()
  {
    return new DefaultWordsScanner(new SchemeLexer(), SchemeTokens.IDENTIFIERS, SchemeTokens.COMMENTS, SchemeTokens.STRINGS);
  }

  public boolean canFindUsagesFor(@NotNull PsiElement psiElement)
  {
//    return psiElement instanceof SchemeSymbol;
    return true;
  }

  public String getHelpId(@NotNull PsiElement psiElement)
  {
    return null;
  }

  @NotNull
  public String getType(@NotNull PsiElement element)
  {
    if (element instanceof SchemeSymbol)
    {
      return "symbol";
    }
    return "entity";
  }

  @NotNull
  public String getDescriptiveName(@NotNull PsiElement element)
  {
    return element.getText();
  }

  @NotNull
  public String getNodeText(@NotNull PsiElement element, boolean useFullName)
  {
    return element.getText();
  }
}
