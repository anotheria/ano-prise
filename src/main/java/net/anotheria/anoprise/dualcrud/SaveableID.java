package net.anotheria.anoprise.dualcrud;

import java.io.Serializable;

/**
 * @author Vlad Lukjanenko
 */
public class SaveableID implements Serializable {

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
	public SaveableID() {

	}

	/**
	 * Constructor.
	 * */
	public SaveableID(String ownerId, String saveableId) {
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

		SaveableID that = (SaveableID) o;

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
		return "SaveableID{" +
				"ownerId='" + ownerId + '\'' +
				", saveableId='" + saveableId + '\'' +
				'}';
	}
}
