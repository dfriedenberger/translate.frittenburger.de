package de.frittenburger.translate.tools;

public class Annotation {

	private String text;
	private String mark;
	private String alternatives;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getAlternatives() {
		return alternatives;
	}

	public void setAlternatives(String alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public String toString() {
		return "Annotation [text=" + text + ", mark=" + mark + ", alternatives=" + alternatives + "]";
	}

	
	
}
