package ru.masomi.holy21.datamodel.objects;

import java.util.Date;

import ru.masomi.holy21.datamodel.ModelObject;

public class Reading extends ModelObject {
	public static final String OBJECT_STORAGE_NAME = "reading"; 
	private Date takeDate;
	private Date returnDate;

	public Reading() {
	}
	
	public Reading(int id, Date takeDate, Date returnDate) {
		super();
		this.id = id;
		this.takeDate = takeDate;
		this.returnDate = returnDate;
	}

	public Date getTakeDate() {
		return takeDate;
	}

	public void setTakeDate(Date takeDate) {
		this.takeDate = takeDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	@Override
	public String toString() {
		return "Reading [takeDate=" + takeDate + ", returnDate=" + returnDate
				+ "]";
	}
}
