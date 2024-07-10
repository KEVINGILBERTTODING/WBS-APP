package com.example.wbs.features.chat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wbs.R;
import com.example.wbs.core.models.SharedUserModel;
import com.example.wbs.core.services.UserService;
import com.example.wbs.features.chat.models.ChatModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private UserService userService;
    private List<ChatModel> chatModelList;
    private boolean isPengadu;
    private SharedUserModel sharedUserModel;

    public ChatAdapter(Context context, List<ChatModel> chatModelList, boolean isPengadu) {
        this.context = context;
        this.chatModelList = chatModelList;
        this.isPengadu = isPengadu;
        userService = new UserService();
        userService.initSharedPref(context);
        sharedUserModel = userService.getUserInfo();

    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatModel chatModel = chatModelList.get(position);


        if (isPengadu) {
            holder.cvChatSender.setVisibility(View.GONE);
            holder.cvChat.setVisibility(View.GONE);


            if (!chatModel.getId_pengguna().equals("0")) {
                holder.cvChatSender.setVisibility(View.VISIBLE);
                holder.tvChatSender.setText(chatModel.getTanggapan());
                holder.getTvDateSender.setText(chatModel.getTgl_tanggapan());
            }else {
                holder.cvChat.setVisibility(View.VISIBLE);
                holder.tvChat.setText(chatModel.getTanggapan());
                holder.tvDate.setText(chatModel.getTgl_tanggapan());
            }
        } else {
            holder.cvChatSender.setVisibility(View.GONE);
            holder.cvChat.setVisibility(View.GONE);

            if (!chatModel.getId_petugas().equals("0")) {
                holder.cvChatSender.setVisibility(View.VISIBLE);
                holder.tvChatSender.setText(chatModel.getTanggapan());
                holder.getTvDateSender.setText(chatModel.getTgl_tanggapan());
            }else {
                holder.cvChat.setVisibility(View.VISIBLE);
                holder.tvChat.setText(chatModel.getTanggapan());
                holder.tvDate.setText(chatModel.getTgl_tanggapan());
            }
        }

    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChat, tvDate, tvChatSender,  getTvDateSender;
        private CardView cvChat, cvChatSender;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChat = itemView.findViewById(R.id.tvIsiPesan);
            tvDate = itemView.findViewById(R.id.tvDate);
            cvChat = itemView.findViewById(R.id.cvChat);
            tvChatSender = itemView.findViewById(R.id.tvIsiPesanSender);
            getTvDateSender = itemView.findViewById(R.id.tvDateSender);
            cvChatSender = itemView.findViewById(R.id.cvChatSender);
        }
    }
}
