package com.iudd.autosora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ToolbarFragment extends Fragment {
    
    private EditText urlEditText;
    private Button goButton;
    private Button refreshButton;
    private WebView webView;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.toolbar, container, false);
        
        initViews(view);
        setupClickListeners();
        setupWebView();
        
        return view;
    }
    
    private void initViews(View view) {
        urlEditText = view.findViewById(R.id.url_edit_text);
        goButton = view.findViewById(R.id.go_button);
        refreshButton = view.findViewById(R.id.refresh_button);
        webView = getActivity().findViewById(R.id.webview);
    }
    
    private void setupClickListeners() {
        goButton.setOnClickListener(v -> {
            String url = urlEditText.getText().toString();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            webView.loadUrl(url);
        });
        
        refreshButton.setOnClickListener(v -> {
            webView.reload();
        });
    }
    
    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                urlEditText.setText(url);
            }
        });
        
        // 加载默认页面
        loadHomePage();
    }
    
    public void loadHomePage() {
        webView.loadUrl("https://www.google.com");
    }
}