package recode.appro.telas;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterCustomDrawer extends ArrayAdapter<DrawerItem> {

	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;
	
	public AdapterCustomDrawer(Context context, int layoutResourceID,
			List<DrawerItem> listItems) {
		
		super(context, layoutResourceID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			
			drawerHolder.ItemNome = (TextView) view.findViewById(R.id.textView_drawer_item_nome);
			drawerHolder.headerTitulo = (TextView) view.findViewById(R.id.textView_drawer_header_titulo);
			drawerHolder.headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
			drawerHolder.itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
			view.setTag(drawerHolder);

		} else {
			
			drawerHolder = (DrawerItemHolder) view.getTag();

		}

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

		if (dItem.getDrawerHeaderTitulo() != null) {
			
			drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.headerTitulo.setText(dItem.getDrawerHeaderTitulo());
			
		} else {

			drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.ItemNome.setText(dItem.getDrawerItemNome());
		}
		return view;
	}

	private static class DrawerItemHolder {
		TextView ItemNome, headerTitulo;
		LinearLayout headerLayout, itemLayout;
		
	}
}