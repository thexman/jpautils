//@formatter:off
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value=LongRangeAdapter.class, type=Range.class),
    @XmlJavaTypeAdapter(value=DateRangeAdapter.class, type=Range.class)
})
//@formatter:on
package com.a9ski.utils;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import com.a9ski.jaxb.DateRangeAdapter;
import com.a9ski.jaxb.LongRangeAdapter;
