package com.xike.xkliveplay.framework.format;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlPara {

	public static List<HashMap<String, String>> parser(String rootElement,String body)
	{
		List<HashMap<String, String>> list;
		try {
			SAXParser parse = SAXParserFactory.newInstance().newSAXParser();
			
		    Saxhandler handler = new Saxhandler(rootElement);
		    
		    InputStream is = new ByteArrayInputStream(body.getBytes("UTF-8")); 
		    
		    parse.parse(is, handler);
		  
		    list = handler.getList();
		    
		    return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
}
