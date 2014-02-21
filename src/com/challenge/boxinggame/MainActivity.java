package com.challenge.boxinggame;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private final int ON_THE_LEFT = 0;	
	private final int ON_THE_RIGHT = 1;
	private final int HEAD = 100;
	private final int HAND = 300;
	private final long MAX_SCORE = 30;
	
	boolean gameOver = false;
	
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

	private Animation animTranslateHeadLeft, animTranslateHeadRight, animTranslateHeadLeftPC, animTranslateHeadRightPC;
	private boolean headMoving = false;
	TextView scoreRight, scoreLeft;
	
	// Thread for playing music background
	private BackgroundSound mBackgroundSound = new BackgroundSound();
	
	// Score
	private long score=0, scorePC =0;

	// Action for computer player
	ArrayList<Integer> actions;
	boolean dodgeRight,dodgeLeft, dodgeRightPC,dodgeLeftPC;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		actions = new ArrayList<Integer>();
		actions.add(1);
		actions.add(2);
		actions.add(3);
		actions.add(4);
		
		dodgeLeft = false; dodgeRight = false;
		
		// play music
		mBackgroundSound.execute();

		animTranslateHeadLeft = AnimationUtils.loadAnimation(this,
				R.anim.anim_transalte_head_left);
		animTranslateHeadRight = AnimationUtils.loadAnimation(this,
				R.anim.anim_transalte_head_right);
		
		animTranslateHeadLeftPC = AnimationUtils.loadAnimation(this,
				R.anim.anim_transalte_head_left);
		animTranslateHeadRightPC = AnimationUtils.loadAnimation(this,
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
		scoreLeft = (TextView) findViewById(R.id.score_left);
		scoreRight = (TextView) findViewById(R.id.score_right);

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
				
				Thread tr = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(HAND);
							if(!dodgeLeftPC){
								score++;
								if(score==MAX_SCORE){
									gameover();
								}
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
            	tr.start();
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
				
				Thread tr = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(HAND);
							if(!dodgeRightPC){
								score++;
								if(scorePC==MAX_SCORE){
									gameover();
								}
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
            	tr.start();
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
						scoreRight.setText(""+score);
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
						scoreRight.setText(""+score);
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
						scoreLeft.setText(""+scorePC);
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
						scoreLeft.setText(""+scorePC);
					}
				});

		// Sensor stuff
		mInitialized = false;
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);

		animTranslateHeadLeftPC.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = true;
				dodgeLeftPC = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = false;
				dodgeLeftPC = false;
			}
		});

		animTranslateHeadRightPC.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = true;
				dodgeRightPC = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = false;
				dodgeRightPC = false;
			}
		});
		
		animTranslateHeadLeft.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = true;
				dodgeLeft = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = false;
				dodgeLeft = false;
			}
		});

		animTranslateHeadRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = true;
				dodgeRight = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				headMoving = false;
				dodgeRight = false;
			}
		});
		
		Thread referee = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		referee.start();
		
		
		// Create Inner Thread Class
        Thread background = new Thread(new Runnable() {
             
            // After call for background.start this run method call
            public void run() {
                try {

                	while(!gameOver){
                		Thread.sleep(1000);
                		int action = (int) (Math.random() * ( 3 - 0 ));
                		threadMsg(actions.get(action)+"");
                	}

                } catch (Throwable t) {
                    // just end the background thread
                    Log.i("Animation", "Thread  exception " + t);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {
                     
                    String aResponse = msg.getData().getString("message");

                    if ((null != aResponse)) {

                        if(aResponse.equals("1")){
                        	leftSideRightHand
							.startAnimation(animTranslateLeftSideRightHand);
                        	Thread tr = new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										Thread.sleep(HAND);
										if(!dodgeRight){
											scorePC++;
											if(scorePC==MAX_SCORE){
												gameover();
											}
											
										}
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
                        	tr.start();
                        	
                        }else if(aResponse.equals("2")){
                        	leftSideLeftHand
							.startAnimation(animTranslateLeftSideLeftHand);
                        	Thread tr = new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										Thread.sleep(HAND);
										if(!dodgeLeft){
											scorePC++;
											if(scorePC==MAX_SCORE){
												gameover();
											}
										}
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
                        	tr.start();
                        }else if(aResponse.equals("3")){
                        	headLeft.startAnimation(animTranslateHeadLeft);
                        }else if(aResponse.equals("4")){
                        	headLeft.startAnimation(animTranslateHeadRight);
                        }
                    }
                    else
                    {

                            // ALERT MESSAGE
                            Toast.makeText(
                                    getBaseContext(),
                                    "Not Got Response From Server.",
                                    Toast.LENGTH_SHORT).show();
                    }   

                }
            };

        });
        // Start Thread
        background.start();  //After call start method thread called run Method
		
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

	public void gameover(){
		
		gameOver = true;
		Message msgObj = handler.obtainMessage();
        Bundle b = new Bundle();
        b.putString("message", "abc");
        msgObj.setData(b);
        handler.sendMessage(msgObj);
		
		//scoreLeft.setText("0");
		//scoreRight.setText("0");
		
		
	}
	
	private final Handler handler = new Handler() {
		 
        public void handleMessage(Message msg) {
             
            String aResponse = msg.getData().getString("message");

            final Dialog dialog = new Dialog(MainActivity.this);
    		dialog.setContentView(R.layout.dialog_layout);
    		dialog.setTitle("GAME OVER");

    		// set the custom dialog components - text, image and
    		// button
    		TextView text = (TextView) dialog
    				.findViewById(R.id.text);
    		if(score==MAX_SCORE){
    			text.setText("You WON!");
    		}else{
    			text.setText("You LOST!");
    		}
    		ImageView image = (ImageView) dialog
    				.findViewById(R.id.image);
    		image.setVisibility(View.GONE);
    		Button dialogButton = (Button) dialog
    				.findViewById(R.id.dialogButtonOK);
    		// if button is clicked, close the custom dialog
    		dialogButton.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				dialog.dismiss();
    			}
    		});

    		dialog.show();
    		
    		score = 0;
    		scorePC = 0;

        }
    };

}
