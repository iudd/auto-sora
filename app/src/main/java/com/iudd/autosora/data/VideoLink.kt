package com.iudd.autosora.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_links")
data class VideoLink(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val url: String,
    val platform: String, // 自动识别平台
    val thumbnail: String? = null,
    val duration: String? = null,
    val extractedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val url: String,
    val createdAt: Long = System.currentTimeMillis()
)