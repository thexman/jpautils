package com.a9ski.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.a9ski.id.Auditable;
import com.a9ski.id.Deletable;
import com.a9ski.id.Identifiable;
import com.a9ski.id.Versioned;

/**
 * 
 * Abstract class for all entities, contains creation date, ID of the user who created the object, date of last edit, ID of the user performing last modification, soft deletion flag. The entity uses optimistic locking.
 * 
 * @author Kiril Arabadzhiyski
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AuditableEntity extends IdentifiableEntity implements Auditable, Identifiable, Deletable, Versioned {

	/**
	 *
	 */
	private static final long serialVersionUID = 7099174489796643017L;

	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name = "edited")
	@Temporal(TemporalType.TIMESTAMP)
	private Date edited;

	@Column(name = "creator")
	private Long creator;

	@Column(name = "editor")
	private Long editor;

	@Column(name = "version")
	@Version
	private long version;

	@Column(name = "deleted")
	private boolean deleted;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#getCreated()
	 */
	@Override
	public Date getCreated() {
		return created;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#setCreated(java.util.Date)
	 */
	@Override
	public void setCreated(final Date created) {
		this.created = created;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#getEdited()
	 */
	@Override
	public Date getEdited() {
		return edited;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#setEdited(java.util.Date)
	 */
	@Override
	public void setEdited(final Date edited) {
		this.edited = edited;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#getCreator()
	 */
	@Override
	public Long getCreator() {
		return creator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#setCreator(java.lang.Long)
	 */
	@Override
	public void setCreator(final Long creator) {
		this.creator = creator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#getEditor()
	 */
	@Override
	public Long getEditor() {
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Auditable#setEditor(java.lang.Long)
	 */
	@Override
	public void setEditor(final Long editor) {
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Versioned#getVersion()
	 */
	@Override
	public long getVersion() {
		return version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Versioned#setVersion(long)
	 */
	@Override
	public void setVersion(final long version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Deletable#isDeleted()
	 */
	@Override
	public boolean isDeleted() {
		return deleted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.a9ski.id.Deletable#setDeleted(boolean)
	 */
	@Override
	public void setDeleted(final boolean deleted) {
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
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((edited == null) ? 0 : edited.hashCode());
		result = prime * result + ((editor == null) ? 0 : editor.hashCode());
		result = prime * result + (int) (version ^ (version >>> 32));
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
		if (!(obj instanceof AuditableEntity)) {
			return false;
		}
		final AuditableEntity other = (AuditableEntity) obj;
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (creator == null) {
			if (other.creator != null) {
				return false;
			}
		} else if (!creator.equals(other.creator)) {
			return false;
		}
		if (deleted != other.deleted) {
			return false;
		}
		if (edited == null) {
			if (other.edited != null) {
				return false;
			}
		} else if (!edited.equals(other.edited)) {
			return false;
		}
		if (editor == null) {
			if (other.editor != null) {
				return false;
			}
		} else if (!editor.equals(other.editor)) {
			return false;
		}
		if (version != other.version) {
			return false;
		}
		return true;
	}
}
