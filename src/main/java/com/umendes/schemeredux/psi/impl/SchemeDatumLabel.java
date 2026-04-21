package com.umendes.schemeredux.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import com.umendes.schemeredux.psi.util.SchemePsiElementFactory;
import org.jetbrains.annotations.NotNull;

public class SchemeDatumLabel extends SchemePsiElementBase implements PsiNamedElement
{
    public SchemeDatumLabel(ASTNode node)
    {
        super(node, "SchemeDatumLabel");
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        ASTNode thisNode = getNode();
        ASTNode newNode = SchemePsiElementFactory.getInstance(getProject()).createSymbolNodeFromText(name);
        ASTNode oldNode = thisNode.getFirstChildNode();
        thisNode.replaceChild(oldNode, newNode);
        return this;
    }
}