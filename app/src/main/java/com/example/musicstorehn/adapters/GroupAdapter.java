package com.example.musicstorehn.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicstorehn.databinding.ItemGroupBinding;
import com.example.musicstorehn.models.Group;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    public interface OnGroupClickListener {
        void onGroupClick(Group group);
        void onJoinClick(Group group);
    }

    private List<Group> groupList;
    private OnGroupClickListener listener;

    public GroupAdapter(List<Group> groupList, OnGroupClickListener listener) {
        this.groupList = groupList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGroupBinding binding = ItemGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.bind(groupList.get(position));
    }

    @Override
    public int getItemCount() {
        return groupList != null ? groupList.size() : 0;
    }

    public void updateGroupList(List<Group> newGroupList) {
        this.groupList = newGroupList;
        notifyDataSetChanged();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private final ItemGroupBinding binding;

        public GroupViewHolder(@NonNull ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Group group) {
            // ✅ IDs CORRECTOS que existen en item_group.xml
            binding.tvGroupName.setText(group.getName());

            // Mostrar información combinada de miembros y canciones
            String groupInfo = group.getMemberCount() + " miembros • " +
                    group.getMediaCount() + " canciones";
            binding.tvGroupCount.setText(groupInfo);

            // Click en toda la tarjeta para ver detalles del grupo
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onGroupClick(group);
            });

            // Click en la flecha para unirse al grupo
            binding.ivNext.setOnClickListener(v -> {
                if (listener != null) listener.onJoinClick(group);
            });
        }
    }
}