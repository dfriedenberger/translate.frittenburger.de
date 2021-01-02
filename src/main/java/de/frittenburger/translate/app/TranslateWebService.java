package de.frittenburger.translate.app;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.translate.impl.AwsTranslateService;
import de.frittenburger.translate.impl.TranslateServiceImpl;
import de.frittenburger.translate.interfaces.TranslateService;

public class TranslateWebService {

	
	private static final Logger logger = LogManager.getLogger(TranslateWebService.class);

	public static void main(String[] args) throws IOException {
		

		
		Map<String,TranslateService> translateServices = new HashMap<>();
		AwsTranslateService awsTranslateService = new AwsTranslateService(new File("config/aws.json"));
		translateServices.put("es-de", new TranslateServiceImpl("es","de",awsTranslateService));
		
		port(4568);

		staticFileLocation("/htdocs");
         
		

		 
		 post("/translate",(request, response) -> {
			 

			    String bearerToken = null;
				String auth = request.headers("Authorization");
				if(auth != null && auth.startsWith("Bearer")) {
					bearerToken = auth.substring("Bearer".length()).trim();
				}
			 
				if(bearerToken == null || bearerToken.isEmpty())
				{
					response.header("WWW-Authenticate", "Bearer");
			        halt(401, "You need a Bearer token");
				}
			 
			 
			    response.type("application/json");
			    Map<String,Object> data = new HashMap<String,Object>();
			    
			    String textstr = request.queryParams("text");
			    String sourceLanguage = request.queryParams("sourcelanguage");
			    String targetLanguage = request.queryParams("targetlanguage");
			    
			    
			    String translateKey = sourceLanguage +"-"+targetLanguage;
			    if(!translateServices.containsKey(translateKey))
			    {
				    halt(404, "tranlate "+translateKey+" not supported");
			    }
		    				
		    	List<String> result = new ArrayList<>();

			    String[] textlist = textstr.split("\n");
			    for(int i = 0;i < textlist.length;i++)
			    {
			    	String text = textlist[i].trim();
			    	if(text.isEmpty()) 
			    	{
			    		result.add("");
			    	}
					String res = translateServices.get(translateKey).translate(text);
					logger.info("{} -> {}",text,res);
		 			result.add(res);
			    }
			    
		    	data.put("text",result);
		    	data.put("status", "OK");
			 			
			    return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data);
		 });
		 
		 logger.info("ready, start on "+port());
	}


	

}
