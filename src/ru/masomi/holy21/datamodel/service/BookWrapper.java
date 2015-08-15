package ru.masomi.holy21.datamodel.service;

import java.util.ArrayList;
import java.util.List;

import ru.masomi.holy21.datamodel.objects.Book;
import ru.masomi.holy21.datamodel.objects.Work;

public class BookWrapper {
	private Book book;
	private List<Work> works = new ArrayList<Work>();

	public BookWrapper(Book book) {
		super();
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public List<Work> getWorks() {
		return works;
	}

}
