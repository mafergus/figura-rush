package com.addressunknowngames.shapeninja;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addressunknowngames.shapeninja.GameWindow.GestureType;

import java.util.ArrayList;
import java.util.List;

public class SelectShapePopupWindow extends PopupWindow {

	private ListView list;
	private SelectShapeListAdapter adapter;

	private Typeface fontRegular;
	private ItemSelectedCallback callback;

	public interface ItemSelectedCallback {
		public void onItemSelected(final GestureType shape);
	}

	public SelectShapePopupWindow(final Context context, final ItemSelectedCallback callback) {
		super(context);
		this.callback = callback;

		View view = LayoutInflater.from(context).inflate(R.layout.select_shape_popup, null, false);
		list = (ListView)view.findViewById(R.id.selecShapeListView);
		adapter = new SelectShapeListAdapter(context);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (callback != null) {
					callback.onItemSelected((GestureType)adapter.getItem(position));
					dismiss();
				}
			}
		});

		fontRegular = Typeface.createFromAsset(context.getAssets(), "font/josefinsans_regular.ttf");

		setBackgroundDrawable(new BitmapDrawable());
		setOutsideTouchable(true);
		setTouchable(true);
		setFocusable(true);
		setWindowLayoutMode(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		setAnimationStyle(R.style.Animations_popup);
		setContentView(view);
	}

	private class SelectShapeListAdapter extends BaseAdapter {
		final Context context;
		private List<GestureType> values = new ArrayList<GestureType>(GameWindow.ALL_SHAPES);

		public SelectShapeListAdapter(Context context) {
			this.context = context;
			values.add(GestureType.INVALID);
		}

		@Override
		public int getCount() {
			return values.size();
		}

		@Override
		public Object getItem(int position) {
			return values.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.select_shape_list_item, null);
			}
			TextView text = (TextView)convertView.findViewById(R.id.selectShapeListItemText);
			text.setTypeface(fontRegular);

			GestureType shape = values.get(position);
			if (shape == GestureType.INVALID) {
				text.setText(context.getString(R.string.random));				
			} else {
				text.setText(context.getString(shape.getStringRes()));
			}

			return convertView;
		}
	}
}
