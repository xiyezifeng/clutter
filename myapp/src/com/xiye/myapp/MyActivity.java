package com.xiye.myapp;

import java.util.ArrayList;
import java.util.List;

import com.xiye.localdata.Resource;
import com.xiye.model.Demo_1_Model;
import com.xiye.myapp.adapter.Demo_1_Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MyActivity extends Activity {
	
	private ListView demo_listview_1;
	private Demo_1_Adapter adapter;
	
	private static List<Demo_1_Model> model_1 = new ArrayList<Demo_1_Model>();
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initData();
        adapter = new Demo_1_Adapter(this, model_1);
        demo_listview_1.setAdapter(adapter);
    }
    
    private void initView(){
    	demo_listview_1 = (ListView) findViewById(R.id.demo_listview_1);
    }
    
    private void initData(){
    	for (int i = 0; i < Resource.images.length; i++) {
			model_1.add(new Demo_1_Model(Resource.images[i],
					Resource.names[i],
					Resource.infos[i]));
		}
    }
}
