package com.a9ski.entities.filters;

import java.util.Set;

/**
 * Filter for querying identifiable entities. All derived entities must have filter which is subclass of this one.
 * 
 * @author Kiril Arabadzhiyski
 *
 */
public class IdentifiableEntityFilter extends PageableFilter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1126544827416924150L;

	private Set<Long> ids;

	/**
	 * Returns a set of entity IDs to be filtered
	 * 
	 * @return the entity ids to be filtered
	 */
	public Set<Long> getIds() {
		return ids;
	}

	/**
	 * Sets entity IDs to be filtered
	 * 
	 * @param ids
	 *            entity IDs to be filtered
	 */
	public void setIds(final Set<Long> ids) {
		this.ids = ids;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ids == null) ? 0 : ids.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifiableEntityFilter other = (IdentifiableEntityFilter) obj;
		if (ids == null) {
			if (other.ids != null)
				return false;
		} else if (!ids.equals(other.ids))
			return false;
		return true;
	}

}
