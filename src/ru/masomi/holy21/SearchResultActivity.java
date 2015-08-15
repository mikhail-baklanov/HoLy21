package ru.masomi.holy21;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.masomi.holy21.datamodel.DataModel.SearchCriteria;
import ru.masomi.holy21.datamodel.DataModel.SearchObject;
import ru.masomi.holy21.datamodel.service.Service;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SearchResultActivity extends ListActivity {
	private EditText searchEditText;
	private CheckBox cbBook;
	private CheckBox cbWork;
	private CheckBox cbAuthor;
	private CheckBox cbPlace;
	private List<Object> foundObjects;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		searchEditText = (EditText) findViewById(R.id.searchStr);

		cbBook = (CheckBox) findViewById(R.id.cbBookName);
		cbWork = (CheckBox) findViewById(R.id.cbWorkName);
		cbAuthor = (CheckBox) findViewById(R.id.cbAuthor);
		cbPlace = (CheckBox) findViewById(R.id.cbPlace);

		cbBook.setChecked(true);
		cbWork.setChecked(true);
		cbAuthor.setChecked(true);
		cbPlace.setChecked(true);

		searchEditText.setText(getIntent().getExtras().getString("text"));
		((Button) findViewById(R.id.search))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						search();
					}
				});
		search();
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						"Click item " + foundObjects.get(position), Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	private void search() {
		String searchStr = searchEditText.getText().toString();
		Set<SearchObject> searchObjects = new HashSet<SearchObject>();
		Set<SearchCriteria> searchCriteria = new HashSet<SearchCriteria>();
		searchObjects.add(SearchObject.BOOK);
		if (cbAuthor.isChecked())
			searchCriteria.add(SearchCriteria.AUTHOR);
		if (cbBook.isChecked())
			searchCriteria.add(SearchCriteria.BOOK_NAME);
		if (cbWork.isChecked())
			searchCriteria.add(SearchCriteria.WORK_NAME);
		if (cbPlace.isChecked())
			searchCriteria.add(SearchCriteria.PLACE);
		Service service = ((HoLyApplication) getApplication())
				.getService();
		foundObjects = service.findObjects(searchStr, searchObjects,
						searchCriteria);
		setListAdapter(new SearchResultAdapter(this, service, foundObjects));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_result, menu);
		return true;
	}

}
