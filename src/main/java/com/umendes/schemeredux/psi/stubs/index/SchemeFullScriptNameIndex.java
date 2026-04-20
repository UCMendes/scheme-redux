package com.umendes.schemeredux.psi.stubs.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.IntStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.umendes.schemeredux.psi.impl.SchemeFile;
import com.umendes.schemeredux.psi.impl.search.SchemeSourceFilterScope;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public class SchemeFullScriptNameIndex extends IntStubIndexExtension<SchemeFile>
{
  public static final StubIndexKey<Integer, SchemeFile> KEY = StubIndexKey.createIndexKey("scm.script.fqn");

  public @NotNull StubIndexKey<Integer, SchemeFile> getKey()
  {
    return KEY;
  }

  public Collection<SchemeFile> get(Integer integer, Project project, GlobalSearchScope scope)
  {
    return super.get(integer, project, new SchemeSourceFilterScope(scope, project));
  }
}