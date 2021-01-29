package com.example.lebsmart.MeetingsFragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.R;

import java.util.List;

public class MeetingRVA extends RecyclerView.Adapter<MeetingRVA.ViewHolder> {

    List<Meetings> meetings;
    int currentUserPositionInList;

    public MeetingRVA(List<Meetings> meetings1, int currentUserPositionInList) {
        this.meetings = meetings1;
        this.currentUserPositionInList = currentUserPositionInList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.meetings_rv_row_item, parent, false);
        MeetingRVA.ViewHolder viewHolder = new MeetingRVA.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.expandCloseMeetingTitle.setText(meetings.get(position).getMeetingTitle());
        holder.meetingTimeContent.setText(meetings.get(position).getMeetingTime());
        holder.meetingDateContent.setText(meetings.get(position).getMeetingDate());
        holder.meetingPlaceContent.setText(meetings.get(position).getMeetingPlace());
        holder.scheduledByContent.setText(meetings.get(position).getScheduledBy());
        holder.meetingDescriptionContent.setText(meetings.get(position).getMeetingDescription());

        if (position == currentUserPositionInList) {
            holder.expandCloseMeetingTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
        }

        boolean isExpanded = meetings.get(position).isExpanded();
        holder.meetingsExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView expandCloseMeetingTitle;
        TextView meetingTimeContent;
        TextView meetingDateContent;
        TextView meetingPlaceContent;
        TextView scheduledByContent;
        TextView meetingDescriptionContent;

        TableLayout meetingsExpandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expandCloseMeetingTitle = itemView.findViewById(R.id.meetingTitleTV);
            meetingTimeContent = itemView.findViewById(R.id.meetingTimeContent);
            meetingDateContent = itemView.findViewById(R.id.meetingDateContent);
            meetingPlaceContent = itemView.findViewById(R.id.meetingPlaceContent);
            scheduledByContent = itemView.findViewById(R.id.scheduledByContent);
            meetingDescriptionContent = itemView.findViewById(R.id.meetingDescriptionContent);

            meetingsExpandableLayout = itemView.findViewById(R.id.meetingsExpandableLayout);

            expandCloseMeetingTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Meetings meetings1 = meetings.get(getAdapterPosition());
                    meetings1.setExpanded(!meetings1.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }// end class ViewHolder
}