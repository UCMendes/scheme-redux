package com.umendes.schemeredux.psi.stubs.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.umendes.schemeredux.psi.impl.SchemeFile;
import org.jetbrains.annotations.NotNull;


public class SchemeClassNameIndex extends StringStubIndexExtension<SchemeFile>
{
  public static final StubIndexKey<String, SchemeFile> KEY = StubIndexKey.createIndexKey("scm.class");

  public @NotNull StubIndexKey<String, SchemeFile> getKey()
  {
    return KEY;
  }
}
