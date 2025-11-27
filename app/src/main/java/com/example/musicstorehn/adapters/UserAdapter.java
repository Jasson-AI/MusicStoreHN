package com.example.musicstorehn.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.musicstorehn.databinding.ItemUserBinding;
import com.example.musicstorehn.models.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public void updateUserList(List<User> newUserList) {
        this.userList = newUserList;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            // ✅ IDs CORRECTOS que existen en item_user.xml
            binding.tvUserName.setText(user.getName());
            binding.tvUserEmail.setText(user.getEmail());

            // Mostrar email si está disponible
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                binding.tvUserEmail.setVisibility(android.view.View.VISIBLE);
                binding.tvUserEmail.setText(user.getEmail());
            } else {
                binding.tvUserEmail.setVisibility(android.view.View.GONE);
            }

            // Cargar imagen de perfil
            if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(user.getProfileImage())
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .into(binding.ivUserImage);
            } else {
                binding.ivUserImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }
}