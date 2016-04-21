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

	ImageButton[] imgBtns;// ���󶴸���

	TextView scoreTextView;// ��ʾ������TextView
	TextView timeRemainTextView;// ��ʾʣ��ʱ��ؼ�
	EditText timeOutEditText;// �û�������Ϸʱ��ؼ�

	int score;// �÷�
	int timeOut;// ��Ϸʱ��(��/s)Ĭ��Ϊ60s
	int timeRemain;// ʣ��ʱ�䣨���룩
	int temp_timeRemain;// ��ʱ�洢ʣ��ʱ�䣨�룩
	static final short SHOW_TIMES = 1000;// ������ֵļ��ʱ�� ��λ������
	static final int TIMEOUT_DEFAULT = 5 * 1000;// ��Ϸʱ��(��/s)Ĭ��Ϊ60s
	
	int index;// ��ʾ����������������������

	Thread childThread;// ���߳�
	boolean startFlag = false;//���߳��Ƿ�����

	// int temp_timeRemain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ��ȡ��ʾ�÷֡�ʣ��ʱ�䡢����Ϸʱ����ı��ؼ�
		scoreTextView = (TextView) findViewById(R.id.score_tv);
		timeRemainTextView = (TextView) findViewById(R.id.timeRemain_tv);
		timeOutEditText = (EditText) findViewById(R.id.timeOut_et);

		// ��ȡImageButton�ؼ�
		imgBtns = new ImageButton[6];
		imgBtns[0] = (ImageButton) findViewById(R.id.imageButton1);
		imgBtns[1] = (ImageButton) findViewById(R.id.imageButton2);
		imgBtns[2] = (ImageButton) findViewById(R.id.imageButton3);
		imgBtns[3] = (ImageButton) findViewById(R.id.imageButton4);
		imgBtns[4] = (ImageButton) findViewById(R.id.imageButton5);
		imgBtns[5] = (ImageButton) findViewById(R.id.imageButton6);

		// �������̣߳���ʱ�������������ʼ��ť������
		childThread = new Thread(new Runnable() {
			public void run() {
				int times = 0;//��Ϸ������ʱ��
				while (times < timeOut) {//�ж��Ƿ񳬳��û��趨��ʱ�䣬��Ĭ��ʱ�䣨60s��
					
					index = new Random().nextInt(6);//��������ĵض�����
					
					//���߳��еĴ��������߳���ִ��,ʵ�����ݴ���ͻ�����ʾ�����ڲ�ͬ���߳���ִ��
					//Android��Ҫ�󣬻�����ʾ�����߳���ִ�У����ݴ��������߳��в�����Ϊ�˻�ø��õ��û�����
					runOnUiThread(new Runnable() {
						public void run() {
							//����Ҫ��ʵ�ĵض��⣬�����Ķ���ʾ�հף��Դﵽ������ʧ��Ч��
							for (int i = 0; i < imgBtns.length; i++) {
								if (index != i) {
									imgBtns[i]
											.setImageResource(R.drawable.kongbai);
									//ΪButton���ñ�ǩ��Ϊ���б����İ�ť�Ƿ��е���
									//beijing��ǩ��û�е���
									//dishu��ǩ���е���
									imgBtns[i].setTag("beijing");
								}
							}
							imgBtns[index]
									.setImageResource(R.drawable.ic_launcher);
							imgBtns[index].setTag("dishu");
						}
					});

					//������ֵļ��ʱ��
					try {
						Thread.sleep(SHOW_TIMES);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//��Ϸ�������¼����Լ�ʣ����¼�����λms��
					times = times + SHOW_TIMES;
					temp_timeRemain = timeOut - times;
					
					//ת��ʣ��ʱ�䵥λ���������߳���Ϊʣ��ʱ��ؼ�����ʣ��ʱ��
					runOnUiThread(new Runnable() {
						public void run() {
							timeRemain = temp_timeRemain / 1000;//��λת��		ms--->s
//							if(0 == timeRemain){//ʣ��ʱ��Ϊ0�����������������Ϸ
//								startFlag = false;
//							}
							timeRemainTextView.setText(timeRemain + "");//����ʣ��ʱ��ؼ��е�ʣ��ʱ��
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
		//��ȡ����İ�ť�ؼ�����ȡ�ñ�ǩ
		ImageButton imgBtn = (ImageButton) view;
		String tag = (String) imgBtn.getTag();
		//�жϵ���İ�ť���Ƿ��е���
		//beijing��ǩ��û�е���
		//dishu��ǩ���е���
		if ("dishu".equals(tag)) {
			score++;
			imgBtn.setImageResource(R.drawable.kongbai);
			imgBtn.setTag("beijing");
		}
		System.out.println("" + score);
		scoreTextView.setText("" + score + "��");
	}

	public void startGame(View view) {
		// ��ȡ�û�������Ϸʱ�䣬����ʼ��Ϸ
		if(false == startFlag){
			String timeS = timeOutEditText.getText().toString();
			if(timeS.equals("")){
				timeOut = TIMEOUT_DEFAULT;
			}
			else{
				timeOut = Integer.parseInt(timeS) * 1000;//��----->����
			}
/*			int time = Integer.parseInt(timeS);
			if(0 != time){//�ж��û��Ƿ�����ʱ�䣬û�еĻ�Ĭ��Ϊ60s
				timeOut = time;
			}
			*/
			childThread.start();
			startFlag = true;
		}
	}

}
