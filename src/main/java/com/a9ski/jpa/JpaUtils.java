package com.a9ski.jpa;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.ejb.ObjectNotFoundException;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.transaction.TransactionSynchronizationRegistry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a9ski.entities.AuditableEntity;
import com.a9ski.entities.AuditableEntity_;
import com.a9ski.entities.IdentifiableEntity;
import com.a9ski.entities.IdentifiableEntity_;
import com.a9ski.entities.filters.AuditableEntityFilter;
import com.a9ski.entities.filters.IdentifiableEntityFilter;
import com.a9ski.exceptions.ObjectAlreadyModifiedException;
import com.a9ski.id.Identifiable;
import com.a9ski.utils.ExtCollectionUtils;
import com.a9ski.utils.NullUtils;

/**
 * Utilities for dealing with JPA entities and queries
 *
 * @author Kiril Arabadzhiyski
 *
 */
public class JpaUtils {

	public static final Logger LOGGER = LoggerFactory.getLogger(JpaUtils.class);

	private final Supplier<EntityManager> entityManagerSupplier;

	/**
	 * Creates a new Utility class that uses entity manager supplied by <tt>entityManagerSupplier</tt>
	 *
	 * @param entityManager
	 *            the entity manager
	 */
	public JpaUtils(final EntityManager entityManager) {
		this(() -> entityManager);
	}

	/**
	 * Creates a new Utility class that uses entity manager supplied by <tt>entityManagerSupplier</tt>
	 *
	 * @param entityManagerSupplier
	 *            the entity manager supplier
	 */
	public JpaUtils(final Supplier<EntityManager> entityManagerSupplier) {
		super();
		NullUtils.checkNotNull(entityManagerSupplier, "Invalid entity manager supplier");
		this.entityManagerSupplier = entityManagerSupplier;
	}

	private EntityManager em() {
		final EntityManager em = entityManagerSupplier.get();
		if (em == null) {
			throw new IllegalStateException("EntityManager is null");
		}
		return em;
	}

	/**
	 * Adds standard predicates for {@link
	 *
	 * @param criteriaApiObjects
	 *            the criteria api objects
	 * @param filter
	 *            auditable entity filter
	 * @param locale
	 *            the locale used for string comparison
	 *
	 * @return a new criteria builder helper
	 */
	public <E extends AuditableEntity, F extends AuditableEntityFilter> CriteriaBuilderHelper addAuditableEntityPredicates(final CriteriaApiObjects<E> criteriaApiObjects, final F filter) {
		return addAuditableEntityPredicates(criteriaApiObjects.getCriteriaBuilder(), criteriaApiObjects.getPath(), filter);
	}

	/**
	 * Adds standard predicates for {@link
	 *
	 * @param cb
	 *            the criteria builder object
	 * @param path
	 *            the JPA entity path
	 * @param filter
	 *            auditable entity filter AuditableEntity> based on provided <tt>filter</tt>
	 * @param locale
	 *            the locale used for string comparison
	 *
	 * @return a criteria builder helper
	 */
	public CriteriaBuilderHelper addAuditableEntityPredicates(final CriteriaBuilder cb, final Path<? extends AuditableEntity> path, final AuditableEntityFilter filter) {
		final CriteriaBuilderHelper cbh = new CriteriaBuilderHelper(cb, filter.getLocale());
		if (filter.getDeleted() != null) {
			cbh.equalBool(path.get(AuditableEntity_.deleted), filter.getDeleted());
		}

		cbh.in(path.get(IdentifiableEntity_.id), filter.getIds());

		cbh.in(path.get(AuditableEntity_.creator), filter.getCreators());

		cbh.in(path.get(AuditableEntity_.editor), filter.getEditors());

		cbh.between(path.get(AuditableEntity_.created), filter.getCreated());

		cbh.between(path.get(AuditableEntity_.edited), filter.getEdited());

		cbh.between(path.get(AuditableEntity_.version), filter.getVersion());

		return cbh;
	}

	/**
	 * Creates a new entity of given class
	 *
	 * @param entityClass
	 *            the entity class
	 * @return a new entity of given class
	 */
	public <T extends Identifiable> T createEntity(final Class<T> entityClass) {
		try {
			return entityClass.newInstance();
		} catch (final InstantiationException | IllegalAccessException ex) {
			throw new IllegalArgumentException("Cannot instantiate entity class " + entityClass.getName(), ex);
		}
	}

