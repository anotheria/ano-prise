package net.anotheria.anoprise.dataspace;

/**
 * DataspaceType used in DataspaceService.
 * 
 * @author abolbat
 */
public enum DataspaceType {

	/**
	 * This type used for storing banners statistic data.
	 */
	BANNER(1),

	/**
	 * This type used for storing tracking data.
	 */
	TRACKING(2);

	/**
	 * Dataspace type id.
	 */
	private int typeId;

	/**
	 * Default private constructor.
	 * 
	 * @param aTypeId
	 *            - a dataspace type id
	 */
	private DataspaceType(int aTypeId) {
		this.typeId = aTypeId;
	}

	public int getTypeId() {
		return typeId;
	}

	/**
	 * Get {@link DataspaceType} type by given id.
	 * 
	 * @param aTypeId
	 *            - id
	 * @return {@link DataspaceType} type or {@link RuntimeException} if type id is unknown
	 */
	public static DataspaceType getTypeById(int aTypeId) {
		for (DataspaceType obj : DataspaceType.values()) {
			if (obj.getTypeId() == aTypeId)
				return obj;
		}

		throw new RuntimeException("UNKNOWN DATASPACE TYPE ID: " + aTypeId);
	}

}
