package com.xike.xkliveplay.framework.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lw
 * <GetContentListResult totalCount ="17" currentCount ="17" >
<ContentList>
<Channel contentId="CHANNEL00000051" channelNumber ="2" 
	name="山东卫视" callSign ="CHANNEL00000051" timeShift ="0" 
	timeShiftDuration="" description="" country="" 
	logoURL="http://219.146.10.218/images/iqilu/2013/12/30/CHANNEL00000051/b0390606-1a95-46a2-a819-cada315a8e52.png"
 	state="" city="" zipCode="" 
 	playURL="http://219.146.10.201:8081/00/SNM/CHANNEL00000051/index.m3u8" language="CH"/>
 */
public class GetCategoryListRes {
	/**栏目列表**/
//	private Category category = new Category();
	private List<Category> categoryList = new ArrayList<Category>();

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public GetCategoryListRes() {
	}
	
	public String toSting()
	{
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("categoryList size is:" + categoryList.size());
		for(int i = 0;i < categoryList.size();i++)
		{
			sBuffer.append("****" + (categoryList.get(i)).toString());
		}
		
		return sBuffer.toString();
	}
}
