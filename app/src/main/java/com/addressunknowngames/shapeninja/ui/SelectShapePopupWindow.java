package com.addressunknowngames.shapeninja.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.addressunknowngames.shapeninja.R;
import com.addressunknowngames.shapeninja.game.GameWindow;
import com.addressunknowngames.shapeninja.model.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

public class SelectShapePopupWindow extends PopupWindow {

	private ListView list;
	private SelectShapeListAdapter adapter;

	private Typeface fontRegular;
	private ItemSelectedCallback callback;

	public interface ItemSelectedCallback {
		void onItemSelected(final Shape shape);
	}

	public SelectShapePopupWindow(final Context context, final ItemSelectedCallback callback) {
		super(context);
		this.callback = callback;

		View view = LayoutInflater.from(context).inflate(R.layout.select_shape_popup, null, false);
		list = view.findViewById(R.id.selecShapeListView);
		adapter = new SelectShapeListAdapter(context);
		list.setAdapter(adapter);
		list.setOnItemClickListener((parent, v, position, id) -> {
			if (callback != null) {
				callback.onItemSelected(adapter.getItem(position));
				dismiss();
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
		private List<Shape> values = new ArrayList<>(GameWindow.ALL_SHAPES);

		public SelectShapeListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return values.size();
		}

		@Override
		public Shape getItem(int position) {
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
			TextView text = convertView.findViewById(R.id.selectShapeListItemText);
			text.setTypeface(fontRegular);

			Shape shape = values.get(position);
			if (shape == null) {
				text.setText(context.getString(R.string.random));
			} else {
				text.setText(context.getString(shape.getStringResId()));
			}

			return convertView;
		}
	}
}
