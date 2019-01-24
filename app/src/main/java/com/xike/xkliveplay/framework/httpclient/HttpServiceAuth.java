/**
 * @author 李伟
 * @date:2014-2-24 下午06:29:12
 * @version : V1.0
 *
 */

package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.Product;
import com.xike.xkliveplay.framework.entity.ServiceAuth;
import com.xike.xkliveplay.framework.entity.ServiceAuthRes;
import com.xike.xkliveplay.framework.http.AsyncHttpRequest;
import com.xike.xkliveplay.framework.http.IRequestCallback;
import com.xike.xkliveplay.framework.http.IUpdateData;
import com.xike.xkliveplay.framework.tools.LogUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author lw 业务鉴权接口
 * 
 */
public class HttpServiceAuth implements IRequestCallback {

	private static final String tag = "HttpServiceAuth";
	public static final String Method = "ServiceAuth";
	private static final String ROOTELEMENT = "Category";
	private ServiceAuth req = null;
	private IUpdateData iUpdateData = null;
	private ServiceAuthRes res = null;

	private String balance = "";
	private String playUrl = "";
	private String expiredTime = "";

	public HttpServiceAuth(IUpdateData updateData) {
//		initTestData();
		iUpdateData = updateData;
	}
	
	

	public HttpServiceAuth(ServiceAuth req, IUpdateData iUpdateData) {
		super();
		this.req = req;
		this.iUpdateData = iUpdateData;
	}



	@SuppressWarnings("unused")
	private void initTestData() {
		req = new ServiceAuth();
		req.setContentID("CHANNEL00000051"); // 可不填
		req.setUserId("testdx11");
		req.setUserToken("FA64182F930B3382409CD25D");
		req.setProductId("PRODUCT2013101000000003");
	}

	public void post() {
		if (req == null) {
			System.err.println("req is not null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		try {
//			HttpSynConn.getInstance().post(VarParam.url, Method, req, this);
			AsyncHttpRequest request = new AsyncHttpRequest();
			request.post(VarParam.url, Method, req, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}

	@Override
	public void requestDidFinished(String flag, String body) {
		// TODO Auto-generated method stub

		LogUtil.i(tag,"requestDidFinished","flag = "+flag + " requestDidFinished body is:" + body);

		if (!Method.equals(flag)) {
			iUpdateData.updateData(Method, null, null, false);
			return;
		}

		List<Product> list = parser(ROOTELEMENT, body);
		if (list == null) {
			LogUtil.e(tag, "requestDidFinished","requestDidFinished XmlPara is null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		} else
			LogUtil.e(tag, "requestDidFinished","list.size = " + list.size());

		res = new ServiceAuthRes();
		res.setProductList(list);
		res.setBalance(balance);
		res.setExpiredTime(expiredTime);
		res.setPlayUrl(playUrl);

		iUpdateData.updateData(Method, null, res, true);
		LogUtil.i(tag, "requestDidFinished","requestDidFinished res is:" + res.toString());
	}

	@Override
	public void requestDidFailed(String method,String body) {
		// TODO Auto-generated method stub
		iUpdateData.updateData(Method, null, null, false);
	}

	public ServiceAuth getReq() {
		return req;
	}

	public void setReq(ServiceAuth req) {
		this.req = req;
	}

	public ServiceAuthRes getRes() {
		return res;
	}

	public void setRes(ServiceAuthRes res) {
		this.res = res;
	}

	public List<Product> parser(String rootElement, String body) {
		List<Product> list;
		try {
			SAXParser parse = SAXParserFactory.newInstance().newSAXParser();

			NativeHandler handler = new NativeHandler(rootElement);

			InputStream is = new ByteArrayInputStream(body.getBytes("UTF-8"));

			parse.parse(is, handler);

			list = handler.getList();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	public class NativeHandler extends DefaultHandler {

		List<Product> list = null;// 存储所有的解析对象
		String currentTag = null; // 正在解析的标签
		String currentValue = null; // 正在解析元素的值
		String nodename = null; // 正在解析节点名称
		Product product = null;

		public NativeHandler(String nodename) {
			this.nodename = nodename;
		}

		public List<Product> getList() {
			return list;
		}

		@Override
		// 读到第一个开始标签的时候触发
		public void startDocument() throws SAXException {

			list = new ArrayList<Product>();
		}

		@Override
		// 当遇到所要解析的节点名称时触发
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if (localName.equals("ServiceAuthResult")) {
				for (int i = 0; i < attributes.getLength(); i++) {
					String key = attributes.getQName(i);
					String value = attributes.getValue(i);
//					Log.e(tag, "key = " + key + " ,value = " + value);
					if (key.equals("expiredTime")) {
						expiredTime = value;
					}else if (key.equals("balance")) {
						balance = value;
					}else if (key.equals("playUrl")) {
						playUrl = value;
					}
				}
			}
			
			if (qName.equals(nodename)) {
				product = new Product();
				if (attributes != null && list != null) {
					for (int i = 0; i < attributes.getLength(); i++) {
						String key = attributes.getQName(i);
						String value = attributes.getValue(i);

						System.out.println("&&&&&key:" + key + ",value:"
								+ value);
						if (key.equals("productId")) {
							product.setProductId(value);
						} else if (key.equals("productName")) {
							product.setProductName(value);
						} else if (key.equals("fee")) {
							product.setFee(value);
						} else if (key.equals("purchaseType")) {
							product.setPurchaseType(value);
						} else if (key.equals("productDesc")) {
							product.setProductDesc(value);
						} else if (key.equals("listPrice")) {
							product.setListPrice(value);
						} else if (key.equals("rentalTerm")) {
							product.setRentalTerm(value);
						} else if (key.equals("limitTimes")) {
							product.setLimitTimes(value);
						}
					}
				}
			}
			currentTag = qName;

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (localName.equals("Product")) {
				list.add(product);
				return;
			}

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
