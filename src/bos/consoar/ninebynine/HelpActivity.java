package bos.consoar.ninebynine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import bos.consoar.ninebynine.views.ActionBar;
import bos.consoar.ninebynine.views.ActionBar.Action;

public class HelpActivity extends Activity {
	private ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		mActionBar = (ActionBar) findViewById(R.id.actionbar);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeAction(new Action() {

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.ic_launcher;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
