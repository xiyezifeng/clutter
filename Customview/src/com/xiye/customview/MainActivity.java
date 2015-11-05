package com.xiye.customview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.xiye.imagepager.ImagePager;
import com.xiye.imagepager.MyPagerAdapter;

public class MainActivity extends Activity {

	private ImagePager imagePager;

	private ViewPager mViewPager;
	private int[] mImgIds = new int[] {
			R.drawable.image_1, R.drawable.image_2,
			R.drawable.image_3, R.drawable.image_4,
			R.drawable.image_5, R.drawable.image_6,
			R.drawable.image_7, R.drawable.image_8,
			R.drawable.image_9, R.drawable.image_10,
			};
	private List<ImageView> mImageViews = new ArrayList<ImageView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		selectePager(0);
	}

	private void initData() {
		for (int imgId : mImgIds) {
			ImageView imageView = new ImageView(getApplicationContext());
			imageView.setScaleType(ScaleType.CENTER_CROP);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(getResources(), imgId, options);
			ImageSize size = ImageSizeUtil.getImageViewSize(imageView);
			int bili = ImageSizeUtil.calculateInSampleSize(options,size.width,size.height);

			options.inSampleSize = bili;
			options.inJustDecodeBounds = false;
			imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId,options));
			mImageViews.add(imageView);
		}
	}

	private void selectePager(int position) {

		switch (position) {
		case 0:
			imagePager = (ImagePager) findViewById(R.id.viewpager);
			initData();
			imagePager.setAdapter(new MyPagerAdapter(mImageViews));
			break;

		default:
			break;
		}

	}

}
