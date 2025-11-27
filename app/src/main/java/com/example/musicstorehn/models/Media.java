package com.example.musicstorehn.models;

// File: app/src/main/java/com/uth/musicstorehn/models/Media.java

import com.google.gson.annotations.SerializedName;

public class Media {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("file_path")
    private String filePath;

    @SerializedName("file_type")
    private String fileType; // "audio" or "video"

    @SerializedName("duration")
    private int duration;

    @SerializedName("file_size")
    private long fileSize;

    @SerializedName("uploaded_by")
    private int uploadedBy;

    @SerializedName("uploader_name")
    private String uploaderName;

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("group_name")
    private String groupName;

    @SerializedName("is_private")
    private boolean isPrivate;

    @SerializedName("created_at")
    private String createdAt;

    public Media() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public int getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(int uploadedBy) { this.uploadedBy = uploadedBy; }

    public String getUploaderName() { return uploaderName; }
    public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }

    public Integer getGroupId() { return groupId; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public boolean isPrivate() { return isPrivate; }
    public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
