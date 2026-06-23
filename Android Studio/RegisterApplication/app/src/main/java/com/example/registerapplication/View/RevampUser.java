package com.example.registerapplication.View;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.registerapplication.Entity.Data.LocationData;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;
import com.example.registerapplication.Response.UserResponse;
import com.example.registerapplication.Service.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 修改用户页面
 */
public class RevampUser extends AppCompatActivity {

    private UserService userRevampService;
    private RevampUser currentActivity = RevampUser.this;

    private static final int PICK_FILE_REQUEST = 2;
    private static final int MAX_RETRIES = 3;
    private Map<Uri, File> tempFileMap = new HashMap<>();
    private Map<Uri, String> uriToImageUrlMap = new HashMap<>();
    private Handler handler = new Handler(Looper.getMainLooper());


    private static final String TAG = "RevampUser";
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1; // 此权限允许应用读取外部存储设备上的文件
    private static final String UPLOAD_URL = "http://10.0.2.2:8080/upload"; // 上传图片的服务器 URL


    private LinearLayout  theme;
    private ImageView UserRevamp_PImage;       // 头像（ImageView）
    private EditText UserRevamp_name;      // 姓名
    private EditText UserRevamp_id;        // 账号
    private EditText UserRevamp_signature; // 个性签名
    private TextView UserRevamp_interests; // 兴趣
    private TextView UserRevamp_sex;      // 性别
    private TextView UserRevamp_BTime;    // 生日
    private TextView UserRevamp_location;  // 定位

    private LinearLayout UserRevamp_drawer_BImage;     // 抽屉
    private LinearLayout UserRevamp_drawer_interests;
    private LinearLayout UserRevamp_drawer_sex;
    private LinearLayout UserRevamp_drawer_BTime;
    private LinearLayout UserRevamp_drawer_location;


    private ImageView UserRevamp_BImage;  // 背景
    private Button UserRevamp_bottom;
    private Button UserRevamp_Image_button;


    private View ViewSex;
    private View ViewBTime;
    private View ViewLocation;

    private PopupWindow PopupWindowSex;
    private PopupWindow PopupWindowBTime;
    private PopupWindow PopupWindowLocation;


    private String string_id;
    private long userId; // 用户 ID
    private String password; // 用户密码
    private String username; // 用户名
    private String sex; // 性别
    private String interests; // 兴趣
    private String signature; // 个签
    private String location; // 定位
    private String portraitImage; // 头像
    private String backgroundImage; // 背景
    private String birthdayTime; // 出生日期
    private String createTime; // 注册时间

    private Spinner spinner_Year, spinner_Month, spinner_Day; //出生日期：年月日
    private String selectedYear, selectedMonth, selectedDay;

    private Spinner spinner_Province, spinner_City;  //地区
    private String selectedProvince, selectedCity;

    private List<String> provinceList = new ArrayList<>();    // 存储省份数据
    private Map<String, List<String>> cityMap = new HashMap<>();  // 存储城市数据，以省份为键，对应城市列表为值


    private int currentSelectedButton = -1;//当前选择
    private ImageView currentSelectedImageView;//上一个选择


    // 用于处理从相册选择图片结果的 ActivityResultLauncher
    private ActivityResultLauncher<Intent> galleryLauncher;
    private int img = 0;

