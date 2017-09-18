package com.medicinedot.www.medicinedot.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;


/**
 * Created by Android on 2017/9/5.
 */
public class RecentchatSupplierFragment  extends EaseConversationListFragment
        implements EMMessageListener,AdapterView.OnItemClickListener{

    @Override
    protected void initView() {
        super.initView();
        EMClient.getInstance().chatManager().addMessageListener(this);
        conversationListView.setOnItemClickListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        refresh();
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        EMConversation conversation = conversationListView.getItem(position);
        String username = conversation.conversationId();
//        if (username.equals(EMClient.getInstance().getCurrentUser()))
//            Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
//        else {
//            // start chat acitivity
//            Intent intent = new Intent(getActivity(), ChatActivity.class);
//            if (conversation.isGroup()) {
//                if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
//                    // it's group chat
//                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
//                } else {
//                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
//                }
//
//            }
//            // it's single chat
//            intent.putExtra(EaseConstant.EXTRA_USER_ID, username);
//
//            startActivity(intent);
//        }

    }

    //    @Override
//    protected Object getTopbarTitle() {
//        return R.string.recentchat;
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_me;
//    }

//    @Override
//    protected void initView(LayoutInflater inflater, View view) {
//        //会话列表控件
//        conversationListView = (EaseConversationList) view.findViewById(R.id.list);
//        //初始化，参数为会话列表集合
//        conversationListView.init(conversationList);
//        //刷新列表
//        conversationListView.refresh();
//    }


//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        switch (v.getId()) {
//        }
//    }

}