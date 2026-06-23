package com.example.registerapplication.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;

import com.example.registerapplication.Entity.Data.UserItem;
import com.example.registerapplication.Entity.User;
import com.example.registerapplication.R;


/**
 * 社区公约
 */
public class Side7CommunityConvention extends AppCompatActivity {

    private LinearLayoutCompat theme;
    private UserItem userItem =UserItem.getUserItem();
    private User user = userItem.getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side7_community_convention);

        initToolbar();
        initViews(); // 先初始化视图


    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.Toolbar_Side7CommunityConvention);
        toolbar.setTitle("社区公约");
        toolbar.setNavigationIcon(R.drawable.toolbar_2);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void  initViews(){
        theme = findViewById(R.id.Theme_Side7CommunityConvention);
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
}