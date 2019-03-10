package org.weirdloop.fs;


import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
	
	/**
	 * Constructs the directory scanner for a given directory
	 * @param dir the directory to act on.
	 * @param filter a file filter to apply to the scan.
	 */
	public DirScanner(Path dir, FileFilter filter) {
		this.path = dir;
		this.filter = filter;
	}
	
	/**
	 * Retrieves the list of recent modified files in descending order.
	 *
	 * @return a list of files.
	 * @throws IOException
	 */
	public List<File> recentModifiedFiles() throws IOException {
		try (DirectoryStream<Path> stream = filterFiles()){
			return StreamSupport
					.stream(stream.spliterator(), false)
					.map(Path::toFile)
					.sorted(Comparator.comparingLong(File::lastModified).reversed())
					.collect(Collectors.toList());
		}
	}
	
	/**
	 * Retrieves the list of recent modified files in descending order,
	 * and applies a {@code predicate} to each file.
	 *
	 * @return a list of files.
	 * @throws IOException
	 */
	public List<File> recentModifiedFiles(Predicate<File> predicate) throws IOException {
		Objects.requireNonNull(predicate);
		try (DirectoryStream<Path> stream = filterFiles()) {
			return StreamSupport
					.stream(stream.spliterator(), false)
					.map(Path::toFile)
					.sorted(Comparator.comparingLong(File::lastModified).reversed())
					.filter(predicate)
					.collect(toList());
		}
	}
	
	private DirectoryStream<Path> filterFiles() throws IOException {
		return Files
				.newDirectoryStream(path, entry -> filter.accept(entry.toFile()));
	}
	
	
}
