package com.umendes.schemeredux.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import com.umendes.schemeredux.psi.util.SchemePsiElementFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SchemeDatumReference extends SchemePsiElementBase implements PsiReference
{
    public SchemeDatumReference(ASTNode node)
    {
        super(node, "SchemeDatumReference");
    }

    @Override
    public @NotNull PsiElement getElement() {
        return this;
    }

    @Override
    public @NotNull TextRange getRangeInElement() {
        // Since only the first hash and the numbers after count as the reference
        return new TextRange(0, getTextLength() - 1);
    }

    @Override
    public @NotNull @NlsSafe String getCanonicalText() {
        return getText();
    }

    public @NotNull String getFormattedCanonicalText() {
        String referenceName = getCanonicalText();
        referenceName = referenceName.substring(0, referenceName.length()-1);
        return referenceName;
    }

    @Override
    public @Nullable PsiElement resolve() {
        System.out.println("Did you manage to get here? good");
        PsiElement brother;
        brother = this.getPrevSibling();
        while (brother != null)
        {
            if (brother instanceof SchemeDatumLabel datumLabel) {
                String labelName = datumLabel.getName();
                String labelSubstring = Objects.requireNonNull(labelName).substring(0, labelName.length() - 1);
                System.out.println(labelName);
                System.out.println(labelSubstring);
                if (getFormattedCanonicalText().equals(labelSubstring))
                {
                    return datumLabel;
                }
            brother = brother.getPrevSibling();
            }
        }
        return null;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String s) throws IncorrectOperationException {
        // Only hash + number need changing
        ASTNode thisNode = getNode();
        ASTNode newNode = SchemePsiElementFactory.getInstance(getProject())
                .createSymbolNodeFromText(s.substring(0, s.length()-1) + "#");
        ASTNode oldNode = thisNode.getFirstChildNode();
        thisNode.replaceChild(oldNode, newNode);
        return this;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException {
        //todo implement me! go check other implementations?
        return this;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement psiElement)
    {
        System.out.println("Did you manage to get here? good");
        // Format both datum label and datum reference names, if they're the same, then check for reference
        if (psiElement instanceof SchemeDatumLabel datumLabel)
        {
            System.out.println("Did you manage to get here? good");
            String labelName = datumLabel.getName();
            if (getFormattedCanonicalText().equals(
                    Objects.requireNonNull(labelName).substring(0, labelName.length() -1)))
            {
                return resolve() == datumLabel;
            }
        }
        return false;
    }

    @Override
    public boolean isSoft() {
        return false;
    }
}
