package com.xike.xkliveplay.framework.httpclient;

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
public class HttpAll6Schedules implements IRequestCallback {

	private static final String tag = "HttpGetScheduleList";
	public static final String Method = "GetScheduleList.xml";
	private static final String ROOTELEMENT = "Schedule";

	private GetScheduleListRes res = null;
	private IUpdateData iUpdateData = null;
	private String totalCount = "";
	private String currentCount = "";

	public HttpAll6Schedules(IUpdateData updateData)
	{
		
		this.iUpdateData = updateData;
	}

	public void post() {
		try {
			AsyncHttpRequest request = new AsyncHttpRequest();
			request.post(VarParam.url, Method, "", this);
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

		List<Schedule> list = parser(ROOTELEMENT, body);
		if (list == null) {
			LogUtil.e(tag, "requestDidFinished","requestDidFinished XmlPara is null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		} else
			LogUtil.e(tag, "requestDidFinished","list.size = " + list.size());

		res = new GetScheduleListRes();
		res.setScheduleList(list);
		res.setTotalCount(totalCount);
		res.setCurrentCount(currentCount);
		DataModel.getInstance().initAll6Schedules(list);
		this.iUpdateData.updateData(Method,"",list, true);
	}

	@Override
	public void requestDidFailed(String method,String body) {
		// TODO Auto-generated method stub
		iUpdateData.updateData(Method, null, null, false);
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
		} catch (Exception e) {
			// TODO: handle exception
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
