package de.frittenburger.translate.model;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class Dictionary extends LinkedHashMap<String,String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5988134538695851906L;

	private static final Logger logger = LogManager.getLogger(Dictionary.class);

	public void insert(Dictionary dict) {
		
		 Iterator<String> itr = dict.keySet().iterator();
	     while (itr.hasNext()) 
	     {
	    	 String key = itr.next();
	    	 insert(key,dict.get(key));
	     }
		
	}

	public void insert(String key, String value) {
		
		 if(containsKey(key))
    	 {
    		 //ignore, always exists
    		 logger.info("Key {} always exists {} / {}",key,get(key),value);
    		 return;
    	 }
    	 
    	 put(key,value);
		
	}

}
