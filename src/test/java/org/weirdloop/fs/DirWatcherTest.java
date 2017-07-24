package org.weirdloop.fs;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileFilter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.hamcrest.core.StringContains;
import org.junit.Test;

public class DirWatcherTest {

  @Test
  public void should_return_sensor_list() {
    DirWatcher dw = new DirWatcher(new File("src/test/resources"), filter());
    List<File> fnames = dw
        .lastModifiedFiles(distinctByKey(f -> f.getName().substring(0, f.getName().indexOf('_'))));
    List<String> ids = normalize(fnames);
    assertThat(ids, containsInAnyOrder("1234", "2345", "4567"));
  }

  @Test
  public void should_return_last_modified_file() {
    DirWatcher dirWatcher = new DirWatcher(new File("src/test/resources"), a1234_filter());
    List<File> fnames = dirWatcher
        .lastModifiedFiles(distinctByKey(f -> f.getName().substring(0, f.getName().indexOf('_'))));
    assertThat(fnames.get(0).getName(), StringContains.containsString("1234_12345465645.csv"));
  }

  private FileFilter a1234_filter() {
    return new PrefixFileFilter(
        Stream.of("1234").map(s -> String.format("%s_", s)).collect(toList()));
  }

  private List<String> normalize(List<File> fnames) {
    return fnames.stream().map(s -> s.getName().split("_")[0]).collect(Collectors.toList());
  }

  private FileFilter filter() {
    return new PrefixFileFilter(prefixedSensors());
  }

  private List<String> prefixedSensors() {
    return Stream.of("1234", "2345", "4567").map(s -> String.format("%s_", s)).collect(toList());
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }
}

