package com.umendes.schemeredux.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;


public class SchemeFormattingModelBuilder implements FormattingModelBuilder
{
  @NotNull
  public FormattingModel createModel(@NotNull FormattingContext formattingContext)
  {
    ASTNode node = formattingContext.getPsiElement().getNode();
    assert node != null;
    PsiFile containingFile = formattingContext.getPsiElement().getContainingFile();
    ASTNode astNode = containingFile.getNode();
    assert astNode != null;
    SchemeBlock schemeBlock = SchemeBlock.create(
            astNode, Alignment.createAlignment(),
            Indent.getAbsoluteNoneIndent(), null,
            formattingContext.getCodeStyleSettings(), null);
    return FormattingModelProvider.createFormattingModelForPsiFile(
            containingFile, schemeBlock, formattingContext.getCodeStyleSettings());
  }

}