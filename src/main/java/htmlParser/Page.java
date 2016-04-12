package htmlParser;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math.util.DoubleArray;
import org.apache.mahout.math.DenseVector;
import org.jsoup.nodes.Element;

public class Page {
	private boolean isHindi;
	private List<Text> content = new ArrayList<Text>();
	private int pageNumber;

	public Page(List<Text> content) {
		this.content = content;
	}

	public Page(Element el) {
		if (el != null && el.childNodeSize() > 0) {
			for (Element child : el.getElementsByClass("t")) {
				Text textEntity = new Text(child, false, null);
				content.add(textEntity);
			}
		}
		this.setHindi();
	}

	public static List<Page> buildPageFromNodeList(Element element) {
		List<Page> pageList = new ArrayList<Page>();
		int pageNumber = 1;
		for (Element child : element.getElementsByClass("pf")) {
			Page page = new Page(child);
			page.setPageNumber(pageNumber);
			pageList.add(page);
			pageNumber++;
		}
		return pageList;

	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isHindi() {
		return isHindi;
	}

	public void setHindi() {
		int hindiCount = 0;
		int contentSize = 0;
		for (int i = 0; i < this.content.size(); i++) {
			if (this.content.get(i).isHindi()) {
				hindiCount++;
			} else if (!content.get(i).toString().equals("\n")) {
				contentSize++;
			}
		}
		float probability = ((float) hindiCount) / contentSize;
		if (probability >= Constants.EXPECTED_PROBABILITY) {
			this.isHindi = true;
		}
	}

	public String toString() {
		String text = "";
		sortTextContent();
		return processTextEntities(text, this.content);
	}

	public String extractTextFeatures() {
		String textFeaturesContent = "";
		for (int i=0; i< this.content.size(); i++){
			Text textEntity = content.get(i);
			textFeaturesContent += textEntity.getTextFeatures() + "\n";
		}
		return textFeaturesContent;
	}

	public Iterator<Text> getTextIterator() {
		return content.iterator();
	}


	private String processTextEntities(String text, List<Text> content) {
		int prevBold = -2;
		boolean pageHeaderFlag = true;
		for (int i = 0; i < content.size(); i++) {
			Text textEntity = content.get(i);
			String fontSizeClass = textEntity.getClasses().containsKey("fs")
					? textEntity.getClasses().get("fs") : "";
			if (!fontSizeClass.equals("fs0") || !pageHeaderFlag) {
				pageHeaderFlag = false;
				if (HtmlToTextProcessor.getConfig().isParagraphForBold()
						|| HtmlToTextProcessor.getConfig().isParagraphForColoured()) {
					Pair<String, Integer> responsePair = addBoundaryToText(textEntity, text,
							Constants.BLOCK_DECORATION_BOUNDARY, i, prevBold);
					text = responsePair.getLeft();
					prevBold = responsePair.getRight();
				}
				text = String.format(Constants.NEWLINE_JOIN_TEMPLATE, text, textEntity.toString());
			}
		}
		return text;
	}

	private void sortTextContent() {
		Collections.sort(this.content);
		removeDuplicates();
	}

	private void removeDuplicates() {
		Iterator<Text> textIterator = this.content.iterator();

		List<Text> toRemoveList = new ArrayList<Text>();
		for (int index = 0, nextIndex = index + 1;
			 index < this.content.size() && nextIndex < this.content.size();){
			Text currentText = this.content.get(index);
			Text nextText = this.content.get(nextIndex);
			if (currentText.compareTo(nextText) == 0) {
				toRemoveList.add(nextText);
				nextIndex++;
			} else {
				index++;
				nextIndex = index+1;
			}
		}

		content.removeAll(toRemoveList);
	}

	private Pair<String, Integer> addBoundaryToText(Text textEntity, String text, String boundary, int index, int prevBold) {
		if ((textEntity.isBold() || textEntity.isColoured()) && index - prevBold > 1) {
			text = String.format(Constants.NEWLINE_JOIN_TEMPLATE, text, boundary);
			prevBold = index;
		}
		return Pair.of(text, prevBold);
	}

	public boolean isEnglish() {
		return !this.isHindi();
	}
}
