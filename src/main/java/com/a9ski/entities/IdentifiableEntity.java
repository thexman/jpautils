package com.a9ski.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

import com.a9ski.id.Identifiable;

/**
 *
 * Abstract class for all entities with numeric Id
 *
 * @author Kiril Arabadzhiyski
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class IdentifiableEntity implements Identifiable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2152770674364422919L;

	@Id
	@SequenceGenerator(name = "sequenceId", sequenceName = "sequence_id", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceId")
	@Column(name = "id")
	private long id;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.a9ski.id.Identifiable#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.a9ski.id.Identifiable#setId(long)
	 */
	@Override
	public void setId(final long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof IdentifiableEntity)) {
			return false;
		}
		final IdentifiableEntity other = (IdentifiableEntity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
