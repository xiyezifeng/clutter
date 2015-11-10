package com.xiye.myapp;

import java.util.ArrayList;
import java.util.List;

import com.xiye.customview.CustomListview;
import com.xiye.customview.CustomScrollView;
import com.xiye.localdata.Resource;
import com.xiye.model.Demo_1_Model;
import com.xiye.myapp.adapter.Demo_1_Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MyActivity extends Activity {
	
	private CustomListview demo_listview_1;
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
    	demo_listview_1 = (CustomListview) findViewById(R.id.demo_listview_1);
    }
    
    private void initData(){
    		for (int i = 0; i < Resource.images.length; i++) {
    			model_1.add(new Demo_1_Model(Resource.images[i],
    					Resource.names[i],
    					Resource.infos[i]));
    		}
    }
    
    public void setListViewHeighBasedOnChildren(ListView listView){
		if(listView.getAdapter() == null){
			return;
		}
		int totalHeight = 0 ;
		for (int i = 0; i < listView.getAdapter().getCount(); i++) {
			View listItem = listView.getAdapter().getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight+
				(listView.getDividerHeight()*(listView.getAdapter().getCount() - 1));
		listView.setLayoutParams(params);
		System.out.println("listview ÉèÖÃ ¸ß¶È :  "+totalHeight);
	}
}