	/**
	 * Finds existing entity by query or creates a new one
	 *
	 * @param q
	 *            the query used for searching an entity. The first result is returned. If the query is null, a new entity will be returned
	 * @param entityClass
	 *            the entity class
	 * @param clearCache
	 *            clears the JPA cache so the fetched entity contains last modification from the database.
	 * @return the first entity returned by the query or a new one if the query returns no results
	 */
	public <T extends Identifiable> T findOrCreateEntity(final TypedQuery<T> q, final Class<T> entityClass, final boolean clearCache) {
		NullUtils.checkNotNull(entityClass, "Invalid argument: entityClass should not be null");
		T entity = null;
		if (q != null) {
			entity = ExtCollectionUtils.get0(q.getResultList());
		}
		if (entity == null) {
			entity = createEntity(entityClass);
		} else if (clearCache) {
			final EntityManager em = em();
			clearCache(em, entityClass, entity.getId());
			em.refresh(entity);
		}
		return entity;
	}

	/**
	 * Finds existing entity by given <tt>id</tt> or creates a new one
	 *
	 * @param entityClass
	 *            the entity class
	 * @param id
	 *            the id to search for. Can be <tt>0</tt> or <tt>null</tt> then a new entity will be created
	 * @param clearCache
	 *            clears the JPA cache so the fetched entity contains last modification from the database.
	 * @return the entity with given <tt>id</tt> or a new entity if there is no such <tt>id</tt> in the database
	 */
	public <T> T findOrCreateEntity(final Class<T> entityClass, final Long id, final boolean clearCache) {
		NullUtils.checkNotNull(entityClass, "Invalid argument: entityClass should not be null");
		final EntityManager em = em();
		T entity = null;
		if (id != null && id != 0) {
			if (clearCache) {
				clearCache(em, entityClass, id);
			}
			entity = em.find(entityClass, id);
		}
		if (entity == null) {
			try {
				entity = entityClass.newInstance();
			} catch (final InstantiationException | IllegalAccessException ex) {
				throw new IllegalArgumentException("Cannot instantiate entity class " + entityClass.getName(), ex);
			}
		}
		return entity;
	}

	/**
	 * Saves an entity into database and flushes the entity manager (synchronizing the persistence context to the underlying database).
	 * <p>
	 * Usually flushing means that the object ID is set, but the transaction is still not committed
	 *
	 * @param entity
	 *            the entity to be saved
	 * @return the saved entity
	 * @throws ObjectAlreadyModifiedException
	 *             throw if the object is already modified by different transaction
	 */
	public <E extends Identifiable> E save(final E entity) throws ObjectAlreadyModifiedException {
		return save(entity, true);
	}

	/**
	 * Saves an entity into database
	 *
	 * @param entity
	 *            the entity to be saved
	 * @param flush
	 *            flushes the entity manager (synchronizing the persistence context to the underlying database). Usually flushing means that the object ID is set, but the transaction is still not committed
	 * @return the saved entity
	 * @throws ObjectAlreadyModifiedException
	 *             throw if the object is already modified by different transaction
	 */
	public <E extends Identifiable> E save(E entity, final boolean flush) throws ObjectAlreadyModifiedException {
		final EntityManager em = em();
		if (entity != null) {
			final long id = entity.getId();
			try {
				if (id != 0) {
					entity = em.merge(entity);
				} else {
					em.persist(entity);
				}
				if (flush) {
					em.flush();
				}
				return entity;
			} catch (final OptimisticLockException ex) {
				throw new ObjectAlreadyModifiedException(ex);
			}
		}
		return null;
	}

	/**
	 * Checks if the entity is not null and returns it. If the entity is null checked ObjectNotFound exception is thrown
	 * <p>
	 * Normally this method should be used in conjunction with other methods like {@link #loadEntity(long, Class, boolean)}
	 *
	 * <pre>
	 * <code>
	 * try {
	 *   jpaUtils.notNull(jpaUtils.loadEntity(42L, MyEntity.class, true));
	 * } catch (ObjectNotFound ex) {
	 *   // error handling
	 * }
	 * </code>
	 * </pre>
	 *
	 * @param e
	 *            the entity
	 * @return the entity if it is not null
	 * @throws ObjectNotFoundException
	 *             thrown if the entity is null
	 */
	public <E> E notNull(final E e) throws ObjectNotFoundException {
		if (e == null) {
			throw new ObjectNotFoundException("Object is null");
		}
		return e;
	}

