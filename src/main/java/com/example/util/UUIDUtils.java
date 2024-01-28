package com.example.util;

import java.util.UUID;

public class UUIDUtils {
	public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
	public static long longUUID()
	{
		return UUID.randomUUID().getMostSignificantBits();
	}

	public static long absLongUUID()
	{
		for (;;)
		{
			long r = longUUID();
			if (r > 0L) {
				return r;
			}
		}
	}
}
