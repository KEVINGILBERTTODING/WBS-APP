package com.example.wbs.features.kriteria.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wbs.R;
import com.example.wbs.features.kriteria.model.KriteriaModel;
import com.example.wbs.features.profile.models.UserModelProfile;
import com.example.wbs.utils.listener.OnClickListener;

import java.util.List;

public class KriteriaAdapter extends RecyclerView.Adapter<KriteriaAdapter.ViewHolder> {
    private Context context;
    private List<KriteriaModel> userModelProfiles;
    private OnClickListener onClickListener;

    public KriteriaAdapter(Context context, List<KriteriaModel> userModelProfiles) {
        this.context = context;
        this.userModelProfiles = userModelProfiles;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public KriteriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kriteria, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KriteriaAdapter.ViewHolder holder, int position) {

        KriteriaModel userModelProfile = userModelProfiles.get(position);
        holder.tvName.setText(userModelProfile.getNama_kriteria());

    }

    @Override
    public int getItemCount() {
        return userModelProfiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);


            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.setOnClickListener(getAdapterPosition(), "edit", userModelProfiles.get(getAdapterPosition()));
                }
            });
        }
    }
}
