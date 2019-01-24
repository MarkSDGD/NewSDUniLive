package com.xike.xkliveplay.framework.format;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Saxhandler extends DefaultHandler{
	List<HashMap<String, String>> list = null;
    String currentTag = null;
    String currentValue = null;
    String nodename = null;
    HashMap<String, String> map = null;

    public Saxhandler(String nodename) {
        this.nodename = nodename;
    }

    public List<HashMap<String, String>> getList() {
        return list;
    }

    @Override

    public void startDocument() throws SAXException {

        list = new ArrayList<HashMap<String, String>>();
    }

    @Override

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (qName.equals(nodename)) {
            map = new HashMap<String, String>();

        }

        if (attributes != null && map != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                map.put(attributes.getQName(i), attributes.getValue(i));
            }
        }

        currentTag = qName;

    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals(nodename)) {
            list.add(map);
            map = null;
        }
        super.endElement(uri, localName, qName);
    }

    @Override

    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentTag != null && map != null) 
        {
            currentValue = new String(ch, start, length);
            if (currentValue != null && !currentValue.trim().equals("")
                    && !currentValue.trim().equals("\n")) {
                map.put(currentTag, currentValue);
            }

//            System.out.println("value:*****" + currentValue);
        }

        currentTag = null;
        currentValue = null;
    }

}
