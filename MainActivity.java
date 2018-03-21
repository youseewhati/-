package cn.itcast.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ViewPager viewpager;
	private List<ImageView> images;
	private String[] words;
	private TextView textView;
	private LinearLayout points;
	private int preRedpointIndex = 0;// 记录前一个红点的位置
	private boolean isPlay = true;// 是否自动轮播

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textview);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		points = (LinearLayout) findViewById(R.id.points);
		// 准备数据
		initData();
		// 设置数据适配器
		viewpager.setAdapter(new MypagerAdapter());
		// 监听ViewPager滑动
		viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 初始化第0页的文字
		textView.setText(words[0]);
		// 初始化第0个红点
		points.getChildAt(0).setEnabled(true);
		// 实现往做无限滑动，设置当前条目位置为一个大值
		int center = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% images.size();
		viewpager.setCurrentItem(center);
		// 自动轮播
		autoPlay();
	}

	private void autoPlay() {
		new Thread(){
			public void run() {
				while(isPlay){
					try {
						Thread.sleep(3000);
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								System.out.println("切换到下一页");
								viewpager.setCurrentItem(viewpager.getCurrentItem()+1);
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		// 当滑动是调用
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		// 当选中某一页时，调用
		@Override
		public void onPageSelected(int position) {
			int newPosition = position % images.size();
			// 把前一个点变白色
			points.getChildAt(preRedpointIndex).setEnabled(false);
			// 当滑到某一页时，改变文字
			textView.setText(words[newPosition]);
			// 把相应位置的点变红色
			points.getChildAt(newPosition).setEnabled(true);
			preRedpointIndex = newPosition;

		}

		// 滑动状态发生变化是调用
		@Override
		public void onPageScrollStateChanged(int state) {

		}

	}

	private void initData() {
		// 初始化文字
		words = new String[] { "巩俐不低俗，我就不地鼠", "朴树有回来了", "北京电影如何升级", "乐视网tv大派送",
				"热血diao丝的反杀" };
		// 准备图片id
		images = new ArrayList<ImageView>();
		int[] imgIds = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,
				R.drawable.d, R.drawable.e };
		for (int i = 0; i < imgIds.length; i++) {
			ImageView imageView = new ImageView(getApplicationContext());
			// // 设置图片的缩放类型
			// imageView.setScaleType(ScaleType.FIT_XY);
			// imageView.setImageResource(imgIds[i]);
			imageView.setBackgroundResource(imgIds[i]);
			images.add(imageView);
			// 创建点的指示器
			ImageView point = new ImageView(getApplicationContext());
			LayoutParams params = new LayoutParams(5, 5);
			if (i != 0) {
				params.leftMargin = 10;
			}
			point.setLayoutParams(params);
			point.setEnabled(false);
			point.setBackgroundResource(R.drawable.point_selector);
			// 添加到线性容器中
			points.addView(point);
		}
	}

	class MypagerAdapter extends PagerAdapter {
		// 返回条目数量
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;// 为了实现无限循环
		}

		// 展示的条目与记录的object有没有关系
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		// 返回条目，类似BaseAdapter中getView
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("position:" + position);
			int newPosition = position % images.size();
			ImageView imageView = images.get(newPosition);
			// 把要返回的控件添加到容器
			container.addView(imageView);
			return imageView;
		}

		// 删除条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
	
	@Override
	protected void onDestroy() {
		isPlay = false;
		super.onDestroy();
	}

}
