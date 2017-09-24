package cn.edu.cqu.mytestapplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈圳林 on 2017/3/22.
 */

public class InternetMusic {
    void CreateData( List<Map<String,Object>> Internetlitems){

        Map<String,Object>InternetItem1=new HashMap<String, Object>();
        InternetItem1.put("ivLogo",R.drawable.kugou);
        InternetItem1.put("tvLogo","Kugou Music");
        Map<String,Object>InternetItem2=new HashMap<String, Object>();
        InternetItem2.put("ivLogo",R.drawable.qqmusic);
        InternetItem2.put("tvLogo","QQ Music");
        Map<String,Object>InternetItem3=new HashMap<String, Object>();
        InternetItem3.put("ivLogo",R.drawable.baidumusic);
        InternetItem3.put("tvLogo","Baidu Music");
        Internetlitems.add(InternetItem1);
        Internetlitems.add(InternetItem2);
        Internetlitems.add(InternetItem3);

    }
}
