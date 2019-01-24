package com.xike.xkliveplay.framework.http;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.annotation.SuppressLint;
import com.xike.xkliveplay.framework.tools.BeanUtil;

public class TR069Util {
	// 请求接口时固定格式string
//	public static String REQUEST_STRING_START = "<soap:Envelope xmlns:encoding=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cwmp=\"urn:dslforum-org:cwmp-1-2\">"
//			+ "<soap:Body>";
	// 请求接口时固定格式string
//	public static String REQUEST_STRING_END = "</soap:Body>"
//			+ "</soap:Envelope>";
	// 请求接口时固定格式string
//	public static String REQUEST_ALL = REQUEST_STRING_START
//			+ REQUEST_STRING_END;

	@SuppressLint("DefaultLocale")
	public static String BeanToXml(Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		Map<String, BeanUtil.BeansInfo> map = BeanUtil.getBeanInfo(clazz);
		String classname = clazz.getSimpleName();

		StringBuffer bf = new StringBuffer();
//		bf.append(REQUEST_STRING_START).append("\n");
		bf.append("<").append(classname).append(">").append("\n");

		Field[] field = clazz.getDeclaredFields();
		for (Field field2 : field) {
			String name = field2.getName();
			String value = "";
			if (map.containsKey(name.toUpperCase())) {
				value = (String) map.get(name.toUpperCase()).getValue(obj);
			}

			if (!"".equals(value) && !"null".equals(value)) {
				bf.append("\t<").append(name).append(">").append(value)
						.append("</").append(name).append(">").append("\n");
			}
		}

		bf.append("</").append(classname).append(">").append("\n");
//		bf.append(REQUEST_STRING_END);

		return bf.toString();
	}
	
	public static String BeanToXmlOther(Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		Map<String, BeanUtil.BeansInfo> map = BeanUtil.getBeanInfo(clazz);
		String classname = clazz.getSimpleName();

		StringBuffer bf = new StringBuffer();
//		bf.append(REQUEST_STRING_START).append("\n");
		bf.append("<").append(classname).append(" ");

		Field[] field = clazz.getDeclaredFields();
		for (Field field2 : field) {
			String name = field2.getName();
			String value = "";
			if (map.containsKey(name.toUpperCase())) {
				value = (String) map.get(name.toUpperCase()).getValue(obj);
			}

			if (!"".equals(value) && !"null".equals(value)) {
				bf.append("\t").append(name).append("=\"").append(value)
						.append("\"");
			}
		}
		bf.append(">").append("\n");
		bf.append("</").append(classname).append(">").append("\n");
//		bf.append(">").append("\n");
//		bf.append(REQUEST_STRING_END);

		return bf.toString();
	}

	public static List<Object> XmlToBean(Class<?> clazz, String xmlString)
			throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(
				new StringReader(xmlString)));
//		Element element = document.getDocumentElement();
		String className = clazz.getSimpleName();

		NodeList targetNode = document.getElementsByTagName(className);
		if (targetNode.getLength() == 0) {
//			LogUtil.i(LogUtil.TOOLS, "无数据");
		}

		List<Object> list = new ArrayList<Object>();
		Map<String, BeanUtil.BeansInfo> map = BeanUtil.getBeanInfo(clazz);
		for (int i = 0; i < targetNode.getLength(); i++) {
			org.w3c.dom.Node n = targetNode.item(i);
			NodeList n1 = n.getChildNodes();
			Object instance = clazz.newInstance();
			for (int j = 0; j < n1.getLength(); j++) {
				org.w3c.dom.Node n2 = n1.item(j);
				String name = n2.getNodeName().toUpperCase();
				String value = n2.getTextContent();

				if (map.containsKey(name)) {
					map.get(name).setValue(instance, value);
				}
			}
			list.add(instance);
		}
		return list;
	}

}
