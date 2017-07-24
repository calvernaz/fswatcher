package org.weirdloop.fs;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class DirWatcher {

  private final FileFilter filter;
  private final File file;

  public DirWatcher(File file, FileFilter filter) {
    this.file = file;
    this.filter = filter;
  }

  public List<File> lastModifiedFiles(Predicate<File> unique) {
    if (Objects.isNull(unique)) {
      return listFilteredFiles();
    }
    return oneOfEachFilteredFile(unique);
  }

  private List<File> oneOfEachFilteredFile(Predicate<File> pred) {
    return listFilteredFiles().stream()
        .sorted(Comparator.comparingLong(File::lastModified))
        .filter(pred)
        .collect(toList());

  }

  private List<File> listFilteredFiles() {
    File[] files = file.listFiles(filter);
    return files != null ? Arrays.asList(files) : new ArrayList<>(0);
  }


}
