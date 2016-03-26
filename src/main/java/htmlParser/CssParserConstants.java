package htmlParser;

public class CssParserConstants {
	public static final String FONT_COLOUR = ".fc";
	public static final String BOLD = "Bold";
	public static final String BASE64_PATTERN = "(?!base64)[^,]+(?=\\) format\\(\"truetype\"\\))";
	public static final String FONT_FAMILY_PATTERN = "(?!font-family: )ff[0-9a-fA-F]+(?=;)";
	public static final String FONT_FACE = "@font-face";
	public static final String FONT_COLOUR_FAMILY = "\\.(fc[0-9]+)";
	public static final String FONT_COLOUR_PATTERN = "(?!\\.fc[0-9]+ \\{ color: rgb\\()[0-9]+, *[0-9]+, *[0-9]+(?=\\) \\})";
	public static final String FONT_COLOUR_TRANSPARENT_PATTERN = "(?!\\.fc[0-9]+ \\{ color: )transparent(?= \\})";
	public static final String X_POSITION_PLAIN = ".x";
	public static final String X_POSITION = "\\.(x[0-9a-fA-F]+)";
	public static final String X_POSITION_PATTERN = "(?!\\.x[0-9a-fA-F]+ \\{ left: )[0-9]+(\\.[0-9]+)?(?=px \\})";
	public static final String Y_POSITION_PLAIN = ".y";
	public static final String Y_POSITION = "\\.(y[0-9a-fA-F]+)";
	public static final String Y_POSITION_PATTERN = "(?!\\.y[0-9a-fA-F]+ \\{ bottom: )[0-9]+(\\.[0-9]+)?(?=px \\})";
	public static final String FONT_SIZE_PLAIN = ".fs";
	public static final String FONT_SIZE = "\\.(fs[0-9a-fA-F]+)";
	public static final String FONT_SIZE_PATTERN = "(?!\\.fs[0-9a-fA-F]+ \\{ font-size: )[0-9]+(\\.[0-9]+)?(?=px \\})";
}
