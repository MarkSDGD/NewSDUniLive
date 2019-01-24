package com.xike.xkliveplay.framework.arcplay;

public interface IPlayerStateListener {

	public static final int PLAYER_FAIL				= 	0x0010;
	public static final int PLAYER_CONNECT_FAIL		 =  0x0011;
	public static final int PLAYER_PREPARE_FAIL		 =  0x0012;
	
	
	public static final int PLAYER_STATE			= 	0x0020;
	public static final int PLAYER_PREPARING		= 	0x00021;
	public static final int PLAYER_PREPARED			=   0x00022;
	
	public void onPlayerStateChanged(int state,int what,int extra);
}
