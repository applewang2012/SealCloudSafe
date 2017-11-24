package plugin.safe.cloud.seal.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import plugin.safe.cloud.seal.R;
import plugin.safe.cloud.seal.model.CarveCorpInfo;
import plugin.safe.cloud.seal.util.UtilTool;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	Context context;
	ArrayList<ArrayList<CarveCorpInfo>> list;

	public ExpandableListAdapter(Context context, ArrayList<ArrayList<CarveCorpInfo>> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public CarveCorpInfo getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
		ChildHolder holder;
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = View.inflate(context, R.layout.item_police_check_show_layout, null);
			holder.name = (TextView) convertView.findViewById(R.id.id_item_police_check_name);
			holder.status = (TextView) convertView.findViewById(R.id.id_item_police_check_status);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		String name = list.get(groupPosition).get(childPosition).getSealDate();
		String status = list.get(groupPosition).get(childPosition).getCheckStatus();
		//holder.start.setText(UtilTool.stampToDateTime(startTime.substring(6, startTime.length() - 2)));
		//holder.end.setText(UtilTool.stampToDateTime(endTime.substring(6, endTime.length() - 2)));
		holder.name.setText(UtilTool.stampToNormalDate(name.substring(6, name.length() - 2)));
		if (status != null && status.equals("0")){
			holder.status.setText("已通过");
			holder.status.setTextColor(Color.parseColor("#20d85d"));
		}else{
			holder.status.setText("待整改");
			holder.status.setTextColor(Color.parseColor("#d43c33"));
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).size();
	}

	@Override
	public List<CarveCorpInfo> getGroup(int groupPosition) {

		return list.get(groupPosition);

	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_police_check_show_expandable_layout, null);
			holder.name =  (TextView) convertView.findViewById(R.id.id_item_police_check_name);
			holder.img =  (ImageView) convertView.findViewById(R.id.id_item_police_check_indicator);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(groupPosition).get(0).getName());

		if (isExpanded) {
			holder.img.setBackgroundResource(R.drawable.history_up);
		} else {
			holder.img.setBackgroundResource(R.drawable.history_down);

		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolder {
		TextView title;
		TextView name;
		ImageView img;
		
	}

	class ChildHolder {
		TextView name;
		TextView status;
	}

}
