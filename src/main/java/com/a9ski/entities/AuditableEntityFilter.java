package com.a9ski.entities;

import java.io.Serializable;
import java.util.Set;

import com.a9ski.utils.DateRange;
import com.a9ski.utils.Range;

/**
 * Filter for querying auditable entities. All derived entities must have filter which is subclass of this one.
 * 
 * @author Kiril Arabadzhiyski
 *
 */
public class AuditableEntityFilter implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5166295555143567362L;

	private Set<Long> ids;

	private Set<Long> creators;

	private Set<Long> editors;

	private DateRange created;

	private DateRange edited;

	private Range<Long> version;

	private Boolean deleted;

	private boolean distinct;

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

	/**
	 * Flag indicating that only distinct entities must be returned
	 * 
	 * @return flag indicating that only distinct entities must be returned
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * Sets flag indicating that only distinct entities must be returned
	 * 
	 * @param distinct
	 *            flag indicating that only distinct entities must be returned
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
}
