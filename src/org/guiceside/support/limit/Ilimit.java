package org.guiceside.support.limit;

import java.util.Collection;
import java.util.List;

public interface Ilimit<T extends Object> {
	public int getRows();

	public List<T> getResult();

	public void setFirstResult(int i);

	public void setMaxResults(int i);

	public void setParaValues(Collection<?> collection);

	public List<T> getResult(int begin, int max);
}
