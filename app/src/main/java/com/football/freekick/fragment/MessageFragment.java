package com.football.freekick.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.adapter.UsersChatAdapter;
import com.football.freekick.beans.ChatMessage;
import com.football.freekick.beans.User;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息界面
 */
public class MessageFragment extends BaseFragment {


    @Bind(R.id.recycler_message)
    RecyclerView mRecyclerMessage;
    private Context mContext;
    private List<String> datas = new ArrayList<>();
    private CommonAdapter mAdapter;

    private String mCurrentUserUid;
    private List<String>  mUsersKeyList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private DatabaseReference messageChatDatabase;
    private ChildEventListener mChildEventListener;
    private UsersChatAdapter mUsersChatAdapter;
    private String userUID;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUsersKeyList = new ArrayList<String>();
        initRecyclerView();
        mAuth = FirebaseAuth.getInstance();
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mCurrentUserUid = PrefUtils.getString(App.APP_CONTEXT,"team_id",null);
                    mChildEventListener = getChildEventListener();
                    mUserRefDatabase.orderByChild("lastEditTimeWith"+mCurrentUserUid).limitToFirst(50).addChildEventListener(mChildEventListener);
                } else {

                }
            }
        };
//        initView();
//        initData();
    }

    private void initRecyclerView() {
        mUsersChatAdapter = new UsersChatAdapter(mContext, new ArrayList<User>());
        mRecyclerMessage.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerMessage.setHasFixedSize(true);
        mRecyclerMessage.setAdapter(mUsersChatAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        clearCurrentUsers();

        if (mChildEventListener != null) {
            mUserRefDatabase.removeEventListener(mChildEventListener);
        }

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }
    private void initData() {
        for (int i = 0; i < 20; i++) {
            datas.add("我是名字" + i);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        if (mRecyclerMessage != null) {
            mRecyclerMessage.setHasFixedSize(true);
        }
        mRecyclerMessage.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter<String>(mContext, R.layout.item_message, datas) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, String s) {
                int itemPosition = holder.getItemPosition();
                holder.setText(R.id.tv_name, s);
                holder.setText(R.id.tv_content, "我是內容我是內容我是內容我是內容我是內容我是內容我是內容我是內容我是內容");
                holder.setText(R.id.tv_msg_num, "99");
//                holder.setText(R.id.tv_time, JodaTimeUtil.progressDate1(mContext, "2017-12-01T10:00:10+08:00"));
                if (itemPosition > 9) {
                    holder.setVisible(R.id.tv_msg_num, false);
                    holder.setVisible(R.id.tv_time, true);
                } else {
                    holder.setVisible(R.id.tv_msg_num, true);
                    holder.setVisible(R.id.tv_time, false);
                }
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                ToastUtil.toastShort(position + "");
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerMessage.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void clearCurrentUsers() {
        mUsersChatAdapter.clear();
        mUsersKeyList.clear();
    }
    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists()){
                    String userUid = dataSnapshot.getKey();
                    Logger.d(dataSnapshot.getKey()+"<--->"+mCurrentUserUid);
                    if(dataSnapshot.getKey().equals(mCurrentUserUid)){
                        User currentUser = dataSnapshot.getValue(User.class);
                        mUsersChatAdapter.setCurrentUserInfo(userUid, currentUser.getEmail(), currentUser.getCreatedAt());
                    }else {

                        final User recipient = dataSnapshot.getValue(User.class);
                        recipient.setRecipientId(userUid);
                        mUsersKeyList.add(userUid);
//                        String chatRef = recipient.createUniqueChatRef(PrefUtils.getLong(App.APP_CONTEXT,"createdAt",0), PrefUtils.getString(App
//                                .APP_CONTEXT, "uid", null));
//                        messageChatDatabase = FirebaseDatabase.getInstance().getReference().child(chatRef);
//                        messageChatDatabase.limitToLast(1).addChildEventListener(new ChildEventListener() {
//
//                            @Override
//                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                if (dataSnapshot.exists()) {
//                                    ChatMessage newMessage = dataSnapshot.getValue(ChatMessage.class);
//                                    recipient.setLastMsg(newMessage.getMessage());
//                                } else {
//                                    recipient.setLastMsg(null);
//                                }
//                                mUsersChatAdapter.notifyData();
//                            }
//
//                            @Override
//                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
                        mUsersChatAdapter.refill(recipient);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    String userUid = dataSnapshot.getKey();
                    if(!userUid.equals(mCurrentUserUid)) {

                        User user = dataSnapshot.getValue(User.class);

                        int index = mUsersKeyList.indexOf(userUid);
                        if(index > -1) {
                            mUsersChatAdapter.changeUser(index, user);
                        }
                    }

                }
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
        };
    }
}
