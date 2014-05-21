package bos.consoar.ninebynine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import bos.consoar.ninebynine.views.ActionBar;

public class MainActivity extends Activity{
	private RelativeLayout mStartRelativeLayout;
	private ImageView mHelp;
	private ActionBar mActionBar;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		setListener();
	}
	private void setListener() {
		// TODO Auto-generated method stub
		mStartRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, GameActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
		mHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, HelpActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
	}
	private void findViews() {
		// TODO Auto-generated method stub
		mStartRelativeLayout = (RelativeLayout) findViewById(R.id.main_start_RelativeLayout);
		mHelp = (ImageView) findViewById(R.id.main_help);
	}
}
