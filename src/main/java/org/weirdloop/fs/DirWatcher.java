package org.weirdloop.fs;


import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class DirWatcher {
	private final FileFilter filter;
	private final File file;
	
	public DirWatcher(File file, FileFilter filter) {
		this.file = file;
		this.filter = filter;
	}
	
	public List<String> lastModifiedFiles(Predicate<LastModifiedFile> pred) {
		File[] files = file.listFiles(filter);
		requireNonNull(files);
		List<LastModifiedFile> modifiedFiles = Stream.of(files)
				.map(LastModifiedFile::of).collect(toList());
		LastModifiedFiles lmf = new LastModifiedFiles(modifiedFiles);
		return lmf.modifiedAfter(pred);
	}

}
