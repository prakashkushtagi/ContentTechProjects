package com.trgr.dockets.core.parser.saxevent.events;

import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.DIV;

import org.apache.commons.lang.ObjectUtils;

import com.trgr.dockets.core.parser.saxevent.DocRange;
import com.trgr.dockets.core.parser.saxevent.HtmlUtil;

public abstract class ElementEvent extends SaxEvent {

	private final String fUri;
	
	private final String fLocalName;
	
	private final String fQName;
	
	protected ElementEvent(final DocRange docRange, final String uri, final String localName, final String qName) {
		super(docRange);
		fUri = uri;
		fLocalName = localName;
		fQName = qName;
	}
	
	@Override public final boolean isElement() {
		return true;
	}
	
	public abstract boolean isStart();
	
	public final boolean isEnd() {
		return !isStart();
	}
	
	public final String getUri() {
		return fUri;
	}
	
	public final String getLocalName() {
		return fLocalName;
	}

	public final String getQName() {
		return fQName;
	}
	
	public final boolean hasQName(final String qName) {
		return ObjectUtils.equals(qName, fQName);
	}
	
	public final boolean isDiv() {
		return hasQName(DIV);
	}
	
	public final boolean matches(final boolean start, final String qName) {
		return (start == isStart()) && hasQName(qName);
	}
	
	@Override public final String getSummary() {
		return HtmlUtil.getTag(fQName, isStart());
	}
}
