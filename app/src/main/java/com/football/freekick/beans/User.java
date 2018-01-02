package com.football.freekick.beans;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * @author Marcelino Yax-marce7j@gmail.com-Android Developer
 *         Created on 12/23/2016.
 */

public class User implements Serializable {

    private String displayName;
    private String email;
    private String connection;
    private int avatarId;
    private long createdAt;
    private long unReadNum;
    private long lastEditTime;
    private String team_url;
    private String team_id;

    private String mRecipientId;

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public long getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(long lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getTeam_url() {
        return team_url;
    }

    public void setTeam_url(String team_url) {
        this.team_url = team_url;
    }

    public User() {
    }

    public User(String displayName, String email, String connection, int avatarId, long createdAt, long readNum) {
        this.displayName = displayName;
        this.email = email;
        this.connection = connection;
        this.avatarId = avatarId;
        this.createdAt = createdAt;
        this.unReadNum = readNum;
    }


    public String createUniqueChatRef(long createdAtCurrentUser, String currentUserEmail) {
        String uniqueChatRef = "";
        if (createdAtCurrentUser > getCreatedAt()) {
            uniqueChatRef = cleanEmailAddress(currentUserEmail) + "-" + cleanEmailAddress(getUserEmail());
        } else {

            uniqueChatRef = cleanEmailAddress(getUserEmail()) + "-" + cleanEmailAddress(currentUserEmail);
        }
        return uniqueChatRef;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    private String cleanEmailAddress(String email) {
        //replace dot with comma since firebase does not allow dot
        if (email != null)
            return email.replace(".", "-");
        return "";
    }

    private String getUserEmail() {
        //Log.e("user email  ", userEmail);
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getConnection() {
        return connection;
    }

    public int getAvatarId() {
        return avatarId;
    }

    @Exclude
    public String getRecipientId() {
        return mRecipientId;
    }

    public void setRecipientId(String recipientId) {
        this.mRecipientId = recipientId;
    }

    public long getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(long readNum) {
        this.unReadNum = readNum;
    }
}
