package com.czdxwx.test.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.czdxwx.test.R;
import com.czdxwx.test.TabFragmentActivity;
import com.czdxwx.test.adapter.PersonAdapter;
import com.czdxwx.test.broadcast.MyReceiver;
import com.czdxwx.test.model.Major;
import com.czdxwx.test.model.Person;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static pl.com.salsoft.sqlitestudioremote.internal.Utils.LOG_TAG;


public class FriendFragment extends Fragment {

    private View myView;
    private Context mContext;
    private Toolbar mToolbar;
    //数据库
    public FinalDb db;
    private SearchView searchView;
    private ListView lv_friend;
    private Menu menu;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_friend, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        mContext = getView().getContext();
        //获取toolbar
        mToolbar = getView().findViewById(R.id.toolBar);
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        mToolbar.setTitle("好友列表");
        //用toolbar替换actionbar
        mToolbar.inflateMenu(R.menu.friend_menu);

        MenuItem menuItem = mToolbar.getMenu().findItem(R.id.action_search);//在菜单中找到对应控件的item
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("SdCardPath")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                File file=new File("/data/data/com.czdxwx.test/files/store_in");
                File dir1 = Environment.getExternalStorageDirectory();
                File dir = new File(dir1, "store_out.txt");
                switch (item.getItemId()) {
                    case R.id.action_addPerson:
                        Intent intent = new Intent();
                        intent.setAction("com.czdxwx.addment");
                        startActivity(intent);
                        break;
                    case R.id.in_import:
                        try {
                            FileInputStream fis=new FileInputStream(file);
                            ObjectInputStream objectInputStream=new ObjectInputStream(fis);
                            Person temp=new Person();
                            ArrayList<Person> tmplist =new ArrayList<>();
                            information.clear();
                            while ((temp = (Person)objectInputStream.readObject())!=null){
                                    information.put(temp.getName(),temp);
                            }
                            personNameList.clear();
                            personNormal.clear();
                            personConcern.clear();
                            for (Map.Entry entry : information.entrySet()) {
                                if (((Person)entry.getValue()).getIsOnFocus() == 1) {
                                    personConcern.add(entry.getKey().toString());
                                } else {
                                    personNormal.add(entry.getKey().toString());
                                }
                            }
                            personNameList.addAll(personConcern);
                            personNameList.addAll(personNormal);
                            if (personNameList.isEmpty()) {
                                showToast("查询结果为零");
                            }
                            adapter.notifyDataSetChanged();
                            fis.close();
                            objectInputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.in_export:
                        try {
                            FileOutputStream fos =new FileOutputStream(file);
                            ObjectOutputStream objectOutputStream =new ObjectOutputStream(fos);
                            for (Map.Entry<String, Person> Entry : information.entrySet()) {
                                objectOutputStream.writeObject(Entry.getValue());
                            }
                            objectOutputStream.writeObject(null);  //对象写完后需加写一个null  作为结束标志
                            objectOutputStream.flush();
                            objectOutputStream.close();
                            objectOutputStream.close();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.out_import:
                        try {
                            FileInputStream fis=new FileInputStream(dir);
                            ObjectInputStream objectInputStream=new ObjectInputStream(fis);
                            Person temp=new Person();
                            ArrayList<Person> tmplist =new ArrayList<>();
                            information.clear();
                            while ((temp = (Person)objectInputStream.readObject())!=null){
                                information.put(temp.getName(),temp);
                            }
                            personNameList.clear();
                            personNormal.clear();
                            personConcern.clear();
                            for (Map.Entry entry : information.entrySet()) {
                                if (((Person)entry.getValue()).getIsOnFocus() == 1) {
                                    personConcern.add(entry.getKey().toString());
                                } else {
                                    personNormal.add(entry.getKey().toString());
                                }
                            }
                            personNameList.addAll(personConcern);
                            personNameList.addAll(personNormal);
                            adapter.notifyDataSetChanged();
                            fis.close();
                            objectInputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.out_export:
                        dir.delete();
                        try {
                            FileOutputStream fos =new FileOutputStream(dir);
                            ObjectOutputStream objectOutputStream =new ObjectOutputStream(fos);
                            for (Map.Entry<String, Person> Entry : information.entrySet()) {
                                objectOutputStream.writeObject(Entry.getValue());
                            }
                            objectOutputStream.writeObject(null);  //对象写完后需加写一个null  作为结束标志
                            objectOutputStream.flush();
                            objectOutputStream.close();
                            objectOutputStream.close();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });

        //获取数据库
        db = FinalDb.create(mContext, "test");
        //构建一个列表适配器
        adapter = new PersonAdapter(mContext, R.layout.item, personNameList, Color.WHITE, personConcern);
        lv_friend = getView().findViewById(R.id.lv_friend);
        init_listView();
    }


    //选中的item
    private int i;
    private PersonAdapter adapter;

    //person表
    public static HashMap<String, Person> information = new HashMap<>();
    //真正的姓名表
    private ArrayList<String> personNameList = new ArrayList<String>();
    //普通
    private ArrayList<String> personNormal = new ArrayList<>();
    //特别关心
    private ArrayList<String> personConcern = new ArrayList<>();


    //刷新适配器
    public void refreshListView() {
        ArrayList<Person> personList = (ArrayList<Person>) db.findAll(Person.class);
        information.clear();
        personNameList.clear();
        personNormal.clear();
        personConcern.clear();
        for (Person person : personList) {
            information.put(person.getName(), person);
            if (person.getIsOnFocus() == 1) {
                personConcern.add(person.getName());
            } else {
                personNormal.add(person.getName());
            }
        }
        personNameList.addAll(personConcern);
        personNameList.addAll(personNormal);
        if (personNameList.isEmpty()) {
            showToast("查询结果为零");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //从xml中构建菜单界面布局
        getActivity().getMenuInflater().inflate(R.menu.friend_popupmenu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        if (personConcern.contains(personNameList.get(info.position))) {
            menu.getItem(2).setTitle("取消关注");
        } else {
            menu.getItem(2).setTitle("关注");
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.popup_delete:
                db.deleteByWhere(Person.class, String.format("number='%s'", information.get(personNameList.get(info.position)).getNumber()));
                refreshListView();
                showToast("删除成功");
                break;
            case R.id.popup_edit:
                Intent intent = new Intent();
                intent.setAction("com.czdxwx.editInformation");
                Person p = information.get(personNameList.get(info.position));
                Bundle bundle = new Bundle();
                bundle.putString("name", p.getName());
                bundle.putString("number", p.getNumber());
                bundle.putString("gender", p.getGender());
                bundle.putString("hobby", p.getHobby());
                bundle.putString("nativePlace", p.getNativePlace());
                bundle.putInt("majorid", p.getMajorid());
                bundle.putString("profile", p.getProfile());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popup_concern:
                String s = information.get(personNameList.get(info.position)).getNumber();
                if (personConcern.contains(personNameList.get(info.position))) {
                    Person person = new Person();
                    person.setIsOnFocus(0);
                    db.update(person, String.format("number='%s'", information.get(personNameList.get(info.position)).getNumber()));
                    refreshListView();
                } else {
                    Person person = new Person();
                    person.setIsOnFocus(1);
                    db.update(person, String.format("number='%s'", information.get(personNameList.get(info.position)).getNumber()));
                    TabFragmentActivity.N = Integer.parseInt(s.substring(s.length() - 1, s.length())) + Integer.parseInt(s.substring(s.length() - 2, s.length() - 1));
                    refreshListView();
                }
                break;
            case R.id.popup_call:
                call(information.get(personNameList.get(info.position)).getNumber());
                break;
        }
        return true;
    }

    //列表视图
    private void init_listView() {
        lv_friend = (ListView) (getView().findViewById(R.id.lv_friend));
        lv_friend.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lv_friend.setPadding(0, 0, 0, 0);//设置四周空白
        lv_friend.setBackgroundColor(Color.TRANSPARENT);//设置背景色
        lv_friend.setTextFilterEnabled(true);
        lv_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction("com.czdxwx.getInformation");
                Person p = information.get(personNameList.get(position));
                Bundle bundle = new Bundle();
                bundle.putString("name", p.getName());
                bundle.putString("number", p.getNumber());
                bundle.putString("gender", p.getGender());
                bundle.putString("hobby", p.getHobby());
                bundle.putString("nativePlace", p.getNativePlace());
                bundle.putString("major", db.findAllByWhere(Major.class, String.format("majorid='%s'", p.getMajorid())).get(0).getMajor());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void onStart() {
        super.onStart();
        //刷新列表
        refreshListView();
        //注册上下文菜单
        registerForContextMenu(lv_friend);
        adapter.getFilter().filter("");
    }

    public void onStop() {
        super.onStop();
        //取消注册
        unregisterForContextMenu(lv_friend);
    }


    //呼叫
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //封装Toast
    private void showToast(String desc) {
        Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show();
    }

}