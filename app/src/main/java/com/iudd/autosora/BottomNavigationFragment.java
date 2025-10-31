package com.iudd.autosora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BottomNavigationFragment extends Fragment {
    
    private Button homeButton;
    private Button bookmarksButton;
    private Button historyButton;
    private Button settingsButton;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_navigation, container, false);
        
        initViews(view);
        setupClickListeners();
        
        return view;
    }
    
    private void initViews(View view) {
        homeButton = view.findViewById(R.id.home_button);
        bookmarksButton = view.findViewById(R.id.bookmarks_button);
        historyButton = view.findViewById(R.id.history_button);
        settingsButton = view.findViewById(R.id.settings_button);
    }
    
    private void setupClickListeners() {
        homeButton.setOnClickListener(v -> {
            // 返回主页
            if (getActivity() != null) {
                ToolbarFragment toolbarFragment = (ToolbarFragment) 
                    getActivity().getSupportFragmentManager().findFragmentById(R.id.toolbar_container);
                if (toolbarFragment != null) {
                    toolbarFragment.loadHomePage();
                }
            }
        });
        
        bookmarksButton.setOnClickListener(v -> {
            // 打开书签页面
            // TODO: 实现书签功能
        });
        
        historyButton.setOnClickListener(v -> {
            // 打开历史记录页面
            // TODO: 实现历史记录功能
        });
        
        settingsButton.setOnClickListener(v -> {
            // 打开设置页面
            // TODO: 实现设置功能
        });
    }
}