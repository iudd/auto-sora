package com.iudd.autosora.extractor

import android.webkit.WebView
import com.iudd.autosora.data.VideoLink
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Pattern

class VideoLinkExtractor {

    private val videoPatterns = mapOf(
        "youtube" to Pattern.compile("(?:https?://)?(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/|youtube\\.com/embed/)([a-zA-Z0-9_-]{11})"),
        "bilibili" to Pattern.compile("(?:https?://)?(?:www\\.)?(?:bilibili\\.com/video/|b23\\.tv/)([a-zA-Z0-9]+)"),
        "douyin" to Pattern.compile("(?:https?://)?(?:www\\.)?(?:douyin\\.com/video/|v\\.douyin\\.com/)([a-zA-Z0-9]+)"),
        "kuaishou" to Pattern.compile("(?:https?://)?(?:www\\.)?(?:kuaishou\\.com/short-video/|v\\.kuaishou\\.com/)([a-zA-Z0-9]+)"),
        "xigua" to Pattern.compile("(?:https?://)?(?:www\\.)?(?:ixigua\\.com/|xigua\\.com/)([a-zA-Z0-9]+)")
    )

    fun extractFromHtml(html: String, pageUrl: String): List<VideoLink> {
        val videoLinks = mutableListOf<VideoLink>()

        try {
            val document: Document = Jsoup.parse(html)

            // 提取各种视频链接
            videoPatterns.forEach { (platform, pattern) ->
                val matcher = pattern.matcher(html)
                while (matcher.find()) {
                    val videoId = matcher.group(1) ?: continue
                    val fullUrl = constructFullUrl(platform, videoId)

                    val videoLink = VideoLink(
                        title = extractTitle(document, platform),
                        url = fullUrl,
                        platform = platform,
                        thumbnail = extractThumbnail(platform, videoId),
                        extractedAt = System.currentTimeMillis()
                    )
                    videoLinks.add(videoLink)
                }
            }

            // 提取通用视频链接 (MP4, M3U8等)
            extractGenericVideoLinks(html, pageUrl).forEach { url ->
                val videoLink = VideoLink(
                    title = extractTitle(document, "generic"),
                    url = url,
                    platform = "通用视频",
                    extractedAt = System.currentTimeMillis()
                )
                videoLinks.add(videoLink)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return videoLinks.distinctBy { it.url } // 去重
    }

    private fun constructFullUrl(platform: String, videoId: String): String {
        return when (platform) {
            "youtube" -> "https://www.youtube.com/watch?v=$videoId"
            "bilibili" -> "https://www.bilibili.com/video/$videoId"
            "douyin" -> "https://www.douyin.com/video/$videoId"
            "kuaishou" -> "https://www.kuaishou.com/short-video/$videoId"
            "xigua" -> "https://www.ixigua.com/$videoId"
            else -> videoId
        }
    }

    private fun extractTitle(document: Document, platform: String): String {
        return document.title() ?: "${platform}视频"
    }

    private fun extractThumbnail(platform: String, videoId: String): String? {
        return when (platform) {
            "youtube" -> "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"
            "bilibili" -> null // B站缩略图需要API
            else -> null
        }
    }

    private fun extractGenericVideoLinks(html: String, pageUrl: String): List<String> {
        val links = mutableListOf<String>()
        val patterns = listOf(
            Pattern.compile("(https?://[^\"']*\\.(?:mp4|m3u8|avi|wmv|flv|webm|mkv))", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(https?://[^\"']*video[^\"']*\\.(?:mp4|m3u8))", Pattern.CASE_INSENSITIVE)
        )

        patterns.forEach { pattern ->
            val matcher = pattern.matcher(html)
            while (matcher.find()) {
                val url = matcher.group(1)
                if (url != null && isValidVideoUrl(url)) {
                    links.add(url)
                }
            }
        }

        return links.distinct()
    }

    private fun isValidVideoUrl(url: String): Boolean {
        return url.length > 10 && !url.contains("ads") && !url.contains("tracking")
    }

    fun extractFromWebView(webView: WebView, callback: (List<VideoLink>) -> Unit) {
        webView.evaluateJavascript("""
            (function() {
                var links = [];
                var elements = document.querySelectorAll('a[href], source[src], video[src]');
                for (var i = 0; i < elements.length; i++) {
                    var element = elements[i];
                    var url = element.href || element.src;
                    if (url && (url.includes('.mp4') || url.includes('.m3u8') || url.includes('video'))) {
                        links.push(url);
                    }
                }
                return links;
            })();
        """) { result ->
            try {
                val urls = result?.removeSurrounding("\"")?.split(",") ?: emptyList()
                val videoLinks = urls.map { url ->
                    VideoLink(
                        title = "提取的视频",
                        url = url,
                        platform = "WebView提取",
                        extractedAt = System.currentTimeMillis()
                    )
                }
                callback(videoLinks)
            } catch (e: Exception) {
                callback(emptyList())
            }
        }
    }
}