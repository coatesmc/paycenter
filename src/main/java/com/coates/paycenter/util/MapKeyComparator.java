package com.coates.paycenter.util;

import java.util.Comparator;

/**
 *
 * <p>Title: MapKeyComparator.java </p>
 * <p>Package com.shenpinkj.utils </p>
 * <p>Description: TODO(比较器类)  </p>
 * <p>Company: www.shenpinkj.cn</p> 
 * @author 牟超
 * @date	2017年10月21日上午9:45:13
 * @version 1.0
 */
public class MapKeyComparator implements Comparator<String> {
	
	 public int compare(String str1, String str2) {  
	        return str1.compareTo(str2);  
	    }
}
