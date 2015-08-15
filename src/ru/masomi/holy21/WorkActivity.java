package ru.masomi.holy21;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WorkActivity extends Activity {

	public static final String AUTHOR_FIELD = "author";
	public static final String WORK_NAME_FIELD = "workName";
	private Button saveButton;
	private EditText authorEdit;
	private EditText workNameEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work);
		saveButton = (Button) findViewById(R.id.saveButton);
		authorEdit = (EditText) findViewById(R.id.authorEdit);
		workNameEdit = (EditText) findViewById(R.id.workNameEdit);
		
		setSaveButtonState();
		TextWatcher textListener = new TextWatcher() {

	        public void afterTextChanged(Editable s) {
				setSaveButtonState();
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
		};
		authorEdit.addTextChangedListener(textListener);
		workNameEdit.addTextChangedListener(textListener);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				resultIntent.putExtra(AUTHOR_FIELD, authorEdit.getText()
						.toString());
				resultIntent.putExtra(WORK_NAME_FIELD, workNameEdit.getText()
						.toString());
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});
	}

	private void setSaveButtonState() {
		saveButton
				.setEnabled(authorEdit.getText().toString().trim()
						.length() > 0
						&& workNameEdit.getText().toString().trim()
								.length() > 0);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_work, menu);
		return true;
	}

}
