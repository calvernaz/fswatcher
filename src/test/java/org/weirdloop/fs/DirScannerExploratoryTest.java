package org.weirdloop.fs;

import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class DirScannerExploratoryTest {
	
	@Test
	public void should_filter_files_with_prefix() {
		File[] files = prefixFilter();
		assertThat(files != null ? files.length : 0, is(4));
	}
	
	@Test
	public void should_filter_files_and_retrieves_last_modified_times() {
		LastModifiedFiles lmf = lastModifiedFiles(prefixFilter());
		assertThat("Expected size 4", lmf, iterableWithSize(4));
		lmf.forEach(lastModifiedFile -> {
			assertThat("Modified times are bigger than 0", lastModifiedFile.getModifiedTime(), is(not(0)));
		});
	}
	
	@Test
	public void should_detect_last_modified_files_hour_ago() {
		LastModifiedFiles lmf = lastModifiedFiles(prefixFilter());
		List<File> sensorids = lmf.apply(hourAgo());
		assertThat(sensorids, iterableWithSize(is(0)));
	}
	
	private Predicate<LastModifiedFile> hourAgo() {
		Date from = Date.from(Instant.now().minus(60, ChronoUnit.MINUTES));
		return lastModifiedFile -> new Date(lastModifiedFile.getModifiedTime()).after(from);
	}
	
	private LastModifiedFiles lastModifiedFiles(File[] files) {
		List<LastModifiedFile> modifiedFiles = Stream.of(files)
				.map(LastModifiedFile::of).collect(toList());
		return new LastModifiedFiles(modifiedFiles);
	}
	
	private File[] prefixFilter() {
		FileFilter filter = new PrefixFileFilter(prefixedFilenames());
		File file = new File("src/test/resources");
		return file.listFiles(filter);
	}
	
	private List<String> prefixedFilenames() {
		return Stream.of("1234", "2345", "4567").map(s -> String.format("%s_", s)).collect(toList());
	}
}
