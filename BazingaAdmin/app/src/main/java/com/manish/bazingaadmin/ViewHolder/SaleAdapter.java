package com.manish.bazingaadmin.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.manish.bazingaadmin.R;

import java.util.HashMap;
import java.util.List;

public class SaleAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> menulist;
    private HashMap<String,List<String>> submenulist;

    public SaleAdapter(Context context, List<String> menulist, HashMap<String, List<String>> submenulist) {
        this.context = context;
        this.menulist = menulist;
        this.submenulist = submenulist;
    }

    @Override
    public int getGroupCount() {
        return this.menulist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.submenulist.get(this.menulist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.menulist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.submenulist.get(this.menulist.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String menuTitle = (String) getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.salemenulist,null);
        }
        TextView header =(TextView)convertView.findViewById(R.id.menuheader);
        header.setText(menuTitle);
        return  convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String submenu=(String)getChild(groupPosition,childPosition);
        int first=submenu.indexOf('#');
        int last=submenu.lastIndexOf('#');
        String quantity=submenu.substring(0,first);
        String total=submenu.substring(first+1,last);
        String name=submenu.substring(last+1);

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.subsalelist,null);
        }

        TextView foodname =(TextView)convertView.findViewById(R.id.subItemName);
        TextView foodquantity =(TextView)convertView.findViewById(R.id.subsalequantity);
        TextView foodtotal =(TextView)convertView.findViewById(R.id.subsaletotal);

        foodname.setText(name);
        foodquantity.setText(quantity);
        foodtotal.setText("Rs. "+total);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
