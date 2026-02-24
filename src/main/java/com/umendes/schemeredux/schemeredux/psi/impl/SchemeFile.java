package com.umendes.schemeredux.schemeredux.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.impl.source.PsiFileWithStubSupport;
import org.jetbrains.annotations.NotNull;
import com.umendes.schemeredux.schemeredux.file.SchemeFileType;
import com.umendes.schemeredux.schemeredux.psi.api.SchemePsiElement;
import com.umendes.schemeredux.schemeredux.psi.impl.list.SchemeList;
import com.umendes.schemeredux.schemeredux.psi.impl.symbols.SchemeIdentifier;
import com.umendes.schemeredux.schemeredux.psi.util.SchemePsiUtil;
import com.umendes.schemeredux.schemeredux.psi.util.SchemeTextUtil;


public class SchemeFile extends PsiFileBase implements PsiFile, PsiFileWithStubSupport, SchemePsiElement
{
  private PsiElement context = null;

  @Override
  public String toString()
  {
    return "SchemeFile";
  }

  public SchemeFile(FileViewProvider viewProvider)
  {
    super(viewProvider, SchemeFileType.SCHEME_LANGUAGE);
  }

  @Override
  public PsiElement getContext()
  {
    if (context != null)
    {
      return context;
    }
    return super.getContext();
  }

  protected PsiFileImpl clone()
  {
    SchemeFile clone = (SchemeFile) super.clone();
    clone.context = context;
    return clone;
  }

  @NotNull
  public FileType getFileType()
  {
    return SchemeFileType.SCHEME_FILE_TYPE;
  }

  @NotNull
  public String getPackageName()
  {
    String ns = getNamespace();
    if (ns == null)
    {
      return "";
    }
    else
    {
      return SchemeTextUtil.getSymbolPrefix(ns);
    }
  }

  public String getNamespace()
  {
    SchemeList ns = getNamespaceElement();
    if (ns == null)
    {
      return null;
    }
    SchemeIdentifier first = ns.findFirstChildByClass(SchemeIdentifier.class);
    if (first == null)
    {
      return null;
    }
    SchemeIdentifier snd = SchemePsiUtil.findNextSiblingByClass(first, SchemeIdentifier.class);
    if (snd == null)
    {
      return null;
    }

    return snd.getNameString();
  }

  public SchemeList getNamespaceElement()
  {
    // TODO CMF
    return null; //SchemePsiUtil.findFormByNameSet(this, SchemeParser.NS_TOKENS);
  }
}
