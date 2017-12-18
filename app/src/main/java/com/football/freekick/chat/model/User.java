package com.football.freekick.chat.model;

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

    private long readNum;

    private String mRecipientId;

    public User() {
    }

    public User(String displayName, String email, String connection, int avatarId, long createdAt, long readNum) {
        this.displayName = displayName;
        this.email = email;
        this.connection = connection;
        this.avatarId = avatarId;
        this.createdAt = createdAt;
        this.readNum = readNum;
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

    public long getReadNum() {
        return readNum;
    }

    public void setReadNum(long readNum) {
        this.readNum = readNum;
    }
}
