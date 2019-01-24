package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.GetScheduleList;
import com.xike.xkliveplay.framework.entity.GetScheduleListRes;
import com.xike.xkliveplay.framework.entity.Schedule;
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
 * CreateTime: 2014年10月30日 下午1:44:06<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class HttpGetScheduleList implements IRequestCallback {

	private static final String tag = "HttpGetScheduleList";
	public static final String Method = "GetScheduleList";
	private static final String ROOTELEMENT = "Schedule";
	private GetScheduleList req = null;

	private GetScheduleListRes res = null;
	private IUpdateData iUpdateData = null;
	private String totalCount = "";
	private String currentCount = "";

	public HttpGetScheduleList(IUpdateData updateData)
	{
		
		this.iUpdateData = updateData;
	}

	public HttpGetScheduleList(IUpdateData updateData, GetScheduleList req) {
		super();
		this.req = req;
		this.iUpdateData = updateData;
	}

	@SuppressWarnings("unused")
	private void initTestData() {
		req = new GetScheduleList();
		req.setCount("5");
		req.setOffset("0");
		req.setChannelIds("CHANNEL00000051");
		req.setStartDateTime("20140222123000");
		req.setEndDateTime("20140226123000");
		req.setUserId("testdx11");
		req.setUserToken("FA64182F930B3382409CD25D");
	}

	public void post() {
		if (req == null) {
			System.err.println("req is not null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		try {
			System.out.println("&&&&&&&&&getSchudel:" + req.toString());
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

		LogUtil.i(tag,"requestDidFinished","flag = " + flag + "requestDidFinished body is:" + body);

		if (!Method.equals(flag)) {
			iUpdateData.updateData(Method, null, null, false);
			return;
		}

		List<Schedule> list = parser(ROOTELEMENT, body);
		if (list == null) 
		{
			LogUtil.e(tag, "requestDidFinished","requestDidFinished XmlPara is null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		} else if (list.size() == 0) 
		{
			LogUtil.e(tag, "requestDidFinished","list.size is 0");
			return;
		}
		LogUtil.e(tag, "requestDidFinished","list.size = " + list.size());

		res = new GetScheduleListRes();
		res.setScheduleList(list);
		res.setTotalCount(totalCount);
		res.setCurrentCount(currentCount);
		
//		LogUtil.e(tag, "AAAAAAAAAAAA = " + req.getChannelIds());
		this.iUpdateData.updateData(Method,req.getChannelIds(),list, true);
//		LogUtil.i(tag, "requestDidFinished res is:" + res.toString());
	}

	@Override
	public void requestDidFailed(String method,String body) {
		iUpdateData.updateData(Method, null, null, false);
	}

	public GetScheduleList getReq() {
		return req;
	}

	public void setReq(GetScheduleList req) {
		this.req = req;
	}

	public List<Schedule> parser(String rootElement, String body) {
		List<Schedule> list;
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

	public List<Schedule> parser(String body) {
		List<Schedule> list;
		try {
			SAXParser parse = SAXParserFactory.newInstance().newSAXParser();

			NativeHandler handler = new NativeHandler(ROOTELEMENT);

			InputStream is = new ByteArrayInputStream(body.getBytes("UTF-8"));

			parse.parse(is, handler);

			list = handler.getList();

			return list;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public class NativeHandler extends DefaultHandler {

		List<Schedule> list = null;// 存储所有的解析对象
		String currentTag = null; // 正在解析的标签
		String currentValue = null; // 正在解析元素的值
		String nodename = null; // 正在解析节点名称
		Schedule schedule = null;

		public NativeHandler(String nodename) {
			this.nodename = nodename;
		}

		public List<Schedule> getList() {
			return list;
		}

		@Override
		// 读到第一个开始标签的时候触发
		public void startDocument() throws SAXException {

			list = new ArrayList<Schedule>();
		}

		@Override
		// 当遇到所要解析的节点名称时触发
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if (localName.equals("GetScheduleListResult")) {
				for (int i = 0; i < attributes.getLength(); i++) {
					String key = attributes.getQName(i);
					String value = attributes.getValue(i);
					// Log.e(tag, "key = " + key + " ,value = " + value);
					if (key.equals("totalCount")) {
						totalCount = value;
					} else if (key.equals("currentCount")) {
						currentCount = value;
					}
				}
			}

			if (qName.equals(nodename)) {
				schedule = new Schedule();
				if (attributes != null && list != null) {
					for (int i = 0; i < attributes.getLength(); i++) {
						String key = attributes.getQName(i);
						String value = attributes.getValue(i);
						if (key.equals("contentId")) {
							schedule.setContentId(value);
						} else if (key.equals("channelId")) {
							schedule.setChannelId(value);
						} else if (key.equals("channelName")) {
							schedule.setChannelName(value);
						} else if (key.equals("programName")) {
							schedule.setProgramName(value);
						} else if (key.equals("startDate")) {
							schedule.setStartDate(value);
						} else if (key.equals("startTime")) {
							schedule.setStartTime(value);
						} else if (key.equals("duration")) {
							schedule.setDuration(value);
						} else if (key.equals("status")) {
							schedule.setStatus(value);
						} else if (key.equals("description")) {
							schedule.setDescription(value);
						} else if (key.equals("playUrl")) {
							schedule.setPlayUrl(value);
						}
					}
				}
			}
			currentTag = qName;

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (localName.equals("Schedule")) {
				list.add(schedule);
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
