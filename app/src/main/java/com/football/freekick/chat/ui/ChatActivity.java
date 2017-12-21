package com.football.freekick.chat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.chat.FireChatHelper.ExtraIntent;
import com.football.freekick.chat.adapter.MessageChatAdapter;
import com.football.freekick.chat.model.ChatMessage;
import com.football.freekick.chat.model.User;
import com.football.freekick.utils.PrefUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    @Bind(R.id.recycler_view_chat)
    RecyclerView mChatRecyclerView;
    @Bind(R.id.edit_text_message)
    EditText mUserMessageChatText;
    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.divider)
    View mDivider;
    @Bind(R.id.btn_send_message)
    ImageButton mBtnSendMessage;
    @Bind(R.id.senderContainer)
    RelativeLayout mSenderContainer;
    @Bind(R.id.progress_for_chat)
    ProgressBar mProgressForChat;


    private String mRecipientId;
    private String mCurrentUserId;
    private MessageChatAdapter messageChatAdapter;
    private DatabaseReference messageChatDatabase;
    private DatabaseReference mUserRefDatabase;
    //    private DatabaseReference mUserRefDatabase;
    private ChildEventListener messageChatListener;
    private String chatRef;
    private String recipient_id;
    private long unReadNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
        setDatabaseInstance();
        setUsersId();
        setChatRecyclerView();
        mUserMessageChatText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    sendMsg();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void initView() {
        String name = getIntent().getStringExtra(ExtraIntent.RECIPIENT_NAME);
        String current_user_id = getIntent().getStringExtra(ExtraIntent.EXTRA_CURRENT_USER_ID);
        recipient_id = getIntent().getStringExtra(ExtraIntent.EXTRA_RECIPIENT_ID);
//        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(current_user_id);
        mTvBack.setTypeface(App.mTypeface);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvName.setText(name);
        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setDatabaseInstance() {
        chatRef = getIntent().getStringExtra(ExtraIntent.EXTRA_CHAT_REF);
        messageChatDatabase = FirebaseDatabase.getInstance().getReference().child(chatRef);
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
    }

    private void setUsersId() {
        mRecipientId = getIntent().getStringExtra(ExtraIntent.EXTRA_RECIPIENT_ID);
        mCurrentUserId = getIntent().getStringExtra(ExtraIntent.EXTRA_CURRENT_USER_ID);
    }

    private void setChatRecyclerView() {
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setHasFixedSize(true);
        messageChatAdapter = new MessageChatAdapter(new ArrayList<ChatMessage>());
        mChatRecyclerView.setAdapter(messageChatAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        messageChatListener = messageChatDatabase.limitToLast(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {

                if (dataSnapshot.exists()) {
                    ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class);
                    if (newMessage.getSender().equals(mCurrentUserId)) {
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.SENDER);
                    } else {
                        newMessage.setRecipientOrSenderStatus(MessageChatAdapter.RECIPIENT);
                    }
                    messageChatAdapter.refillAdapter(newMessage);
                    mChatRecyclerView.scrollToPosition(messageChatAdapter.getItemCount() - 1);
//                    PrefUtils.putInt(App.APP_CONTEXT, chatRef, messageChatAdapter.getItemCount());
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
        mUserRefDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    String userUid = dataSnapshot.getKey();
                    if (userUid.equals(mCurrentUserId)){
                        User mCurrentUser = dataSnapshot.getValue(User.class);
                        unReadNum = mCurrentUser.getUnReadNum();
//                        mUserRefDatabase.child(recipient_id).child("unReadNum").setValue(0);
                        mUserRefDatabase.child(mCurrentUserId).child("from"+recipient_id+"unReadNum").setValue(0);
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
    protected void onStop() {
        super.onStop();

        if (messageChatListener != null) {
            messageChatDatabase.removeEventListener(messageChatListener);
        }
        messageChatAdapter.cleanUp();

    }

    @OnClick(R.id.btn_send_message)
    public void btnSendMsgListener(View sendButton) {
        sendMsg();
    }

    private void sendMsg() {
        String senderMessage = mUserMessageChatText.getText().toString().trim();

        if (!senderMessage.isEmpty()) {
            unReadNum += 1;
            ChatMessage newMessage = new ChatMessage(senderMessage, mCurrentUserId, mRecipientId, new Date().getTime());
            messageChatDatabase.push().setValue(newMessage);
            mUserRefDatabase.child(recipient_id).child("lastEditTimeWith"+mCurrentUserId).setValue(-new Date().getTime());
            mUserRefDatabase.child(mCurrentUserId).child("lastEditTimeWith"+mCurrentUserId).setValue(-new Date().getTime());
            mUserRefDatabase.child(recipient_id).child("lastEditTimeWith").child("lastEditTimeWith"+mCurrentUserId).setValue(-new Date().getTime());
            mUserRefDatabase.child(mCurrentUserId).child("lastEditTimeWith").child("lastEditTimeWith"+mCurrentUserId).setValue(-new Date().getTime());
            mUserMessageChatText.setText("");
            mUserRefDatabase.child(recipient_id).child("from"+mCurrentUserId+"unReadNum").setValue(unReadNum);
        }
    }
}
