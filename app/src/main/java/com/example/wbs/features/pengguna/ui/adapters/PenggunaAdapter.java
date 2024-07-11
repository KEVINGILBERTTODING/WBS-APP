package com.example.wbs.features.pengguna.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wbs.R;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.utils.listener.OnClickListener;

import java.util.List;

public class PenggunaAdapter extends RecyclerView.Adapter<PenggunaAdapter.ViewHolder> {
    private Context context;
    private List<UserModelProfile> userModelProfiles;
    private OnClickListener onClickListener;

    public PenggunaAdapter(Context context, List<UserModelProfile> userModelProfiles) {
        this.context = context;
        this.userModelProfiles = userModelProfiles;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public PenggunaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pengguna, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenggunaAdapter.ViewHolder holder, int position) {

        UserModelProfile userModelProfile = userModelProfiles.get(position);
        holder.tvName.setText(userModelProfile.getNama());
        holder.tvEmail.setText(userModelProfile.getEmail());


    }

    @Override
    public int getItemCount() {
        return userModelProfiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);


            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.setOnClickListener(getAdapterPosition(), "edit", userModelProfiles.get(getAdapterPosition()));
                }
            });
        }
    }
}
