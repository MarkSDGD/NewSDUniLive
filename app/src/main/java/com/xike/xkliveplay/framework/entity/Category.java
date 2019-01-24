package com.xike.xkliveplay.framework.entity;

import java.io.Serializable;

/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年11月5日 下午5:05:31<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class Category implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 栏目分类id --primaryid**/
	private String id = "";
	/** 所属类型 1：VOD,2：频道类,3：节目单 **/
	private String type = "";
	/** 栏目分类名称 **/
	private String name = "";
	/** 栏目分类编号 **/
	private String code = "";
	/** 栏目分类图片 **/
	private String imgurl = "";
	/** 是否叶栏目 1：表示叶栏目，2：表示非叶栏目 **/
	private String isLeaf = "";

	private String position = "";
	private String sequence = "";
	private String description = "";
	private String pfile = "";
	private String pname = "";
	private String childs = "";
	private String displaytype = "";
	private String parentid = "";

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPfile() {
		return pfile;
	}

	public void setPfile(String pfile) {
		this.pfile = pfile;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getChilds() {
		return childs;
	}

	public void setChilds(String childs) {
		this.childs = childs;
	}

	public String getDisplaytype() {
		return displaytype;
	}

	public void setDisplaytype(String displaytype) {
		this.displaytype = displaytype;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public Category() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Override
	public String toString() {
		return "Category{" +
				"id='" + id + '\'' +
				", type='" + type + '\'' +
				", name='" + name + '\'' +
				", code='" + code + '\'' +
				", imgurl='" + imgurl + '\'' +
				", isLeaf='" + isLeaf + '\'' +
				", position='" + position + '\'' +
				", sequence='" + sequence + '\'' +
				", description='" + description + '\'' +
				", pfile='" + pfile + '\'' +
				", pname='" + pname + '\'' +
				", childs='" + childs + '\'' +
				", displaytype='" + displaytype + '\'' +
				", parentid='" + parentid + '\'' +
				'}';
	}
}
