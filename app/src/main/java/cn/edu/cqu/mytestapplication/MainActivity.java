package cn.edu.cqu.mytestapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    ImageButton btnStart;
    ImageButton btnStop;
    ImageButton btnNext;
    private Button btnGetResource;
    private TextView textView01;
    private TextView tvCurName;
    private TextView tvTime;
    private MediaPlayer mediaPlayer01;
    private boolean blsPause=false;
    //private SeekBar seekBar=null;
    private String[]strModes={"Just One","Single Cycle","Random Play","Order Play"};

    private Intent intent0,intent1,intent2;
    private Intent[] intends={intent0,intent1,intent2};

    private TabHost.TabSpec tabSpec0,tabSpec1,tabSpec2;
    private TabHost.TabSpec[] tabSpecs={tabSpec0,tabSpec1,tabSpec2};
    private String[] tabMenu={"Remote","Local","Personal"};
    private ListView lvMode;
    private ListView lvVolume;
    private ListView lvLocal;
    private ListView lvInternet;
    private SimpleAdapter InetrnetSimpleAdapter;

    private List<Map<String,Object>>Localitems;
    private final int JustOne=0;
    private final int SingleCycle=1;
    private final int Random=2;
    private final int OrderPlay=3;
    private int PlayMode=0;
    private String CurPath="";
    private ArrayList PathList=new ArrayList();
    private boolean isStop;
    private float iVolume=1.0f;
    private boolean isLoop=false;
    //******************Internet部分********************//
    private  Button btnSearch;
    private EditText etUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMode=(ListView)findViewById(R.id.lvMode);
        lvVolume=(ListView)findViewById(R.id.lvVolume);
        lvLocal=(ListView)findViewById(R.id.lvLocal);
        lvInternet=(ListView)findViewById(R.id.lvInternet);
        btnStart=(ImageButton)findViewById(R.id.btnStart);
        btnNext=(ImageButton)findViewById(R.id.btnNext);
        btnStop=(ImageButton)findViewById(R.id.btnStop);
        btnGetResource=(Button)findViewById(R.id.btnGetResource);
        textView01=(TextView)findViewById(R.id.text01);
        //seekBar=(SeekBar)findViewById(R.id.seekbar);

        btnSearch=(Button)findViewById(R.id.btnSearch);
        etUrl=(EditText)findViewById(R.id.etUrl);

        tvCurName=(TextView)findViewById(R.id.tvCurName);
        tvTime=(TextView)findViewById(R.id.tvTime);
       /****************播放模式*******/
        //Play mode control
        //lvMode.setSelection(0);
        lvMode.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,strModes));
        lvMode.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:mediaPlayer01.setLooping(false);
                        PlayMode=JustOne;
                        break;
                    case 1:mediaPlayer01.setLooping(true);
                        PlayMode=SingleCycle;
                        break;
                    case 2:mediaPlayer01.setLooping(false);
                        PlayMode=Random;
                        break;
                    case 3:mediaPlayer01.setLooping(false);
                        PlayMode=OrderPlay;
                        break;
                }
            }
        });



        /************音量控制****************/
        //Volume Control
        List<Map<String,Object>>Volumeitems=new ArrayList<Map<String,Object>>();
        Map<String,Object>volumeItemAdd=new HashMap<String, Object>();
        volumeItemAdd.put("ivLogo",R.drawable.volumeadd);
        volumeItemAdd.put("tvLogo","Add Volume");
        Map<String,Object>volumeItemSub=new HashMap<String, Object>();
        volumeItemSub.put("ivLogo",R.drawable.volumesub);
        volumeItemSub.put("tvLogo","Sub Volume");
        Volumeitems.add(volumeItemAdd);
        Volumeitems.add(volumeItemSub);
        SimpleAdapter volumeSimpleAdapter=new SimpleAdapter(this,Volumeitems,R.layout.item,new String[]{"ivLogo","tvLogo"},new int[]{R.id.ivLogo,R.id.tvLogo});
        lvVolume.setAdapter(volumeSimpleAdapter);
        lvVolume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mediaPlayer01.setAudioStreamType(AudioManager.STREAM_MUSIC);
                switch (position){
                    case 0:if(iVolume>=1.0f){
                        iVolume=1.0f;
                    }else {
                        iVolume+=0.1f;
                    }
                        break;
                    case 1:if(iVolume<=0.0f){
                        iVolume=0.0f;
                    }else {
                        iVolume-=0.1f;
                    }
                        break;
                }
                mediaPlayer01.setVolume(iVolume,iVolume);
            }
        });
        /************音量控制****************/
