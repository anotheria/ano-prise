package net.anotheria.anoprise.fs;

import java.io.Serializable;

/**
 * Interface of the file system service.
 * 
 * @author Vlad Lukjanenko
 */
public class FSSaveableID implements Serializable {

	/**
	 * Path name.
	 * */
	private String ownerId;

	/**
	 * File name.
	 * */
	private String saveableId;


	/**
	 * Default constructor.
	 * */
	public FSSaveableID() {

	}

	/**
	 * Constructor.
	 * */
	public FSSaveableID(String ownerId, String saveableId) {
		this.ownerId = ownerId;
		this.saveableId = saveableId;
	}


	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getSaveableId() {
		return saveableId;
	}

	public void setSaveableId(String saveableId) {
		this.saveableId = saveableId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FSSaveableID that = (FSSaveableID) o;

		if (ownerId != null ? !ownerId.equals(that.ownerId) : that.ownerId != null) return false;
		return saveableId != null ? saveableId.equals(that.saveableId) : that.saveableId == null;

	}

	@Override
	public int hashCode() {
		int result = ownerId != null ? ownerId.hashCode() : 0;
		result = 31 * result + (saveableId != null ? saveableId.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "FSSaveableID{" +
				"ownerId='" + ownerId + '\'' +
				", saveableId='" + saveableId + '\'' +
				'}';
	}
}
