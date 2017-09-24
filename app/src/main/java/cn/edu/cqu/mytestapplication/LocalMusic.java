package cn.edu.cqu.mytestapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈圳林 on 2017/3/18.
 */

public class LocalMusic {

        void CreateData3(String url,Context context){
                File f=new File(url);
                String fileName=f.getName();
                ContentValues values=new ContentValues();
                values.put("tvName",fileName);
                values.put("tvPath",url);
                values.put("ivIcon",R.drawable.music);
                DBHelper helper=new DBHelper(context);
                helper.insert(values);

        }
        void RefreshList(List<Map<String,Object>>Localitems,Context context,ArrayList PathList, ListView lvLocal){
                //数据库数据导入
                PathList.clear();
                Localitems=new ArrayList<Map<String,Object>>();
                LocalMusic localMusic=new LocalMusic();
                final DBHelper helper=new DBHelper(context);
                Cursor cursor=helper.query();
                int pathlist=0;
                if(cursor.moveToFirst()){
                        do{
                                String fileName=cursor.getString(cursor.getColumnIndex("tvName"));
                                String url=cursor.getString(cursor.getColumnIndex("tvPath"));
                                Map<String,Object>item1=new HashMap<String, Object>();
                                item1.put("ivIcon",R.drawable.music);
                                item1.put("tvName",fileName);
                                item1.put("tvPath",url);
                                Localitems.add(item1);
                                PathList.add(url);
                                pathlist++;
                        }while (cursor.moveToNext());
                }
                SimpleAdapter LocalSimpleAdapter=new SimpleAdapter(context,Localitems,R.layout.layout_storagefileitem,new String[]{"ivIcon","tvName","tvPath"},new int[]{R.id.ivIcon,R.id.tvName,R.id.tvPath});
                lvLocal.setAdapter(LocalSimpleAdapter);

        }
}