/****************播放模式*******/
        /******************本地列表*********************/
        //Local list

        //数据库数据导入
        //Database set
        LocalMusic localMusic=new LocalMusic();
        localMusic.RefreshList(Localitems,getApplicationContext(),PathList,lvLocal);
        //数据库数据导入
        lvLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String,String>map=(HashMap<String,String>) lvLocal.getItemAtPosition(position);


                    String Path = map.get("tvPath");
                    
                    try {
                        mediaPlayer01.release();
                        mediaPlayer01 = null;
                        mediaPlayer01 = new MediaPlayer();
                        //mediaPlayer.setDataSource(this,uri);
                        mediaPlayer01.setDataSource(Path);
                        CurPath=Path;
                        mediaPlayer01.prepare();
                        mediaPlayer01.start();
                        File f=new File(CurPath);
                        String fileName=f.getName();
                        tvCurName.setText(fileName);
                        f.delete();


                        ;
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        });
        /******************本地列表*********************/
        /******************网络功能*********************/
        //Internet function
        final List<Map<String,Object>>Internetlitems=new ArrayList<Map<String,Object>>();
        InternetMusic internetMusic=new InternetMusic();
        internetMusic.CreateData(Internetlitems);

        InetrnetSimpleAdapter=new SimpleAdapter(this,Internetlitems,R.layout.item,new String[]{"ivLogo","tvLogo"},new int[]{R.id.ivLogo,R.id.tvLogo});
        lvInternet.setAdapter(InetrnetSimpleAdapter);
