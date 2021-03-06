package cmw.co.id.pmc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cmw.co.id.pmc.data.TaskItem;
import cmw.co.id.pmc.R;

/**
 * Created by CMW on 29/01/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.CustomViewHolder>{
    private List<TaskItem> TaskItemList;
    private Context mContext;
    //private OnItemClickListener onItemClickListener;
    View view;
    public TaskAdapter(Context context, List<TaskItem> TaskItemList) {
        this.TaskItemList = TaskItemList;
        this.mContext = context;
//        this.ProjectItemList = new ArrayList<ProjectItem>();
        // we copy the original list to the filter list and use it for setting row values
//        this.ProjectItemList.addAll(this.ProjectItemList);
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_list, null, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final TaskItem TaskItem = TaskItemList.get(i);
        //Setting text view title
        customViewHolder.textView.setText(Html.fromHtml(TaskItem.getName()));
        customViewHolder.status.setText(Html.fromHtml(TaskItem.getStatus()));
        customViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), TaskItem.getName(), Toast.LENGTH_SHORT).show();            }
        });
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textView;
        protected TextView status;

        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.tvTaskName);
            this.status = (TextView) view.findViewById(R.id.tvTaskStatus);
        }
    }


    @Override
    public int getItemCount() {
        return TaskItemList.size();
    }

    public void setFilter(List<TaskItem> countryModels) {
        TaskItemList = new ArrayList<>();
        TaskItemList.addAll(countryModels);
        notifyDataSetChanged();
    }
}
