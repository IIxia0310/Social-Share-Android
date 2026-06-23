//package com.example.registerapplication.View;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.ColorDrawable;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.Toolbar;
//import android.widget.VideoView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.LinearLayoutCompat;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.example.registerapplication.Entity.Data.NoteItem;
//import com.example.registerapplication.Entity.Note;
//import com.example.registerapplication.Entity.Data.UserItem;
//import com.example.registerapplication.Entity.User;
//import com.example.registerapplication.R;
//import com.example.registerapplication.Response.NoteResponse;
//import com.example.registerapplication.Service.NoteService;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Callback;
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import retrofit2.Call;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class NoteAdd extends AppCompatActivity {
//    private NoteAdd currentActivity = NoteAdd.this;
//
//    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
//    private static final int PICK_FILE_REQUEST = 2;
//    private static final int MAX_RETRIES = 3;
//    private static final String UPLOAD_URL = "http://10.0.2.2:8080/upload"; // 真机需替换为真实IP
//    private static final String[] REQUIRED_PERMISSIONS = {
//            Manifest.permission.READ_EXTERNAL_STORAGE
//    };
//
//    private Map<Uri, File> tempFileMap = new HashMap<>();
//    private Map<Uri, String> uriToImageUrlMap = new HashMap<>();
//    private Handler handler = new Handler(Looper.getMainLooper());
//
//    private LinearLayoutCompat theme;
//    private LinearLayout imageContainer;
//    private boolean[] isImageSelectedArray = new boolean[1];
//    private VideoView videoContainer;
//
//    private Button button_select_image;
//    private Button button_select_interest;
//    private TextView textview_interest;
//
//    private EditText note_title, note_content;
//
//    private LinearLayout button_look;
//    private PopupWindow popupWindow;
//    private View popupView;
//
//    private Button button_bottom1_draft, button_bottom2_preview, button_bottom3_post_Notest;
//    private int currentSelectedButton = -1;
//    private ImageView currentSelectedImageView;
//    private NoteService noteService;
//
//    private long note_id = 0;
//    private long user_id;
//    private int type = 0;
//    private int visibility = 1; // 默认设置为 open
//    private int draft = 2;
//    private String interest;
//    private String title;
//    private String content;
//    private String image_urls;
//    private String video_url;
//    private String create_time = getCurrentTime();
//    private String update_time = getCurrentTime();
//
//    private NoteItem noteItem;
//    private UserItem userItem = UserItem.getUserItem();
//    private User user;
//
//    // 百度云API配置
//    private static final String API_KEY = "QqjrGYmcLS3Tn9jlxncxl7bg"; // 替换为你的API Key
//    private static final String SECRET_KEY = "yuMPuXS4byBiuE4pv3lG8bLIcK2tJsDU"; // 替换为你的Secret Key
//    private static final String BAIDU_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
//    private static final String BAIDU_TEXT_CENSOR_URL = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
//    private String accessToken = null;
//    private long tokenExpireTime = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.note_add);
//
//        UserItem.getUserItem().setAddNoteInterest("—空");
//
//        initToolbar();
//        initViews(); // 先初始化视图
//        initButton(); // 按钮
//
//        noteItem = new NoteItem();
//        user = new User();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(createOkHttpClient())
//                .build();
//
//        noteService = retrofit.create(NoteService.class);
//
//        // 检查并请求权限
//        if (!hasRequiredPermissions()) {
//            requestPermissions();
//        }
//    }
//
//    private boolean hasRequiredPermissions() {
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(
//                this,
//                REQUIRED_PERMISSIONS,
//                PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
//        );
//    }
//
//
//    private void initToolbar() {
//        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.Toolbar_Add);
//        toolbar.setTitle("添加笔记");
//        toolbar.setNavigationIcon(R.drawable.toolbar_2);
//        toolbar.setNavigationOnClickListener(v -> finish());
//    }
//
//
//    // 初始化组件
//    private void initViews() {
//        imageContainer = findViewById(R.id.image_container);
//        videoContainer = findViewById(R.id.video_container);
//        note_title = findViewById(R.id.Add_title);
//        note_content = findViewById(R.id.Add_content);
//
//        theme = findViewById(R.id.Theme_NoteAdd);
//        user = userItem.getUser();
//        if (user != null) {
//            switch (user.getTheme()) {
//                case 1:
//                    theme.setBackgroundResource(R.drawable.background1_pink_blue);
//                    break;
//                case 2:
//                    theme.setBackgroundResource(R.drawable.background2_yellow_blue);
//                    break;
//                case 3:
//                    theme.setBackgroundResource(R.drawable.background3_pink_yellow);
//                    break;
//                case 4:
//                    theme.setBackgroundResource(R.drawable.background4_pink_purple);
//                    break;
//                case 5:
//                    theme.setBackgroundResource(R.drawable.background5_purple_green);
//                    break;
//                case 6:
//                    theme.setBackgroundResource(R.drawable.background6_green_blue);
//                    break;
//                default:
//                    theme.setBackgroundResource(R.drawable.background1_pink_blue);
//                    break;
//            }
//        }
//    }
//
//    // 按钮
//    private void initButton() {
//        // 选择兴趣
//        button_select_interest = findViewById(R.id.Add_button_interest);
//        button_select_interest.setOnClickListener(v -> {
//            Intent intent = new Intent(currentActivity, InterestsNote.class);
//            startActivity(intent);
//        });
//
//        // 选择图片
//        button_select_image = findViewById(R.id.Add_button_image);
//        button_select_image.setOnClickListener(v -> {
//            if (!isNetworkAvailable()) {
//                Toast.makeText(this, "网络不可用，请检查网络连接", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!hasRequiredPermissions()) {
//                Toast.makeText(this, "需要读取存储权限才能选择文件", Toast.LENGTH_SHORT).show();
//                requestPermissions();
//                return;
//            }
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*");
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            startActivityForResult(intent, PICK_FILE_REQUEST);
//        });
//
//        // 选择可见性
//        button_look = findViewById(R.id.Add_drawer1_visibility);
//        button_look.setOnClickListener(v -> LookDrawerMenu());
//
//        // 选择草稿
//        button_bottom1_draft = findViewById(R.id.Add_bottom_draft);
//        button_bottom1_draft.setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
//            builder.setTitle("提示")
//                    .setMessage("确定要存草稿吗？")
//                    .setPositiveButton("确定", (dialog, which) -> {
//                        draft = 1;
//                        AddNote();
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        });
//
//        // 选择预览
//        button_bottom2_preview = findViewById(R.id.Add_bottom_preview);
//        button_bottom2_preview.setOnClickListener(v -> showPreview());
//
//        // 选择发布笔记
//        button_bottom3_post_Notest = findViewById(R.id.Add_bottom_postNotes);
//        button_bottom3_post_Notest.setOnClickListener(v -> AddNote());
//    }
//
//    // 预览
//    private void showPreview() {
//        // 获取数据
//        user = UserItem.getUserItem().getUser();
//        title = note_title.getText().toString();
//        content = note_content.getText().toString();
//
//        if (image_urls == null) {
//            Toast.makeText(currentActivity, "请先添加图片", Toast.LENGTH_SHORT).show();
//        } else {
//            Note note1 = new Note(null, user_id, 0L, visibility, draft, interest, title, content, image_urls, video_url, create_time, update_time);
//
//            noteItem.setNote(note1);
//            noteItem.setUser(user);
//            noteItem.setLikeSum((long) 0);
//            noteItem.setLikeIng(0);
//
//            // 点击了定位，打开笔记
//            Intent intent1 = new Intent(NoteAdd.this, NoteDetailsShowPreview.class);
//            intent1.putExtra("note_item", noteItem);
//            startActivity(intent1);
//        }
//    }
//
//    // 笔记类型显示
//    @Override
//    protected void onResume() {
//        super.onResume();
//        textview_interest = findViewById(R.id.Add_interest);
//        String interest = UserItem.getUserItem().getAddNoteInterest();
//        Log.d("AddActivity", "Get interest: " + interest);
//
//        if (textview_interest != null) {
//            textview_interest.setText(interest != null ?
//                    "类型内容为：" + interest : "未获取到选择的内容");
//        }
//    }
//
//    // 可见性功能，抽屉
//    private void LookDrawerMenu() {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        popupView = inflater.inflate(R.layout.drawer_note_add_notelook, null);
//
//        final int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true;
//        popupWindow = new PopupWindow(popupView, width, height, focusable);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        LinearLayout add_open = popupView.findViewById(R.id.AddLook_drawer1_open);
//        LinearLayout add_privacy = popupView.findViewById(R.id.AddLook_drawer2_privacy);
//        LinearLayout add_intercorrelation = popupView.findViewById(R.id.AddLook_drawer3_intercorrelation);
//
//        final TextView add_open_textView = add_open.findViewById(R.id.AddLook_middle1_open);
//        final TextView add_privacy_TextView = add_privacy.findViewById(R.id.AddLook_middle2_privacy);
//        final TextView add_intercorrelation_TextView = add_intercorrelation.findViewById(R.id.AddLook_middle3_intercorrelation);
//
//        final TextView drawerMiddle = button_look.findViewById(R.id.Add_middle1_visibility);
//
//        // 根据保存的状态设置默认选中的选项
//        switch (visibility) {
//            case 1:
//                currentSelectedButton = R.id.AddLook_right1_open;
//                updateImageView(R.id.AddLook_right1_open, R.drawable.ic_all2);
//                break;
//            case 2:
//                currentSelectedButton = R.id.AddLook_right2_privacy;
//                updateImageView(R.id.AddLook_right2_privacy, R.drawable.ic_all2);
//                break;
//            case 3:
//                currentSelectedButton = R.id.AddLook_right3_intercorrelation;
//                updateImageView(R.id.AddLook_right3_intercorrelation, R.drawable.ic_all2);
//                break;
//            default:
//                currentSelectedButton = R.id.AddLook_right1_open;
//                updateImageView(R.id.AddLook_right1_open, R.drawable.ic_all2);
//                break;
//        }
//
//        add_open.setOnClickListener(v -> {
//            drawerMiddle.setText(add_open_textView.getText());
//            visibility = 1; // 更新当前可见性状态
//            popupWindow.dismiss();
//        });
//
//        add_privacy.setOnClickListener(v -> {
//            drawerMiddle.setText(add_privacy_TextView.getText());
//            visibility = 2; // 更新当前可见性状态
//            popupWindow.dismiss();
//        });
//
//        add_intercorrelation.setOnClickListener(v -> {
//            drawerMiddle.setText(add_intercorrelation_TextView.getText());
//            visibility = 3; // 更新当前可见性状态
//            popupWindow.dismiss();
//        });
//
//        popupWindow.showAtLocation(button_look, Gravity.BOTTOM, 0, 0);
//    }
//
//    // 抽屉显示设置更改
//    private void updateImageView(int imageViewId, int iconResId) {
//        // 重置上一个选中的 ImageView
//        if (currentSelectedImageView != null && currentSelectedButton != imageViewId) {
//            currentSelectedImageView.setImageResource(R.drawable.ic_all2); // 设置为未选中状态图标
//        }
//        // 查找并更新当前选中的 ImageView
//        ImageView targetImageView = popupView.findViewById(imageViewId);
//        if (targetImageView != null) {
//            targetImageView.setImageResource(R.drawable.ic_all3); // 设置为选中状态图标
//            currentSelectedButton = imageViewId;
//            currentSelectedImageView = targetImageView;
//        }
//    }
//
//    // 时间
//    private String getCurrentTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sdf.format(new Date());
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
//            List<Uri> fileUris = new ArrayList<>();
//            if (data.getClipData() != null) {
//                int count = data.getClipData().getItemCount();
//                for (int i = 0; i < count; i++) {
//                    Uri uri = data.getClipData().getItemAt(i).getUri();
//                    fileUris.add(uri);
//                }
//            } else if (data.getData() != null) {
//                fileUris.add(data.getData());
//            }
//
//            StringBuilder newImageUrls = new StringBuilder();
//            if (image_urls != null) {
//                newImageUrls.append(image_urls);
//            }
//            boolean isVideoSelected = false;
//            final boolean[] isImageSelected = {type == 1};
//
//            for (final Uri uri : fileUris) {
//                String mimeType = getContentResolver().getType(uri);
//                if (mimeType != null) {
//                    if (mimeType.startsWith("image")) {
//                        if (isVideoSelected) {
//                            video_url = null;
//                            videoContainer.setVisibility(View.GONE);
//                            isVideoSelected = false;
//                        }
//                        type = 1;
//                        isImageSelected[0] = true;
//
//                        ImageView imageView = createImageView();
//                        loadImage(uri, imageView);   // 显示图片
//
//                        ImageView deleteIcon = createDeleteIcon(uri, imageView);  // 显示删除按钮
//
//                        FrameLayout frameLayout = createFrameLayout(imageView, deleteIcon);
//
//                        // 显示图片
//                        imageContainer.addView(frameLayout);
//                        videoContainer.setVisibility(View.GONE);
//
//                        String imageUrl = uriToImageUrlMap.get(uri);
//                        if (imageUrl != null) {
//                            if (newImageUrls.length() > 0) {
//                                newImageUrls.append(",");
//                            }
//                            newImageUrls.append(imageUrl);
//                        }
//                    }
//                }
//                new Thread(new UploadFileRunnable(uri)).start();
//            }
//
//            if (isVideoSelected) {
//                newImageUrls.setLength(0);
//            }
//
//            image_urls = newImageUrls.toString();
//            if (image_urls.isEmpty()) {
//                image_urls = null;
//            }
//        }
//    }
//
//    // 显示图片的尺寸
//    private ImageView createImageView() {
//        ImageView imageView = new ImageView(this);
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int size = (int) (100 * displayMetrics.density);
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
//        return imageView;
//    }
//
//    // 图片删除功能
//    private ImageView createDeleteIcon(final Uri uri, final ImageView imageView) {
//        ImageView deleteIcon = new ImageView(this);
//        deleteIcon.setImageResource(R.drawable.add_button3);
//        deleteIcon.setLayoutParams(new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                Gravity.TOP | Gravity.END
//        ));
//        deleteIcon.setOnClickListener(v -> {
//            View parent = (View) imageView.getParent();
//            imageContainer.removeView(parent);
//            File tempFile = tempFileMap.get(uri);
//            if (tempFile != null) {
//                tempFile.delete();
//                tempFileMap.remove(uri);
//            }
//            String urlToRemove = uriToImageUrlMap.get(uri);
//            if (urlToRemove != null) {
//                if (image_urls != null) {
//                    String[] urls = image_urls.split(",");
//                    StringBuilder newUrls = new StringBuilder();
//                    for (String url : urls) {
//                        if (!url.equals(urlToRemove)) {
//                            if (newUrls.length() > 0) {
//                                newUrls.append(",");
//                            }
//                            newUrls.append(url);
//                        }
//                    }
//                    image_urls = newUrls.toString();
//                }
//                uriToImageUrlMap.remove(uri);
//            }
//
//            if (imageContainer.getChildCount() == 0) {
//                type = 0;
//                isImageSelectedArray[0] = false;
//                image_urls = null;
//            }
//            video_url = null;
//        });
//        return deleteIcon;
//    }
//
//    private FrameLayout createFrameLayout(ImageView imageView, ImageView deleteIcon) {
//        FrameLayout frameLayout = new FrameLayout(this);
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int size = (int) (100 * displayMetrics.density);
//        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                size + 1, size + 1
//        ));
//        frameLayout.addView(imageView);
//        frameLayout.addView(deleteIcon);
//        return frameLayout;
//    }
//
//    // 显示图片
//    private void loadImage(Uri uri, ImageView imageView) {
//        Glide.with(this)
//                .load(uri)
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.jz)
//                        .error(R.drawable.image_bizhi1)
//                        .centerCrop())
//                .into(imageView);
//    }
//
//    // 图片上传功能
//    private class UploadFileRunnable implements Runnable {
//        private Uri fileUri;
//
//        public UploadFileRunnable(Uri fileUri) {
//            this.fileUri = fileUri;
//        }
//
//        @Override
//        public void run() {
//            boolean success = false;
//            int currentRetry = 0;
//
//            while (!success && currentRetry < MAX_RETRIES) {
//                try {
//                    File tempFile = createTempFileFromUri(fileUri);
//                    if (tempFile != null) {
//                        ContentResolver contentResolver = getContentResolver();
//                        String mimeType = contentResolver.getType(fileUri);
//                        MediaType mediaType = MediaType.parse(mimeType != null ? mimeType : "application/octet-stream");
//                        OkHttpClient client = createOkHttpClient();
//                        RequestBody requestBody = new MultipartBody.Builder()
//                                .setType(MultipartBody.FORM)
//                                .addFormDataPart("file", tempFile.getName(), RequestBody.create(mediaType, tempFile))
//                                .build();
//                        Request request = new Request.Builder()
//                                .url(UPLOAD_URL)
//                                .post(requestBody)
//                                .build();
//                        try (Response response = client.newCall(request).execute()) {
//                            if (response.isSuccessful()) {
//                                String responseBody = response.body().string();
//                                if (isValidJson(responseBody)) {
//                                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//                                    if (jsonObject.has("url")) {
//                                        String fileUrl = jsonObject.get("url").getAsString();
//                                        success = true;
//                                        handler.post(() -> {
//                                            if (mimeType != null && mimeType.startsWith("image")) {
//                                                if (image_urls == null) {
//                                                    image_urls = fileUrl;
//                                                } else {
//                                                    image_urls += "," + fileUrl;
//                                                }
//                                                uriToImageUrlMap.put(fileUri, fileUrl);
//                                            }
//                                            Toast.makeText(currentActivity, "上传成功", Toast.LENGTH_SHORT).show();
//                                        });
//                                    } else {
//                                        logError("服务器响应缺少url字段", response);
//                                    }
//                                } else {
//                                    logError("服务器返回非JSON响应", response);
//                                }
//                            } else {
//                                if (response.code() == 404) {
//                                    Log.e("UploadError", "HTTP 404: 服务器无法找到该接口");
//                                    Log.e("UploadError", "请求的 URL: " + UPLOAD_URL);
//                                }
//                                logError("上传失败，HTTP状态码: " + response.code(), response);
//                            }
//                        }
//                        tempFile.delete();
//                    } else {
//                        handler.post(() -> Toast.makeText(currentActivity, "创建临时文件失败", Toast.LENGTH_SHORT).show());
//                    }
//                } catch (IOException e) {
//                    logError("上传过程中出现IO异常: " + e.getMessage(), null);
//                } catch (Exception e) {
//                    logError("上传过程中出现未知异常: " + e.getMessage(), null);
//                }
//
//                if (!success) {
//                    currentRetry++;
//                    if (currentRetry < MAX_RETRIES) {
//                        try {
//                            Thread.sleep(1000 * currentRetry);
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//                }
//            }
//
//            if (!success) {
//                handler.post(() -> Toast.makeText(currentActivity,
//                        "上传失败，已达到最大重试次数", Toast.LENGTH_SHORT).show());
//            }
//        }
//
//        private boolean isValidJson(String json) {
//            try {
//                new JsonParser().parse(json);
//                return true;
//            } catch (Exception e) {
//                return false;
//            }
//        }
//
//        private void logError(String message, Response response) {
//            String fullMessage = message;
//            if (response != null) {
//                try {
//                    fullMessage += "\n响应体: " + response.body().string();
//                } catch (IOException e) {
//                    fullMessage += "\n无法读取响应体";
//                }
//            }
//            Log.e("UploadError", fullMessage);
//            handler.post(() -> Toast.makeText(currentActivity,
//                    "上传失败: " + message, Toast.LENGTH_SHORT).show());
//        }
//    }
//
//    private File createTempFileFromUri(Uri uri) {
//        InputStream inputStream = null;
//        FileOutputStream outputStream = null;
//        try {
//            ContentResolver contentResolver = getContentResolver();
//            inputStream = contentResolver.openInputStream(uri);
//            if (inputStream != null) {
//                File tempFile = File.createTempFile("temp", null, getCacheDir());
//                outputStream = new FileOutputStream(tempFile);
//
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//                tempFileMap.put(uri, tempFile);
//                return tempFile;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    // 检查网络
//    private OkHttpClient createOkHttpClient() {
//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS);
//        clientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                Request newRequest = originalRequest.newBuilder()
//                        .addHeader("Accept", "application/json")
//                        .build();
//                return chain.proceed(newRequest);
//            }
//        });
//        return clientBuilder.build();
//    }
//
//    // 检查选择图片的网络连接
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
//    // 存储权限
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 权限已授予，可以继续操作
//            } else {
//                Toast.makeText(this, "需要读取存储权限才能选择文件", Toast.LENGTH_SHORT).show();
//                showPermissionSettingsDialog();
//            }
//        }
//    }
//
//    // 查看权限方法
//    private void showPermissionSettingsDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("权限请求")
//                .setMessage("您拒绝了存储权限，这将影响文件选择功能。请前往设置中开启权限。")
//                .setPositiveButton("前往设置", (dialog, which) -> {
//                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                })
//                .setNegativeButton("取消", null)
//                .show();
//    }
//
//    // 发布笔记
//    private void AddNote() {
//        // 获取数据
//        user_id = UserItem.getUserItem().getUserlogin();
//        interest = UserItem.getUserItem().getAddNoteInterest();
//        title = note_title.getText().toString();
//        content = note_content.getText().toString();
//
//        if (interest == null || interest.equals("—空")) {
//            Toast.makeText(currentActivity, "请选择笔记类型", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (type == 0) {
//            Toast.makeText(currentActivity, "请添加图片", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 检查内容安全
//        checkContentSafety(title, content, new OnCheckResultListener() {
//            @Override
//            public void onCheckPass() {
//                // 内容审核通过，继续发布
//                Long pageViews = Long.valueOf(0);
//                Note note = new Note(null, user_id, pageViews, visibility, draft, interest, title, content, image_urls, video_url, create_time, update_time);
//
//                Call<NoteResponse> call = noteService.addNote(note);
//                call.enqueue(new retrofit2.Callback<NoteResponse>() {
//                    @Override
//                    public void onResponse(Call<NoteResponse> call, retrofit2.Response<NoteResponse> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            NoteResponse noteResponse = response.body();
//                            if (noteResponse.isSuccess()) {
//                                finish();
//                                setResult(RESULT_OK);
//
//                                Toast.makeText(currentActivity,
//                                        draft == 1 ? "添加草稿箱成功" : "添加笔记成功",
//                                        Toast.LENGTH_SHORT).show();
//
//                                // 发送广播
//                                Intent broadcastIntent = new Intent("com.example.registerapplication.REFRESH_NOTE_LIST");
//                                sendBroadcast(broadcastIntent);
//                            } else {
//                                Toast.makeText(currentActivity, "添加失败", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            try {
//                                String errorBody = response.errorBody() != null ?
//                                        response.errorBody().string() : "未知错误";
//                                Log.e("API_ERROR", "错误响应: " + errorBody);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<NoteResponse> call, Throwable t) {
//                        Log.e("LoginFailure", "添加笔记：网络请求失败: " + t.getMessage());
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCheckBlocked(String reason) {
//                Toast.makeText(currentActivity, "内容违规：" + reason, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCheckFailed(String error) {
//                Toast.makeText(currentActivity, "内容审核失败：" + error, Toast.LENGTH_LONG).show();
//                // 审核失败时，仍然允许发布（可根据需求调整）
//                Long pageViews = Long.valueOf(0);
//                Note note = new Note(null, user_id, pageViews, 2, draft, interest, title, content, image_urls, video_url, create_time, update_time);
//
//                Call<NoteResponse> call = noteService.addNote(note);
//                call.enqueue(new retrofit2.Callback<NoteResponse>() {
//                    @Override
//                    public void onResponse(Call<NoteResponse> call, retrofit2.Response<NoteResponse> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            NoteResponse noteResponse = response.body();
//                            if (noteResponse.isSuccess()) {
//                                finish();
//                                setResult(RESULT_OK);
//
//                                Toast.makeText(currentActivity,
//                                        draft == 1 ? "添加草稿箱成功" : "添加笔记成功",
//                                        Toast.LENGTH_SHORT).show();
//
//                                // 发送广播
//                                Intent broadcastIntent = new Intent("com.example.registerapplication.REFRESH_NOTE_LIST");
//                                sendBroadcast(broadcastIntent);
//                            } else {
//                                Toast.makeText(currentActivity, "添加失败", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            try {
//                                String errorBody = response.errorBody() != null ?
//                                        response.errorBody().string() : "未知错误";
//                                Log.e("API_ERROR", "错误响应: " + errorBody);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<NoteResponse> call, Throwable t) {
//                        Log.e("LoginFailure", "添加笔记：网络请求失败: " + t.getMessage());
//
//                    }
//                });
//            }
//        });
//    }
//
//    // NoteAdd.java
//    // 定义回调接口
//    public interface OnNoteAddedListener {
//        void onNoteAdded();
//    }
//
//    private OnNoteAddedListener onNoteAddedListener;
//
//    public void setOnNoteAddedListener(OnNoteAddedListener listener) {
//        this.onNoteAddedListener = listener;
//    }
//
//    // 使用 OkHttp 调用百度内容审核 API
//    private void checkContentSafety(String title, String content, final OnCheckResultListener listener) {
//        // 检查 accessToken 是否存在或过期
//        if (accessToken == null || System.currentTimeMillis() > tokenExpireTime) {
//            // 获取新的 accessToken
//            getAccessToken(new OnTokenReceivedListener() {
//                @Override
//                public void onTokenReceived(String token, long expireTime) {
//                    accessToken = token;
//                    tokenExpireTime = expireTime;
//                    performTextCensor(title, content, listener);
//                }
//
//                @Override
//                public void onTokenError(String error) {
//                    listener.onCheckFailed("获取认证失败：" + error);
//                }
//            });
//        } else {
//            // 直接使用现有 token 进行内容审核
//            performTextCensor(title, content, listener);
//        }
//    }
//
//    // 执行文本审核请求
//    // 执行文本审核请求
//    private void performTextCensor(String title, String content, final OnCheckResultListener listener) {
//        OkHttpClient client = new OkHttpClient();
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        String requestBody = "text=" + title + " " + content;
//
//        Request request = new Request.Builder()
//                .url(BAIDU_TEXT_CENSOR_URL + "?access_token=" + accessToken)
//                .method("POST", RequestBody.create(mediaType, requestBody))
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//                currentActivity.runOnUiThread(() -> {
//                    listener.onCheckFailed("网络请求失败：" + e.getMessage());
//                });
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String result = response.body().string();
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        if (json.has("conclusionType")) {
//                            int conclusionType = json.getInt("conclusionType");
//                            if (conclusionType == 1) {
//                                currentActivity.runOnUiThread(() -> {
//                                    listener.onCheckPass();
//                                });
//                            } else {
//                                StringBuilder reason = new StringBuilder();
//                                if (json.has("data")) {
//                                    JSONArray dataArray = json.getJSONArray("data");
//                                    for (int i = 0; i < dataArray.length(); i++) {
//                                        JSONObject item = dataArray.getJSONObject(i);
//                                        if (item.has("msg")) {
//                                            reason.append(item.getString("msg")).append("; ");
//                                        }
//                                    }
//                                }
//
//                                // 确保reason有内容
//                                if (reason.length() == 0) {
//                                    reason.append("内容包含违规信息");
//                                }
//
//                                currentActivity.runOnUiThread(() -> {
//                                    listener.onCheckBlocked(reason.toString());
//                                });
//                            }
//                        } else {
//                            currentActivity.runOnUiThread(() -> {
//                                listener.onCheckFailed("API返回格式异常：缺少conclusionType字段");
//                            });
//                        }
//                    } catch (JSONException e) {
//                        currentActivity.runOnUiThread(() -> {
//                            listener.onCheckFailed("解析结果失败：" + e.getMessage());
//                        });
//                    }
//                } else {
//                    currentActivity.runOnUiThread(() -> {
//                        listener.onCheckFailed("API返回错误：" + response.code());
//                    });
//                }
//            }
//        });
//    }
//
//    // 获取百度云 access_token
//    private void getAccessToken(final OnTokenReceivedListener listener) {
//        OkHttpClient client = new OkHttpClient();
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        String requestBody = "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
//
//        Request request = new Request.Builder()
//                .url(BAIDU_TOKEN_URL)
//                .method("POST", RequestBody.create(mediaType, requestBody))
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
//                Log.e("BaiduAPI", "获取AccessToken失败: " + e.getMessage());
//                listener.onTokenError("网络错误：" + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String result = response.body().string();
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        if (json.has("access_token") && json.has("expires_in")) {
//                            String token = json.getString("access_token");
//                            long expiresIn = json.getLong("expires_in");
//                            // 设置过期时间（提前5分钟刷新）
//                            long expireTime = System.currentTimeMillis() + (expiresIn - 300) * 1000;
//                            listener.onTokenReceived(token, expireTime);
//                        } else {
//                            Log.e("BaiduAPI", "AccessToken响应格式异常: " + result);
//                            listener.onTokenError("API返回格式异常：缺少access_token或expires_in字段");
//                        }
//                    } catch (JSONException e) {
//                        Log.e("BaiduAPI", "解析AccessToken失败: " + e.getMessage());
//                        listener.onTokenError("解析JSON失败：" + e.getMessage());
//                    }
//                } else {
//                    Log.e("BaiduAPI", "获取AccessToken失败，HTTP状态码: " + response.code());
//                    listener.onTokenError("HTTP错误：" + response.code());
//                }
//            }
//        });
//
//    }
//
//    // 定义获取令牌的回调接口
//    private interface OnTokenReceivedListener {
//        void onTokenReceived(String token, long expireTime);
//
//        void onTokenError(String error);
//    }
//
//    // 定义内容审核结果的回调接口
//    private interface OnCheckResultListener {
//        void onCheckPass();
//
//        void onCheckBlocked(String reason);
//
//        void onCheckFailed(String error);
//    }
//
//}