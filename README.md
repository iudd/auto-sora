# 自动Sora浏览器 - 开发需求文档

## 📱 项目概述

**项目名称**: 自动Sora浏览器  
**平台**: Android  
**开发工具**: AIDE (Android IDE)  
**目标**: 轻量级浏览器，专注于视频分享地址的批量获取和存储

## 🎯 核心功能

### 1. 基础浏览功能
- ✅ 地址栏输入网址
- ✅ 网页加载和显示 (WebView)
- ✅ 基础导航 (后退、前进、刷新)
- ✅ 书签功能 (收藏常用网站)

### 2. 视频地址提取 ⭐⭐⭐
- ✅ 自动检测页面中的视频分享地址
- ✅ 支持各种视频平台和网站
- ✅ 提取多种分享链接格式：
  - 标准分享URL
  - 嵌入代码
  - API链接
  - 播放地址

### 3. 批量存储管理 ⭐⭐⭐
- ✅ 自动保存提取的视频地址
- ✅ 本地数据库存储 (SQLite)
- ✅ 分类管理 (按平台、日期等)
- ✅ 导出功能：
  - 文本文件导出
  - JSON格式导出
  - 批量复制到剪贴板
- ✅ 历史记录查看
- ✅ 删除/编辑功能

### 4. 用户界面
- ✅ 简洁的Material Design风格
- ✅ 底部导航栏：
  - 首页 (浏览器)
  - 收藏夹
  - 历史记录
  - 设置
- ✅ 夜间模式
- ✅ 手势操作支持

## 🏗️ 技术架构

### 前端 (UI)
- **语言**: Kotlin
- **UI框架**: Android Jetpack Compose (推荐) 或 XML Layout
- **主题**: Material 3 Design

### 核心功能
- **WebView**: 网页渲染
- **Room**: 数据持久化
- **Jsoup/Regex**: HTML解析和地址提取
- **OkHttp**: 网络请求 (如果需要)

### 数据结构
```kotlin
// 视频地址实体
data class VideoLink(
    val id: Long = 0,
    val title: String,
    val url: String,
    val platform: String, // 自动识别平台
    val thumbnail: String? = null,
    val duration: String? = null,
    val extractedAt: Long = System.currentTimeMillis()
)

// 书签实体
data class Bookmark(
    val id: Long = 0,
    val title: String,
    val url: String,
    val createdAt: Long = System.currentTimeMillis()
)
```

## 📋 开发计划

### Phase 1: 基础框架 (1-2天)
- [ ] 创建Android项目结构
- [ ] 配置基本依赖
- [ ] 实现主界面布局
- [ ] WebView基础功能

### Phase 2: 核心功能 (2-3天)
- [ ] 视频地址提取算法
- [ ] 数据库设计和实现
- [ ] 存储管理界面
- [ ] 批量导出功能

### Phase 3: 优化完善 (1-2天)
- [ ] UI/UX优化
- [ ] 错误处理
- [ ] 性能优化
- [ ] 测试和调试

## 🔧 技术要求

### Android版本兼容性
- **最低版本**: API 21 (Android 5.0)
- **目标版本**: API 34 (Android 14)

### 权限需求
- `INTERNET`: 网络访问
- `ACCESS_NETWORK_STATE`: 网络状态检查
- `WRITE_EXTERNAL_STORAGE`: 文件导出 (API < 29)
- `READ_EXTERNAL_STORAGE`: 文件读取 (API < 29)

### 第三方库
```gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'androidx.room:room-ktx:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'
    implementation 'org.jsoup:jsoup:1.16.1' // HTML解析
}
```

## 🎨 UI设计草图

```
[顶部工具栏]
地址栏 | 刷新 | 更多

[WebView区域 - 主要内容]

[底部导航栏]
🏠 浏览器 | ⭐ 收藏 | 📚 历史 | ⚙️ 设置
```

## 📊 预期成果

1. **APK文件**: 可安装的Android应用
2. **源码**: 完整的Kotlin项目
3. **文档**: 使用说明和API文档

## ⚡ 性能目标

- **启动时间**: < 2秒
- **内存占用**: < 100MB
- **地址提取速度**: < 1秒/页面
- **存储效率**: 支持1000+地址存储

---

**开发人员**: AI助手  
**开始日期**: 2025-10-30  
**预计完成**: 1周内  

---

## ❓ 待确认问题

1. 是否需要夜间模式？
2. 是否支持多标签页？
3. 是否需要广告拦截功能？
4. 导出格式的具体要求？
5. 是否需要云端同步功能？

请确认这些需求，我将开始具体开发！