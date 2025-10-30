package com.iudd.autosora

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iudd.autosora.data.AppDatabase
import com.iudd.autosora.data.VideoLink
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class VideoLinksActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View
    private lateinit var fabExport: FloatingActionButton
    private lateinit var adapter: VideoLinkAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_links)

        database = AppDatabase.getDatabase(this)

        initializeViews()
        setupRecyclerView()
        setupToolbar()
        setupFab()
        loadVideoLinks()
    }

    private fun initializeViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recycler_view)
        emptyState = findViewById(R.id.empty_state)
        fabExport = findViewById(R.id.fab_export)
    }

    private fun setupRecyclerView() {
        adapter = VideoLinkAdapter { videoLink ->
            // Handle item click - copy URL
            copyToClipboard(videoLink.url)
            Toast.makeText(this, "链接已复制到剪贴板", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupFab() {
        fabExport.setOnClickListener {
            exportVideoLinks()
        }
    }

    private fun loadVideoLinks() {
        lifecycleScope.launch {
            database.videoLinkDao().getAllVideoLinks().collectLatest { videoLinks ->
                adapter.submitList(videoLinks)
                updateEmptyState(videoLinks.isEmpty())
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("video_url", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun exportVideoLinks() {
        lifecycleScope.launch {
            val videoLinks = database.videoLinkDao().getAllVideoLinks()
            videoLinks.collectLatest { links ->
                val exportText = buildString {
                    append("自动Sora浏览器 - 视频链接导出\n")
                    append("导出时间: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\n")
                    append("共 ${links.size} 个链接\n\n")

                    links.forEachIndexed { index, link ->
                        append("${index + 1}. ${link.title}\n")
                        append("   平台: ${link.platform}\n")
                        append("   链接: ${link.url}\n")
                        append("   时间: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(link.extractedAt))}\n\n")
                    }
                }

                copyToClipboard(exportText)
                Toast.makeText(this@VideoLinksActivity, "已导出 ${links.size} 个链接到剪贴板", Toast.LENGTH_LONG).show()
            }
        }
    }

    private inner class VideoLinkAdapter(
        private val onItemClick: (VideoLink) -> Unit
    ) : RecyclerView.Adapter<VideoLinkViewHolder>() {

        private var videoLinks = emptyList<VideoLink>()

        fun submitList(links: List<VideoLink>) {
            videoLinks = links
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoLinkViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video_link, parent, false)
            return VideoLinkViewHolder(view, onItemClick)
        }

        override fun onBindViewHolder(holder: VideoLinkViewHolder, position: Int) {
            holder.bind(videoLinks[position])
        }

        override fun getItemCount() = videoLinks.size
    }

    private inner class VideoLinkViewHolder(
        itemView: View,
        private val onItemClick: (VideoLink) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvUrl: TextView = itemView.findViewById(R.id.tv_url)
        private val tvPlatform: TextView = itemView.findViewById(R.id.tv_platform)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val btnCopy: Button = itemView.findViewById(R.id.btn_copy)
        private val btnOpen: Button = itemView.findViewById(R.id.btn_open)

        fun bind(videoLink: VideoLink) {
            tvTitle.text = videoLink.title
            tvUrl.text = videoLink.url
            tvPlatform.text = videoLink.platform

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            tvDate.text = dateFormat.format(Date(videoLink.extractedAt))

            itemView.setOnClickListener { onItemClick(videoLink) }

            btnCopy.setOnClickListener {
                copyToClipboard(videoLink.url)
                Toast.makeText(itemView.context, "链接已复制", Toast.LENGTH_SHORT).show()
            }

            btnOpen.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink.url))
                    itemView.context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(itemView.context, "无法打开链接", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}