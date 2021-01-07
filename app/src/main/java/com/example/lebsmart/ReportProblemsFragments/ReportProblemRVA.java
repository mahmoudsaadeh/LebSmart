package com.example.lebsmart.ReportProblemsFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.MeetingsFragments.MeetingRVA;
import com.example.lebsmart.MeetingsFragments.Meetings;
import com.example.lebsmart.R;

import java.util.List;

public class ReportProblemRVA extends RecyclerView.Adapter<ReportProblemRVA.ViewHolder> {

    List<Problem> problems;

    public ReportProblemRVA(List<Problem> problemList) {
        this.problems = problemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.problems_rv_row_item, parent, false);
        ReportProblemRVA.ViewHolder viewHolder = new ReportProblemRVA.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.reportNumber.setText("Report " + (position + 1));
        holder.problemDescriptionContent.setText(problems.get(position).getProblemDescription());
        holder.reportDateContent.setText(problems.get(position).getProblemReportDate());
        holder.problemReportedBy.setText(problems.get(position).getProblemReportedBy());

        boolean isExpanded = problems.get(position).isExpanded();
        holder.problemExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return problems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView reportNumber;
        TextView problemDescriptionContent;
        TextView reportDateContent;
        TextView problemReportedBy;

        TableLayout problemExpandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reportNumber = itemView.findViewById(R.id.reportNumber);
            problemDescriptionContent = itemView.findViewById(R.id.problemDescriptionContent);
            reportDateContent = itemView.findViewById(R.id.reportDateContent);
            problemReportedBy = itemView.findViewById(R.id.reportedByProblemContent);

            problemExpandableLayout = itemView.findViewById(R.id.problemsExpandableLayout);

            reportNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Problem report = problems.get(getAdapterPosition());
                    report.setExpanded(!report.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }

    }// end class ViewHolder

}
