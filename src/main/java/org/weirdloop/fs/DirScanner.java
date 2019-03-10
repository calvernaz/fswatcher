package org.weirdloop.fs;


import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirScanner {
	
	private final FileFilter filter;
	private final Path path;
	
	/**
	 * Constructs the directory scanner for a given directory.
	 *
	 * @param dir the directory to act on.
	 * @param filter a file filter to apply to the scan.
	 */
	public DirScanner(File dir, FileFilter filter) {
		this(dir.toPath(), filter);
	}
	
	public DirScanner(Path dir, FileFilter filter) {
		this.path = dir;
		this.filter = filter;
	}
	
	public List<File> recentModifiedFiles() throws IOException {
		return filterFiles().collect(Collectors.toList());
	}
	
	public List<File> recentModifiedFiles(Predicate<File> predicate) throws IOException {
		Objects.requireNonNull(predicate);
		
		return filterFiles()
				.sorted(Comparator.comparingLong(File::lastModified).reversed())
				.filter(predicate)
				.collect(toList());
	}
	
	private Stream<File> filterFiles() throws IOException {
		return Files.list(path)
		            .filter(p -> filter.accept(p.toFile()))
		            .map(Path::toFile);
		
	}
	
	
}
