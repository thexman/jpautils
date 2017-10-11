package com.a9ski.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-10-12T01:24:35.674+0300")
@StaticMetamodel(AuditableEntity.class)
public class AuditableEntity_ extends IdentifiableEntity_ {
	public static volatile SingularAttribute<AuditableEntity, Date> created;
	public static volatile SingularAttribute<AuditableEntity, Date> edited;
	public static volatile SingularAttribute<AuditableEntity, Long> creator;
	public static volatile SingularAttribute<AuditableEntity, Long> editor;
	public static volatile SingularAttribute<AuditableEntity, Long> version;
	public static volatile SingularAttribute<AuditableEntity, Boolean> deleted;
}