    private UserItem userItem =UserItem.getUserItem();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revamp_user);

        initToolbar();
        initViews(); // 先初始化视图
        initButton();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();

        userRevampService = retrofit.create(UserService.class);


        // 检查并请求读取外部存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            loadUserInfo();
        }

        // 从相册选择图片结果, 初始化从相册选择图片的回调
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                new Thread(new UploadFileRunnable(imageUri)).start();
                                if (img == 0) {
                                    // 先清空背景图
                                    UserRevamp_BImage.setImageBitmap(null);
                                    UserRevamp_BImage.setBackground(null);

                                    loadImage(imageUri, UserRevamp_BImage);   //显示背景
                                } else if (img == 1) {
                                    UserRevamp_PImage.setImageBitmap(null);
                                    loadImage(imageUri, UserRevamp_PImage);   //显示头像
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_RevampUser);
        toolbar.setTitle("编辑资料");
        toolbar.setNavigationIcon(R.drawable.toolbar_2); // 设置菜单栏图标

        // 设置导航图标的点击监听器
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 初始化视图
    private void initViews() {


        UserRevamp_PImage = findViewById(R.id.RevampUser_PImage);  // 头像
        UserRevamp_name = findViewById(R.id.RevampUser_name);
        UserRevamp_id = findViewById(R.id.RevampUser_id);
        UserRevamp_signature = findViewById(R.id.RevampUser_signature);


        UserRevamp_BImage = findViewById(R.id.RevampUser_BImage);  // 背景
        UserRevamp_interests = findViewById(R.id.RevampUser_interests);
        UserRevamp_sex = findViewById(R.id.RevampUser_sex);
        UserRevamp_BTime = findViewById(R.id.RevampUser_BTime);
        UserRevamp_location = findViewById(R.id.RevampUser_location);

        theme = findViewById(R.id.Theme_RevampUser);
        user = userItem.getUser();
        if (user!= null) {
            switch (user.getTheme()) {
                case 1:
                    theme.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
                case 2:
                    theme.setBackgroundResource(R.drawable.background2_yellow_blue);
                    break;
                case 3:
                    theme.setBackgroundResource(R.drawable.background3_pink_yellow);
                    break;
                case 4:
                    theme.setBackgroundResource(R.drawable.background4_pink_purple);
                    break;
                case 5:
                    theme.setBackgroundResource(R.drawable.background5_purple_green);
                    break;
                case 6:
                    theme.setBackgroundResource(R.drawable.background6_green_blue);
                    break;
                default:
                    theme.setBackgroundResource(R.drawable.background1_pink_blue);
                    break;
            }
        }
    }

    private void initButton() {

        // 更改头像按钮
        UserRevamp_Image_button = findViewById(R.id.UserRevamp_Image_button);
        UserRevamp_Image_button.setOnClickListener(v -> {
            img = 1;
            checkAndOpenGallery();
        });


        // 更换背景图
        UserRevamp_drawer_BImage = findViewById(R.id.RevampUser_drawer_BImage);
        UserRevamp_drawer_BImage.setOnClickListener(v -> {
            img = 0;
            checkAndOpenGallery();
        });

        // 更换性别
        UserRevamp_drawer_sex = findViewById(R.id.RevampUser_drawer_sex);
        UserRevamp_drawer_sex.setOnClickListener(v -> SexDrawerMenu());

        // 更换生日
        UserRevamp_drawer_BTime = findViewById(R.id.RevampUser_drawer_BTime);
        UserRevamp_drawer_BTime.setOnClickListener(v -> BTimeDrawerMenu());


        // 更换地区
        UserRevamp_drawer_location = findViewById(R.id.RevampUser_drawer_location);
        UserRevamp_drawer_location.setOnClickListener(v -> LocationDrawerMenu());


        //  // 保存按钮
        UserRevamp_bottom = findViewById(R.id.RevampUser_bottom);
        UserRevamp_bottom.setOnClickListener(v -> GetUserData());
    }

    // 获取修改数据
    private void GetUserData() {

        string_id = UserRevamp_id.getText().toString();      //账号
        username = UserRevamp_name.getText().toString();    //姓名
        signature = UserRevamp_signature.getText().toString();    //个性签名


        //判断姓名是否有空格
        boolean isUserName = false;
        for (int i = 0; i < username.length(); i++) {
            char ch = username.charAt(i);
            if (Character.isWhitespace(ch) || (!Character.isLetterOrDigit(ch))) {
                isUserName = true;
                break;
            }
        }

        //判断输入是否为空
        if (string_id.isEmpty()) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;

        } else if (username.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }


        //判断输入格式是否正确
        if (!isId(string_id)) {
            Toast.makeText(this, "输入的不是有效的手机号", Toast.LENGTH_SHORT).show();
            return;
        }else if(isUserName){
            Toast.makeText(this, "（姓名）包含空格或特殊符号，请重新输入", Toast.LENGTH_SHORT).show();
        }else {
            userId = Long.parseLong(string_id);
            UserRevamp();//调用修改方法
        }
    }

    // 修改用户信息方法
    private void UserRevamp() {
        long userIdIng = UserItem.getUserItem().getUserlogin();          // 创建User对象
        User user = new User(userId, password, username,1,
                sex, interests, signature, location, portraitImage, backgroundImage, birthdayTime, createTime);
        // 调用接口，传User
        Call<UserResponse> call = userRevampService.userRevamp(userIdIng, user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseBody error = response.errorBody();
                    UserResponse userRevampResponse = response.body();
                    if (error == null) {
                        UserResponse userResponse = response.body();
                        if (userResponse != null && userResponse.isSuccess()) {
                            // 增加延迟，确保数据库更新完成
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                // 再次查询用户信息，确保更新成功
                                User updatedUser = userRevampResponse.getUser();
                                UserItem.getUserItem().setUser(updatedUser);
                                Toast.makeText(currentActivity, "信息更改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }, 500); // 延迟 500 毫秒
                        } else {
                            Toast.makeText(currentActivity, "信息更改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("UserRevampFailure", "网络请求失败: " + t.getMessage()); // 记录失败日志
            }
        });
    }

    //性别功能，抽屉
    private void SexDrawerMenu() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewSex = inflater.inflate(R.layout.drawer_user_revamp_sex, null);

        final int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindowSex = new PopupWindow(ViewSex, width, height, focusable);
        PopupWindowSex.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout sex1 = ViewSex.findViewById(R.id.UserRevamp_drawer1_sex1);
        LinearLayout sex2 = ViewSex.findViewById(R.id.UserRevamp_drawer1_sex2);

        //中间文字
        final TextView middle1 = sex1.findViewById(R.id.UserRevamp_middle_sex1);
        final TextView middle2 = sex2.findViewById(R.id.UserRevamp_middle_sex2);

        // 根据保存的状态设置默认选中的选项
        switch (sex) {
            case "男":
                currentSelectedButton = R.id.UserRevamp_right1_sex1;
                updateImageView(R.id.UserRevamp_right1_sex1, R.drawable.ic_all2);
                break;
            case "女":
                currentSelectedButton = R.id.UserRevamp_right1_sex2;
                updateImageView(R.id.UserRevamp_right1_sex2, R.drawable.ic_all2);
                break;
            default:
                break;
        }

        sex1.setOnClickListener(v -> {
            UserRevamp_sex.setText(middle1.getText());
            UserRevamp_sex.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));
            sex = "男";
            PopupWindowSex.dismiss();
        });

        sex2.setOnClickListener(v -> {
            UserRevamp_sex.setText(middle2.getText());
            UserRevamp_sex.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));
            sex = "女";
            PopupWindowSex.dismiss();
        });

        PopupWindowSex.showAtLocation(UserRevamp_drawer_sex, Gravity.BOTTOM, 0, 0);
    }

    //抽屉显示设置更改
    private void updateImageView(int imageViewId, int iconResId) {
        //重置上一个选中的 ImageView
        if (currentSelectedImageView != null && currentSelectedButton != imageViewId) {
            currentSelectedImageView.setImageResource(R.drawable.ic_all2); // 设置为未选中状态图标
        }

        //查找并更新当前选中的 ImageView
        ImageView targetImageView = ViewSex.findViewById(imageViewId);
        if (targetImageView != null) {
            targetImageView.setImageResource(R.drawable.ic_all3); // 设置为选中状态图标
            currentSelectedButton = imageViewId;
            currentSelectedImageView = targetImageView;
        }
    }

    //生日日期选择功能，抽屉
    private void BTimeDrawerMenu() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewBTime = inflater.inflate(R.layout.drawer_user_revamp_btime, null);
        final int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindowBTime = new PopupWindow(ViewBTime, width, height, focusable);
        PopupWindowBTime.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //年月日
        spinner_Year = ViewBTime.findViewById(R.id.UserRevamp_spinnerYear);
        spinner_Month = ViewBTime.findViewById(R.id.UserRevamp_spinnerMonth);
        spinner_Day = ViewBTime.findViewById(R.id.UserRevamp_spinnerDay);

        // 设置年份数据
        List<String> yearList = new ArrayList<>();
        for (int i = 1920; i <= 2025; i++) {
            yearList.add(String.valueOf(i));
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                yearList
        );
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Year.setAdapter(yearAdapter);

        // 设置月份数据
        List<String> monthList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            monthList.add(String.valueOf(i));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                monthList
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Month.setAdapter(monthAdapter);

        // 设置日期数据（简单示例，未考虑大小月及闰年情况）
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            dayList.add(String.valueOf(i));
        }

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dayList
        );
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Day.setAdapter(dayAdapter);

        // 为 Spinner 添加选择监听器
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = spinner_Year.getSelectedItem().toString();
                selectedMonth = spinner_Month.getSelectedItem().toString();
                selectedDay = spinner_Day.getSelectedItem().toString();

                birthdayTime = selectedYear + "-" + selectedMonth + "-" + selectedDay; // 出生日期
                UserRevamp_BTime.setText(birthdayTime);
                UserRevamp_BTime.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 不做处理
            }
        };

        spinner_Year.setOnItemSelectedListener(listener);
        spinner_Month.setOnItemSelectedListener(listener);
        spinner_Day.setOnItemSelectedListener(listener);

        // 初始化显示当前选择的日期
        selectedYear = spinner_Year.getSelectedItem().toString();
        selectedMonth = spinner_Month.getSelectedItem().toString();
        selectedDay = spinner_Day.getSelectedItem().toString();
        birthdayTime = selectedYear + "-" + selectedMonth + "-" + selectedDay; // 出生日期


        UserRevamp_BTime.setText(birthdayTime);
        UserRevamp_BTime.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));

        PopupWindowBTime.showAtLocation(UserRevamp_drawer_BTime, Gravity.BOTTOM, 0, 0);
    }


    //地区选择功能，抽屉
    private void LocationDrawerMenu() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewLocation = inflater.inflate(R.layout.drawer_user_revamp_location, null);
        final int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindowLocation = new PopupWindow(ViewLocation, width, height, focusable);
        PopupWindowLocation.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //初始化数据
        spinner_Province = ViewLocation.findViewById(R.id.UserRevamp_location_province);
        spinner_City = ViewLocation.findViewById(R.id.UserRevamp_location_city);

        // 从 RegionDataManager 获取省份和城市数据
        provinceList = LocationData.getProvinceList();
        cityMap = LocationData.getCityMap();

        // 设置省份 Spinner 的适配器
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, provinceList);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Province.setAdapter(provinceAdapter);

        // 省份 Spinner 的选择监听器
        spinner_Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProvince = provinceList.get(position);
                List<String> cities = cityMap.get(selectedProvince);
                // 设置城市 Spinner 的适配器
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(RevampUser.this,
                        android.R.layout.simple_spinner_item, cities);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_City.setAdapter(cityAdapter);

                // 当省份改变时，更新地区显示
                selectedCity = cities != null && !cities.isEmpty() ? cities.get(0) : "";
                location = selectedProvince + "·" + selectedCity;
                UserRevamp_location.setText(location);
                UserRevamp_location.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 城市 Spinner 的选择监听器
        spinner_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProvince = spinner_Province.getSelectedItem().toString();
                selectedCity = spinner_City.getSelectedItem().toString();
                // 拼接
                location = selectedProvince + "·" + selectedCity; // 地区
                UserRevamp_location.setText(location);
                UserRevamp_location.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 初始化显示当前选择的地区
        selectedProvince = spinner_Province.getSelectedItem().toString();
        List<String> initialCities = cityMap.get(selectedProvince);
        selectedCity = initialCities != null && !initialCities.isEmpty() ? initialCities.get(0) : "";
        location = selectedProvince + "·" + selectedCity;
        UserRevamp_location.setText(location);
        UserRevamp_location.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));

        PopupWindowLocation.showAtLocation(UserRevamp_drawer_location, Gravity.BOTTOM, 0, 0);
    }

    // 检查权限并打开相册
    private void checkAndOpenGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    // 打开相册
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // 权限被拒绝的处理逻辑
                Log.e(TAG, "Read external storage permission denied");
            }
        }
    }

    // 假设这里是加载用户信息的方法，设置 EditText 等视图的默认值
    private void loadUserInfo() {
        User userIng = UserItem.getUserItem().getUser();
        if (userIng != null) {
            // 更新UI
            // 获取用户数据

            string_id = String.valueOf(userIng.getUserId());
            username = userIng.getUsername();
            password = userIng.getPassword();
            sex = userIng.getSex();
            interests = userIng.getInterests();
            signature = userIng.getSignature();
            location = userIng.getLocation();
            portraitImage = userIng.getPortraitImage();
            backgroundImage = userIng.getBackgroundImage();
            birthdayTime = userIng.getBirthdayTime();
            createTime = userIng.getCreateTime();


            // 设置初始文本和颜色
            setInitialTextAndColor(UserRevamp_id, string_id, R.color.cover);
            setInitialTextAndColor(UserRevamp_name, username, R.color.cover);
            setInitialTextAndColor(UserRevamp_sex, sex, R.color.cover);
            setInitialTextAndColor(UserRevamp_interests, interests, R.color.cover);
            setInitialTextAndColor(UserRevamp_signature, signature, R.color.cover);
            setInitialTextAndColor(UserRevamp_location, location, R.color.cover);
            setInitialTextAndColor(UserRevamp_BTime, birthdayTime, R.color.cover);

            String imageUrl = "http://10.0.2.2:8080/uploads/";

            String userIngBackgroundImage = userIng.getBackgroundImage();
            String BackgroundImageUrl = imageUrl + userIngBackgroundImage;
            new ImageDownloadTask(ImageType.BACKGROUND).execute(BackgroundImageUrl);

            String userIngPortraitImage = userIng.getPortraitImage();
            String PortraitImageUrl = imageUrl + userIngPortraitImage;
            new ImageDownloadTask(ImageType.PORTRAIT).execute(PortraitImageUrl);
        } else {
            Log.e(TAG, "user____null ");
        }
    }

    private enum ImageType {
        PORTRAIT,
        BACKGROUND
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
        private ImageType imageType;

        public ImageDownloadTask(ImageType imageType) {
            this.imageType = imageType;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    return BitmapFactory.decodeStream(inputStream);
                } else {
                    Log.e(TAG, "Request failed with code: " + response.code());
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException occurred: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                switch (imageType) {
                    case PORTRAIT:
                        UserRevamp_PImage.setImageBitmap(bitmap);
                        break;
                    case BACKGROUND:
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(RevampUser.this.getResources(), bitmap);
                        UserRevamp_BImage.setBackground(bitmapDrawable);
                        break;
                }
            } else {
                Log.e(TAG, "Bitmap is null, image loading failed.");
            }
        }
    }

    // 编辑时的字体颜色更改
    private void setInitialTextAndColor(TextView view, String text, int colorResId) {
        view.setText(text);
        view.setTextColor(ContextCompat.getColor(this, colorResId));
        if (view instanceof EditText) {
            ((EditText) view).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        view.setTextColor(ContextCompat.getColor(RevampUser.this, R.color.black));
                    } else {
                        view.setTextColor(ContextCompat.getColor(RevampUser.this, colorResId));
                    }
                }
            });
        }
    }

    // 从 Uri 获取 Bitmap
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        InputStream input = getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        input.close();

        // 计算缩放比例
        int reqWidth = UserRevamp_PImage.getWidth();
        int reqHeight = UserRevamp_PImage.getHeight();
        int inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
        input.close();
        return bitmap;
    }

    // 计算缩放比例
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {


        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    // 判断手机号ID格式
    public boolean isId(String string_id) {
        // 正则表达式，用于匹配常见的中国手机号格式
        String regex =  "^1\\d{10}$";
        return Pattern.matches(regex, string_id);
    }

    //网络
    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        });

        return clientBuilder.build();
    }

    private File createTempFileFromUri(Uri uri) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            ContentResolver contentResolver = getContentResolver();
            inputStream = contentResolver.openInputStream(uri);
            if (inputStream != null) {
                File tempFile = File.createTempFile("temp", null, getCacheDir());
                outputStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                tempFileMap.put(uri, tempFile);
                return tempFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private void loadImage(Uri uri, ImageView imageView) {
        Glide.with(this)
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.jz)
                        .error(R.drawable.jz )
                )
                .into(imageView);
    }


    private class UploadFileRunnable implements Runnable {
        private Uri fileUri;

        public UploadFileRunnable(Uri fileUri) {
            this.fileUri = fileUri;
        }

        @Override
        public void run() {
            boolean success = false;
            int currentRetry = 0;

            while (!success && currentRetry < MAX_RETRIES) {
                try {
                    File tempFile = createTempFileFromUri(fileUri);
                    if (tempFile != null) {
                        ContentResolver contentResolver = getContentResolver();
                        String mimeType = contentResolver.getType(fileUri);
                        MediaType mediaType = MediaType.parse(mimeType != null ? mimeType : "application/octet-stream");
                        OkHttpClient client = createOkHttpClient();
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", tempFile.getName(), RequestBody.create(mediaType, tempFile))
                                .build();
                        Request request = new Request.Builder()
                                .url(UPLOAD_URL)
                                .post(requestBody)
                                .build();
                        try (Response response = client.newCall(request).execute()) {
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                if (isValidJson(responseBody)) {
                                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                                    if (jsonObject.has("url")) {
                                        String fileUrl = jsonObject.get("url").getAsString();
                                        success = true;
                                        handler.post(() -> {

                                            if (img == 0) {
                                                // 先清空背景图
                                                backgroundImage = fileUrl;

                                            } else if (img == 1) {
                                                portraitImage = fileUrl;

                                            }
                                            Toast.makeText(currentActivity, "上传成功", Toast.LENGTH_SHORT).show();
                                        });
                                    } else {
                                        logError("服务器响应缺少url字段", response);
                                    }
                                } else {
                                    logError("服务器返回非JSON响应", response);
                                }
                            } else {
                                if (response.code() == 404) {
                                    Log.e("UploadError", "HTTP 404: 服务器无法找到该接口");
                                    Log.e("UploadError", "请求的 URL: " + UPLOAD_URL);
                                }
                                logError("上传失败，HTTP状态码: " + response.code(), response);
                            }
                        }
                        tempFile.delete();
                    } else {
                        handler.post(() -> Toast.makeText(currentActivity, "创建临时文件失败", Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException e) {
                    logError("上传过程中出现IO异常: " + e.getMessage(), null);
                } catch (Exception e) {
                    logError("上传过程中出现未知异常: " + e.getMessage(), null);
                }

                if (!success) {
                    currentRetry++;
                    if (currentRetry < MAX_RETRIES) {
                        try {
                            Thread.sleep(1000 * currentRetry);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            if (!success) {
                handler.post(() -> Toast.makeText(currentActivity,
                        "上传失败，已达到最大重试次数", Toast.LENGTH_SHORT).show());
            }
        }

        private boolean isValidJson(String json) {
            try {
                new JsonParser().parse(json);
                return true;
            } catch (JsonSyntaxException e) {
                return false;
            }
        }

        private void logError(String message, Response response) {
            String fullMessage = message;
            if (response != null) {
                try {
                    fullMessage += "\n响应体: " + response.body().string();
                } catch (IOException e) {
                    fullMessage += "\n无法读取响应体";
                }
            }
            Log.e("UploadError", fullMessage);
            handler.post(() -> Toast.makeText(currentActivity,
                    "上传失败: " + message, Toast.LENGTH_SHORT).show());
        }
    }

}