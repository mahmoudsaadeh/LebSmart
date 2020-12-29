package com.example.lebsmart.ApartmentsFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.R;

import java.util.List;

public class ApartmentsRecyclerViewAdapter extends RecyclerView.Adapter<ApartmentsRecyclerViewAdapter.ViewHolder> {


    List<Apartment> list;

    public ApartmentsRecyclerViewAdapter(List<Apartment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.apartments_rv_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.info.setText(String.valueOf(position));
        holder.expandCloseBN.setText(list.get(position).getBuilding());
        holder.stateContent.setText(list.get(position).getState());
        holder.priceContent.setText(list.get(position).getPrice());
        holder.areaContent.setText(list.get(position).getArea());
        holder.phoneContent.setText(list.get(position).getPhoneNumber());
        holder.floorContent.setText(list.get(position).getFloor());

        boolean isExpanded = list.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        TextView expandCloseBN;
        TextView stateContent;
        TextView priceContent;
        TextView areaContent;
        TextView phoneContent;
        TextView floorContent;

        ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expandCloseBN = itemView.findViewById(R.id.expandCloseBN);
            stateContent = itemView.findViewById(R.id.stateContent);
            priceContent = itemView.findViewById(R.id.priceContent);
            areaContent = itemView.findViewById(R.id.areaContent);
            phoneContent = itemView.findViewById(R.id.phoneContent);
            floorContent = itemView.findViewById(R.id.floorContent);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            // expand or close on click
            expandCloseBN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Apartment apartment = list.get(getAdapterPosition());
                    apartment.setExpanded(!apartment.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            //itemView.setOnClickListener(this);

            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    list.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                }
            });*/

        }

        /*@Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "" + list.get(getAdapterPosition()).getPrice(), Toast.LENGTH_SHORT).show();
        }*/

    }// end class ViewHolder

}
