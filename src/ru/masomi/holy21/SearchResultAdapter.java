package ru.masomi.holy21;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ru.masomi.holy21.datamodel.objects.Book;
import ru.masomi.holy21.datamodel.objects.BookImage;
import ru.masomi.holy21.datamodel.objects.DataFile;
import ru.masomi.holy21.datamodel.objects.Work;
import ru.masomi.holy21.datamodel.service.BookWrapper;
import ru.masomi.holy21.datamodel.service.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<Object> objects;
	private Context context;
	private Service service;

	private static class ViewHolder {
		String objectClassName;
	}

	private static class BookViewHolder extends ViewHolder {
		ImageView bookImage;
		TextView bookName;
		TextView place;
		Button takeOrReturnButton;
		Button readButton;
		LinearLayout works;
	}

	public SearchResultAdapter(Context context, Service service, List<Object> objects) {
		super();
		this.context = context;
		this.objects = objects;
		this.service = service;
		mLayoutInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private void loadImage(ImageView imageView, String fileName) {
		Bitmap bitmap = null;
		InputStream is;
		try {
			is = service.getDataModel().getFileInputStream(fileName);
			bitmap = BitmapFactory.decodeStream(is);
			// bitmap.recycle();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageView.setImageBitmap(bitmap);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		BookViewHolder holder;

		Object object = objects.get(position);
		if (object instanceof BookWrapper) {
			if (convertView == null
					|| !BookWrapper.class
							.getName()
							.equals(((ViewHolder) convertView.getTag()).objectClassName)) {
				convertView = mLayoutInflater.inflate(R.layout.book_list_item,
						null);
				holder = new BookViewHolder();
				holder.objectClassName = BookWrapper.class.getName();
				holder.bookImage = (ImageView) convertView
						.findViewById(R.id.bookImage);
				holder.bookName = (TextView) convertView
						.findViewById(R.id.bookName);
				holder.place = (TextView) convertView.findViewById(R.id.place);
				holder.works = (LinearLayout) convertView
						.findViewById(R.id.works);
				holder.takeOrReturnButton = (Button) convertView
						.findViewById(R.id.takeOrReturnButton);
				holder.readButton = (Button) convertView
						.findViewById(R.id.readButton);
				convertView.setTag(holder);
			} else {
				holder = (BookViewHolder) convertView.getTag();
			}

			final BookWrapper book = (BookWrapper) object;

			if (book.getBook().getImage() != null) {
				loadImage(holder.bookImage, service.getDataModel()
						.getFullFileName(book.getBook().getImage().getName()));
				holder.bookImage.setOnClickListener(null);
			} else {
				holder.bookImage.setImageResource(R.drawable.noimage);
				holder.bookImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Toast.makeText(context, "select image",
								Toast.LENGTH_SHORT).show();
						selectFile(book.getBook());
					}

				});
			}
			holder.bookName.setText(book.getBook().getName());
			holder.place.setText(book.getBook().getPlace().getName());
			holder.takeOrReturnButton
					.setText(book.getBook().isOnSite() ? "Выдать" : "Вернуть");
			holder.readButton.setVisibility(View.VISIBLE);

			holder.works.removeAllViews();
			for (Work work : book.getWorks()) {
				View workListItem = mLayoutInflater.inflate(
						R.layout.work_list_item, null);
				TextView author = (TextView) workListItem
						.findViewById(R.id.author);
				author.setText(work.getAuthor());
				TextView name = (TextView) workListItem
						.findViewById(R.id.workName);
				name.setText(work.getName());
				holder.works.addView(workListItem);
			}
		} else {
			convertView = mLayoutInflater.inflate(R.layout.empty_list_item,
					null);
		}
		return convertView;
	}

	private void selectFile(final Book book) {
		File mPath = new File(Environment.getExternalStorageDirectory()
				+ "//DIR//");
		FileDialog fileDialog = new FileDialog(context, mPath);
		fileDialog.setFileEndsWith(".*\\.png$|.*\\.jpg$");
		fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
			public void fileSelected(File file) {
				try {
					DataFile df = service.getDataModel().importDataFile(
							file.toString());
					BookImage rel = new BookImage(book.getId(), df.getId());
					rel.setDm(book.getDm());
					book.getDm().applyCommand(rel);
					notifyDataSetChanged();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		fileDialog.showDialog();
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
