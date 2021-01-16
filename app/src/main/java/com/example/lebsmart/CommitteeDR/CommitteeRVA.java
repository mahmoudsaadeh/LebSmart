package com.example.lebsmart.CommitteeDR;

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

public class CommitteeRVA extends RecyclerView.Adapter<CommitteeRVA.ViewHolder> {

    List<CommitteeDR> committeeDRS;

    public CommitteeRVA(List<CommitteeDR> committeeDRList) {
        this.committeeDRS = committeeDRList;
    }

    @NonNull
    @Override
    public CommitteeRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.committee_rv_row_item, parent, false);
        CommitteeRVA.ViewHolder viewHolder = new CommitteeRVA.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommitteeRVA.ViewHolder holder, int position) {
        holder.announcementTitleTV.setText(committeeDRS.get(position).getAnnouncementTitle());
        holder.announcementDateCommitteeContent.setText(committeeDRS.get(position).getAnnouncementDate());
        holder.announcementDescriptionCommitteeContent.setText(committeeDRS.get(position).getAnnouncementDescription());

        boolean isExpanded = committeeDRS.get(position).isExpanded();
        holder.committeeExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return committeeDRS.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView announcementTitleTV;
        TextView announcementDateCommitteeContent;
        TextView announcementDescriptionCommitteeContent;

        TableLayout committeeExpandableLayout;
        LinearLayout committeeExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            announcementTitleTV = itemView.findViewById(R.id.announcementTitleTV);
            announcementDateCommitteeContent = itemView.findViewById(R.id.announcementDateCommitteeContent);
            announcementDescriptionCommitteeContent = itemView.findViewById(R.id.announcementDescriptionCommitteeContent);

            committeeExpandableLayout = itemView.findViewById(R.id.committeeExpandableLayout);
            committeeExpand = itemView.findViewById(R.id.committeeExpand);

            committeeExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommitteeDR committeeDR = committeeDRS.get(getAdapterPosition());
                    committeeDR.setExpanded(!committeeDR.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }// end class ViewHolder

}
