//package com.czdxwx.test;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//
//import com.czdxwx.test.R;
//import com.czdxwx.test.adapter.PersonAdapter;
//import com.czdxwx.test.model.Major;
//import com.czdxwx.test.broadcast.MyReceiver;
//import com.czdxwx.test.model.Person;
//import com.czdxwx.test.service.NormalService;
//import com.czdxwx.test.service.ReStartService;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import androidx.appcompat.widget.SearchView;
//import androidx.appcompat.widget.Toolbar;
//
//import android.os.IBinder;
//import android.view.ContextMenu;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//
//import android.widget.Toast;
//
//
//import net.tsz.afinal.FinalDb;
//import net.tsz.afinal.annotation.view.ViewInject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;
//
//import static android.widget.AdapterView.*;
//
//public class FriendListActivity extends AppCompatActivity {
//    public static long N = -1;
//    //广播接收器
//    private MyReceiver myReceiver;
//    private IntentFilter intentFilter;
//    //服务相关意图
//    private Intent mIntent;
//    //数据库
//    private FinalDb db;
//    private SearchView searchView;
//    private Menu menu;
//    private PersonAdapter adapter;
//    @ViewInject(id = R.id.lv_friend)
//    ListView lv_friend;
//    //选中的item
//    private int i;
//    //person表
//    public static HashMap<String, Person> information = new HashMap<>();
//    //真正的姓名表
//    public static final ArrayList<String> personNameList = new ArrayList<String>();
//    //普通
//    private final ArrayList<String> personNormal = new ArrayList<>();
//    //特别关心
//    private final ArrayList<String> personConcern = new ArrayList<>();
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_friend_list);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            showToast(bundle.getString("content"));
//        }
//        //连接SQLite Studio
//        SQLiteStudioService.instance().start(this);
//        //afinal框架FinalDb操作
//        db = FinalDb.create(this, "test");
//        //广播
//        myReceiver = new MyReceiver();
//        intentFilter = new IntentFilter();
//        intentFilter.addAction("com.czdxwx.updateMessage");
//        init_menu();
//        init_listView();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private void init_menu() {
//        //隐藏默认actionbar
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//        Toolbar toolBar = findViewById(R.id.toolBar);
//        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
//        toolBar.setTitle("好友列表");
//        //用toolbar替换actionbar
//        setSupportActionBar(toolBar);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.friend_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
//        searchView = (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                adapter.getFilter().filter(query);
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        //从xml中构建菜单界面布局
//        getMenuInflater().inflate(R.menu.friend_popupmenu, menu);
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
//        if (personConcern.contains(personNameList.get(info.position))) {
//            menu.getItem(2).setTitle("取消关注");
//        } else {
//            menu.getItem(2).setTitle("关注");
//        }
//    }
//
//    @Override
//    //选项菜单监听
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_addPerson:
//                Intent intent = new Intent();
//                intent.setAction("com.czdxwx.addment");
//                startActivity(intent);
//                break;
//            case R.id.in_import:
//
//                break;
//            case R.id.in_export:
////                try {
////                    FileOutputStream fos = openFileOutput("store_in", Context.MODE_PRIVATE);
////                    //FileOutputStream是字节流，如果是写文本的话，需要进一步把FileOutputStream包装 UTF-8是编码
////                    OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
////                    //写
////                    osw.write(et.getText().toString());
////                    osw.flush();
////                    fos.flush();
////                    osw.close();
////                    fos.close();
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                } catch (UnsupportedEncodingException e) {
////                    // TODO Auto-generated catch block
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    // TODO Auto-generated catch block
////                    e.printStackTrace();
////                }
//                break;
//            case R.id.out_import:
//
//                break;
//            case R.id.out_export:
//
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    //弹出菜单监听
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//        switch (item.getItemId()) {
//            case R.id.popup_delete:
//                db.deleteByWhere(Person.class, String.format("number='%s'", information.get(personNameList.get(info.position)).getNumber()));
//                refreshListView();
//                showToast("删除成功");
//                break;
//            case R.id.popup_edit:
//                Intent intent = new Intent();
//                intent.setAction("com.czdxwx.editInformation");
//                Person p = information.get(personNameList.get(info.position));
//                Bundle bundle = new Bundle();
//                bundle.putString("name", p.getName());
//                bundle.putString("number", p.getNumber());
//                bundle.putString("gender", p.getGender());
//                bundle.putString("hobby", p.getHobby());
//                bundle.putString("nativePlace", p.getNativePlace());
//                bundle.putInt("majorid", p.getMajorid());
//                bundle.putString("profile", p.getProfile());
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            case R.id.popup_concern:
//                String s = information.get(personNameList.get(info.position)).getNumber();
//                if (personConcern.contains(personNameList.get(info.position))) {
//                    Person person = new Person();
//                    person.setIsOnFocus(0);
//                    db.update(person, String.format("number='%s'", information.get(personNameList.get(info.position)).getNumber()));
//                    refreshListView();
//                } else {
//                    Person person = new Person();
//                    person.setIsOnFocus(1);
//                    db.update(person, String.format("number='%s'", information.get(personNameList.get(info.position)).getNumber()));
//                    N = Integer.parseInt(s.substring(s.length() - 1, s.length())) + Integer.parseInt(s.substring(s.length() - 2, s.length() - 1));
//                    refreshListView();
//                }
//                break;
//            case R.id.popup_call:
//                call(information.get(personNameList.get(info.position)).getNumber());
//                break;
//        }
//        return true;
//    }
//
//    //列表视图
//    private void init_listView() {
//        //构建一个列表适配器
//        adapter = new PersonAdapter(this, R.layout.item, personNameList, Color.WHITE, personConcern);
//        lv_friend = (ListView) findViewById(R.id.lv_friend);
//        lv_friend.setAdapter(adapter);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lv_friend.setPadding(0, 0, 0, 0);//设置四周空白
//        lv_friend.setBackgroundColor(Color.TRANSPARENT);//设置背景色
//        lv_friend.setTextFilterEnabled(true);
//        lv_friend.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setAction("com.czdxwx.getInformation");
//                Person p = information.get(personNameList.get(position));
//                Bundle bundle = new Bundle();
//                bundle.putString("name", p.getName());
//                bundle.putString("number", p.getNumber());
//                bundle.putString("gender", p.getGender());
//                bundle.putString("hobby", p.getHobby());
//                bundle.putString("nativePlace", p.getNativePlace());
//                bundle.putString("major", Major.major_name[p.getMajorid()]);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//    }
//
//
//    protected void onStart() {
//        super.onStart();
//        //刷新列表
//        refreshListView();
//        //注册上下文菜单
//        registerForContextMenu(lv_friend);
//        adapter.getFilter().filter("");
//    }
//
//    protected void onResume() {
//        super.onResume();
//        //注册广播
//        registerReceiver(myReceiver, intentFilter);
//    }
//
//
//    protected void onPause() {
//        super.onPause();
//    }
//
//    protected void onStop() {
//        super.onStop();
//        if (reStartService != null) {
//            //解绑服务。如果先前服务立即绑定，则此时解绑之后自动停止服务
//            unbindService(mFirstConn);
//            reStartService = null;
//        }
//        //取消注册
//        unregisterForContextMenu(lv_friend);
//    }
//
//
//    private Intent ttt = new Intent();
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //注销注册
//        unregisterReceiver(myReceiver);
//        if (N == -1) {
//            //创建一个通往普通服务的意图
//            ttt.setClass(this, NormalService.class);
//            ttt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //启动指定意图的服务
//            startService(ttt);
//        } else {
//            //创建一个通往立即绑定服务的意图
//            mIntent = new Intent(this, ReStartService.class);
//            mIntent.putExtra("Delayed", (long)N * 1000);
//            mIntent.putExtra("PackageName", this.getPackageName());
//            //绑定服务。如果服务未启动，则系统先启动该服务再绑定
//            boolean bindFlag = bindService(mIntent, mFirstConn, Context.BIND_AUTO_CREATE);
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //启动指定意图的服务
//            startService(mIntent);
//        }
//        N = -1;
//    }
//
//    private ReStartService reStartService;//声明一个服务对象
//    //绑定服务连接
//    private final ServiceConnection mFirstConn = new ServiceConnection() {
//        @Override
//        //获取对象时的操作
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            //如果服务运行于另外一个进程，则不能直接强制转换类型
//            //否则会报错
//            reStartService = ((ReStartService.LocalBinder) service).getService();
//        }
//
//        @Override
//        //无法获取到服务对象时的操作
//        public void onServiceDisconnected(ComponentName name) {
//            reStartService = null;
//        }
//    };
//
//    //刷新适配器
//    private void refreshListView() {
//        ArrayList<Person> personList = (ArrayList<Person>) db.findAll(Person.class);
//        information.clear();
//        personNameList.clear();
//        personNormal.clear();
//        personConcern.clear();
//        for (Person person : personList) {
//            information.put(person.getName(), person);
//            if (person.getIsOnFocus() == 1) {
//                personConcern.add(person.getName());
//            } else {
//                personNormal.add(person.getName());
//            }
//        }
//        personNameList.addAll(personConcern);
//        personNameList.addAll(personNormal);
//        if (personNameList.isEmpty()) {
//            showToast("查询结果为零");
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//    //呼叫
//    private void call(String phone) {
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
//
//    //封装Toast
//    private void showToast(String desc) {
//        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
//    }
//
//
//}