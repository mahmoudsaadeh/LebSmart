package com.example.lebsmart.LostFoundAnnouncements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lebsmart.R;

import java.util.List;


public class LFAsRVA extends RecyclerView.Adapter<LFAsRVA.ViewHolder> {

    List<LFA> lfaList;

    public LFAsRVA(List<LFA> lfas) {
        this.lfaList = lfas;
    }

    @NonNull
    @Override
    public LFAsRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lfa_rv_row_item, parent, false);
        LFAsRVA.ViewHolder viewHolder = new LFAsRVA.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LFAsRVA.ViewHolder holder, int position) {
        holder.lfaTitleTV.setText(lfaList.get(position).getTitleLFA());
        holder.announcementDateLFAContent.setText(lfaList.get(position).getDateFoundLFA());
        holder.announcementDescriptionLFAContent.setText(lfaList.get(position).getDescriptionLFA());

        //get data from the stored ID
        holder.foundWithLFAContent.setText(lfaList.get(position).getAddedByLFA());
        //holder.phoneLFAContent.setText(lfaList.get(position).get());

        boolean isExpanded = lfaList.get(position).isExpanded();
        holder.lfaExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return lfaList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView lfaTitleTV;
        TextView announcementDateLFAContent;
        TextView announcementDescriptionLFAContent;
        TextView foundWithLFAContent;
        TextView phoneLFAContent;

        TableLayout lfaExpandableLayout;
        LinearLayout lfaExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lfaTitleTV = itemView.findViewById(R.id.lfaTitleTV);
            announcementDateLFAContent = itemView.findViewById(R.id.announcementDateLFAContent);
            announcementDescriptionLFAContent = itemView.findViewById(R.id.announcementDescriptionLFAContent);
            foundWithLFAContent = itemView.findViewById(R.id.foundWithLFAContent);
            phoneLFAContent = itemView.findViewById(R.id.phoneLFAContent);

            lfaExpandableLayout = itemView.findViewById(R.id.lfaExpandableLayout);
            lfaExpand = itemView.findViewById(R.id.lfaExpand);

            lfaExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LFA lfa = lfaList.get(getAdapterPosition());
                    lfa.setExpanded(!lfa.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }// end class ViewHolder

}
