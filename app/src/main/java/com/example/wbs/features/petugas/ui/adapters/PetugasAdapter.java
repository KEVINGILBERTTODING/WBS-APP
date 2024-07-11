package com.example.wbs.features.petugas.ui.adapters;

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

public class PetugasAdapter extends RecyclerView.Adapter<PetugasAdapter.ViewHolder> {
    private Context context;
    private List<UserModelProfile> userModelProfiles;
    private OnClickListener onClickListener;

    public PetugasAdapter(Context context, List<UserModelProfile> userModelProfiles) {
        this.context = context;
        this.userModelProfiles = userModelProfiles;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public PetugasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_petugas, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetugasAdapter.ViewHolder holder, int position) {

        UserModelProfile userModelProfile = userModelProfiles.get(position);
        holder.tvName.setText(userModelProfile.getNama_petugas());
        holder.tvUsername.setText(userModelProfile.getUsername());
        holder.tvTelp.setText(userModelProfile.getTelp());
        holder.tvRole.setText(userModelProfile.getLevel());
        holder.tvKriteria.setText(userModelProfile.getNama_kriteria());

    }

    @Override
    public int getItemCount() {
        return userModelProfiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvUsername, tvTelp,
        tvRole, tvKriteria;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvTelp = itemView.findViewById(R.id.tvTelp);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvKriteria = itemView.findViewById(R.id.tvKriteria);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.setOnClickListener(getAdapterPosition(), "edit", userModelProfiles.get(getAdapterPosition()));
                }
            });
        }
    }
}
