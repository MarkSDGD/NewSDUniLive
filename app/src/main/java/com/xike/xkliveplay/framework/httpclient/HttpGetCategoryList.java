package com.xike.xkliveplay.framework.httpclient;

import com.xike.xkliveplay.framework.entity.Category;
import com.xike.xkliveplay.framework.entity.GetCategoryList;
import com.xike.xkliveplay.framework.entity.GetCategoryListRes;
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
 * @author lw
 * <?xml version="1.0" encoding="UTF-8" ?>
<GetCategoryListResult>
<CategoryList>
<Category id="00000001000000052013101900000002" type ="1" name="全部" code ="00000001000000052013101900000002" imgurl ="" isLeaf="0"/>

<Category id="20130815000000004" type ="1" name="本地" code ="20130815000000004" imgurl ="" isLeaf="0"/>

<Category id="20130814000000008" type ="1" name="央视" code ="20130814000000008" imgurl ="" isLeaf="0"/>

<Category id="20130815000000001" type ="1" name="卫视" code ="20130815000000001" imgurl ="" isLeaf="0"/>

<Category id="20130815000000002" type ="1" name="少儿" code ="20130815000000002" imgurl ="" isLeaf="0"/>

<Category id="00000001000000052013092000000002" type ="1" name="轮播" code ="00000001000000052013092000000002" imgurl ="" isLeaf="0"/>

<Category id="20130814000000009" type ="1" name="高清" code ="20130814000000009" imgurl ="" isLeaf="0"/>

</CategoryList>
</GetCategoryListResult>
 * 
 */

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 上午9:55:21<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class HttpGetCategoryList implements IRequestCallback {

	private static final String tag = "HttpGetCategoryList";
	public static final String Method = "GetCategoryList";
	private static final String ROOTELEMENT = "Category";
	private GetCategoryList req = null;
	private GetCategoryListRes res = null;
	private IUpdateData iUpdateData = null;
	
	public HttpGetCategoryList(IUpdateData updateData) {
		
		this.iUpdateData = updateData;
	}
	public HttpGetCategoryList(IUpdateData updateData, GetCategoryList req) {
//		initTestData();
		this.req = new GetCategoryList();
		this.req = req;
		
		this.iUpdateData = updateData;
	}

	@SuppressWarnings("unused")
	private void initTestData() {
		req = new GetCategoryList();
		req.setParentCategoryId("20130814000000007");
		req.setUserId("testdx11");
		req.setUserToken("FA64182F930B3382409CD25D");
	}

	public void post() 
	{
		if (req == null) 
		{
			System.err.println("req is not null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		try {
//			HttpSynConn.getInstance().post(VarParam.url, Method, req, this);
			AsyncHttpRequest aRequest = new AsyncHttpRequest();
			aRequest.post(VarParam.url, Method, req, this);
			LogUtil.e(tag, "post","req = " + req.getParentCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
	}

	@Override
	public void requestDidFinished(String flag, String body) {
		// TODO Auto-generated method stub

		if (!Method.equals(flag)) 
		{
			iUpdateData.updateData(Method, null, null, false);
			return;
		}
		
		if (body.contains("sockettimeout")
				||body.contains("unknownhost")
				||body.contains("connectionrefused")
				||body.contains("error")) 
		{
			iUpdateData.updateData(Method, body, null, true);
			return;
		}

		List<Category> list = parser(ROOTELEMENT, body);
		if (list == null) {
			LogUtil.e(tag, "requestDidFinished","requestDidFinished XmlPara is null");
			iUpdateData.updateData(Method, null, null, false);
			return;
		}

		res = new GetCategoryListRes();
		res.setCategoryList(list);
		LogUtil.e(tag, "requestDidFinished","id = " + req.getParentCategoryId());
		this.iUpdateData.updateData(Method, req.getParentCategoryId(),list, true);
		
		LogUtil.i(tag, "requestDidFinished","requestDidFinished:" + res.toSting());
	}

	@Override
	public void requestDidFailed(String method,String body) {
		iUpdateData.updateData(Method, null, null, false);
	}


	public GetCategoryList getReq() {
		return req;
	}

	public void setReq(GetCategoryList req) {
		this.req = req;
	}

	public List<Category> getRes() {
		if(res != null)
		{
			return res.getCategoryList();
		}else {
			return null;
		}
	}

	public  List<Category> parser(String rootElement, String body)
	{
		List<Category> list;
		try {
			SAXParser parse = SAXParserFactory.newInstance().newSAXParser();
			
		    NativeHandler handler = new NativeHandler(rootElement);
		    
		    InputStream is = new ByteArrayInputStream(body.getBytes("UTF-8")); 
		    
		    parse.parse(is, handler);
		  
		    list = handler.getList();
		    
		    return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

	public  List<Category> parser(String body)
	{
		List<Category> list;
		try {
			SAXParser parse = SAXParserFactory.newInstance().newSAXParser();

			NativeHandler handler = new NativeHandler(ROOTELEMENT);

			InputStream is = new ByteArrayInputStream(body.getBytes("UTF-8"));

			parse.parse(is, handler);

			list = handler.getList();

			return list;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	
	public class NativeHandler extends DefaultHandler{

		List<Category> list = null;// 存储所有的解析对象
	    String currentTag = null; // 正在解析的标签
	    String currentValue = null; // 正在解析元素的值
	    String nodename = null; // 正在解析节点名称
	    Category category = null;

	    public NativeHandler(String nodename) {
	        this.nodename = nodename;
	    }

	    public List<Category> getList() {
	        return list;
	    }

	    @Override
	    // 读到第一个开始标签的时候触发
	    public void startDocument() throws SAXException {

	        list = new ArrayList<Category>();
	    }

	    @Override
	    // 当遇到所要解析的节点名称时触发
	    public void startElement(String uri, String localName, String qName,
	            Attributes attributes) throws SAXException {

	    	if (qName.equals(nodename)) 
	    	{
	    		category = new Category();
	    		if (attributes != null && list != null) 
	    		{
		            for (int i = 0; i < attributes.getLength(); i++) 
		            {
		                String key = attributes.getQName(i);
		                String value = attributes.getValue(i);
		                
//		                System.out.println("&&&&&key:" + key + ",value:" + value);
		                if(key.equals("id"))
		                {
		                	
		                	category.setId(value);
		                }else if(key.equals("type"))
		                {
		                	category.setType(value);
		                }else if(key.equals("name"))
		                {
		                	category.setName(value);
		                }else if(key.equals("code"))
		                {
		                	category.setCode(value);
		                }else if(key.equals("imgurl"))
		                {
		                	category.setImgurl(value);
		                }else if(key.equals("isLeaf"))
		                {
		                	category.setIsLeaf(value);
		                }
		            }
		        }
	        }
	        currentTag = qName;

	    }

	    @Override
	    public void endElement(String uri, String localName, String qName)
	            throws SAXException {
	    	if (localName.equals("Category")) 
	    	{
				list.add(category);
				return;
			}

	    }

	    @Override
	    // 处理xml文件读取到的内容
	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
	    	
//	    	String theString = new String(ch,start,length);
	    	
			return;
			
	    }


	}

}