	/**
	 * Loads entity from database.
	 *
	 * @param id
	 *            the entity id
	 * @param entityClass
	 *            the entity class
	 * @param clearCache
	 *            clears the JPA cache so the fetched entity contains last modification from the database.
	 * @return the entity with given <tt>id</tt> or null if no such entity exists
	 */
	public <E> E loadEntity(final long id, final Class<E> entityClass, final boolean clearCache) {
		NullUtils.checkNotNull(entityClass, "Invalid argument: entityClass should not be null");
		final EntityManager em = em();
		if (clearCache) {
			clearCache(em, entityClass, id);
		}
		final E e = em.find(entityClass, id);
		return e;
	}

	/**
	 * Loads entity from database.
	 *
	 * @param q
	 *            the query that returns the entity
	 * @param entityClass
	 *            the entity class
	 * @param clearCache
	 *            clears the JPA cache so the fetched entity contains last modification from the database.
	 * @return the entity returned by given query or null if the query returns no results
	 */
	public <E extends Identifiable> E loadEntity(final TypedQuery<E> q, final Class<E> entityClass, final boolean clearCache) {
		final E entity = ExtCollectionUtils.get0(q.getResultList());
		if (entity != null && clearCache) {
			final EntityManager em = em();
			clearCache(em, entityClass, entity.getId());
			em.refresh(entity);
		}
		return entity;
	}

	/**
	 * Counts entities that matches given query configuration
	 *
	 * @param queryConfigFactory
	 *            the query configuration factory
	 * @param entityClass
	 *            the entity class
	 * @return the number of entities matching the query configuration
	 */
	public <E> long countEntities(final Function<CriteriaApiObjects<E>, QueryConfig> queryConfigFactory, final Class<E> entityClass) {
		final EntityManager em = em();
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Long> cq = cb.createQuery(Long.class);

		final Root<E> root = cq.from(entityClass);

		final QueryConfig qc = createQueryConfig(queryConfigFactory, cb, cq, root);
		if (qc.isDistinct()) {
			cq.select(cb.countDistinct(root));
		} else {
			cq.select(cb.count(root));
		}

		cq.where(qc.getPredicatesArray());

		final TypedQuery<Long> q = em.createQuery(cq);
		applyParams(q, qc);

		final long count = q.getSingleResult();
		return count;
	}

	/**
	 * Counts entities that matches given filter
	 *
	 * @param filter
	 *            the query filter
	 * @param entityClass
	 *            the entity class
	 * @return the number of entities matching the query filter
	 */
	public <E extends AuditableEntity, F extends AuditableEntityFilter> long countEntities(final F filter, final BiFunction<CriteriaApiObjects<E>, F, QueryConfig> queryConfigFactory, final Class<E> entityClass) {
		return countEntities(cao -> queryConfigFactory.apply(cao, filter), entityClass);
		// final Function<CriteriaApiObjects<E>, QueryConfig> queryConfigFactory = o -> new QueryConfig(addAuditableEntityPredicates(o, filter, Locale.getDefault()).getPredicates(), null, null, filter.isDistinct());
		// return countEntities(queryConfigFactory, entityClass);
	}

	/**
	 * Applies parameters to typed query
	 *
	 * @param q
	 *            the typed query
	 * @param qc
	 *            query config containing the parameters
	 */
	protected void applyParams(final TypedQuery<?> q, final QueryConfig qc) {
		if (qc != null && ExtCollectionUtils.isNotEmpty(qc.getParameters())) {
			qc.getParameters().forEach(pair -> q.setParameter(pair.getKey(), pair.getValue()));
		}
	}

