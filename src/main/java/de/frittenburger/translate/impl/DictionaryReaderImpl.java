package de.frittenburger.translate.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import de.frittenburger.translate.interfaces.DictionaryReader;
import de.frittenburger.translate.model.Dictionary;

public class DictionaryReaderImpl implements DictionaryReader {

	private final File file;

	public DictionaryReaderImpl(File file) {
		this.file = file;
	}

	@Override
	public Dictionary readAll() throws IOException {
		
		try(
				InputStream is = new FileInputStream(file);
				BufferedReader in = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8))
		   )
		{
			Dictionary dictionary  = new Dictionary();

			while(true)
			{
				String line = in.readLine();
				if(line == null) break;
				if(line.trim().equals("")) continue;
				if(line.trim().startsWith("#")) continue;
				int i = line.indexOf("::");
				if(i < 0) continue;
				String key = line.substring(0, i).trim();
				String[] values = line.substring(i+2).trim().split(";");
				dictionary.insert(key, values[0]);
			}
			return dictionary;
		} 
	}

	@Override
	public void append(String text, String translation) throws IOException {
		
		try (OutputStreamWriter writer =
	             new OutputStreamWriter(new FileOutputStream(file,true), StandardCharsets.UTF_8))
	    {
			writer.write(text +"::"+translation+"\r\n");
	    } 
	
		
	}
	@Override
	public void append(String text, String translation,String comment) throws IOException {
		
		try (OutputStreamWriter writer =
	             new OutputStreamWriter(new FileOutputStream(file,true), StandardCharsets.UTF_8))
	    {
			writer.write("#"+comment+"\r\n");
			writer.write(text +"::"+translation+"\r\n");
	    } 
	
		
	}

}
