package com.example.lebsmart.About;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.R;

import java.util.ArrayList;
import java.util.List;

public class LORsRVA extends RecyclerView.Adapter<LORsRVA.ViewHolder> {

    List<LORs> loRsList;
    Context context;
    String currentUserId;
    ArrayList<String> userz;

    public LORsRVA(List<LORs> loRs, Context context1, String currentUserId1, ArrayList<String> usersIds) {
        this.loRsList = loRs;
        this.context = context1;
        this.currentUserId = currentUserId1;
        this.userz = usersIds;
    }


    @NonNull
    @Override
    public LORsRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lors_rv_row_item, parent, false);
        LORsRVA.ViewHolder viewHolder = new LORsRVA.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LORsRVA.ViewHolder holder, int position) {

        if (currentUserId.equals(userz.get(position))) {
            holder.residentNameTV.setText(loRsList.get(position).getFullNames() + " (You)");
        }
        else {
            holder.residentNameTV.setText(loRsList.get(position).getFullNames());
        }

        holder.residentEmailContent.setText(loRsList.get(position).getMails());
        holder.residentPhoneContent.setText(loRsList.get(position).getPhones());
        holder.residentUserTypeContent.setText(loRsList.get(position).getUserTypes());

        if (loRsList.get(position).getUserTypes().equals("Committee member")) {
            holder.residentNameTV.setTypeface(null, Typeface.BOLD_ITALIC);
            holder.residentNameTV.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        boolean isExpanded = loRsList.get(position).isExpanded();
        holder.residentsListExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return loRsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView residentNameTV;
        TextView residentEmailContent;
        TextView residentPhoneContent;
        TextView residentUserTypeContent;

        TableLayout residentsListExpandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            residentNameTV = itemView.findViewById(R.id.residentNameTV);
            residentEmailContent = itemView.findViewById(R.id.residentEmailContent);
            residentPhoneContent = itemView.findViewById(R.id.residentPhoneContent);
            residentUserTypeContent = itemView.findViewById(R.id.residentUserTypeContent);

            residentsListExpandableLayout = itemView.findViewById(R.id.residentsListExpandableLayout);

            residentNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LORs loRs = loRsList.get(getAdapterPosition());
                    loRs.setExpanded(!loRs.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }// end class ViewHolder

}
