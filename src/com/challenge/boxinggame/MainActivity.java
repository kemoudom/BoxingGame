package com.challenge.boxinggame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private final int ON_THE_LEFT = 0;
	private final int ON_THE_RIGHT = 1;
	int side;

	Button buttonLeft, buttonRight, leftSideLeftHand, leftSideRightHand,
			rightSideLeftHand, rightSideRightHand;

	RelativeLayout container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		final Animation animTranslateLeftSideRightHand = AnimationUtils
				.loadAnimation(this, R.anim.anim_translate);
		final Animation animTranslateRightSideRightHand = AnimationUtils
				.loadAnimation(this, R.anim.right_anim_translate);
		final Animation animTranslateLeftSideLeftHand = AnimationUtils
				.loadAnimation(this, R.anim.anim_translate);
		final Animation animTranslateRightSideLeftHand = AnimationUtils
				.loadAnimation(this, R.anim.right_anim_translate);

		container = (RelativeLayout) findViewById(R.id.container);

		side = ON_THE_LEFT; // by default, for the HOST of the game

		buttonLeft = (Button) findViewById(R.id.left_punch);
		buttonRight = (Button) findViewById(R.id.right_punch);
		leftSideLeftHand = (Button) findViewById(R.id.left_left);
		leftSideRightHand = (Button) findViewById(R.id.left_right);
		rightSideLeftHand = (Button) findViewById(R.id.right_left);
		rightSideRightHand = (Button) findViewById(R.id.right_right);

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
