package com.a9ski.entities.filters;

import java.util.Set;

import com.a9ski.utils.DateRange;
import com.a9ski.utils.Range;

/**
 * Filter for querying auditable entities. All derived entities must have filter which is subclass of this one.
 * 
 * @author Kiril Arabadzhiyski
 *
 */
public class AuditableEntityFilter extends IdentifiableEntityFilter {

	/**
	 *
	 */
	private static final long serialVersionUID = -5166295555143567362L;

	private Set<Long> creators;

	private Set<Long> editors;

	private DateRange created;

	private DateRange edited;

	private Range<Long> version;

	private Boolean deleted;

	/**
	 * Returns a set of user IDs used for filtering
	 * 
	 * @return a set of user IDs used for filtering
	 */
	public Set<Long> getCreators() {
		return creators;
	}

	/**
	 * Sets of user IDs used for filtering
	 * 
	 * @param creators
	 *            user IDs used for filtering
	 */
	public void setCreators(final Set<Long> creators) {
		this.creators = creators;
	}

	/**
	 * Returns a set of user IDs used for filtering
	 * 
	 * @return a set of user IDs used for filtering
	 */
	public Set<Long> getEditors() {
		return editors;
	}

	/**
	 * Sets of user IDs used for filtering
	 * 
	 * @param editors
	 *            user IDs used for filtering
	 */
	public void setEditors(final Set<Long> editors) {
		this.editors = editors;
	}

	/**
	 * Date range filter for entity creation date
	 * 
	 * @return date range filter for entity creation date
	 */
	public DateRange getCreated() {
		return created;
	}

	/**
	 * Sets date range filter for entity creation date
	 * 
	 * @param created
	 *            date range filter for entity creation date
	 */
	public void setCreated(final DateRange created) {
		this.created = created;
	}

	/**
	 * Date range filter for entity last modification date
	 * 
	 * @return date range filter for entity last modification date
	 */
	public DateRange getEdited() {
		return edited;
	}

	/**
	 * Sets date range filter for entity last modification date
	 * 
	 * @param edited
	 *            date range filter for entity last modification date
	 */
	public void setEdited(final DateRange edited) {
		this.edited = edited;
	}

	/**
	 * Numeric range filter for entity version
	 * 
	 * @return Numeric range filter for entity version
	 */
	public Range<Long> getVersion() {
		return version;
	}

	/**
	 * Sets numeric range filter for entity version
	 * 
	 * @param version
	 *            numeric range filter for entity version
	 */
	public void setVersion(final Range<Long> version) {
		this.version = version;
	}

	/**
	 * Soft delete filter
	 * 
	 * @return soft delete filter
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * Sets soft delete filter
	 * 
	 * @param deleted
	 *            soft delete filter
	 */
	public void setDeleted(final Boolean deleted) {
		this.deleted = deleted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((creators == null) ? 0 : creators.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + ((edited == null) ? 0 : edited.hashCode());
		result = prime * result + ((editors == null) ? 0 : editors.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof AuditableEntityFilter)) {
			return false;
		}
		AuditableEntityFilter other = (AuditableEntityFilter) obj;
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (creators == null) {
			if (other.creators != null) {
				return false;
			}
		} else if (!creators.equals(other.creators)) {
			return false;
		}
		if (deleted == null) {
			if (other.deleted != null) {
				return false;
			}
		} else if (!deleted.equals(other.deleted)) {
			return false;
		}
		if (edited == null) {
			if (other.edited != null) {
				return false;
			}
		} else if (!edited.equals(other.edited)) {
			return false;
		}
		if (editors == null) {
			if (other.editors != null) {
				return false;
			}
		} else if (!editors.equals(other.editors)) {
			return false;
		}
		if (version == null) {
			if (other.version != null) {
				return false;
			}
		} else if (!version.equals(other.version)) {
			return false;
		}
		return true;
	}
}
