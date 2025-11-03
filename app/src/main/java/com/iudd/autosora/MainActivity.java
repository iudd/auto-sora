package com.iudd.autosora;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.iudd.autosora.R;

public class MainActivity extends AppCompatActivity {

    private Button homeButton;
    private Button bookmarksButton;
    private Button historyButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化按钮
        homeButton = findViewById(R.id.home_button);
        bookmarksButton = findViewById(R.id.bookmarks_button);
        historyButton = findViewById(R.id.history_button);
        settingsButton = findViewById(R.id.settings_button);

        setupClickListeners();
    }

    private void setupClickListeners() {
        homeButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 返回主页
                // TODO: 实现主页功能
            }
        });
        
        bookmarksButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 打开书签页面
                // TODO: 实现书签功能
            }
        });
        
        historyButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 打开历史记录页面
                // TODO: 实现历史记录功能
            }
        });
        
        settingsButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // 打开设置页面
                // TODO: 实现设置功能
            }
        });
    }
}