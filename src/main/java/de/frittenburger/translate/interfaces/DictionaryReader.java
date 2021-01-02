package de.frittenburger.translate.interfaces;

import java.io.IOException;

import de.frittenburger.translate.model.Dictionary;

public interface DictionaryReader {

	Dictionary readAll() throws IOException;

	void append(String text, String translation) throws IOException;

	void append(String text, String translation, String comment) throws IOException;

}
