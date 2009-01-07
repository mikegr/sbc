package marmik.xvsm;

import org.xvsm.core.AtomicEntry;

public class GenericHelper {
  @SuppressWarnings("unchecked")
  public static AtomicEntry createAtomicEntry(Object element) {
    return new AtomicEntry(element, element.getClass());
  }
}
