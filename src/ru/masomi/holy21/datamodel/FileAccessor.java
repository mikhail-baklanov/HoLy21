package ru.masomi.holy21.datamodel;

import java.io.IOException;
import java.io.InputStream;

public interface FileAccessor {
	InputStream getFileInputStream(String fileName) throws IOException;
}
