package com.example.lebsmart.BestServiceProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.R;
import com.example.lebsmart.ReportProblemsFragments.Problem;
import com.example.lebsmart.ReportProblemsFragments.ReportProblemRVA;

import java.util.List;

public class BSPsRVA extends RecyclerView.Adapter<BSPsRVA.ViewHolder> {

    List<BSP> bsps;

    public BSPsRVA(List<BSP> bspList) {
        this.bsps = bspList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bsp_rv_row_item, parent, false);
        BSPsRVA.ViewHolder viewHolder = new BSPsRVA.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.spName.setText(bsps.get(position).getSpName());
        holder.spPhone.setText(bsps.get(position).getSpPhone());
        holder.bspJobContent.setText(bsps.get(position).getSpJob());
        holder.bspPhoneContent.setText(bsps.get(position).getSpPhone());
        holder.residencePlaceBSPContent.setText(bsps.get(position).getSpPlaceOfResidence());
        holder.bspRatingBar.setRating(Float.parseFloat(bsps.get(position).getSpRating()));

        boolean isExpanded = bsps.get(position).isExpanded();
        holder.bspExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return bsps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView spName;
        TextView spPhone;
        TextView bspJobContent;
        TextView bspPhoneContent;
        TextView residencePlaceBSPContent;
        RatingBar bspRatingBar;

        TableLayout bspExpandableLayout;
        LinearLayout bspExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            spName = itemView.findViewById(R.id.spName);
            spPhone = itemView.findViewById(R.id.spPhone);
            bspJobContent = itemView.findViewById(R.id.bspJobContent);
            bspPhoneContent = itemView.findViewById(R.id.bspPhoneContent);
            residencePlaceBSPContent = itemView.findViewById(R.id.residencePlaceBSPContent);
            bspRatingBar = itemView.findViewById(R.id.bspRatingBar);

            bspExpandableLayout = itemView.findViewById(R.id.bspExpandableLayout);
            bspExpand = itemView.findViewById(R.id.bspExpand);

            bspExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BSP bsp = bsps.get(getAdapterPosition());
                    bsp.setExpanded(!bsp.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }// end class ViewHolder

}
