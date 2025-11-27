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
import com.example.musicstorehn.activities.GroupDetailActivity;
import com.example.musicstorehn.adapters.GroupAdapter;
import com.example.musicstorehn.models.Group;
import com.example.musicstorehn.models.Response;
import com.example.musicstorehn.network.RetrofitClient;
import com.example.musicstorehn.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GroupsFragment extends Fragment implements GroupAdapter.OnGroupClickListener {
    private RecyclerView rvGroups;
    private ProgressBar progressBar;
    private LinearLayout emptyState;
    private GroupAdapter adapter;
    private SessionManager session;
    private List<Group> groupList = new ArrayList<>();

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
        loadGroups();
    }

    private void initViews(View view) {
        rvGroups = view.findViewById(R.id.rv_media);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyState = view.findViewById(R.id.empty_state);
    }

    private void setupRecyclerView() {
        adapter = new GroupAdapter(groupList, this);
        rvGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGroups.setAdapter(adapter);
    }

    private void loadGroups() {
        showLoading(true);

        RetrofitClient.getApiService()
                .getGroups(session.getAuthToken())
                .enqueue(new Callback<Response<List<Group>>>() {
                    @Override
                    public void onResponse(Call<Response<List<Group>>> call,
                                           retrofit2.Response<Response<List<Group>>> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            Response<List<Group>> apiResponse = response.body();

                            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                                groupList.clear();
                                groupList.addAll(apiResponse.getData());
                                adapter.notifyDataSetChanged();

                                if (groupList.isEmpty()) {
                                    showEmptyState(true);
                                } else {
                                    showEmptyState(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<List<Group>>> call, Throwable t) {
                        showLoading(false);
                        Toast.makeText(getContext(),
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onGroupClick(Group group) {
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra("group_id", group.getId());
        startActivity(intent);
    }

    @Override
    public void onJoinClick(Group group) {
        joinGroup(group);
    }

    private void joinGroup(Group group) {
        RetrofitClient.getApiService()
                .joinGroup(session.getAuthToken(), group.getId())
                .enqueue(new Callback<Response<String>>() {
                    @Override
                    public void onResponse(Call<Response<String>> call,
                                           retrofit2.Response<Response<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().isSuccess()) {
                                Toast.makeText(getContext(),
                                        "¡Te uniste al grupo!",
                                        Toast.LENGTH_SHORT).show();
                                loadGroups();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<String>> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        rvGroups.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean show) {
        emptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvGroups.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}