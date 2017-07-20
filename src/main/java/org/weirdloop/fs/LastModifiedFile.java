package org.weirdloop.fs;

import java.io.File;

public class LastModifiedFile {
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
