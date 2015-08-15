package ru.masomi.holy21;

import ru.masomi.holy21.datamodel.DataModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int REQUEST_BOOK = 1;
	private DataModel dm;
	private TextView bookName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		dm = ((HoLyApplication) getApplication()).getDataModel();

		Button searchButton = (Button) findViewById(R.id.searchButton);
		bookName = (TextView) findViewById(R.id.searchStr);
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
				intent.putExtra("text", bookName.getText().toString());
				startActivity(intent);
			}
		});
		Button addBookButton = (Button) findViewById(R.id.buttonAddBook);
		addBookButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, BookActivity.class);
				//intent.putExtra("text", bookName.getText().toString());
				dm.startTransaction();
				startActivityForResult(intent, REQUEST_BOOK);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (REQUEST_BOOK): {
			if (resultCode == Activity.RESULT_OK) {
				dm.commitTransaction();
			} else {
				dm.rollbackTransaction();
			}
			break;
		}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
}
