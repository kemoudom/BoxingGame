package com.challenge.boxinggame;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements SensorEventListener {

	

	private final int ON_THE_LEFT = 0;
	private final int ON_THE_RIGHT = 1;
	int side;
	Button buttonLeft, buttonRight, leftSideLeftHand, leftSideRightHand,
			rightSideLeftHand, rightSideRightHand;
	RelativeLayout container;
	ImageView headLeft, headRight;

	// sensor stuff
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private final float NOISE = (float) 2.0;

	private Animation animTranslateHeadLeft, animTranslateHeadRight;
	private boolean headMoving = false;
	
	// Thread for playing music background
	private BackgroundSound mBackgroundSound = new BackgroundSound();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		// play music
		mBackgroundSound.execute();

		animTranslateHeadLeft = AnimationUtils.loadAnimation(this,
				R.anim.anim_transalte_head_left);
		animTranslateHeadRight = AnimationUtils.loadAnimation(this,
				R.anim.anim_transalte_head_right);

		final Animation animTranslateLeftSideRightHand = AnimationUtils
				.loadAnimation(this, R.anim.anim_translate);
		final Animation animTranslateRightSideRightHand = AnimationUtils
				.loadAnimation(this, R.anim.right_anim_translate);
		final Animation animTranslateLeftSideLeftHand = AnimationUtils
				.loadAnimation(this, R.anim.anim_translate);
		final Animation animTranslateRightSideLeftHand = AnimationUtils
				.loadAnimation(this, R.anim.right_anim_translate);

		container = (RelativeLayout) findViewById(R.id.container);

		side = ON_THE_RIGHT; // by default, for the HOST of the game

		buttonLeft = (Button) findViewById(R.id.left_punch);
		buttonRight = (Button) findViewById(R.id.right_punch);
		leftSideLeftHand = (Button) findViewById(R.id.left_left);
		leftSideRightHand = (Button) findViewById(R.id.left_right);
		rightSideLeftHand = (Button) findViewById(R.id.right_left);
		rightSideRightHand = (Button) findViewById(R.id.right_right);
		headLeft = (ImageView) findViewById(R.id.head_left);
		headRight = (ImageView) findViewById(R.id.head_right);

		buttonLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (side == ON_THE_LEFT) {
					leftSideLeftHand
							.startAnimation(animTranslateLeftSideLeftHand);
				} else {
					rightSideLeftHand
							.startAnimation(animTranslateRightSideLeftHand);
				}
			}
		});

		buttonRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (side == ON_THE_LEFT) {
					leftSideRightHand
							.startAnimation(animTranslateLeftSideRightHand);
				} else {
					rightSideRightHand
							.startAnimation(animTranslateRightSideRightHand);
				}
			}
		});

		leftSideLeftHand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		leftSideRightHand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		rightSideLeftHand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		rightSideRightHand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		animTranslateRightSideLeftHand
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						buttonLeft.setEnabled(false);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						buttonLeft.setEnabled(true);
					}
				});

		animTranslateRightSideRightHand
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						buttonRight.setEnabled(false);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						buttonRight.setEnabled(true);
					}
				});

		animTranslateLeftSideLeftHand
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						buttonLeft.setEnabled(false);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						buttonLeft.setEnabled(true);
					}
				});

		animTranslateLeftSideRightHand
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						buttonRight.setEnabled(false);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						buttonRight.setEnabled(true);
					}
				});

		// Sensor stuff
		mInitialized = false;
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);

		animTranslateHeadLeft.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = false;
			}
		});

		animTranslateHeadRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBackgroundSound!=null){
			if(mBackgroundSound.player!=null){
				mBackgroundSound.player.stop();
				mBackgroundSound.player.release();
			}
			mBackgroundSound.cancel(true);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} else {
			float deltaX = (mLastX - x);
			float deltaY = (mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE)
				deltaX = (float) 0.0;
			if (deltaY < NOISE)
				deltaY = (float) 0.0;
			if (deltaZ < NOISE)
				deltaZ = (float) 0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			if (mLastY < -3) {
				Log.d("sensor", "=====LEFT====");
				if (!headMoving) {

					if (side == ON_THE_LEFT) {
						headLeft.startAnimation(animTranslateHeadLeft);
					} else {
						headRight.startAnimation(animTranslateHeadLeft);
					}
				}
			} else if ((mLastY) > 3) {
				Log.d("sensor", "=====RIGHT====");
				if (!headMoving) {
					if (side == ON_THE_LEFT) {
						headLeft.startAnimation(animTranslateHeadRight);
					} else {
						headRight.startAnimation(animTranslateHeadRight);
					}
				}

			}
		}
	}

	public class BackgroundSound extends AsyncTask<Void, Void, Void> {
		MediaPlayer player;
	    
		@Override
	    protected Void doInBackground(Void... params) {
	        player = MediaPlayer.create(MainActivity.this, R.raw.background); 
	        player.setLooping(true); // Set looping 
	        player.setVolume(100,100);
	        player.start();		    
	        return null;
	    }
	}
}
