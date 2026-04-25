package com.umendes.schemeredux.formatter.processors;

import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;
import com.umendes.schemeredux.formatter.SchemeBlock;
import com.umendes.schemeredux.lexer.SchemeTokens;
import com.umendes.schemeredux.parser.AST;


public class SchemeSpacingProcessor
{
  private static final Spacing NO_SPACING = Spacing.createSpacing(0, 0, 0, false, 0);
  private static final Spacing NO_SPACING_WITH_NEWLINE = Spacing.createSpacing(0, 0, 0, true, 1);
  private static final Spacing COMMON_SPACING = Spacing.createSpacing(1, 1, 0, true, 100);

  public static Spacing getSpacing(Block child1, Block child2)
  {
    if (!(child1 instanceof SchemeBlock block1) || !(child2 instanceof SchemeBlock block2))
    {
      return null;
    }

      ASTNode node1 = block1.getNode();
    ASTNode node2 = block2.getNode();

    return getSpacingForAST(node1, node2);
  }

  public static Spacing getSpacingForAST(ASTNode node1, ASTNode node2)
  {
    IElementType type1 = node1.getElementType();
    IElementType type2 = node2.getElementType();

    if ((node2.getPsi() instanceof PsiComment)) {
      ASTNode preNode = node2.getTreePrev();
      if ((preNode.getElementType() == SchemeTokens.WHITESPACE)
              && (preNode.getText().indexOf('\n') < 0)) {
        return Spacing.getReadOnlySpacing();
      }
    }
    if (SchemeTokens.DATUM_PREFIXES.contains(type1)
            || type1 == AST.AST_BAD_CHARACTER)
    {
      return NO_SPACING;
    }

    if (SchemeTokens.BRACES.contains(type1) || SchemeTokens.BRACES.contains(type2))
    {
      return NO_SPACING_WITH_NEWLINE;
    }

    return COMMON_SPACING;
  }
}
