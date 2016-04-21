package com.it.dadishu2;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ImageButton[] imgBtns;// 地鼠洞个数

	TextView scoreTextView;// 显示分数的TextView
	TextView timeRemainTextView;// 显示剩余时间控件
	EditText timeOutEditText;// 用户输入游戏时间控件

	int score;// 得分
	int timeOut;// 游戏时间(秒/s)默认为60s
	int timeRemain;// 剩余时间（毫秒）
	int temp_timeRemain;// 临时存储剩余时间（秒）
	static final short SHOW_TIMES = 1000;// 地鼠出现的间隔时间 单位：毫秒
	static final int TIMEOUT_DEFAULT = 5 * 1000;// 游戏时间(秒/s)默认为60s
	
	int index;// 显示地鼠的索引，产生的随机数

	Thread childThread;// 子线程
	boolean startFlag = false;//子线程是否启动

	// int temp_timeRemain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 获取显示得分、剩余时间、和游戏时间的文本控件
		scoreTextView = (TextView) findViewById(R.id.score_tv);
		timeRemainTextView = (TextView) findViewById(R.id.timeRemain_tv);
		timeOutEditText = (EditText) findViewById(R.id.timeOut_et);

		// 获取ImageButton控件
		imgBtns = new ImageButton[6];
		imgBtns[0] = (ImageButton) findViewById(R.id.imageButton1);
		imgBtns[1] = (ImageButton) findViewById(R.id.imageButton2);
		imgBtns[2] = (ImageButton) findViewById(R.id.imageButton3);
		imgBtns[3] = (ImageButton) findViewById(R.id.imageButton4);
		imgBtns[4] = (ImageButton) findViewById(R.id.imageButton5);
		imgBtns[5] = (ImageButton) findViewById(R.id.imageButton6);

		// 创建子线程，暂时不启动。点击开始按钮后启动
		childThread = new Thread(new Runnable() {
			public void run() {
				int times = 0;//游戏经过的时间
				while (times < timeOut) {//判断是否超出用户设定的时间，或默认时间（60s）
					
					index = new Random().nextInt(6);//产生地鼠的地洞索引
					
					//子线程中的代码在主线程中执行,实现数据处理和画面显示操作在不同的线程中执行
					//Android中要求，画面显示在主线程中执行，数据处理在子线程中操作，为了获得更好的用户体验
					runOnUiThread(new Runnable() {
						public void run() {
							//除了要现实的地洞外，其他的都显示空白，以达到地鼠消失的效果
							for (int i = 0; i < imgBtns.length; i++) {
								if (index != i) {
									imgBtns[i]
											.setImageResource(R.drawable.kongbai);
									//为Button设置标签，为了判别点击的按钮是否有地鼠。
									//beijing标签：没有地鼠
									//dishu标签，有地鼠
									imgBtns[i].setTag("beijing");
								}
							}
							imgBtns[index]
									.setImageResource(R.drawable.ic_launcher);
							imgBtns[index].setTag("dishu");
						}
					});

					//地鼠出现的间隔时间
					try {
						Thread.sleep(SHOW_TIMES);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//游戏经过的事件，以及剩余的事件（单位ms）
					times = times + SHOW_TIMES;
					temp_timeRemain = timeOut - times;
					
					//转换剩余时间单位，并在主线程中为剩余时间控件设置剩余时间
					runOnUiThread(new Runnable() {
						public void run() {
							timeRemain = temp_timeRemain / 1000;//单位转换		ms--->s
//							if(0 == timeRemain){//剩余时间为0，则可以重新启动游戏
//								startFlag = false;
//							}
							timeRemainTextView.setText(timeRemain + "");//设置剩余时间控件中的剩余时间
						}
					});
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void fun(View view) {
		//获取点击的按钮控件，并取得标签
		ImageButton imgBtn = (ImageButton) view;
		String tag = (String) imgBtn.getTag();
		//判断点击的按钮中是否有地鼠
		//beijing标签：没有地鼠
		//dishu标签，有地鼠
		if ("dishu".equals(tag)) {
			score++;
			imgBtn.setImageResource(R.drawable.kongbai);
			imgBtn.setTag("beijing");
		}
		System.out.println("" + score);
		scoreTextView.setText("" + score + "分");
	}

	public void startGame(View view) {
		// 获取用户输入游戏时间，并开始游戏
		if(false == startFlag){
			String timeS = timeOutEditText.getText().toString();
			if(timeS.equals("")){
				timeOut = TIMEOUT_DEFAULT;
			}
			else{
				timeOut = Integer.parseInt(timeS) * 1000;//秒----->毫秒
			}
/*			int time = Integer.parseInt(timeS);
			if(0 != time){//判断用户是否输入时间，没有的话默认为60s
				timeOut = time;
			}
			*/
			childThread.start();
			startFlag = true;
		}
	}

}
