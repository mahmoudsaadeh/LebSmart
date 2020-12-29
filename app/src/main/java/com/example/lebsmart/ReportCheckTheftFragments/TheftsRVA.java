package com.example.lebsmart.ReportCheckTheftFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lebsmart.R;

import java.util.List;

public class TheftsRVA extends RecyclerView.Adapter<TheftsRVA.ViewHolderT> {

    List<Thefts> thefts;

    public TheftsRVA (List<Thefts> list) {
        this.thefts = list;
    }

    @NonNull
    @Override
    public ViewHolderT onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.thefts_rv_row_item, parent, false);
        TheftsRVA.ViewHolderT viewHolder = new TheftsRVA.ViewHolderT(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderT holder, int position) {
        holder.expandCloseTheftTitle.setText(thefts.get(position).getTitle());
        holder.reportedByContent.setText(thefts.get(position).getReportedBy());
        holder.dateContent.setText(thefts.get(position).getDate());
        holder.timeContent.setText(thefts.get(position).getTime());
        holder.messageContent.setText(thefts.get(position).getMessage());
        holder.locationContent.setText(thefts.get(position).getLocation());

        boolean isExpanded = thefts.get(position).isExpanded();
        holder.theftsExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return thefts.size();
    }

    class ViewHolderT extends RecyclerView.ViewHolder {

        TextView expandCloseTheftTitle;
        TextView reportedByContent;
        TextView dateContent;
        TextView timeContent;
        TextView messageContent;
        TextView locationContent;

        TableLayout theftsExpandableLayout;

        public ViewHolderT(@NonNull View itemView) {
            super(itemView);

            expandCloseTheftTitle = itemView.findViewById(R.id.theftTitleTV);
            reportedByContent = itemView.findViewById(R.id.reportedByContent);
            dateContent = itemView.findViewById(R.id.theftDateContent);
            timeContent = itemView.findViewById(R.id.theftTimeContent);
            messageContent = itemView.findViewById(R.id.theftMessageContent);
            locationContent = itemView.findViewById(R.id.theftLocationContent);

            theftsExpandableLayout = itemView.findViewById(R.id.theftsExpandableLayout);

            expandCloseTheftTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thefts thefts1 = thefts.get(getAdapterPosition());
                    thefts1.setExpanded(!thefts1.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }// end class ViewHolder

}


