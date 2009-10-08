/* ------------------------------------------------------------------------- *
$Source$
$Author$
$Date$
$Revision$


Copyright 2004-2005 by FriendScout24 GmbH, Munich, Germany.
All rights reserved.

This software is the confidential and proprietary information
of FriendScout24 GmbH. ("Confidential Information").  You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with FriendScout24 GmbH.
See www.friendscout24.de for details.
** ------------------------------------------------------------------------- */
package net.anotheria.anoprise.cache;

public class CachedObjectWrapper<T> {
	private T obj;
	private long timestamp;
	
	public CachedObjectWrapper(T anObject){
		obj = anObject;
		timestamp = System.currentTimeMillis();
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T anObj) {
		obj = anObj;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}

/* ------------------------------------------------------------------------- *
 * $Log$
 * Revision 1.2  2006-11-15 13:00:49  lrosenberg
 * *** empty log message ***
 *
 * Revision 1.1  2006/02/07 14:53:03  lrosenberg
 * *** empty log message ***
 *
 */