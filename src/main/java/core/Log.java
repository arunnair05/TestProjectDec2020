package core;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This is Generic Log Class with all static methods.
 * <p/>
 * It can not be used for object creation and gives all log related utility
 * methods.
 * 
 */
public class Log {

	/**
	 * This class only has static methods and should not be instantiated. Private
	 * constructor to avoid un-wanted objects for this class.
	 */
	private Log() {
		super();
	}

	// Initialize default log4j configuration.
	static {
		String log4jConfigFile = ConfigManager.get("config.log4j.configuration");
		PropertyConfigurator.configure(log4jConfigFile);
	}

	/**
	 * Uses the logger to post a debug message. Automatically appends a date-time
	 * stamp, the message level, and the calling method to the message passed in.
	 * 
	 * @param message the text to post using the log object.
	 */
	public static void debug(Object message) {
		Log.log(Level.DEBUG, message, null);
	}

	/**
	 * This method will log debug message with exception details.
	 * 
	 * @param message the text to post using the log object.
	 * @param t       Throw able object instance to read stack trace
	 */
	public static void debug(Object message, Throwable t) {
		Log.log(Level.DEBUG, message, t);
	}

	/**
	 * Uses the logger to post a error message.
	 * 
	 * @param message the text to post using the log object.
	 */
	public static void error(Object message) {
		Log.log(Level.ERROR, message, null);
	}

	/**
	 * Logs message at error level with error stack trace.
	 * 
	 * @param message the text to post using the log object.
	 * @param t       Throw able object instance to read stack trace
	 */
	public static void error(Object message, Throwable t) {
		Log.log(Level.ERROR, message, t);
	}

	/**
	 * Uses the logger to post a warning message. Automatically appends a date-time
	 * stamp, the message level, and the calling method to the message passed in.
	 * 
	 * @param message the text to post using the log object.
	 */
	public static void fatal(Object message) {
		Log.log(Level.FATAL, message, null);
	}

	/**
	 * logs message at fatal level with error stack trace.
	 * 
	 * @param message the text to post using the log object.
	 * @param t       Throw able object instance to read stack trace
	 */
	public static void fatal(Object message, Throwable t) {
		Log.log(Level.FATAL, message, t);
	}

	/**
	 * Gets the specified calling method name at the depth requested by the
	 * parameter.
	 * 
	 * @param callStackDepth the depth of method in the stack to retrieve.
	 * @return the name of the method at the stack depth specified.
	 */
	private static String getCallingClass(int callStackDepth) {
		String className = Thread.currentThread().getStackTrace()[callStackDepth].getClassName();
		// return from here.
		return className;

	}

	/**
	 * Gets the specified calling method name at the depth requested by the
	 * parameter.
	 * 
	 * @param callStackDepth the depth of method in the stack to retrieve.
	 * @return the name of the method at the stack depth specified.
	 */
	private static String getCallingMethod(int callStackDepth) {
		String methodName = Thread.currentThread().getStackTrace()[callStackDepth].getMethodName();
		// return from here.
		return methodName;

	}

	/**
	 * Uses the logger to post a basic information message. Automatically appends a
	 * date-time stamp, the message level, and the calling method to the message
	 * passed in.
	 * 
	 * @param message the text to post using the log object.
	 */
	public static void info(Object message) {
		Log.log(Level.INFO, message, null);
	}

	/**
	 * @param message the text to post using the log object.
	 * @param t       Throw able object instance to read stack trace
	 */
	public static void info(Object message, Throwable t) {
		Log.log(Level.INFO, message, t);
	}

	/**
	 * @return
	 */
	public static boolean isDebugEnabled() {
		return Log.isEnabledFor(Level.DEBUG);
	}

	/**
	 * @param level
	 * @return
	 */
	public static boolean isEnabledFor(Level level) {
		String clazzName = Log.getCallingClass(4);
		if (clazzName.contentEquals(Log.getCallingClass(3))) {
			clazzName = Log.getCallingClass(5);
		}
		Logger logger = Logger.getLogger(clazzName);
		return logger.isEnabledFor(level);
	}

	/**
	 * @return
	 */
	public static boolean isInfoEnabled() {
		return Log.isEnabledFor(Level.INFO);
	}

	/**
	 * @return
	 */
	public static boolean isTraceEnabled() {
		return Log.isEnabledFor(Level.TRACE);
	}

	/**
	 * logs error message with all specified parameter values. most flexible method
	 * to do logging.
	 * 
	 * @param level   Log level for logger
	 * @param message the text to post using the log object.
	 * @param t       Throw able object instance to read stack trace
	 */
	public static void log(Level level, Object message, Throwable t) {
		String clazzName = Log.getCallingClass(4);
		if (clazzName.contentEquals(Log.getCallingClass(3))) {
			clazzName = Log.getCallingClass(5);
		}
		Logger logger = Logger.getLogger(clazzName);
		// Only process if logger is enabled for this class.
		if (logger.isEnabledFor(level)) {
			String methodName = Log.getCallingMethod(4);
			if (methodName.contentEquals(Log.getCallingMethod(3))) {
				methodName = Log.getCallingMethod(5);
			}
			String logMsg = "[" + Thread.currentThread().getName() + "][" + methodName + "] " + message;

			if (t != null) {
				logger.log(level, logMsg, t);
			} else {
				logger.log(level, logMsg);
			}
		}
	}

	/**
	 * Uses the logger to post a warning message. Automatically appends a date-time
	 * stamp, the message level, and the calling method to the message passed in.
	 * 
	 * @param message the text to post using the log object.
	 */
	public static void trace(Object message) {
		Log.log(Level.TRACE, message, null);
	}

	/**
	 * Logs message with warning level.
	 * 
	 * @param message the text to post using the log object.
	 * @param t       Throw able object instance to read stack trace
	 */
	public static void trace(Object message, Throwable t) {
		Log.log(Level.TRACE, message, t);
	}

	/**
	 * Uses the logger to post a warning message. Automatically appends a date-time
	 * stamp, the message level, and the calling method to the message passed in.
	 * 
	 * @param message the text to post using the log object.
	 */
	public static void warn(Object message) {
		Log.log(Level.WARN, message, null);
	}

	/**
	 * Logs message with warning level.
	 * 
	 * @param message the text to post using the log object.
	 * @param t       Throw able object instance to read stack trace
	 */
	public static void warn(Object message, Throwable t) {
		Log.log(Level.WARN, message, t);
	}

}