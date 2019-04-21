package com.alisawalter.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String displayName) {
        mActivity = activity;
        mDatabaseReference = databaseReference.child("messages");
        mDatabaseReference.addChildEventListener(mChildEventListener);
        mDisplayName = displayName;
        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder {
        TextView authorName;
        TextView messageText;
        TextView messageDate;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public Message getItem(int position) {
        DataSnapshot snapshot = mSnapshotList.get(position);
        return snapshot.getValue(Message.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) convertView.findViewById(R.id.author);
            holder.messageText = (TextView) convertView.findViewById(R.id.message);
            holder.messageDate = (TextView) convertView.findViewById(R.id.time);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);

        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        String author = message.getMessageAuthor();
        holder.authorName.setText(author);
        boolean isMyMessage = message.getMessageAuthor().equals(mDisplayName);
        setMessageRow(isMyMessage, holder);

        String text = message.getMessageText();
        holder.messageText.setText(text);

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String time = df.format(message.getMessageTime());
        holder.messageDate.setText(time);

        return convertView;
    }

    private void setMessageRow(boolean isMyMessage, ViewHolder holder) {
        if (isMyMessage) {
            holder.params.gravity = Gravity.END;
            holder.messageText.setBackgroundResource(R.drawable.bubble2);
            holder.authorName.setTextColor(Color.BLUE);
        } else {
            holder.params.gravity = Gravity.START;
            holder.messageText.setBackgroundResource(R.drawable.bubble1);
            holder.authorName.setTextColor(Color.RED);
        }
        holder.authorName.setLayoutParams(holder.params);
        holder.messageDate.setLayoutParams(holder.params);
        holder.messageText.setLayoutParams(holder.params);

    }

    public void cleanup() {
        mDatabaseReference.removeEventListener(mChildEventListener);
    }
}
