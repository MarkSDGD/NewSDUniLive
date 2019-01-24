package com.xike.xkliveplay.framework.tools;

import android.annotation.SuppressLint;
import java.io.ByteArrayOutputStream;
import javax.crypto.Cipher; 
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密
 * @author ll
 *
 */
public class ThreeDes {
	
	private static final String Algorithm = "TripleDES";
	
	public static byte[] ecrypt(byte[] keybyte, byte[] source){
		try {            
			//生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			//加密          
//			Cipher c1 = Cipher.getInstance("TripleDES/ECB/PKCS5Padding");
			Cipher c1 = Cipher.getInstance("desede/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(source);        
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();        
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();        
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();        
		}
		return null;
	}
	
	public static byte[] decrypt(byte[] keybyte, byte[] source){
		try {
			//生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte,Algorithm);
			//解密           
			Cipher c1 = Cipher.getInstance("TripleDES/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(source);        
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();        
			return null;
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
			return null;
		} catch (java.lang.Exception e3) {    
			e3.printStackTrace();
			return null;
		}
	}
	
    public static byte[] hexStringToBytes(String in) {  
    	byte[] arrB = in.getBytes();  
    	int iLen = arrB.length;  
    	// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2  
    	byte[] arrOut = new byte[iLen / 2];  
    	for (int i = 0; i < iLen; i = i + 2) {  
    		String strTmp = new String(arrB, i, 2);  
    		arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);  
    	}  
    	return arrOut;  
    }
    
    public static byte[] decode(String bytes) 

    { 

	    ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
	
	  //将每2位16进制整数组装成一个字节
	
	    String hexString="0123456789ABCDEF"; 
	
	    for(int i=0;i<bytes.length();i+=2){
	    	 baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));
	    }
	    return baos.toByteArray(); 
    }
    
    public static byte[] decode2(String hexString) throws Exception{
    	byte[] bts = new byte[hexString.length()/2];
    	for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte)Integer.parseInt(hexString.substring(2*i, 2*i+2),16);
		}
    	return bts;
    }


    //数组转16进制字符串  
    @SuppressLint("DefaultLocale")
	public static String byte2Hex(byte[] arrB) throws Exception {   
        int iLen = arrB.length;  
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍 
        StringBuffer sb = new StringBuffer(iLen * 2);  
        for (int i = 0; i < iLen; i++) {  
            int intTmp = arrB[i];  
            //  把负数转换为正数 
            while (intTmp < 0) {  
                intTmp = intTmp + 256;  
            }  
            //  // 小于0F的数需要在前面补0   
            if (intTmp < 16) {  
                sb.append("0");  
            }  
            sb.append(Integer.toString(intTmp, 16));  
        }  
        // 最大128位 
        String result = sb.toString();  
//      if(result.length()>128){  
//          result = result.substring(0,result.length()-1);  
//      }  
        return result.toUpperCase();  //
    }  
 /*   public static void main(String[] args) throws Exception {
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		String userID = "123456";//
		int lenth = userID.length();
		for (int i = 0; i < 24-lenth; i++) {
			userID = userID+"0";
		}
		
		byte[] keybyte = userID.getBytes();
		System.out.println("keybyte="+new String(keybyte)+"--length:"+keybyte.length);
		
		String authStr="800F4472017A9DC2C96ACD8576216103$6D220F967B78A4380296C5B3$testxxt01$OTT$192.168.1.104$AC:4A:FE:39:FB:32$0$CTC";
//		String authStr="43432$48198A23F0A2B72A082391F9$testdx06$HS64543434$10.0.117.158$ac:4a:fe:39:f9:b1$$CTC22";

		byte[] source = authStr.getBytes();
		byte[] data3 = ecrypt(keybyte, source);
		String data = byte2Hex(data3);
		System.out.println("密文"+data);
		
//		String authenticator=data;
		String authenticator = "4629E8DEC4391E1A1A4EE4E5119BB12FB66795222DB2D9963A9D68220A07A8666FBE5507614238DAD3BF6A8B7E3B2926AFC04AADBF7BB9C805861B7608EC65CEA606DB418C6EFFB704252C069C4EA8D6A61595E0BCB797716192F1B3E3363DCFC8F881526E0817A7958C8DD895C910CB4E73018179A8A1A0";
//		String authenticator = "5D37AF6BB3921072EE976E24274836C52ED8F1BEF9991BF978EFC44E5FA178D8AEFB268CB7B44038F764F3701A4259FBCD441B82AB1E8B2164B405F9A5B6F3461DE629A6A6E79A5C1A5F32B9D1BA89C08452D073365A43F8EFBC774411BD3";
		byte[] source1 = hexStringToBytes(authenticator);
//		byte[] source1 = authenticator.getBytes();
//		byte[] source1 = decode(authenticator);
		System.out.println("source1-"+new String(source1).toString());
		System.out.println("s1-"+new String(s1).toString());
		
		//byte[] source = authenticator.getBytes();
		
		byte[] data2 = decrypt(keybyte, source1);
		
		System.out.println("data2:"+data2);
		
		String realData = new String(data2);
//		String realData = "12639364$ 3DE1B6ED6EDD0C47AB6F013CADF0309E$11111111$001001990070114000034C09B4BE1B65$192.168.200.70$4C:09:B4:BE:1B:65$990070|$CTC";
		System.out.println("明文"+realData+"---"+realData.indexOf("$"));
		String[] dataStr = realData.split("\\$");
		System.out.println(dataStr.length+"--"+dataStr[0]);
		String encryToken = dataStr[1];
		String userId = dataStr[2];
		String stbid = dataStr[3];
		String stbip = dataStr[4];
		String mac = dataStr[5];
		
		System.out.println("encryToken = " + encryToken);
		System.out.println("userId = " + userId);
		System.out.println("STBID = " + stbid);
		System.out.println("IP = " + stbip);
		System.out.println("MAC = " + mac);
    	
	}*/
}
