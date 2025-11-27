package com.example.musicstorehn.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicstorehn.databinding.ItemMediaBinding;
import com.example.musicstorehn.models.Media;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<Media> mediaList;
    private OnMediaClickListener listener;

    public interface OnMediaClickListener {
        void onPlayClick(Media media);
        void onDownloadClick(Media media);
        void onMoreClick(Media media);
    }

    public MediaAdapter(List<Media> mediaList, OnMediaClickListener listener) {
        this.mediaList = mediaList != null ? mediaList : new ArrayList<>();
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

    public void updateMediaList(List<Media> newMediaList) {
        this.mediaList = newMediaList;
        notifyDataSetChanged();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        private ItemMediaBinding binding;

        public MediaViewHolder(ItemMediaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Media media) {
            // Usar los IDs correctos que existen en tu XML
            binding.tvTitle.setText(media.getTitle());
            binding.tvArtist.setText(media.getUploaderName()); // Cambiado de tvUploader a tvArtist

            // Si tienes fecha, podrías mostrarla en el artista o título
            // binding.tvArtist.setText(media.getUploaderName() + " • " + media.getCreatedAt());

            // El ImageView de más funciona como botón más
            binding.ivMore.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreClick(media);
                }
            });

            // Para play y download, necesitas agregar estos botones al XML
            // o usar clicks en otros elementos existentes
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlayClick(media);
                }
            });

            // Para download, podrías usar long click o agregar el botón
            binding.getRoot().setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onDownloadClick(media);
                    return true;
                }
                return false;
            });
        }
    }
}