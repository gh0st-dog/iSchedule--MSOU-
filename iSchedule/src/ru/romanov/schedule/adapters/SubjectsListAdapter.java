package ru.romanov.schedule.adapters;

import java.util.ArrayList;

import ru.romanov.schedule.R;
import ru.romanov.schedule.utils.Subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SubjectsListAdapter extends BaseExpandableListAdapter{
	
	private ArrayList<String> groups;
    private ArrayList<ArrayList<Subject>> children;
    private Context context;
    
    public SubjectsListAdapter(Context context, ArrayList<String> groups, ArrayList<ArrayList<Subject>> children) {
    	this.context = context;
    	this.children = children;
    	this.groups = groups;
    }

	@Override
	public Subject getChild(int groupPosition, int childPosition) {
		return children.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		Subject subj = getChild(groupPosition, childPosition);
		
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.schedule_item_element, null);
        }
		
		TextView subjView = (TextView) convertView.findViewById(R.id.schedule_item_subject);
		TextView groupView = (TextView) convertView.findViewById(R.id.schedule_item_group);
		TextView timeView = (TextView) convertView.findViewById(R.id.schedule_item_time);
		TextView placeView = (TextView) convertView.findViewById(R.id.schedule_item_place);
		TextView actView = (TextView) convertView.findViewById(R.id.schedule_item_act);
		
		subjView.setText(subj.getSubject());
		groupView.setText(subj.getGroups());
		timeView.setText(subj.getT_start());
		placeView.setText(subj.getPlace());
		actView.setText("Pare");

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return children.get(groupPosition).size();
	}

	@Override
	public String getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String group = getGroup(groupPosition);
		
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.schedule_list_item, null);
        }
		
		TextView dateView = (TextView) convertView.findViewById(R.id.schedule_list_item_date);
		
		dateView.setText(group);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
