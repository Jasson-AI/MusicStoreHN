package com.example.musicstorehn.models;

// File: app/src/main/java/com/uth/musicstorehn/models/Group.java

import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("created_by")
    private int createdBy;

    @SerializedName("creator_name")
    private String creatorName;

    @SerializedName("cover_image")
    private String coverImage;

    @SerializedName("member_count")
    private int memberCount;

    @SerializedName("media_count")
    private int mediaCount;

    @SerializedName("created_at")
    private String createdAt;

    public Group() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

    public int getMediaCount() { return mediaCount; }
    public void setMediaCount(int mediaCount) { this.mediaCount = mediaCount; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
