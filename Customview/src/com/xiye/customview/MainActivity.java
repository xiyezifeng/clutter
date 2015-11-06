package com.xiye.customview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private ListView listView ;
	private String [] header = {"viewpager ÇÐ»»¶¯»­"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.demolistview);
		ArrayAdapter< String> adapter = new  ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1 , header);
		listView.setBackgroundColor(Color.BLACK);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectePager(position);
			}
		});
	}
	
	private void selectePager(int position) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
			intent.setClass(this, DemoPagerTransition.class);
			break;

		default:
			break;
		}
		this.startActivity(intent);

	}
	

}
