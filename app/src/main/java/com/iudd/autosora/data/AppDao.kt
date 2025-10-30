package com.iudd.autosora.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoLinkDao {
    @Query("SELECT * FROM video_links ORDER BY extractedAt DESC")
    fun getAllVideoLinks(): Flow<List<VideoLink>>

    @Query("SELECT * FROM video_links WHERE platform = :platform ORDER BY extractedAt DESC")
    fun getVideoLinksByPlatform(platform: String): Flow<List<VideoLink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoLink(videoLink: VideoLink): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoLinks(videoLinks: List<VideoLink>)

    @Update
    suspend fun updateVideoLink(videoLink: VideoLink)

    @Delete
    suspend fun deleteVideoLink(videoLink: VideoLink)

    @Query("DELETE FROM video_links")
    suspend fun deleteAllVideoLinks()

    @Query("SELECT COUNT(*) FROM video_links")
    fun getVideoLinksCount(): Flow<Int>
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark): Long

    @Update
    suspend fun updateBookmark(bookmark: Bookmark)

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAllBookmarks()
}