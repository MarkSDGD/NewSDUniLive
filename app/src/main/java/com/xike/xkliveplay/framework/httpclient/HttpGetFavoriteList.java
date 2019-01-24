package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.entity.GetFavoriteList;
import com.xike.xkliveplay.framework.entity.GetFavoriteListRes;
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
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 下午1:42:52<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class HttpGetFavoriteList implements IRequestCallback{


	private static final String tag = "HttpGetFavoriteList";
	public static final String Method = "GetFavoriteList";
	private static final String ROOTELEMENT = "Channel";
	private GetFavoriteList req = null;

	private GetFavoriteListRes res = null;
	private String totalCount = "";
	private String currentCount = "";
	private IUpdateData iUpdateData = null;

	public HttpGetFavoriteList(IUpdateData _update) {
		iUpdateData = _update;
	}


	public void post() {
		if (req == null) {
			System.err.println("req is not null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		try {
			AsyncHttpRequest request = new AsyncHttpRequest();
			request.post(VarParam.url, Method, req, this);
		} catch (Exception e) {
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}

	@Override
	public void requestDidFinished(String flag, String body) {

		if (!Method.equals(flag)) {
			iUpdateData.updateData(Method, null, null, false);
			return;
		}

		List<ContentChannel> list = parser(ROOTELEMENT, body);
		if (list == null) {
			LogUtil.e(tag, "requestDidFinished","requestDidFinished XmlPara is null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		} else
			LogUtil.e(tag, "requestDidFinished","list.size = " + list.size());

		res = new GetFavoriteListRes();
		res.setChannelList(list);
		res.setTotalCount(totalCount);
		res.setCurrentCount(currentCount);
		
		this.iUpdateData.updateData(Method, null, list, true);

		LogUtil.i(tag, "requestDidFinished","requestDidFinished res is:" + res.toString());
	}

	@Override
	public void requestDidFailed(String method,String body)
	{
		// TODO Auto-generated method stub
		this.iUpdateData.updateData(Method, null, null, false);
	}

	public GetFavoriteList getReq() {
		return req;
	}

	public void setReq(GetFavoriteList req) {
		this.req = req;
	}

	public GetFavoriteListRes getRes() {
		return res;
	}

	public void setRes(GetFavoriteListRes res) {
		this.res = res;
	}

	public List<ContentChannel> parser(String rootElement, String body) {
		List<ContentChannel> list;
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

		List<ContentChannel> list = null;// 存储所有的解析对象
		String currentTag = null; // 正在解析的标签
		String currentValue = null; // 正在解析元素的值
		String nodename = null; // 正在解析节点名称
		ContentChannel channel = null;

		public NativeHandler(String nodename) {
			this.nodename = nodename;
		}

		public List<ContentChannel> getList() {
			return list;
		}

		@Override
		// 读到第一个开始标签的时候触发
		public void startDocument() throws SAXException {

			list = new ArrayList<ContentChannel>();
		}

		@Override
		// 当遇到所要解析的节点名称时触发
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
//			Log.e(tag, "localName = " + localName + "qName = " + qName);
			if (localName.equals("GetFavoriteListResult")) {
				for (int i = 0; i < attributes.getLength(); i++) {
					String key = attributes.getQName(i);
					String value = attributes.getValue(i);
//					Log.e(tag, "key = " + key + " ,value = " + value);
					if (key.equals("totalCount")) {
						totalCount = value;
					}else if (key.equals("currentCount")) {
						currentCount = value;
					}
				}
			}
			

			if (qName.equals(nodename)) {
				channel = new ContentChannel();
				if (attributes != null && list != null) {
					for (int i = 0; i < attributes.getLength(); i++) {
						String key = attributes.getQName(i);
						String value = attributes.getValue(i);
						if (key.equals("contentId")) {
							channel.setContentId(value);
						} else if (key.equals("channelNumber")) {
							channel.setChannelNumber(value);
						} else if (key.equals("name")) {
							channel.setName(value);
						} else if (key.equals("callSign")) {
							channel.setCallSign(value);
						} else if (key.equals("timeShift")) {
							channel.setTimeShift(value);
						} else if (key.equals("timeShiftDuration")) {
							channel.setTimeShiftDuration(value);
						} else if (key.equals("description")) {
							channel.setDescription(value);
						} else if (key.equals("country")) {
							channel.setCountry(value);
						} else if (key.equals("logoURL")) {
							channel.setLogoURL(value);
						}else if (key.equals("state")) {
							channel.setState(value);
						}else if (key.equals("city")) {
							channel.setCity(value);
						}else if (key.equals("zipCode")) {
							channel.setZipCode("save");
						}else if (key.equals("playURL")) {
							channel.setPlayURL(value);
						}else if (key.equals("language")) {
							channel.setLanguage("save");
						}
					}
				}
			}
			currentTag = qName;

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (localName.equals("Channel")) {
				list.add(channel);
				return;
			}

		}

		@Override
		// 处理xml文件读取到的内容
		public void characters(char[] ch, int start, int length)
				throws SAXException {

//			String theString = new String(ch, start, length);

			return;

		}

	}


}
