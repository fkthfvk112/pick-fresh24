package mart.fresh.com.util;

import java.util.ArrayList;
import java.util.List;

public class CustomPageable {
	static public <T> List<T> sliceList(List<T> list, int offset, int limit) {
	    List<T> resultList = new ArrayList<T>();
	    int start = (offset)*limit;
	    int end = Math.min(start + limit, list.size());
	    
	    for(int i = start; i < end; i++) {
	    	resultList.add(list.get(i));
	    }
	    
	    return resultList;
	}
}