	/**
	 * List entities matching given query configuration
	 *
	 * @param firstResult
	 *            the position of the first result to retrieve.
	 * @param maxResults
	 *            the maximum number of results to retrieve.
	 * @param queryConfigFactory
	 *            the query configuration factory
	 * @param entityClass
	 *            the entity class
	 * @return a list of entities matching given query configuration
	 */
	public <E> List<E> listEntities(final int firstResult, final int maxResults, final Function<CriteriaApiObjects<E>, QueryConfig> queryConfigFactory, final Class<E> entityClass) {
		final EntityManager em = em();
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<E> cq = cb.createQuery(entityClass);
		final Root<E> root = cq.from(entityClass);

		cq.select(root);

		return executeQuery(em, firstResult, maxResults, queryConfigFactory, cb, cq, root);
	}

	/**
	 * List entities matching given filter
	 *
	 * @param filter
	 *            the filter
	 * @param queryConfigFactory
	 *            the query configuration factory
	 * @param entityClass
	 *            the entity class
	 * @return List entities matching given filter
	 */
	public <E, F extends AuditableEntityFilter> List<E> listEntities(final F filter, final BiFunction<CriteriaApiObjects<E>, F, QueryConfig> queryConfigFactory, final Class<E> entityClass) {
		return listEntities(filter.getFirstResult(), filter.getMaxResults(), cao -> queryConfigFactory.apply(cao, filter), entityClass);
	}

	/**
	 * List entity IDs matching given query configuration
	 *
	 * @param firstResult
	 *            the position of the first result to retrieve.
	 * @param maxResults
	 *            the maximum number of results to retrieve.
	 * @param queryConfigFactory
	 *            the query configuration factory
	 * @param entityClass
	 *            the entity class
	 * @return a list of entities matching given query configuration
	 */
	public <E extends IdentifiableEntity> List<Long> listEntityIds(final int firstResult, final int maxResults, final Function<CriteriaApiObjects<E>, QueryConfig> queryConfigFactory, final Class<E> entityClass) {
		final EntityManager em = em();
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		final Root<E> root = cq.from(entityClass);

		cq.select(root.get(IdentifiableEntity_.id));

		return executeQuery(em, firstResult, maxResults, queryConfigFactory, cb, cq, root);
	}

	/**
	 * List entities matching given filter
	 *
	 * @param filter
	 *            the filter
	 * @param queryConfigFactory
	 *            the query configuration factory
	 * @param entityClass
	 *            the entity class
	 * @return List entities matching given filter
	 */
	public <E extends IdentifiableEntity, F extends IdentifiableEntityFilter> List<Long> listEntityIds(final F filter, final BiFunction<CriteriaApiObjects<E>, F, QueryConfig> queryConfigFactory, final Class<E> entityClass) {
		return listEntityIds(filter.getFirstResult(), filter.getMaxResults(), cao -> queryConfigFactory.apply(cao, filter), entityClass);
	}

	/**
	 * Executes a query and returns a list of entities
	 *
	 * @param em
	 *            the entity manager
	 * @param firstResult
	 *            the position of the first result to retrieve.
	 * @param maxResults
	 *            the maximum number of results to retrieve.
	 * @param queryConfigFactory
	 *            the query configuration factory
	 * @param cb
	 *            criteria builder
	 * @param cq
	 *            criteria query
	 * @param root
	 *            the entity root
	 * @return list of entities
	 */
	protected <E, R> List<R> executeQuery(final EntityManager em, final int firstResult, final int maxResults, final Function<CriteriaApiObjects<E>, QueryConfig> queryConfigFactory, final CriteriaBuilder cb, final CriteriaQuery<R> cq, final Root<E> root) {
		final QueryConfig qc = createQueryConfig(queryConfigFactory, cb, cq, root);

		cq.distinct(qc.isDistinct());

		cq.where(qc.getPredicatesArray());

		if (ExtCollectionUtils.isNotEmpty(qc.getSortOrders())) {
			cq.orderBy(qc.getSortOrders());
		}

		final TypedQuery<R> q = em.createQuery(cq);
		applyParams(q, qc);

		if (firstResult > 0) {
			q.setFirstResult(firstResult);
		}
		if (maxResults > 0) {
			q.setMaxResults(maxResults);
		}

		final List<R> ids = ExtCollectionUtils.defaultList(q.getResultList());
		return ids;
	}

