package com.example.wbs.features.pengaduan.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wbs.R;
import com.example.wbs.features.pengaduan.model.PengaduanModel;
import com.example.wbs.utils.listener.OnClickListener;

import java.util.List;

public class PengaduanAdapter extends RecyclerView.Adapter<PengaduanAdapter.VH> {
    private Context context;
    private List<PengaduanModel> pengaduanModels;
    private OnClickListener onClickListener;

    public PengaduanAdapter(Context context, List<PengaduanModel> pengaduanModels) {
        this.context = context;
        this.pengaduanModels = pengaduanModels;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public PengaduanAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_aduan, parent, false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengaduanAdapter.VH holder, int position) {

        holder.tvDate.setText(pengaduanModels.get(position).getTgl_pengaduan());
        holder.tvName.setText(pengaduanModels.get(position).getNama());
        holder.tvDesc.setText(pengaduanModels.get(position).getIsi_laporan());


        if (pengaduanModels.get(position).getStatus().equals("0")) {
            holder.tvStatus.setText("Pending");
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.red));
        }else if (pengaduanModels.get(position).getStatus().equals("selesai")) {
            holder.tvStatus.setText("Selesai");
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.green));
        }else if (pengaduanModels.get(position).getStatus().equals("proses")) {
            holder.tvStatus.setText("Proses");
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.yellow));
        }


    }

    @Override
    public int getItemCount() {
        return pengaduanModels.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvDate, tvName, tvDesc, tvStatus;
        private CardView cvStatus;
        public VH(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            cvStatus = itemView.findViewById(R.id.cvAduan);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.setOnClickListener(getAdapterPosition(), "click", pengaduanModels.get(getAdapterPosition()));
                }
            });
        }
    }
}
