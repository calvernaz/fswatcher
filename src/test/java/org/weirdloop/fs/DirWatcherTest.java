package org.weirdloop.fs;

import java.io.File;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.junit.Test;

import java.io.FileFilter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class DirWatcherTest {

	@Test
	public void should_return_sensor_list() {
		DirWatcher dw = new DirWatcher(new File("src/test/resources"), filter());
		List<File> fnames = dw.lastModifiedFiles(hourAgo());
		List<String> ids = normalize(fnames);
		assertThat(ids, containsInAnyOrder("1234", "2345", "4567"));
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
	
	private Predicate<LastModifiedFile> hourAgo() {
		Date from = Date.from(Instant.now().minus(60, ChronoUnit.MINUTES));
		return lastModifiedFile -> new Date(lastModifiedFile.getModifiedTime()).before(from);
	}
	
}
