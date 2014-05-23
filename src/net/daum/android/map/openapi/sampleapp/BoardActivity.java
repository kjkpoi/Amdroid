package net.daum.android.map.openapi.sampleapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BoardActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		
		ListView jkkim_listview = (ListView) findViewById(R.id.jkkim_listview);
		
		List<Pair<String, String>> itemList = new ArrayList<Pair<String, String>>();
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		itemList.add(new Pair<String, String>("a", "c"));
		itemList.add(new Pair<String, String>("b", "d"));
		
		BoardItemAdapter adapter = new BoardItemAdapter(this, itemList);
		jkkim_listview.setAdapter(adapter);
	}
	
	private class BoardItemAdapter extends ArrayAdapter<String> {
		private List<Pair<String, String>> itemList;
		
		public BoardItemAdapter(Context context, List<Pair<String, String>> itemList) {
			super(context, R.layout.board_item);
			this.itemList = itemList;
		}
		
		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			
			if(convertView == null) {
				viewHolder = new ViewHolder();
				
				LayoutInflater layoutInflator = LayoutInflater.from(getContext());
				convertView = layoutInflator.inflate(R.layout.board_item, null);

				viewHolder.tx1 = (TextView) convertView.findViewById(R.id.textView1);
				viewHolder.tx2 = (TextView) convertView.findViewById(R.id.textView2);
				
				convertView.setTag(viewHolder);
			}
			else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.tx1.setText(itemList.get(position).first);
			viewHolder.tx2.setText(itemList.get(position).second);
			
			return convertView;
		}
		
		class ViewHolder {
			TextView tx1;
			TextView tx2;
		}
	}
}
