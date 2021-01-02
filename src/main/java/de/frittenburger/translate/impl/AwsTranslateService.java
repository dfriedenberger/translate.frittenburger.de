package de.frittenburger.translate.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.translate.interfaces.FallbackTranslateService;

public class AwsTranslateService implements FallbackTranslateService  {

	
	private static final Logger logger = LogManager.getLogger(AwsTranslateService.class);

	private final AmazonTranslate translate;

	public AwsTranslateService(File awsConfig) {
		
		//Fallback-Service via AWS 
		Map<String, String> config = null;
		try {
			config = new ObjectMapper().readValue(new File("config/aws.json"),new TypeReference<HashMap<String,String>>() {});
		} catch (IOException e) {
			logger.error(e);
		}
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(config.get("accessKeyId"), config.get("secretAccessKey"));

		translate = AmazonTranslateClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(config.get("region"))
                .build();
	}
	
	@Override
	public String translate(String text, String sourceLanguage, String targetLanguage) {
		
		TranslateTextRequest request = new TranslateTextRequest()
		        .withText(text)
		        .withSourceLanguageCode(sourceLanguage)
		        .withTargetLanguageCode(targetLanguage);

		TranslateTextResult result  = translate.translateText(request);
		String translation = result.getTranslatedText();
		
		return translation;
	}

}
