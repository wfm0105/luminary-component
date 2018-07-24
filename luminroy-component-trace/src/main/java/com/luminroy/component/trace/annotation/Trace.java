package com.luminroy.component.trace.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.luminroy.component.trace.tracker.Tracker;

/**
 * 
* <p>Title: 链路跟踪注解</p>  
* <p>Description: 用来标注该类，方法是否进行链路跟踪</p>  
* @author wulinfeng
* @date 2018年7月20日上午10:12:40
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {

	Class<? extends Tracker> value();
	
}
