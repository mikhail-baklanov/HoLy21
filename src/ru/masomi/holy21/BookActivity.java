package ru.masomi.holy21;

import java.util.ArrayList;
import java.util.List;

import ru.masomi.holy21.datamodel.DataModel;
import ru.masomi.holy21.datamodel.objects.Book;
import ru.masomi.holy21.datamodel.objects.BookUpdate;
import ru.masomi.holy21.datamodel.objects.BookWork;
import ru.masomi.holy21.datamodel.objects.BookWorkDelete;
import ru.masomi.holy21.datamodel.objects.Place;
import ru.masomi.holy21.datamodel.objects.Work;
import ru.masomi.holy21.datamodel.service.Service;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class BookActivity extends Activity {
	protected static final int REQUEST_WORK = 1;
	private static final String BOOK_ID_FIELD = "bookId";
	private LayoutInflater mLayoutInflater;
	private LinearLayout worksLayout;
	private Book book;
	private Spinner placeSpinner;
	private EditText bookNameEdit;
	private DataModel dm;
	private Service service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);

		dm = ((HoLyApplication) getApplication()).getDataModel();
		service = ((HoLyApplication) getApplication()).getService();

		Intent intent = getIntent();

		int bookId = 0;
		if (savedInstanceState != null) {
			bookId = savedInstanceState.getInt(BOOK_ID_FIELD);
			if (bookId == 0)
				bookId = intent.getIntExtra(BOOK_ID_FIELD, 0);

		}
		if (bookId == 0) {
			book = new Book();
			dm.applyCommand(book);
		} else {
			book = (Book) (dm.getRegisterModelObject(Book.class, bookId));
		}
		
		mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		worksLayout = (LinearLayout) findViewById(R.id.works);
		placeSpinner = (Spinner) findViewById(R.id.placeSpinner);
		bookNameEdit = (EditText) findViewById(R.id.bookNameEdit);
		Button addWorkButton = (Button) findViewById(R.id.addWorkButton);
		Button saveButton = (Button) findViewById(R.id.saveButton);

		outputAllWorks();

		String[] data = getPlacesArray();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		placeSpinner.setAdapter(adapter);
		// заголовок
		placeSpinner.setPrompt("Место хранения");
		placeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// показываем позицию нажатого элемента
				// Toast.makeText(getBaseContext(), "Position = " + position,
				// Toast.LENGTH_SHORT).show();
			}
		});

		addWorkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent workIntent = new Intent(BookActivity.this,
						WorkActivity.class);
				startActivityForResult(workIntent, REQUEST_WORK);
			}
		});
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(BOOK_ID_FIELD, book.getId());
	}

	private String[] getPlacesArray() {
		String[] data;
		List<String> d = new ArrayList<String>();
		List<Place> places = service.getPlaces();
		for (Place p : places) {
			d.add(p.getName());
		}
		data = d.toArray(new String[0]);
		return data;
	}

	public void outputAllWorks() {
		for (Work work : book.getWorks()) {
			outputWork(work);
		}
	}

	private void outputWork(final Work work) {
		String authorStr = work.getAuthor();
		String workNameStr = work.getName();
		final View workListItem = mLayoutInflater.inflate(
				R.layout.book_work_item, null);
		TextView author = (TextView) workListItem.findViewById(R.id.author);
		author.setText(authorStr);
		TextView workName = (TextView) workListItem.findViewById(R.id.workName);
		workName.setText(workNameStr);
		Button deleteButton = (Button) workListItem
				.findViewById(R.id.deleteButton);
		Button editButton = (Button) workListItem.findViewById(R.id.editButton);
		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editWork(workListItem, work);
			}
		});
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(BookActivity.this)
						.setTitle("Удаление произведения")
						.setMessage("Честно-честно удалить?")
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int whichButton) {
										deleteWork(workListItem, work);
									}
								}).setNegativeButton(android.R.string.no, null)
						.show();

			}
		});
		worksLayout.addView(workListItem);
	}

	protected void deleteWork(View workListItem, Work work) {
		dm.applyCommand(new BookWorkDelete(book.getId(), work.getId()));
		((ViewManager) workListItem.getParent()).removeView(workListItem);
	}

	protected void editWork(View workListItem, Work work) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (REQUEST_WORK): {
			if (resultCode == Activity.RESULT_OK) {
				Work work = new Work(0,
						data.getStringExtra(WorkActivity.WORK_NAME_FIELD),
						data.getStringExtra(WorkActivity.AUTHOR_FIELD));
				dm.applyCommand(work);
				dm.applyCommand(new BookWork(book.getId(), work.getId()));
				outputWork(work);
			}
			break;
		}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_book, menu);
		return true;
	}

	private void updateBook(String bookName, Place place) {
		if (bookName.equals(book.getName()) || book.getPlace() != place) {
			BookUpdate bookUpdate = new BookUpdate(book.getId(), bookName,
					place, book.getImage());
			dm.applyCommand(bookUpdate);
		}
	}

	private void save() {
		String bookNameStr = bookNameEdit.getText().toString();

		Place place = book.getPlace(); // TODO заполнить введенным
										// значением

		updateBook(bookNameStr, place);

		Intent intent = new Intent();
		intent.putExtra(BOOK_ID_FIELD, book.getId());
		setResult(RESULT_OK, intent);
		finish();
	}

}
