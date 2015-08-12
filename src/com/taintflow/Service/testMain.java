package com.taintflow.Service;

public class testMain {
	
	public String ENCRYPT(String mingwen_str){
		
		//16 wei
		int len=mingwen_str.length()%16;
		if(len != 0){
			for(int i=16-len;i>0;i--){
				mingwen_str += " ";
			}
		}
		
		byte[] mingwen = mingwen_str.getBytes();
		
		//key
		byte[] key = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
				(byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc,
				(byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
		SMS4 sm4 = new SMS4();
		
		//mingwen length
		int inLen = mingwen.length;
		
		//jiamijieguo
		byte[] jiamijieguo_byte = new byte[mingwen.length];
		
		sm4.sms4(mingwen, inLen, key, jiamijieguo_byte, 1);//1--encrypt mode
		
		//String -> byte[] 
		String jiamijieguo_s = "";
		int[] jiamijieguo_int = new int[jiamijieguo_byte.length];
		
		for (int i = 0; i < jiamijieguo_byte.length; i++) {
			jiamijieguo_int[i] = (int) jiamijieguo_byte[i] + 128;
			jiamijieguo_s += (String.valueOf(jiamijieguo_int[i]) + " ");
			
		}
		
		return jiamijieguo_s;
	}

	public String DECRYPT(String jiamijieguo_s){

		//String -> String[] 
		String[] strar = jiamijieguo_s.trim().split(" ");
		//int[]
		int[] out2_int = new int[strar.length];
		//byte[]
		byte[] out2 = new byte[strar.length];
		//String[] -> int[] -> byte[]
		for (int i = 0; i < strar.length; i++) {
			out2_int[i] = Integer.parseInt(strar[i]);
			out2_int[i] -= 128;
			out2[i] = (byte) out2_int[i];
		}
		
		byte[] jiemi_mingwen=new byte[strar.length];
		int inLen2 = strar.length;
		
		//key
		byte[] key = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
				(byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc,
				(byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
		//the function
		SMS4 sm4 = new SMS4();
		sm4.sms4(out2, inLen2, key, jiemi_mingwen, 0);//0--decrypt mode
		
		//return(byte[] -> String) 
		return new String(jiemi_mingwen);
	}

	public static void main(String[] args){
		
		testMain tm = new testMain();
		
		String mingwen_str = "yaojiamidewenziasdfafeaffcaefafaaaad";
		
		String a = tm.ENCRYPT(mingwen_str);//encrypt function
		
		System.out.println("string after encrypt£º");
		System.out.println(a);
		
		String b = tm.DECRYPT(a);//decrypt function
		
		System.out.println("string after decrypt£º");
		System.out.println(b);
		
	}
}
