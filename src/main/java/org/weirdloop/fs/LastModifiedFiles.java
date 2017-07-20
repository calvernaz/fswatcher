package org.weirdloop.fs;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
