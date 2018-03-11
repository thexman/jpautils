package com.a9ski.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-03-11T16:17:21.093+0200")
@StaticMetamodel(AuditableEntity.class)
public class AuditableEntity_ extends IdentifiableEntity_ {
	public static volatile SingularAttribute<AuditableEntity, Date> created;
	public static volatile SingularAttribute<AuditableEntity, Date> edited;
	public static volatile SingularAttribute<AuditableEntity, Long> creator;
	public static volatile SingularAttribute<AuditableEntity, Long> editor;
	public static volatile SingularAttribute<AuditableEntity, Long> version;
	public static volatile SingularAttribute<AuditableEntity, Boolean> deleted;
}
