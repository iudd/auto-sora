package com.iudd.autosora;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    
    private ToolbarFragment toolbarFragment;
    private BottomNavigationFragment bottomNavigationFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 设置工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // 初始化Fragment
        initFragments();
    }
    
    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // 添加工具栏Fragment
        toolbarFragment = new ToolbarFragment();
        transaction.replace(R.id.toolbar_container, toolbarFragment);
        
        // 添加底部导航Fragment
        bottomNavigationFragment = new BottomNavigationFragment();
        transaction.replace(R.id.bottom_navigation_container, bottomNavigationFragment);
        
        transaction.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_settings) {
            // 处理设置点击
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}