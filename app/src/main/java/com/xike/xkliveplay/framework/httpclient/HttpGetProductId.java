package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Administrator
 * 
 */
public class HttpGetProductId implements IRequestCallback {

	private static final String tag = "HttpGetProductId";
	public static final String Method = "SERVICE.PRODUCTID";
	private static final String ROOTELEMENT = "service";
	private IUpdateData iUpdateData = null;
	private String productId = "";
	private String packageName = "";

	public HttpGetProductId(IUpdateData updateData, String packageName) {
		initTestData();

		this.iUpdateData = updateData;
		this.packageName = packageName;
	}

	private void initTestData() {
	}

	public void post() {

		try {
			// HttpSynConn.getInstance().post(VarParam.url, Method, req, this);
			AsyncHttpRequest aRequest = new AsyncHttpRequest();
			aRequest.post(VarParam.default_url, Method, null, this);
		} catch (Exception e) {
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}

	@Override
	public void requestDidFinished(String flag, String body) {
		// TODO Auto-generated method stub

		LogUtil.i(tag,"requestDidFinished","flag = " + flag + "requestDidFinished body is:" + body);

		if (!Method.equals(flag)) {
			iUpdateData.updateData(Method, null, null, false);
			return;
		}

	    parser(ROOTELEMENT, body);

		 this.iUpdateData.updateData(Method, null, productId,
		 true);
		// LogUtil.i(tag, "requestDidFinished res is:" + res.toSting());
	}

	@Override
	public void requestDidFailed(String method,String body) {
		// TODO Auto-generated method stub
		iUpdateData.updateData(Method, null, null, false);
	}

	public void parser(String rootElement, String body) {
		try {
			SAXParser parse = SAXParserFactory.newInstance().newSAXParser();

			NativeHandler handler = new NativeHandler(rootElement);

			InputStream is = new ByteArrayInputStream(body.getBytes("UTF-8"));

			parse.parse(is, handler);


		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public class NativeHandler extends DefaultHandler {

		String currentTag = null; // 正在解析的标签
		String currentValue = null; // 正在解析元素的值
		String nodename = null; // 正在解析节点名称

		public NativeHandler(String nodename) {
			this.nodename = nodename;
		}


		@Override
		// 读到第一个开始标签的时候触发
		public void startDocument() throws SAXException {

		}

		@Override
		// 当遇到所要解析的节点名称时触发
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if (qName.equals(nodename)) {
				if (attributes != null ) {
					for (int i = 0; i < attributes.getLength(); i++) {
						String key = attributes.getQName(i);
						String value = attributes.getValue(i);

						// System.out.println("&&&&&key:" + key + ",value:" +
						// value);
						if (key.equals("id")) {
							if (value.equals(packageName)) {
								productId = attributes.getValue("productId");
								break;
							}
							
						}
					}
				}
			}
			currentTag = qName;

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
//			if (localName.equals("Category")) {
//				list.add(category);
//				return;
//			}

		}

		@Override
		// 处理xml文件读取到的内容
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			// String theString = new String(ch,start,length);

			return;

		}

	}

}
