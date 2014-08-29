package net.anotheria.anoprise.dualcrud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryResult<T extends CrudSaveable> implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 4138579115416433003L;

	private List<T> primaryResult;

	private List<T> secondaryResult;

	public QueryResult(List<T> primary, List<T> secondary) {
		if (primary != null)
			this.primaryResult = primary;
		else
			this.primaryResult = new ArrayList<T>();

		if (secondary != null)
			this.secondaryResult = secondary;
		else
			this.secondaryResult = new ArrayList<T>();
	}

	public List<T> getResult(boolean merge) {
		if (!merge)
			return getResultPrimary();

		return merge();

	}

	public List<T> getResultPrimary() {
		return new ArrayList<T>(primaryResult);
	}

	public List<T> getResultSecondary() {
		return new ArrayList<T>(secondaryResult);
	}

	private List<T> merge() {
		Set<T> result = new HashSet<T>(primaryResult);
		for (T o : secondaryResult)
			if (!result.contains(o))
				result.add(o);

		return new ArrayList<T>(result);
	}

}
