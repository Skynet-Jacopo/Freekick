package com.football.freekick.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.NoticeActivity;
import com.football.freekick.chat.FireChatHelper.ChatHelper;
import com.football.freekick.chat.FireChatHelper.ExtraIntent;
import com.football.freekick.chat.model.ChatMessage;
import com.football.freekick.chat.model.User;
import com.football.freekick.chat.ui.ChatActivity;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.views.RoundImageView;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcel on 11/11/2015.
 */
public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.ViewHolderUsers> {

    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    private List<User> mUsers;
    private Context mContext;
    private String mCurrentUserEmail;
    private Long mCurrentUserCreatedAt;
    private String mCurrentUserId;
    private DatabaseReference messageChatDatabase;
    private List<ChatMessage> msgs;

    public UsersChatAdapter(Context context, List<User> fireChatUsers) {
        mUsers = fireChatUsers;
        mContext = context;
        msgs = new ArrayList<>();
    }

    @Override
    public ViewHolderUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderUsers(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,
                parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolderUsers holder, int position) {

        final User fireChatUser = mUsers.get(position);

        // Set avatar
        int userAvatarId = ChatHelper.getDrawableAvatarId(fireChatUser.getAvatarId());
        Drawable avatarDrawable = ContextCompat.getDrawable(mContext, userAvatarId);
        ImageLoaderUtils.displayImage(MyUtil.getImageUrl(fireChatUser.getTeam_url()),holder.getUserAvatar(),R.drawable.icon_default);

        // Set display name
        holder.getUserDisplayName().setText(fireChatUser.getDisplayName());
        // Set presence status
//        holder.getStatusConnection().setText(fireChatUser.getConnection());

        // Set presence text color
//        if(fireChatUser.getConnection().equals(ONLINE)) {
//            // Green color
//            holder.getStatusConnection().setTextColor(Color.parseColor("#00FF00"));
//        }else {
//            // Red color
//            holder.getStatusConnection().setTextColor(Color.parseColor("#FF0000"));
//        }

        final String chatRef = fireChatUser.createUniqueChatRef(mCurrentUserCreatedAt, PrefUtils.getString(App.APP_CONTEXT,"uid",null));
        messageChatDatabase = FirebaseDatabase.getInstance().getReference().child(chatRef);

        messageChatDatabase.addChildEventListener(new ChildEventListener() {
            int count = 0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class);
                    msgs.add(newMessage);
                }
                if (msgs.size() > 0) {
                    holder.mTvContent.setText(msgs.get(msgs.size() - 1).getMessage());
                    count+=1;
                    int num = count - PrefUtils.getInt(App.APP_CONTEXT, chatRef, 0);
                    if (num > 0) {
                        holder.mTvMsgNum.setVisibility(View.VISIBLE);
                        holder.mTvMsgNum.setText(num + "");
                        holder.mTvTime.setVisibility(View.GONE);
                    } else {
                        holder.mTvMsgNum.setVisibility(View.GONE);
                        holder.mTvTime.setVisibility(View.VISIBLE);
                        holder.mTvTime.setText(JodaTimeUtil.progressDate1(mContext, msgs.get(msgs.size() - 1)
                                .getCreatedAt()));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void refill(User users) {
        mUsers.add(users);
        notifyDataSetChanged();
    }

    public void changeUser(int index, User user) {
        mUsers.set(index, user);
        notifyDataSetChanged();
    }

    public void setCurrentUserInfo(String userUid, String email, long createdAt) {
        mCurrentUserId = userUid;
        mCurrentUserEmail = email;
        mCurrentUserCreatedAt = createdAt;
    }

    public void clear() {
        mUsers.clear();
    }


    /* ViewHolder for RecyclerView */
    public class ViewHolderUsers extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mUserAvatar;
        private TextView mUserDisplayName;
        private TextView mTvContent;
        private TextView mTvTime;
        private TextView mTvMsgNum;
        private Context mContextViewHolder;

        public ViewHolderUsers(Context context, View itemView) {
            super(itemView);
            mUserAvatar = (ImageView) itemView.findViewById(R.id.iv_head);
            mUserDisplayName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
            mTvMsgNum = (TextView) itemView.findViewById(R.id.tv_msg_num);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mContextViewHolder = context;

            itemView.setOnClickListener(this);
        }

        public ImageView getUserAvatar() {
            return mUserAvatar;
        }

        public TextView getUserDisplayName() {
            return mUserDisplayName;
        }
//        public TextView getStatusConnection() {
//            return mStatusConnection;
//        }


        @Override
        public void onClick(View view) {

            User user = mUsers.get(getLayoutPosition());

            String chatRef = user.createUniqueChatRef(mCurrentUserCreatedAt, mCurrentUserEmail);

            Intent chatIntent = new Intent(mContextViewHolder, ChatActivity.class);
            chatIntent.putExtra(ExtraIntent.EXTRA_CURRENT_USER_ID, mCurrentUserId);
            chatIntent.putExtra(ExtraIntent.EXTRA_RECIPIENT_ID, user.getRecipientId());
            chatIntent.putExtra(ExtraIntent.EXTRA_CHAT_REF, chatRef);
            chatIntent.putExtra(ExtraIntent.RECIPIENT_NAME, user.getDisplayName());

            // Start new activity
            mContextViewHolder.startActivity(chatIntent);

        }
    }

}
