package com.czdxwx.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.czdxwx.test.adapter.MajorAdapter;
import com.czdxwx.test.fragment.FriendFragment;
import com.czdxwx.test.model.Major;
import com.czdxwx.test.model.Person;
import com.czdxwx.test.model.Place;
import com.czdxwx.test.utils.ImgUtil;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addActivity extends FinalActivity {
    /*数据库*/
    private FinalDb db;
    /*头像框*/

    @ViewInject(id = R.id.imageView)
    ImageView imageView;
    private String ImagePath;

    //姓名框
    @ViewInject(id = R.id.et_name)
    EditText et_name;

    //姓名格式验证
    public static boolean isLegalName(String name) {
        if (name.contains("·") || name.contains("•")) {
            return name.matches("^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]+$");
        } else {
            return name.matches("^[\\u4e00-\\u9fa5]+$");
        }
    }

    //手机框
    @ViewInject(id = R.id.et_number)
    EditText et_number;

    //手机格式验证
    public static boolean isMobileNumber(String mobiles) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    /*性别的单选按钮*/

    @ViewInject(id = R.id.radioGroup)
    RadioGroup radioGroup;
    @ViewInject(id = R.id.rdoBtn_male)
    RadioButton male;
    @ViewInject(id = R.id.rdoBtn_female)
    RadioButton female;
    //属性性别
    private String gender = "男";

    /*爱好多选框*/

    @ViewInject(id = R.id.chkBtn_sports)
    CheckBox chkbtn_sport;
    @ViewInject(id = R.id.chkBtn_travel)
    CheckBox chkbtn_travel;
    @ViewInject(id = R.id.chkBtn_other)
    CheckBox chkbtn_other;
    private ArrayList<CheckBox> chkboxArr = new ArrayList<CheckBox>();
    private ArrayList<String> hobby = new ArrayList<String>();

    /*籍贯下拉框*/

    //籍贯数组
//    public static String[] list = {"江苏", "天津", "上海", "重庆", "河北", "山西", "辽宁", "吉林", "黑龙江", "北京", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾", "内蒙古", "广西", "西藏", "宁夏", "新疆", "香港", "澳门"};

    @ViewInject(id = R.id.sp_nativePlace)
    Spinner sp_nativePlace;
    //属性籍贯
    private String nativePlace = "江苏";

    //初始化籍贯下拉框
    private void init_sp_nativePlace() {
        InputStream in;
        try {
            in = this.getResources().openRawResource(R.raw.place);
            List = pullxml(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (Place place : List) {
            list[place.getId()]=place.getPlace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_select, list);
        //设置数组分配器的布局样式
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //获取下拉框
        sp_nativePlace = findViewById(R.id.sp_nativePlace);
        //设置下拉框标题
        sp_nativePlace.setPrompt("请选择籍贯");
        //设置适配器
        sp_nativePlace.setAdapter(adapter);
        //设置下拉框默认显示第一项
        sp_nativePlace.setSelection(0);
        sp_nativePlace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nativePlace = list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private List<Place> List = new ArrayList<>();
    public static String[] list=new String[34];

    /**
     * xml文件的获取
     * 1.xml文件放在res的xml文件夹下（推荐）使用XmlResourceParser xmlParser = this.getResources().getXml(R.xml.XXX);
     * 2.xml文件放在raw的xml文件夹下使用InputStream inputStream =this.getResources().openRawResource(R.raw.XXX);
     * 3.xml文件放在assets文件夹下(本人测试发现通过此方法获取的XML文档不能带有首行：<?xml version="1.0" encoding="utf-8"?>，
     * 否则解析报错，具体原因未查明:InputStream inputStream = getResources().getAssets().open(fileName);
     * 4.xml文件放在SD卡， path路径根据实际项目修改，此次获取SDcard根目录：
     * String path = Environment.getExternalStorageDirectory().toString();
     * File xmlFlie = new File(path+fileName);
     * InputStream inputStream = new FileInputStream(xmlFlie);
     */
    public List<Place> pullxml(InputStream in) throws Exception {
        List<Place> list = null;
        Place place = null;
        // 由android.util.Xml创建一个XmlPullParser实例
        XmlPullParser parser = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        parser.setInput(in, "UTF-8");
        // 产生第一个事件
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    list = new ArrayList<Place>();// 初始化list集合
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("Place")) { // 判断开始标签元素是否是Place
                        place = new Place();
                    } else if (parser.getName().equals("id")) {
                        eventType = parser.next();
                        // 得到Place标签的属性值，并设置Place的id
                        place.setId(Integer.parseInt(parser.getText()));
                    } else if (parser.getName().equals("place")) {
                        eventType = parser.next();
                        place.setPlace(parser.getText());
                    }
                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("place")) { // 判断结束标签元素是否是Place
                        list.add(place); // 将Place添加到Place集合
                        place = null;
                    }
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = parser.next();
        }
        return list;
    }


    /*是否关注开关*/

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @ViewInject(id = R.id.switch_focus)
    Switch switch_isOnFocus;

    /*专业列表*/

    @ViewInject(id = R.id.lv_major)
    ListView lv_major;
    private ArrayList<String> majorList = new ArrayList<String>();
    private int majorid = -1;
    private MajorAdapter adapter;

    //初始化列表
    private void init_listView() {
        ArrayList<Major> List = (ArrayList<Major>) db.findAll(Major.class);
        majorList.clear();
        for (Major it : List) {
            majorList.add(it.getMajor());
        }
        //构建一个专业列表适配器
        adapter = new MajorAdapter(this, R.layout.item, majorList, Color.WHITE);
        lv_major = (ListView) findViewById(R.id.lv_major);
        lv_major.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lv_major.setPadding(0, 100, 0, 100);//设置四周空白
        lv_major.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                majorid = position;
                adapter.p = majorid;
                adapter.notifyDataSetChanged();
            }
        });
    }

    /*两个按钮*/

    //ok按钮
    @ViewInject(id = R.id.btn_ok, click = "ok")
    Button btn_ok;

    //ok按钮绑定方法---功能：添加联系人
    public void ok(View v) {
        String name = et_name.getText().toString();
        String number = et_number.getText().toString();
        StringBuilder temp = new StringBuilder();
        ArrayList<String> arr = new ArrayList<String>();
        for (CheckBox it : chkboxArr) {
            if (it.isChecked()) {
                arr.add(it.getText().toString());
                temp.append(it.getText().toString()).append(",");
            }
        }
        if (name.length() <= 0) {
            showToast("请先填写姓名");
            return;
        } else if (!isLegalName(name)) {
            showToast("姓名格式错误");
            return;
        } else if (FriendFragment.information.containsKey(name)) {
            showToast("列表已存在该用户");
            return;
        } else if (!isMobileNumber(number)) {
            showToast("请输入正确的手机号");
            return;
        } else if (majorid == -1) {
            showToast("请选择所学专业");
            return;
        }


        Person info = new Person();
        info.setName(name);
        info.setNumber(number);
        info.setGender(gender);
        info.setHobby(temp.toString());
        info.setNativePlace(nativePlace);
        info.setMajorid(majorid);
        info.setIsOnFocus((switch_isOnFocus.isChecked()) ? 1 : 0);
        info.setProfile(ImagePath);
        db.save(info);
        showToast("数据已写入SQLite数据库");

        finish();
    }

    //cancel按钮
    @ViewInject(id = R.id.btn_cancel, click = "cancel")
    Button btn_cancel;

    //cancel按钮绑定方法
    public void cancel(View v) {
        finish();
    }

    /*标题栏*/

    @ViewInject(id = R.id.toolBar2)
    Toolbar toolBar2;

    //标题栏初始化
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init_toolBar() {
        //隐藏默认actionbar
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolBar2.setTitle("添加陌生人");
        //用toolbar替换actionbar
        setActionBar(toolBar2);
        getActionBar().setDisplayHomeAsUpEnabled(true);//显示toolbar的返回按钮
        toolBar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public addActivity() {
    }

    private SharedPreferences mShared;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        TabFragmentActivity.N = -1;
        //这一两行代码主要是向用户请求权限
        if (ActivityCompat.checkSelfPermission(addActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        db = FinalDb.create(this, "test");
        init();
    }

    //版本问题注解
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //初始化
    private void init() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                gender = rb.getText().toString();
            }
        });
        chkboxArr.add(chkbtn_travel);
        chkboxArr.add(chkbtn_sport);
        chkboxArr.add(chkbtn_other);
        init_toolBar();
        init_listView();
        init_sp_nativePlace();
    }


    @Override
    //从xml中构建选项菜单界面
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    //从xml中构建上下文菜单界面
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.main_image_menu, menu);
    }

    @Override
    //头像弹出菜单点击事件
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //头像相应点击事件
            case R.id.main_select_image:
                getPicFromLocal();
                break;
            case R.id.main_takePhoto:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getPicFromCamera();
                }
                break;
        }
        return true;
    }

    //相机拍摄的头像文件(本次演示存放在SD卡根目录下)
    private static final File USER_ICON = new File(Environment.getExternalStorageDirectory(), "user_icon.jpg");
    //请求识别码(分别为本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;

    /**
     * 从本机相册获取图片
     */
    private void getPicFromLocal() {
        // 获取本地相册方法一
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CODE_PHOTO_REQUEST);
    }

    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getPicFromCamera() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", USER_ICON);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, CODE_CAMERA_REQUEST);

    }



    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        /*outputX outputY 是裁剪图片宽高
         *这里仅仅是头像展示，不建议将值设置过高
         * 否则超过binder机制的缓存大小的1M限制
         * 报TransactionTooLargeException
         */
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_PHOTO_CLIP);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case CODE_CAMERA_REQUEST:
                if (USER_ICON.exists()) {
                    photoClip(Uri.fromFile(USER_ICON));
                }
                break;
            case CODE_PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case CODE_PHOTO_CLIP:
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //onStart生命周期
    protected void onStart() {
        super.onStart();
        registerForContextMenu(imageView);
        mShared = getSharedPreferences("share", MODE_PRIVATE);
        Map<String, Object> mapParam = (Map<String, Object>) mShared.getAll();
        if (!mapParam.isEmpty()) {
            if (!FriendFragment.information.containsKey(mapParam.get("name").toString())) {
                et_name.setText(mapParam.get("name").toString());
                et_number.setText(mapParam.get("number").toString());
                gender = mapParam.get("gender").toString();
                switch (gender) {
                    case "男":
                        female.setChecked(false);
                        male.setChecked(true);
                        break;
                    case "女":
                        male.setChecked(false);
                        female.setChecked(true);
                        break;
                    default:
                        female.setChecked(false);
                        male.setChecked(true);
                        break;
                }
                String[] temp = mapParam.get("hobby").toString().split(",");
                HashMap<String, CheckBox> map = new HashMap<>();
                for (CheckBox checkBox : chkboxArr) {
                    map.put(checkBox.getText().toString(), checkBox);
                }
                for (String s : temp) {
                    if (map.containsKey(s)) {
                        map.get(s).setChecked(map.containsKey(s));
                    }
                }
                switch_isOnFocus.setChecked((Boolean) mapParam.get("isOnFocus"));
                ArrayList<String> arr = new ArrayList<>();
                Collections.addAll(arr, addActivity.list);
                sp_nativePlace.setSelection(arr.indexOf(mapParam.get("nativePlace").toString()));
                majorid = (int) mapParam.get("majorid");
                adapter.p = majorid;
                adapter.notifyDataSetChanged();
                if (mapParam.get("profile") != null) {
                    imageView.setImageBitmap(ImgUtil.getImage(mapParam.get("profile").toString()));
                }
            }
        }
    }


    private SharedPreferences.Editor editor;

    //onStop
    protected void onStop() {
        super.onStop();
        unregisterForContextMenu(imageView);
        String name = et_name.getText().toString();
        String number = et_number.getText().toString();
        StringBuilder temp = new StringBuilder();
        ArrayList<String> arr = new ArrayList<String>();
        for (CheckBox it : chkboxArr) {
            if (it.isChecked()) {
                arr.add(it.getText().toString());
                temp.append(it.getText().toString()).append(",");
            }
        }
        //写入编辑器
        editor = mShared.edit();
        editor.putString("name", name);
        editor.putString("number", number);
        editor.putString("gender", gender);
        editor.putString("hobby", temp.toString());
        editor.putString("nativePlace", nativePlace);
        editor.putBoolean("isOnFocus", switch_isOnFocus.isChecked());
        editor.putInt("majorid", majorid);
        editor.putString("profile", ImagePath);
        //存到sharePreferen中
        editor.apply();

    }

    //封装Toast
    public void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }

//    public static void save(List<Place> persons, OutputStream outStream) throws Exception{
//        XmlSerializer serializer = Xml.newSerializer();
//        serializer.setOutput(outStream, "UTF-8");
//        serializer.startDocument("UTF-8", true);
//        serializer.startTag(null, "Places");
//        for(Place place : persons){
//            serializer.startTag(null, "place");
//
//            serializer.attribute(null, "id", place.getId()+"");
//            serializer.startTag(null, "place");
//            serializer.text(place.getPlace());
//            serializer.endTag(null, "place");
//
//            serializer.endTag(null, "place");
//        }
//        serializer.endTag(null, "Places");
//        serializer.endDocument();
//        outStream.flush();
//        outStream.close();
//    }
}