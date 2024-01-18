package br.com.emailproject.util;

import org.apache.log4j.Logger;

public class LogUtil {
	private LogUtil() {
	}

	public static <T> Logger getLogger(T object) {
		return Logger.getLogger(object.getClass());
	}
}