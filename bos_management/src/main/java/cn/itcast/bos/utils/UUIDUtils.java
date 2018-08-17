package cn.itcast.bos.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String getUUID() {
		UUID randomUUID = UUID.randomUUID();
		return randomUUID.toString().replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}
