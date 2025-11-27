package com.example.musicstorehn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.example.musicstorehn.R;
import com.example.musicstorehn.databinding.ItemMediaBinding;
import com.example.musicstorehn.models.Media;
import java.util.List;
public class MediaAdapter extends RecyclerView.Adapter&lt;MediaAdapter.MediaViewHolder&gt
private List&lt;Media&gt; mediaList;
private OnMediaClickListener listener;
public interface OnMediaClickListener {
    void onPlayClick(Media media);
    void onDownloadClick(Media media);
    void onMoreClick(Media media);
}
public MediaAdapter(List&lt;Media&gt; mediaList, OnMediaClickListener listener) {
        this.mediaList = mediaList;
 this.listener = listener;
 }
@NonNull
@Override
public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemMediaBinding binding = ItemMediaBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
    return new MediaViewHolder(binding);
}
@Override
public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
    Media media = mediaList.get(position);
    holder.bind(media);
}
@Override
public int getItemCount() {
    return mediaList.size();
}
public class MediaViewHolder extends RecyclerView.ViewHolder {
    private ItemMediaBinding binding;
    public MediaViewHolder(ItemMediaBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Media media) {
        binding.tvTitle.setText(media.getTitle());
        binding.tvUploader.setText("Subido por " + media.getUploaderName());
        binding.tvDate.setText(media.getCreatedAt());

        binding.btnPlay.setOnClickListener(v -&gt; {
            if (listener != null) {
                listener.onPlayClick(media);
            }
        });
        binding.btnDownload.setOnClickListener(v -&gt; {
            if (listener != null) {
                listener.onDownloadClick(media);
            }
        });
        binding.btnMore.setOnClickListener(v -&gt; {
            if (listener != null) {
                listener.onMoreClick(media);
            }
        });
    }
}
 }