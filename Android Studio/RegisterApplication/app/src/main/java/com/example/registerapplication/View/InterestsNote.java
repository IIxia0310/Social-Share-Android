package com.example.registerapplication.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registerapplication.Adapters.InterestsNoteAdapter;
import com.example.registerapplication.Entity.Data.InterestsItem;
import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择兴趣页面类
 */
public class InterestsNote extends AppCompatActivity {

    private InterestsNote currentActivity = InterestsNote.this;

    private GridView gridView;
    private Button confirmButton;
    private InterestsNoteAdapter interestsSelectNoteAdapter;

    private LinearLayout theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user = userItem.getUser();

    // 定义主页面类名常量
    public static final String MAIN_ACTIVITY_CLASS_NAME = "com.example.registerapplication.View.Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_interests);


        initViews(); // 先初始化视图
        initButton(); // 按钮
    }
    private void  initViews(){
        gridView = findViewById(R.id.Interests2_gridView);
        confirmButton = findViewById(R.id.btn_confirm);

        // 准备数据
        List<InterestsItem> dataList = new ArrayList<>();
        dataList.add(new InterestsItem(R.drawable.a1, "旅游"));
        dataList.add(new InterestsItem(R.drawable.a2, "美食"));
        dataList.add(new InterestsItem(R.drawable.a3, "彩妆"));
        dataList.add(new InterestsItem(R.drawable.a4, "护肤"));
        dataList.add(new InterestsItem(R.drawable.a5, "发型"));
        dataList.add(new InterestsItem(R.drawable.a6, "游戏"));
        dataList.add(new InterestsItem(R.drawable.a7, "摄影"));
        dataList.add(new InterestsItem(R.drawable.a8, "影视"));
        dataList.add(new InterestsItem(R.drawable.a9, "头像"));
        dataList.add(new InterestsItem(R.drawable.a10, "搞笑"));
        dataList.add(new InterestsItem(R.drawable.a11, "音乐"));
        dataList.add(new InterestsItem(R.drawable.a12, "学习"));
        dataList.add(new InterestsItem(R.drawable.a13, "舞蹈"));
        dataList.add(new InterestsItem(R.drawable.a14, "绘画"));
        dataList.add(new InterestsItem(R.drawable.a15, "家居"));
        dataList.add(new InterestsItem(R.drawable.a16, "情感"));
        dataList.add(new InterestsItem(R.drawable.a17, "明星"));
        dataList.add(new InterestsItem(R.drawable.a18, "运动"));
        dataList.add(new InterestsItem(R.drawable.a19, "健身"));
        dataList.add(new InterestsItem(R.drawable.a20, "社交"));
        dataList.add(new InterestsItem(R.drawable.a21, "露营"));
        dataList.add(new InterestsItem(R.drawable.a22, "机车"));
        dataList.add(new InterestsItem(R.drawable.a23, "汽车"));
        dataList.add(new InterestsItem(R.drawable.a24, "婚礼"));
        dataList.add(new InterestsItem(R.drawable.a25, "母婴"));
        dataList.add(new InterestsItem(R.drawable.a26, "宠物"));
        dataList.add(new InterestsItem(R.drawable.a27, "家具"));

        interestsSelectNoteAdapter = new InterestsNoteAdapter(this, dataList);
        gridView.setAdapter(interestsSelectNoteAdapter);


        theme = findViewById(R.id.Theme_NoteInterests);
        if (user != null) {
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
    private void  initButton() {
        // 处理 GridView 的点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                interestsSelectNoteAdapter.setSelectedPosition(position);
            }
        });

        // 处理确认按钮的点击事件
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interestsSelectNoteAdapter != null) {
                    int selectedPosition = interestsSelectNoteAdapter.getSelectedPosition();
                    if (selectedPosition != -1) {
                        InterestsItem selectedItem = (InterestsItem) interestsSelectNoteAdapter.getItem(selectedPosition);
                        if (selectedItem != null) {
                            String interest = selectedItem.getText();
                            Log.d("InterestsSelectActivity", "Selected interest: " + interest);
                            if (interest != null) {

                                UserItem.getUserItem().setAddNoteInterest(interest);
                                Toast.makeText(currentActivity, "你选择了："+interest, Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                Log.e("InterestsSelectActivity", "Interest text is null");
                                Toast.makeText(currentActivity, "选择的兴趣文本为空，请重新选择", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("InterestsSelectActivity", "Selected item is null");
                            Toast.makeText(currentActivity, "选择的项目为空，请重新选择", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(currentActivity, "请选择一个类型", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(currentActivity, "适配器为空，无法获取选择项", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}