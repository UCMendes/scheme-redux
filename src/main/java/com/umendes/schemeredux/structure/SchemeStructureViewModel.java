package com.umendes.schemeredux.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.umendes.schemeredux.psi.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SchemeStructureViewModel extends StructureViewModelBase implements
        StructureViewModel.ElementInfoProvider
{
  public SchemeStructureViewModel(@Nullable Editor editor, PsiFile file)
  {
    super(file, editor, new SchemeStructureViewElement(file));
  }

  public Sorter @NotNull [] getSorters()
  {
    return new Sorter[] { Sorter.ALPHA_SORTER };
  }

  protected Class<?> @NotNull [] getSuitableClasses()
  {
    return new Class[] { SchemeFormDefineBase.class,
            SchemeFormLibrary.class, SchemeFormExport.class, SchemeSymbolDefine.class, SchemeDatumLabel.class};
  }

  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement structureViewTreeElement) {
    return false;
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement structureViewTreeElement) {
    return false;
  }
}