/******************网络功能*********************/

        /********************标签页***********************/
        //Set tab
        TabHost tabHost=(TabHost)findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Local Music",null).setContent(R.id.localLaout));
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Play Setting",null).setContent(R.id.layoutSetting));
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Internet Fuction",null).setContent(R.id.InternetLayout));
        /********************标签页***********************/

        /**********************音乐路径*********************************/
        //get the resource from local
        btnGetResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(Intent.ACTION_GET_CONTENT);
                intend.setType("*/*");
                intend.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intend,1);
            }
        });

        /**********************音乐路径*********************************/

        /***********************音乐播放******************************/
        //Control Music play buton
        mediaPlayer01=MediaPlayer.create(this,R.raw.test);

        //btnStart.setOnClickListener();
        final MyMusicPlayControl myMusicPlayControl=new MyMusicPlayControl();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myMusicPlayControl.Play(mediaPlayer01,textView01);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myMusicPlayControl.Stop(mediaPlayer01,textView01);
               // seekBar.setProgress(0);
                isStop=true;
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer01.release();
                mediaPlayer01 = null;
                mediaPlayer01 = new MediaPlayer();
                textView01.setText("start");
                for(int i=0;i<PathList.size();i++){
                    if(CurPath.equals(PathList.get(PathList.size()-1))){
                        CurPath=PathList.get(0).toString();
                        try {
                            //mediaPlayer01.release();
                            //mediaPlayer.setDataSource(this,uri);
                            mediaPlayer01.setDataSource(CurPath);
                            mediaPlayer01.prepare();
                            mediaPlayer01.start();
                            File f=new File(CurPath);
                            String fileName=f.getName();
                            tvCurName.setText(fileName);
                            f.delete();
                            ;
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(PathList.get(i).toString().equals(CurPath)){
                        CurPath=PathList.get(i+1).toString();
                        try {
                            //mediaPlayer01.release();
                            mediaPlayer01 = new MediaPlayer();
                            //mediaPlayer.setDataSource(this,uri);
                            mediaPlayer01.setDataSource(CurPath);
                            mediaPlayer01.prepare();
                            mediaPlayer01.start();
                            File f=new File(CurPath);
                            String fileName=f.getName();
                            tvCurName.setText(fileName);
                            f.delete();

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        });

        mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public   void onCompletion(MediaPlayer arg0){
              myMusicPlayControl.Completion(mediaPlayer01,textView01);
                //Control the Play mode of orderplay and random play
                if(PlayMode==OrderPlay){
                    mediaPlayer01.release();
                    mediaPlayer01 = null;
                    mediaPlayer01 = new MediaPlayer();
                    textView01.setText("start");
                    for(int i=0;i<PathList.size();i++){
                        if(CurPath.equals(PathList.get(PathList.size()-1))){
                            CurPath=PathList.get(0).toString();
                            try {
                                //mediaPlayer01.release();
                                //mediaPlayer.setDataSource(this,uri);
                                mediaPlayer01.setDataSource(CurPath);
                                mediaPlayer01.prepare();
                                mediaPlayer01.start();
                                File f=new File(CurPath);
                                String fileName=f.getName();
                                tvCurName.setText(fileName);
                                f.delete();
                                ;
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else if(PathList.get(i).toString().equals(CurPath)){
                            CurPath=PathList.get(i+1).toString();
                            try {
                                //mediaPlayer01.release();
                                mediaPlayer01 = new MediaPlayer();
                                //mediaPlayer.setDataSource(this,uri);
                                mediaPlayer01.setDataSource(CurPath);
                                mediaPlayer01.prepare();
                                mediaPlayer01.start();
                                File f=new File(CurPath);
                                String fileName=f.getName();
                                tvCurName.setText(fileName);
                                f.delete();

                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                if(PlayMode==Random){
                    mediaPlayer01.release();
                    mediaPlayer01 = null;
                    mediaPlayer01 = new MediaPlayer();
                    textView01.setText("start");
                    for(int i=0;i<PathList.size();i++){
                        if(CurPath.equals(PathList.get(PathList.size()-1))){
                            CurPath=PathList.get(0).toString();
                            try {
                                //mediaPlayer01.release();
                                //mediaPlayer.setDataSource(this,uri);
                                mediaPlayer01.setDataSource(CurPath);
                                mediaPlayer01.prepare();
                                mediaPlayer01.start();
                                File f=new File(CurPath);
                                String fileName=f.getName();
                                tvCurName.setText(fileName);
                                f.delete();
                                ;
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else if(PathList.get(i).toString().equals(CurPath)){
                            Random random=new Random();
                            CurPath=PathList.get(random.nextInt(PathList.size())).toString();
                            try {
                                //mediaPlayer01.release();
                                mediaPlayer01 = new MediaPlayer();
                                //mediaPlayer.setDataSource(this,uri);
                                mediaPlayer01.setDataSource(CurPath);
                                mediaPlayer01.prepare();
                                mediaPlayer01.start();

                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }

            }
        });
        mediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener(){
            @Override
            public boolean onError(MediaPlayer arg0,int arg1,int arg2){
                myMusicPlayControl.Error(mediaPlayer01,textView01);
                return  false;
            }
        });

/******************************Internet功能****************************************/
        //Get into the website
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer01.release();
                mediaPlayer01 = null;
                Intent intent=new Intent();
                String webUrl=etUrl.getText().toString();
                intent.setClass(MainActivity.this,WebBrowserActivity.class);
                intent.putExtra("url",webUrl);
                MainActivity.this.startActivity(intent);
            }
        });
        lvInternet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String webUrl="http://www.baidu.com";
                Intent intent=new Intent();
                switch (position){
                    case 0:webUrl="http://web.kugou.com";
                        break;
                    case 1:webUrl="http://y.qq.com";
                        break;
                    case 2:webUrl="http://music.baidu.com";
                        break;
                }
                mediaPlayer01.release();
                mediaPlayer01 = null;
                intent.setClass(MainActivity.this,WebBrowserActivity.class);
                intent.putExtra("url",webUrl);
                MainActivity.this.startActivity(intent);

            }
        });
        /*****************************************************************/
       /* seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/



    }
//Get Resource from Local
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            String url;
            url=uri.getPath();
            File file;
            MyMusicPlayControl myMusicPlayControl=new MyMusicPlayControl();
            try {
                mediaPlayer01.release();
                mediaPlayer01 = null;
                mediaPlayer01=new MediaPlayer();
                //mediaPlayer.setDataSource(this,uri);
                mediaPlayer01.setDataSource(url);
                CurPath=url;
                mediaPlayer01.prepare();
                mediaPlayer01.start();
                File f=new File(CurPath);
                String fileName=f.getName();
                tvCurName.setText(fileName);
                f.delete();

                LocalMusic localMusic=new LocalMusic();
                //localMusic.CreateData2(Localitems,url);
                localMusic.CreateData3(url,getApplicationContext());
                //lvLocal.deferNotifyDataSetChanged();
                localMusic.RefreshList(Localitems,getApplicationContext(),PathList,lvLocal);


            }catch (IllegalStateException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
