package de.frittenburger.translate.impl;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.translate.interfaces.DictionaryReader;
import de.frittenburger.translate.interfaces.FallbackTranslateService;
import de.frittenburger.translate.interfaces.TranslateService;
import de.frittenburger.translate.model.Dictionary;


public class TranslateServiceImpl implements TranslateService {

	private static final Logger logger = LogManager.getLogger(TranslateServiceImpl.class);

	private final String toLang;
	private final String fromLang;

	private final File folder;
	private final Dictionary dictionary = new Dictionary();

	private FallbackTranslateService fallbackservice;


	public TranslateServiceImpl(String fromLang, String toLang,FallbackTranslateService fallbackservice) {
	
		this.fromLang = fromLang;
		this.toLang = toLang;
		this.folder = new File("dict/"+fromLang+"-"+toLang);
		if(!this.folder.isDirectory())
			throw new IllegalArgumentException(this.folder.getPath()+" has to be a directory");
		
		this.fallbackservice = fallbackservice;
		
		
		List<String> dictionaryFilenames = Arrays.stream(folder.list()).filter(fn -> fn.endsWith(".txt")).sorted().collect(Collectors.toList());
		
		for(String filename : dictionaryFilenames)
		{
			logger.info("Read {}",filename);
			DictionaryReader dictionaryReader = new DictionaryReaderImpl(new File(folder,filename));
			
			try {
				Dictionary dict = dictionaryReader.readAll();
				dictionary.insert(dict);
			} catch (IOException e) {
				logger.error(e);
			}
		}

				 
	
				
		
	}

	@Override
	public String translate(String text) {

		if(dictionary.containsKey(text))
			return dictionary.get(text);
		
		
		String translation = fallbackservice.translate(text,fromLang,toLang);
		
		if(translation == null) 
			return null;
				
		dictionary.put(text, translation);
		
		
		DictionaryReader dictionaryReader = new DictionaryReaderImpl(new File(folder,"999_fallback.txt"));

		try {
			dictionaryReader.append(text,translation);
		} catch (IOException e) {
			logger.error(e);
		}
		
		
		
		return translation;
	}
	

}
