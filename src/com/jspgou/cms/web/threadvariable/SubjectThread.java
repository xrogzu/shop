package com.jspgou.cms.web.threadvariable;

import org.apache.shiro.subject.Subject;

/**
* @preserve
*/
public class SubjectThread {
	private static ThreadLocal<Subject> instance = new ThreadLocal<Subject>();

	public static Subject get() {
		return instance.get();
	}

	public static void set(Subject subject) {
		instance.set(subject);
	}

	public static void remove() {
		instance.remove();
	}
}
