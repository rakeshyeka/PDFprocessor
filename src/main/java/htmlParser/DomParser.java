package htmlParser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DomParser {
	public Document dom;
	public List<Page> pages;
	public DomParser(String file) {

		try {
			File input = new File(file);
			this.dom = Jsoup.parse(input, "UTF-8", "");

			CssParser.parseCssText(this.dom);

			this.pages = Page.buildPageFromNodeList(this.dom.getElementById(Constants.PAGE_CONTAINER_TAG));
		} catch (IOException ioe) {

		}

	}

	public String getHindiPages() {
		String text = "";
		for (int i = 0; i < this.pages.size(); i++) {
			if (this.pages.get(i).isHindi()) {
				text = Util.newLineJoin(text, Constants.PAGE_DECORATION_BOUNDARY);
				text = Util.newLineJoin(text, this.pages.get(i).toString());
				text = Util.newLineJoin(text, "");
			}
		}
		return text;
	}

	public String getEnglishPages() {
		String text = "";
		for (int i = 0; i < this.pages.size(); i++) {
			if (this.pages.get(i).isEnglish()) {
				text = Util.newLineJoin(text, Constants.PAGE_DECORATION_BOUNDARY);
				text = Util.newLineJoin(text, this.pages.get(i).toString());
				text = Util.newLineJoin(text, "");
			}
		}
		return text;
	}

	public String getPages() {
		String text = "";
		for (int i = 0; i < this.pages.size(); i++) {
			text = Util.newLineJoin(text, Constants.PAGE_DECORATION_BOUNDARY);
			text = Util.newLineJoin(text, this.pages.get(i).toString());
			text = Util.newLineJoin(text, "");
		}
		return text;
	}

	public Iterator<Page> getPagesIterator() {
		return this.pages.iterator();
	}
}
