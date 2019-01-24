package com.xike.xkliveplay.framework.entity;




/**
 * @author Lw
 * 
 */
public class Content {
	/** 连续剧 **/
	private Series series = new Series();
	/** TV频道信息 **/
	private ContentChannel channel = new ContentChannel();
	/** 节目信息 **/
	private Program program = new Program();
	/** 节目单信息 **/
	private Schedule schedule = new Schedule();

	public Content() {
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public ContentChannel getChannel() {
		return channel;
	}

	public void setChannel(ContentChannel channel) {
		this.channel = channel;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public String toString() {
		return "ContentList [series=" + series + ", channel=" + channel
				+ ", program=" + program + ", schedule=" + schedule + "]";
	}

}
