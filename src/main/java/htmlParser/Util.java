package htmlParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import com.google.common.io.BaseEncoding;

public class Util {

	private static final String FILE_SEPERATOR = "/";
	private static Logger LOGGER = Logger.getAnonymousLogger();
	private static boolean isFileHandlerSet = false;

	// public static String getTextVal(Node el) {
	// String textval = null;
	// if (el.getNodeName().equals(Constants.RAW_TEXT_CHILD_TAG)) {
	// textval = el.getNodeValue();
	// } else {
	// textval = el.getFirstChild().getNodeValue();
	// }
	// return textval;
	// }

	public static String getStringAttribute(Element el, String attribute) {
		return el.attr(attribute);
	}

	public static int getIntegerAttribute(Element child, String attribute) {
		String attributeValue = child.attr(attribute);
		try {
			return Integer.parseInt(attributeValue);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static String pathJoin(String prePath, String postPath) {
		prePath = StringUtils.stripEnd(prePath, FILE_SEPERATOR);
		postPath = StringUtils.strip(postPath, FILE_SEPERATOR);
		return String.format("%s/%s", prePath, postPath);
	}

	public static boolean isNullOrEmptyOrWhiteSpace(String data) {
		return (data == null || data.isEmpty() || data.trim().length() <= 0);
	}

	public static void logMessage(Level level, String message) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (!isFileHandlerSet) {
			FileHandler logFileHandler = null;
			try {
				logFileHandler = new FileHandler(Constants.logFile);
			} catch (IOException e) {
				throw new RuntimeErrorException(new Error(e.getMessage()));
			}
			logFileHandler.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(logFileHandler);
			isFileHandlerSet = true;
		}
		LOGGER.logp(level, stackTraceElements[2].getClassName(), stackTraceElements[2].getMethodName(), message);
	}

	public static void closeLogger() {
		for (Handler handler : LOGGER.getHandlers()) {
			handler.close();
		}
	}

	public static String decode(String s) {
		String decoded = null;
		try {
			decoded = new String(BaseEncoding.base64().decode(s), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decoded;
	}

	public static String substringRegex(String data, String regex, int group) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		if (matcher.find()) {
			return matcher.group(group);
		} else {
			return null;
		}

	}

	public static String substringRegex(String data, String regex) {
		return substringRegex(data, regex, 0);
	}

	public static boolean isNumber(String data) {
		String subString = substringRegex(data, "\n[0-9]+");
		if (!Util.isNullOrEmptyOrWhiteSpace(subString)) {
			return data.equals(subString);
		}
		return false;
	}

	public static boolean stringsEqual(String string1, String string2) {
		if (string1 != null) {
			return string1.equals(string2);
		} else {
			if (string2 != null) {
				return string2.equals(string1);
			}
			return true;
		}
	}

	public static String stripSlashes(String text) {
		return StringUtils.strip(text, FILE_SEPERATOR);
	}

	public static String newLineJoin(String text1, String text2) {
		return String.format(Constants.NEWLINE_JOIN_TEMPLATE, text1, text2);
	}

	public static List<String> readContentFromFilePath(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		List<String> lines = new ArrayList<String>();
		try {
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			return lines;
		} finally {
			br.close();
		}
	}
}
