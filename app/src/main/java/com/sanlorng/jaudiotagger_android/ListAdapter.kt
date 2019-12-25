package com.sanlorng.jaudiotagger_android

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_item.view.*
import org.jaudiotagger.audio.AudioFileIO

class ListAdapter(private val cursor: Cursor): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
    private val idIndex = cursor.getColumnIndex(BaseColumns._ID)
    override fun getItemCount(): Int {
        return cursor.count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.itemView.apply {
            cursor.run {
                val title = getString(titleIndex)
                val id = getLong(idIndex)
                textTitle.text = title
                textTitle.setOnClickListener {
                    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(id.toString()).build()
                    context.grantUriPermission(context.packageName,uri,Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    val audioHeader = AudioFileIO.read(context,uri)
                    audioHeader.tag.fields.forEach {
                        Log.e("tag",it.id)
                    }
                }
            }
        }

    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        cursor.close()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item,parent,false)) {}
    }
}