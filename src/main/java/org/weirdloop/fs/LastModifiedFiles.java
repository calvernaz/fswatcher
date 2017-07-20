package org.weirdloop.fs;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class LastModifiedFile {
	private File file;
	private Long modifiedTime;
	
	public static LastModifiedFile of(File file) {
		return new LastModifiedFile(file, file.lastModified());
	}
	
	public LastModifiedFile(File file, long modifiedTime) {
		this.file = file;
		this.modifiedTime = modifiedTime;
	}
	
	public Long getModifiedTime() {
		return modifiedTime;
	}
	
	public File getFile() {
		return file;
	}
	
}

public class LastModifiedFiles implements Iterable<LastModifiedFile> {
	private List<LastModifiedFile> lastModifiedFiles;
	
	public LastModifiedFiles(List<LastModifiedFile> modifiedFiles) {
		this.lastModifiedFiles = modifiedFiles;
	}
	
	@Override
	public Iterator<LastModifiedFile> iterator() {
		return lastModifiedFiles.iterator();
	}
	
	public List<String> modifiedAfter(Predicate<LastModifiedFile> pred) {
		return lastModifiedFiles.stream()
				.filter(pred)
				.map(lastModifiedFile -> lastModifiedFile.getFile().getName())
				.collect(Collectors.toList());
	}
}