	/**
	 * Constructs a query configuraion object using provided factory
	 *
	 * @param queryConfigFactory
	 *            the factory
	 * @param cb
	 *            the criteria builder
	 * @param cq
	 *            the criteria query
	 * @param root
	 *            the root of the entity
	 * @return query configuration object
	 */
	protected <E> QueryConfig createQueryConfig(final Function<CriteriaApiObjects<E>, QueryConfig> queryConfigFactory, final CriteriaBuilder cb, final CriteriaQuery<?> cq, final Root<E> root) {
		QueryConfig c = null;
		if (queryConfigFactory != null) {
			final CriteriaApiObjects<E> param = new CriteriaApiObjects<>(cb, cq, root);
			c = queryConfigFactory.apply(param);
		}
		if (c == null) {
			c = new QueryConfig(null, null, null, true);
		}
		return c;
	}

	/**
	 * Logs query SQL to logger using debug level
	 *
	 * @param logger
	 *            the logger
	 * @param dialect
	 *            the database dialect
	 * @param q
	 *            the query
	 */
	public static void logSql(Logger logger, final JpaDialect dialect, final TypedQuery<?> q) {
		if (logger == null) {
			logger = LOGGER;
		}
		if (logger.isDebugEnabled()) {
			try {
				final String sqlString = getSql(dialect, q);
				logger.debug(sqlString);
			} catch (final PersistenceException ex) {
				if (logger.isErrorEnabled()) {
					logger.error("Cannot get query sql", ex);
				}
			}
		}
	}

	/**
	 * Gets the query SQL
	 *
	 * @param dialect
	 *            the database dialect
	 * @param q
	 *            the query
	 * @return the query SQL
	 */
	public static String getSql(final JpaDialect dialect, final TypedQuery<?> q) {
		NullUtils.checkNotNull(dialect, "Invalid argumant: dialect cannot be null");
		NullUtils.checkNotNull(q, "Invalid argumant: q cannot be null");
		return dialect.getSql(q);
	}

	/**
	 * Returns next ID from the sequence
	 *
	 * @param dialect
	 *            the database dialect
	 * @param sequenceName
	 *            the sequence name
	 * @return the next ID from the sequence
	 */
	public long generateId(final DatabaseDialect dialect, final String sequenceName) {
		NullUtils.checkNotNull(dialect, "Invalid argumant: dialect cannot be null");
		if (StringUtils.isBlank(sequenceName)) {
			throw new IllegalArgumentException("Invalid argument: sequenceName cannot be blank");
		}
		final Query q = em().createNativeQuery(dialect.createSequenceNextValueSql(sequenceName));
		return ((Number) q.getSingleResult()).longValue();
	}

	/**
	 * Returns the ID of the transaction bound to the current thread at the time this method is called
	 *
	 * @return the ID of the transaction bound to the current thread at the time this method is called
	 */
	public static String getTransactionId() {
		String id = "";
		try {
			final TransactionSynchronizationRegistry reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
			if (reg != null) {
				final Object key = reg.getTransactionKey();
				if (key != null) {
					id = key.toString();
				}
			}
		} catch (final Exception ex) {
			// ignore
		}
		return id;
	}

	/**
	 * Clears the whole JPA cache
	 */
	public void clearCache() {
		em().getEntityManagerFactory().getCache().evictAll();
	}

	/**
	 * Clears the JPA cache for specified class
	 *
	 * @param clazz
	 *            the class which entities are to be removed
	 */
	public void clearCache(final Class<?> clazz) {
		em().getEntityManagerFactory().getCache().evict(clazz);
	}

	/**
	 * Clears the JPA cache for specified object
	 *
	 * @param e
	 *            the entity's data to be removed
	 */
	public void clearCache(final Identifiable e) {
		if (e != null) {
			clearCache(e.getClass(), e.getId());
		}
	}

	/**
	 * Clears the JPA cache for specified object
	 *
	 * @param entityClass
	 *            the entity class
	 * @param entityId
	 *            the entity id
	 */
	public void clearCache(final Class<?> entityClass, final Long entityId) {
		final EntityManager em = em();
		clearCache(em, entityClass, entityId);
	}

	/**
	 * Clears the JPA cache for specified object
	 *
	 * @param em
	 *            the entity manager
	 * @param entityClass
	 *            the entity class
	 * @param entityId
	 *            the entity id
	 */
	protected void clearCache(final EntityManager em, final Class<?> entityClass, final Long entityId) {
		if (entityClass != null && entityId != null) {
			em.getEntityManagerFactory().getCache().evict(entityClass, entityId);
		}
	}
}
