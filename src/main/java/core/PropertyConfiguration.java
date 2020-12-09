package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A subclass of Properties that allows recursive references for property
 * values. For example,
 *
 * <pre>
 * <code>
 * A=12345678
 * B={A}90
 * C={B} plus more
 * </code>
 * </pre>
 * <p>
 * will result in <code>getProperty("C")</code> returning the value "1234567890
 * plus more". Based on code example by Chris Mair
 */
public class PropertyConfiguration extends Properties {

	/**
	 * This field - serialVersionUID is used for _.
	 */
	private static final long serialVersionUID = -2639866861926912511L;

	// The prefix and suffix for constant names
	// within property values
	private static final String START_CONST = "{";
	private static final String END_CONST = "}";

	// The maximum depth for recursive substitution
	// of constants within property values
	// (e.g., A={B} .. B={C} .. C={D} .. etc.)
	private static final int MAX_SUBST_DEPTH = 5;

	/**
	 * Creates an empty property list with no default values.
	 */
	PropertyConfiguration() {
		super();
	}

	/**
	 * Creates an empty property list with the specified defaults.
	 *
	 * @param defaults the defaults.
	 */
	public PropertyConfiguration(Properties defaults) {
		super(defaults);
		this.updateValues();
	}

	/**
	 */
	public synchronized Object setProperty(String key, String value) {
		Object retObj = super.setProperty(key, value);
		this.updateValues();
		return retObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Properties#load(java.io.InputStream)
	 */
	public synchronized void load(InputStream inStream) throws IOException {
		super.load(inStream);
		this.updateValues();
	}

	/**
	 *
	 */
	private void updateValues() {
		for (String key : super.stringPropertyNames()) {
			super.setProperty(key, this.getProperty(key, 0));
		}
	}

	/**
	 * Searches for the property with the specified key in this property list. If
	 * the key is not found in this property list, the default property list, and
	 * its defaults, recursively, are then checked. The method returns
	 * <code>null</code> if the property is not found.
	 * <p>
	 * The level parameter specifies the current level of recursive constant
	 * substitution. If the requested property value includes a constant, its value
	 * is substituted in place through a recursive call to this method, incrementing
	 * the level. Once level exceeds MAX_SUBST_DEPTH, no further constant
	 * substitutions are performed within the current requested value.
	 *
	 * @param key   the property key.
	 * @param level the level of recursion so far
	 * @return the value in this property list with the specified key value.
	 */
	private String getProperty(String key, int level) {
		String value = super.getProperty(key);
		if (value == null)
			return value; // Return the value as is

		// Get the index of the first constant, if any
		int beginIndex = 0;
		int startName = value.indexOf(START_CONST, beginIndex);

		while (startName != -1) {
			if (level + 1 > MAX_SUBST_DEPTH) {
				// Exceeded MAX_SUBST_DEPTH
				// Return the value as is
				return value;
			}

			int endName = value.indexOf(END_CONST, startName);
			if (endName == -1) {
				// Terminating symbol not found
				// Return the value as is
				return value;
			}

			String constName = value.substring(startName + 1, endName);
			String constValue = getProperty(constName, level + 1);
			if (constValue == null) {
				// Property name not found
				// Return the value as is
				return value;
			}

			// Insert the constant value into the
			// original property value
			String newValue = (startName > 0) ? value.substring(0, startName) : "";
			newValue += constValue;

			// Start checking for constants at this index
			beginIndex = newValue.length();

			// Append the remainder of the value
			newValue += value.substring(endName + 1);

			value = newValue;

			// Look for the next constant
			startName = value.indexOf(START_CONST, beginIndex);
		}

		return value;
	}
}