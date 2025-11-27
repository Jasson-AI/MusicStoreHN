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

public class MyMusicFragment extends Fragment implements MediaAdapter.OnMediaClickListener {
    private RecyclerView rvMyMusic;
    private ProgressBar progressBar;
    private LinearLayout emptyState;
    private MediaAdapter adapter;
    private SessionManager session;
    private List<Media> mediaList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // ✅ CORREGIDO: Usar el layout correcto para My Music
        return inflater.inflate(R.layout.fragment_my_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(requireContext());

        initViews(view);
        setupRecyclerView();
        loadMyMedia();
    }

    private void initViews(View view) {
        // ✅ Asegúrate de que estos IDs existan en fragment_my_music.xml
        rvMyMusic = view.findViewById(R.id.rv_media);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyState = view.findViewById(R.id.empty_state);
    }

    private void setupRecyclerView() {
        adapter = new MediaAdapter(mediaList, this);
        rvMyMusic.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyMusic.setAdapter(adapter);
    }

    private void loadMyMedia() {
        showLoading(true);

        RetrofitClient.getApiService()
                .getUserMedia(session.getAuthToken(), session.getUserId())
                .enqueue(new Callback<Response<List<Media>>>() {
                    @Override
                    public void onResponse(Call<Response<List<Media>>> call,
                                           retrofit2.Response<Response<List<Media>>> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            Response<List<Media>> apiResponse = response.body();

                            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                                mediaList.clear();
                                mediaList.addAll(apiResponse.getData());
                                adapter.notifyDataSetChanged();

                                showEmptyState(mediaList.isEmpty());
                            } else {
                                // ✅ Manejar error de la API
                                Toast.makeText(getContext(),
                                        "Error: " + apiResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                showEmptyState(true);
                            }
                        } else {
                            // ✅ Manejar error HTTP
                            Toast.makeText(getContext(),
                                    "Error del servidor: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                            showEmptyState(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<List<Media>>> call, Throwable t) {
                        showLoading(false);
                        Toast.makeText(getContext(),
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        showEmptyState(true);
                    }
                });
    }

    @Override
    public void onPlayClick(Media media) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        //intent.putExtra("media", media);
        intent.putExtra("media", (CharSequence) media);
        startActivity(intent);
    }

    @Override
    public void onDownloadClick(Media media) {
        Toast.makeText(getContext(), "Descargando: " + media.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoreClick(Media media) {
        Toast.makeText(getContext(), "Más opciones: " + media.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvMyMusic.setVisibility(show ? View.GONE : View.VISIBLE);
        emptyState.setVisibility(View.GONE);
    }

    private void showEmptyState(boolean show) {
        emptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvMyMusic.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}