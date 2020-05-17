package com.cloudwell.paywell.services.activity.education.bbc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.education.bbc.model.CoursesItem;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

/**
 * Created by Sepon on 3/20/2020.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private static final int UNSELECTED = -1;

    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;
    private static CourseClick courseClick;

    Context mContext;


   private List<CoursesItem>  courselist;


    public TestAdapter(RecyclerView recyclerView, Context mContext, List<CoursesItem> courselist, CourseClick courseClick) {
        this.recyclerView = recyclerView;
        this.mContext = mContext;
        this.courselist = courselist;
        TestAdapter.courseClick = courseClick;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bbc_course_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {

        holder.courseName.setText(courselist.get(position).getCourseName());
        holder.courseAmount.setText(courselist.get(position).getAmount());
        holder.courseName.setText(courselist.get(position).getCourseName());
    }

    @Override
    public int getItemCount() {
        return courselist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
        private ExpandableLayout expandableLayout;
        private LinearLayout expandButton;
        private ConstraintLayout hader;
        private TextView courseName, courseAmount, coursDescription;
        private Button startCourse;

        public ViewHolder(View itemView) {
            super(itemView);

            hader = itemView.findViewById(R.id.hader);
            coursDescription = itemView.findViewById(R.id.coursDescription);
            courseName = itemView.findViewById(R.id.courseName);
            courseAmount = itemView.findViewById(R.id.courseAmount);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(this);
            expandButton = itemView.findViewById(R.id.btn_item);
            startCourse = itemView.findViewById(R.id.btn_start_course);
            startCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
                     courseClick.courseOnclick(courselist.get(getAdapterPosition()));
                }
            });
            expandButton.setOnClickListener(this);
        }

        public void bind() {
            int position = getAdapterPosition();
            boolean isSelected = position == selectedItem;

          //  coursDescription.setText(courselist.get(position).getDescription());
            //expandButton.setText(position + ". Tap to expand");
            expandButton.setSelected(isSelected);
            expandableLayout.setExpanded(isSelected, false);
        }

        @Override
        public void onClick(View view) {
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            String description = courselist.get(getAdapterPosition()).getDescription();

            coursDescription.setText(description);
            if (holder != null) {
                holder.expandButton.setSelected(false);
                holder.expandableLayout.collapse();

            }

            int position = getAdapterPosition();
            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {

                expandButton.setSelected(true);
                expandableLayout.expand();
                selectedItem = position;
                expandableLayout.setBackgroundColor(Color.parseColor("#ffffff"));

            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            Log.d("ExpandableLayout", "State: " + state);
            if (state == ExpandableLayout.State.EXPANDING) {
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }
    }

   public interface CourseClick{
         void courseOnclick(CoursesItem coursesItem );
    }

}
