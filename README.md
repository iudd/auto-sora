# Auto Sora Browser

一个简单而功能完整的Android浏览器应用。

## 功能特性

- 🌐 **网页浏览**: 基于WebView的完整网页浏览体验
- 🔍 **地址栏**: 智能URL输入，自动添加https://前缀
- 🔄 **页面刷新**: 一键刷新当前页面
- 🏠 **主页导航**: 快速返回默认主页
- 📚 **书签功能**: 保存和管理常用网站 (待实现)
- 📜 **历史记录**: 查看浏览历史 (待实现)
- ⚙️ **设置选项**: 个性化配置 (待实现)

## 技术架构

### 核心组件

- **MainActivity**: 主活动，管理整体界面布局
- **ToolbarFragment**: 地址栏和导航控制
- **BottomNavigationFragment**: 底部导航栏
- **WebView**: 网页内容渲染

### 技术栈

- **语言**: Java
- **UI框架**: Android Jetpack (AppCompatActivity, Fragment)
- **布局**: XML布局文件
- **WebView**: Android系统WebView组件

### 项目结构

```
app/src/main/java/com/iudd/autosora/
├── MainActivity.java              # 主活动
├── ToolbarFragment.java           # 工具栏Fragment
└── BottomNavigationFragment.java # 底部导航Fragment

app/src/main/res/
├── layout/
│   ├── activity_main.xml         # 主界面布局
│   ├── toolbar.xml               # 工具栏布局
│   └── bottom_navigation.xml    # 底部导航布局
├── menu/
│   └── main_menu.xml             # 主菜单
├── values/
│   ├── strings.xml               # 字符串资源
│   ├── colors.xml                # 颜色资源
│   └── themes.xml                # 主题样式
└── AndroidManifest.xml           # 应用清单
```

## 权限说明

应用需要以下权限：

- `INTERNET`: 访问网络内容
- `ACCESS_NETWORK_STATE`: 检查网络连接状态

## 编译要求

- **最小SDK版本**: 21 (Android 5.0)
- **目标SDK版本**: 34 (Android 14)
- **编译SDK版本**: 34

## 依赖库

```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.fragment:fragment:1.6.2'
```

## 使用说明

1. **启动应用**: 打开应用后会自动加载Google主页
2. **浏览网页**: 在地址栏输入URL，点击"前往"按钮
3. **刷新页面**: 点击"刷新"按钮重新加载当前页面
4. **返回主页**: 点击底部导航的"主页"按钮

## 开发计划

### 已完成功能
- [x] 基础网页浏览
- [x] 地址栏和导航
- [x] 页面刷新
- [x] 底部导航界面

### 待实现功能
- [ ] 书签管理
- [ ] 历史记录
- [ ] 设置页面
- [ ] 下载管理
- [ ] 多标签页支持
- [ ] 全屏浏览
- [ ] 夜间模式

## 贡献指南

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

本项目采用MIT许可证开源。