package de.frittenburger.translate.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.frittenburger.translate.impl.DictionaryReaderImpl;
import de.frittenburger.translate.interfaces.DictionaryReader;


public class ImportTool {

	
	public static void main(String[] args) throws IOException {

		AnnotationParser parser = new AnnotationParser();
		try(
				InputStream is = new FileInputStream("tmp/dict_es_de.txt.import");
				BufferedReader in = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8))
		   )
		{
			
			File folder = new File("tmp");
			DictionaryReader nouns = new DictionaryReaderImpl(new File(folder,"100_nouns.txt"));
			DictionaryReader adjectives = new DictionaryReaderImpl(new File(folder,"101_adjectives.txt"));
			DictionaryReader adverbs = new DictionaryReaderImpl(new File(folder,"102_adverbs.txt"));
			DictionaryReader pronoms = new DictionaryReaderImpl(new File(folder,"103_pronoms.txt"));
			DictionaryReader verbs = new DictionaryReaderImpl(new File(folder,"104_verbs.txt"));
			DictionaryReader others = new DictionaryReaderImpl(new File(folder,"105_others.txt"));

			
			while(true)
			{
				String line = in.readLine();
				if(line == null) break;
				if(line.trim().equals("")) continue;
				if(line.trim().startsWith("#")) continue;
				List<String> p = Arrays.stream(line.split("\t")).collect(Collectors.toList());
				
				Annotation es = parser.parse(p.get(0));
				Annotation de = parser.parse(p.get(1));
				String type = p.size() > 2 ? p.get(2) :"";
				


				switch(type)	
				{
				case "noun":
					nouns.append(es.getText(),de.getText());
					break;
				case "adj":
				case "adj adv":
					adjectives.append(es.getText(),de.getText());
					break;
				case "adv":
					adverbs.append(es.getText(),de.getText());
					break;
				case "pron":
					pronoms.append(es.getText(),de.getText());
					break;
				case "verb":
					verbs.append(es.getText(),de.getText());
					break;
				case "prep":
				case "conj":
				case "prep conj":
				case "prep adv":
				case "adj past-p":
				case "adj pres-p":
				case "adj prefix":
				case "adv prep":
				case "past-p":
					others.append(es.getText(),de.getText());
					break;
				case "":
					//System.out.println("Ignore: "+es.getText() +"=" + de.getText());
					break;
				
				default:
					System.err.println("es="+es+" de="+de+" type="+type+" p="+p);
					break;
				}
			}
		} 
		
		
		
		
	}

}
