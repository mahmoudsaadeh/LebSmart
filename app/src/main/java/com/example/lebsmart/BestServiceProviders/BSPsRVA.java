package com.example.lebsmart.BestServiceProviders;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.Activities.BSPsActivity;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BSPsRVA extends RecyclerView.Adapter<BSPsRVA.ViewHolder> {

    List<BSP> bsps;
    int currentUserPositionInList;
    String category;
    Context context;
    ProgressDialog progressDialog;
    ArrayList<String> ratings;

    public BSPsRVA(List<BSP> bspList, int currentUserPositionInList, String category, Context context
            , ProgressDialog p, ArrayList<String> ratings) {
        this.bsps = bspList;
        this.currentUserPositionInList = currentUserPositionInList;
        this.category = category;
        this.context = context;
        this.progressDialog = p;
        this.ratings = ratings;
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
        //holder.bspRatingBar.setRating(Float.parseFloat(bsps.get(position).getSpRating())); // overall rating
        holder.overallRating.setText(bsps.get(position).getSpRating());
        holder.bspAddedByContent.setText(bsps.get(position).getAddedBy());

        if (position == currentUserPositionInList) {
            holder.spPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
        }
        if (!ratings.get(position).equals("nf") && ratings.size() > 0) {
            Log.i("bspRva", ratings.get(position));
            holder.bspRatingBar2.setRating(Float.parseFloat(ratings.get(position)));
        }

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
        TextView bspAddedByContent;
        //RatingBar bspRatingBar;
        RatingBar bspRatingBar2;
        TextView overallRating;

        TableLayout bspExpandableLayout;
        ConstraintLayout bspExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            spName = itemView.findViewById(R.id.spName);
            spPhone = itemView.findViewById(R.id.spPhone);
            bspJobContent = itemView.findViewById(R.id.bspJobContent);
            bspPhoneContent = itemView.findViewById(R.id.bspPhoneContent);
            residencePlaceBSPContent = itemView.findViewById(R.id.residencePlaceBSPContent);
            //bspRatingBar = itemView.findViewById(R.id.bspRatingBar);
            bspAddedByContent = itemView.findViewById(R.id.bspAddedByContent);
            bspRatingBar2 = itemView.findViewById(R.id.bspRatingBar2);
            overallRating = itemView.findViewById(R.id.overallRating);

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

            bspRatingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, final boolean fromUser) {
                    // change rating in db
                    if (fromUser) {
                        CommonMethods.displayLoadingScreen(progressDialog);
                    }

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                            .getReference("SPsRatings").child(category)
                            .child(bsps.get(getAdapterPosition()).getSpPhone())
                            .child("ratedBy").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("rating", "submitted successfully!");
                                if (fromUser){
                                    Toast.makeText(context, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
                                    CommonMethods.dismissLoadingScreen(progressDialog);
                                }
                            }
                            else {
                                Log.i("rating", "submitted successfully!");
                                if (fromUser){
                                    Toast.makeText(context, "There was a problem! Your rating wasn't submitted.", Toast.LENGTH_SHORT).show();
                                    CommonMethods.dismissLoadingScreen(progressDialog);
                                }
                            }
                        }
                    });
                }
            });

        }

    }// end class ViewHolder

}
