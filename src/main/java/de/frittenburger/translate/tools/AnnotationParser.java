package de.frittenburger.translate.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnotationParser {

	public Annotation parse(String text) {

		Annotation annotation = new Annotation();
		
        Pattern pattern = Pattern.compile("[{](.+)[}]");
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()) {
        	text = text.substring(0,matcher.start()) + text.substring(matcher.end());
        	annotation.setMark(matcher.group(1));
        }
		
        Pattern pattern2 = Pattern.compile("\\[(.+)\\]");
        Matcher matcher2 = pattern2.matcher(text);
        if(matcher2.find()) {
        	text = text.substring(0,matcher2.start()) + text.substring(matcher2.end());
        	annotation.setAlternatives(matcher2.group(1));
        }
        
        Pattern pattern3 = Pattern.compile("\\((.+)\\)");
        Matcher matcher3 = pattern3.matcher(text);
        if(matcher3.find()) {
        	text = text.substring(0,matcher3.start()) + text.substring(matcher3.end());
        }
        
        text = text.replaceAll("\\s{2,}", " ");
        text = text.trim();
	    annotation.setText(text);
	    
		return annotation;
	}

}
