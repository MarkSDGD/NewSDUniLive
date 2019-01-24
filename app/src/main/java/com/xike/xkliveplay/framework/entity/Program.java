package com.xike.xkliveplay.framework.entity;


/**
 * 
 * @author LiWei <br>
 * CreateTime: 2014年10月30日 上午10:03:32<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
public class Program {
	/** 全局唯一标识 **/
	private String contentId = "";
	/** 节目名称 **/
	private String name = "";
	/** 原名 **/
	private String originalName = "";
	/** 演员列表，多个用英文逗号隔开 **/
	private String actors = "";
	/** 作者列表，多个用英文逗号隔开 **/
	private String writers = "";
	/** 国家地区 **/
	private String orginalCountry = "";
	/** 语言 **/
	private String language = "";
	/** 上映年份YYYY **/
	private String releaseYear = "";
	/** 首播日期(YYYYMMDD) **/
	private String orgAirDate = "";
	/** 节目描述 **/
	private String description = "";
	/** 0：普通vod 1：连续剧剧集 **/
	private String seriesFlag = "";
	/** 节目提供商代码 **/
	private String contentProvider = "";
	/** 看点 **/
	private String viewPoint = "";
	/** 推荐星级 1-10,越大星级越高，默认3 **/
	private String starLevel = "";
	/** 采用国际通用的Rating等级 **/
	private String rating = "";
	/** 所含奖项，多个奖项之间用；隔开 **/
	private String awards = "";
	/** 播放时长 **/
	private String duration = "";
	/**
	 * 类型 1000：电影 1101：新闻/时事 1102：财经 1103：体育 1104：专题 1105：法制 1106：访谈 1107：综艺娱乐
	 * 1108：音乐 1109：戏剧 1110：外语 1111：动画 1112：记录 1113：少儿 1114：生活 1115：教育 1116：军事
	 * 1117：农业 1200：广告
	 **/
	private String programType = "";
	/** 海报列表 **/
	private PictureList pictureList = new PictureList();
	/** 剧集编号 **/
	private String volId = "";
	/** 续看点（单位秒） **/
	private String contentBM = "";
	/** 是否有收藏 1：表示收藏过 0：表示未收藏过，当SeriesFlag为普通VOD节目时必填 **/
	private String favoriteFlg = "";

	public Program() {
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getWriters() {
		return writers;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public String getOrginalCountry() {
		return orginalCountry;
	}

	public void setOrginalCountry(String orginalCountry) {
		this.orginalCountry = orginalCountry;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getOrgAirDate() {
		return orgAirDate;
	}

	public void setOrgAirDate(String orgAirDate) {
		this.orgAirDate = orgAirDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeriesFlag() {
		return seriesFlag;
	}

	public void setSeriesFlag(String seriesFlag) {
		this.seriesFlag = seriesFlag;
	}

	public String getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(String contentProvider) {
		this.contentProvider = contentProvider;
	}

	public String getViewPoint() {
		return viewPoint;
	}

	public void setViewPoint(String viewPoint) {
		this.viewPoint = viewPoint;
	}

	public String getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public PictureList getPictureList() {
		return pictureList;
	}

	public void setPictureList(PictureList pictureList) {
		this.pictureList = pictureList;
	}

	public String getVolId() {
		return volId;
	}

	public void setVolId(String volId) {
		this.volId = volId;
	}

	public String getContentBM() {
		return contentBM;
	}

	public void setContentBM(String contentBM) {
		this.contentBM = contentBM;
	}

	public String getFavoriteFlg() {
		return favoriteFlg;
	}

	public void setFavoriteFlg(String favoriteFlg) {
		this.favoriteFlg = favoriteFlg;
	}

	@Override
	public String toString() {
		return "Program [contentId=" + contentId + ", name=" + name
				+ ", originalName=" + originalName + ", actors=" + actors
				+ ", writers=" + writers + ", orginalCountry=" + orginalCountry
				+ ", language=" + language + ", releaseYear=" + releaseYear
				+ ", orgAirDate=" + orgAirDate + ", description=" + description
				+ ", seriesFlag=" + seriesFlag + ", contentProvider="
				+ contentProvider + ", viewPoint=" + viewPoint + ", starLevel="
				+ starLevel + ", rating=" + rating + ", awards=" + awards
				+ ", duration=" + duration + ", programType=" + programType
				+ ", pictureList=" + pictureList + ", volId=" + volId
				+ ", contentBM=" + contentBM + ", favoriteFlg=" + favoriteFlg
				+ "]";
	}

}
