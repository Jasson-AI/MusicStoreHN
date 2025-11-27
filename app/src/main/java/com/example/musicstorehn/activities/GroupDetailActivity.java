package com.example.musicstorehn.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstorehn.R;
import com.example.musicstorehn.adapters.MediaAdapter;
import com.example.musicstorehn.adapters.UserAdapter;
import com.example.musicstorehn.models.Group;
import com.example.musicstorehn.models.Media;
import com.example.musicstorehn.models.Response;
import com.example.musicstorehn.models.User;
import com.example.musicstorehn.network.RetrofitClient;
import com.example.musicstorehn.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GroupDetailActivity extends AppCompatActivity implements MediaAdapter.OnMediaClickListener {

    private MaterialToolbar toolbar;
    private TextView tvGroupName, tvDescription, tvMembersCount, tvMediaCount;
    private MaterialButton btnJoinGroup, btnUploadToGroup;
    private RecyclerView rvMembers, rvMedia;
    private ProgressBar progressBar;
    private LinearLayout emptyStateMembers, emptyStateMedia;

    private UserAdapter membersAdapter;
    private MediaAdapter mediaAdapter;
    private SessionManager session;

    private int groupId = -1;
    private Group currentGroup;

    private List<User> membersList = new ArrayList<>();
    private List<Media> mediaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        session = new SessionManager(this);

        groupId = getIntent().getIntExtra("group_id", -1);
        if (groupId == -1) {
            Toast.makeText(this, "Grupo no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerViews();
        setupListeners();

        loadGroupDetails();
        loadGroupMembers();
        loadGroupMedia();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvGroupName = findViewById(R.id.tv_group_name);
        tvDescription = findViewById(R.id.tv_description);
        tvMembersCount = findViewById(R.id.tv_members_count);
        tvMediaCount = findViewById(R.id.tv_media_count);
        btnJoinGroup = findViewById(R.id.btn_join_group);
        btnUploadToGroup = findViewById(R.id.btn_upload_to_group);
        rvMembers = findViewById(R.id.rv_members);
        rvMedia = findViewById(R.id.rv_media);
        progressBar = findViewById(R.id.progress_bar);
        emptyStateMembers = findViewById(R.id.empty_state_members);
        emptyStateMedia = findViewById(R.id.empty_state_media);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerViews() {
        membersAdapter = new UserAdapter(membersList);
        rvMembers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvMembers.setAdapter(membersAdapter);

        mediaAdapter = new MediaAdapter(mediaList, this);
        rvMedia.setLayoutManager(new LinearLayoutManager(this));
        rvMedia.setAdapter(mediaAdapter);
    }

    private void setupListeners() {
        btnJoinGroup.setOnClickListener(v -> joinGroup());
        btnUploadToGroup.setOnClickListener(v ->
                Toast.makeText(this, "Subir a grupo (pendiente)", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadGroupDetails() {
        showLoading(true);

        RetrofitClient.getApiService()
                .getGroups(session.getAuthToken())
                .enqueue(new Callback<Response<List<Group>>>() {
                    @Override
                    public void onResponse(Call<Response<List<Group>>> call,
                                           retrofit2.Response<Response<List<Group>>> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null &&
                                response.body().isSuccess() && response.body().getData() != null) {

                            for (Group g : response.body().getData()) {
                                if (g.getId() == groupId) {
                                    currentGroup = g;
                                    break;
                                }
                            }

                            if (currentGroup != null) {
                                displayGroupInfo();
                            } else {
                                Toast.makeText(GroupDetailActivity.this,
                                        "No se encontró el grupo", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(GroupDetailActivity.this,
                                    "Error al cargar grupo", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<List<Group>>> call, Throwable t) {
                        showLoading(false);
                        Toast.makeText(GroupDetailActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayGroupInfo() {
        tvGroupName.setText(currentGroup.getName());
        tvDescription.setText(currentGroup.getDescription());
        tvMembersCount.setText(currentGroup.getMemberCount() + " miembros");
        tvMediaCount.setText(currentGroup.getMediaCount() + " canciones");

        toolbar.setTitle(currentGroup.getName());
    }

    private void loadGroupMembers() {
        RetrofitClient.getApiService()
                .getGroupMembers(session.getAuthToken(), groupId)
                .enqueue(new Callback<Response<List<User>>>() {
                    @Override
                    public void onResponse(Call<Response<List<User>>> call,
                                           retrofit2.Response<Response<List<User>>> response) {

                        if (response.isSuccessful() && response.body() != null &&
                                response.body().isSuccess() && response.body().getData() != null) {

                            membersList.clear();
                            membersList.addAll(response.body().getData());
                            membersAdapter.notifyDataSetChanged();

                            if (membersList.isEmpty()) {
                                emptyStateMembers.setVisibility(View.VISIBLE);
                                rvMembers.setVisibility(View.GONE);
                            } else {
                                emptyStateMembers.setVisibility(View.GONE);
                                rvMembers.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<List<User>>> call, Throwable t) {
                        Toast.makeText(GroupDetailActivity.this,
                                "Error al cargar miembros", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadGroupMedia() {
        RetrofitClient.getApiService()
                .getGroupMedia(session.getAuthToken(), groupId)
                .enqueue(new Callback<Response<List<Media>>>() {
                    @Override
                    public void onResponse(Call<Response<List<Media>>> call,
                                           retrofit2.Response<Response<List<Media>>> response) {

                        if (response.isSuccessful() && response.body() != null &&
                                response.body().isSuccess() && response.body().getData() != null) {

                            mediaList.clear();
                            mediaList.addAll(response.body().getData());
                            mediaAdapter.notifyDataSetChanged();

                            if (mediaList.isEmpty()) {
                                emptyStateMedia.setVisibility(View.VISIBLE);
                                rvMedia.setVisibility(View.GONE);
                            } else {
                                emptyStateMedia.setVisibility(View.GONE);
                                rvMedia.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<List<Media>>> call, Throwable t) {
                        Toast.makeText(GroupDetailActivity.this,
                                "Error al cargar canciones", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void joinGroup() {
        RetrofitClient.getApiService()
                .joinGroup(session.getAuthToken(), groupId)
                .enqueue(new Callback<Response<String>>() {
                    @Override
                    public void onResponse(Call<Response<String>> call,
                                           retrofit2.Response<Response<String>> response) {

                        if (response.isSuccessful() && response.body() != null &&
                                response.body().isSuccess()) {
                            Toast.makeText(GroupDetailActivity.this,
                                    "Te uniste al grupo", Toast.LENGTH_SHORT).show();
                            loadGroupMembers();
                        } else {
                            Toast.makeText(GroupDetailActivity.this,
                                    "No se pudo unir al grupo", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<String>> call, Throwable t) {
                        Toast.makeText(GroupDetailActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPlayClick(Media media) {
        // Aquí puedes abrir PlayerActivity y pasar el media seleccionado
        Toast.makeText(this, "Reproducir: " + media.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadClick(Media media) {
        Toast.makeText(this, "Descargar: " + media.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMoreClick(Media media) {
        Toast.makeText(this, "Más opciones para: " + media.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
