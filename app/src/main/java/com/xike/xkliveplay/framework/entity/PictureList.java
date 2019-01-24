package com.xike.xkliveplay.framework.entity;

/**
 * @author lw
 * 
 */
public class PictureList {
	/**图片文件URL**/
	private String fileURL = "";
	/**描述**/
	private String description = "";
	/**图片类型	
	 * 0：缩略图	1：海报	2：剧照
	 * 3：图标	4：标题图	5：广告图
	 * 6：草图	7：背景图	9：频道图片
	 * 10：频道黑白图片		11：频道Logo
	 * 12：频道名字图片		99：其他
	 * **/
	private String type = "";

	public PictureList() {
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PictureList [fileURL=" + fileURL + ", description="
				+ description + ", type=" + type + "]";
	}



}
