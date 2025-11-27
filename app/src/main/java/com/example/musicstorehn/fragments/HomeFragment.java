package com.example.musicstorehn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicstorehn.R;
import com.example.musicstorehn.activities.PlayerActivity;
import com.example.musicstorehn.adapters.MediaAdapter;
import com.example.musicstorehn.models.Media;
import com.example.musicstorehn.models.Response;
import com.example.musicstorehn.network.RetrofitClient;
import com.example.musicstorehn.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment implements MediaAdapter.OnMediaClickListener {

    private RecyclerView rvMedia;
    private ProgressBar progressBar;
    private LinearLayout emptyState;
    private MediaAdapter adapter;
    private SessionManager session;
    private List<Media> mediaList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(getContext());

        initViews(view);
        setupRecyclerView();
        loadMedia();
    }

    private void initViews(View view) {
        rvMedia = view.findViewById(R.id.rv_media);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyState = view.findViewById(R.id.empty_state);
    }

    private void setupRecyclerView() {
        adapter = new MediaAdapter(mediaList, this);
        rvMedia.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedia.setAdapter(adapter);
    }

    private void loadMedia() {
        showLoading(true);

        RetrofitClient.getApiService()
                .getAllMedia(session.getAuthToken(), 1, 20)
                .enqueue(new Callback<Response<List<Media>>>() {
                    @Override
                    public void onResponse(Call<Response<List<Media>>> call,
                                           RetrofitResponse<Response<List<Media>>> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            Response<List<Media>> apiResponse = response.body();

                            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                                mediaList.clear();
                                mediaList.addAll(apiResponse.getData());
                                adapter.notifyDataSetChanged();

                                showEmptyState(mediaList.isEmpty());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<List<Media>>> call, Throwable t) {
                        showLoading(false);
                        Toast.makeText(getContext(),
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onPlayClick(Media media) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra("media", media);
        startActivity(intent);
    }

    @Override
    public void onDownloadClick(Media media) {
        Toast.makeText(getContext(), "Descargando: " + media.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoreClick(Media media) {
        Toast.makeText(getContext(), "Más opciones", Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvMedia.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        emptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvMedia.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
