package ru.masomi.holy21.datamodel.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.SparseArray;
import ru.masomi.holy21.datamodel.DataModel;
import ru.masomi.holy21.datamodel.ModelCommand;
import ru.masomi.holy21.datamodel.DataModel.SearchCriteria;
import ru.masomi.holy21.datamodel.DataModel.SearchObject;
import ru.masomi.holy21.datamodel.objects.Book;
import ru.masomi.holy21.datamodel.objects.Place;
import ru.masomi.holy21.datamodel.objects.Work;

public class Service {
	private DataModel dataModel;

	public Service(DataModel dataModel) {
		super();
		this.dataModel = dataModel;
	}

	public List<Place> getPlaces() {
		List<Place> result = new ArrayList<Place>();
		SparseArray<ModelCommand> placesMap = dataModel
				.getRegisteredObjects().get(Place.class);
		for (int i = 0; i < placesMap.size(); i++) {
			Place place = (Place) placesMap.valueAt(i);
			result.add(place);
		}
		Collections.sort(result, new Comparator<Place>() {

			@Override
			public int compare(Place lhs, Place rhs) {
				String name1 = lhs.getName();
				String name2 = rhs.getName();
				Pattern p = Pattern.compile("(.*?)(\\d+)");
				Matcher m1 = p.matcher(name1);
				Matcher m2 = p.matcher(name2);
				if (m1.matches() && m2.matches()) {
					String s1 = m1.group(1);
					String n1 = m1.group(2);
					String s2 = m2.group(1);
					String n2 = m2.group(2);
					if (s1.equals(s2)) {
						return Integer.parseInt(n1) - Integer.parseInt(n2);
					}
				}
				return lhs.getName()
						.compareTo(rhs.getName());
			}
		});
		return result;
	}
	
	public List<Object> findObjects(String searchStr,
			Set<SearchObject> searchObjects, Set<SearchCriteria> searchCriteria) {
		List<Object> result = new ArrayList<Object>();
		List<BookWrapper> bookResult = new ArrayList<BookWrapper>();
		if (searchObjects.contains(SearchObject.BOOK)) {
			boolean searchAuthor = searchCriteria
					.contains(SearchCriteria.AUTHOR);
			boolean searchBook = searchCriteria
					.contains(SearchCriteria.BOOK_NAME);
			boolean searchWork = searchCriteria
					.contains(SearchCriteria.WORK_NAME);
			boolean searchPlace = searchCriteria.contains(SearchCriteria.PLACE);

			SparseArray<ModelCommand> booksMap = dataModel
					.getRegisteredObjects().get(Book.class);
			if (booksMap != null) {

				for (int i = 0; i < booksMap.size(); i++) {
					Book b = (Book) booksMap.valueAt(i);
					boolean bookIsFound = false;
					BookWrapper book = new BookWrapper(b);
					if ((searchBook && inStr(b.getName(), searchStr))
							|| (searchPlace && inStr(b.getPlace().getName(),
									searchStr))) {
						bookIsFound = true;
					}
					for (Work w : b.getWorks()) {
						if ((searchAuthor && inStr(w.getAuthor(), searchStr))
								|| (searchWork && inStr(w.getName(), searchStr))) {
							bookIsFound = true;
							book.getWorks().add(w);
						}
					}
					if (bookIsFound)
						bookResult.add(book);

				}
			}
			Collections.sort(bookResult, new Comparator<BookWrapper>() {

				@Override
				public int compare(BookWrapper lhs, BookWrapper rhs) {
					return lhs.getBook().getName()
							.compareTo(rhs.getBook().getName());
				}
			});
			result.addAll(bookResult);
		}
		return result;

	}

	private boolean inStr(String s, String substring) {
		if (s == null || substring == null)
			return false;
		return s.toUpperCase().indexOf(substring.toUpperCase()) >= 0;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

}
