/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.parser.saxevent;

public final class HtmlUtil {
	
    public static final String A = "a";
    public static final String ALIGN = "align";
    public static final String ABBR = "abbr";
    public static final String ACRONYM = "acronym";
    public static final String ADDRESS = "address";
    public static final String APPLET = "applet";
    public static final String AREA = "area";
    public static final String ARTICLE= "article";
    public static final String ASIDE= "aside";
    public static final String AUDIO= "audio";
    public static final String B = "b";
    public static final String BIG = "big";
    public static final String BLOCK_QUOTE = "blockquote";
    public static final String BODY = "body";
    public static final String BORDER = "border";
    public static final String BR = "br";
    public static final String BASE = "base";
    public static final String BASEFONT = "basefont";
    public static final String BDI = "bdi";
    public static final String BDO = "bdo";
    public static final String BGSOUND = "bgsound";
    public static final String BLINK = "blink";
    public static final String BUTTON = "button";
    public static final String CELLSPACING = "cellspacing";
    public static final String CENTER = "center";
    public static final String CLASS = "class";
    public static final String COLGROUP = "colgroup";
    public static final String CANVAS = "canvas";
    public static final String CAPTION = "caption";
    public static final String CITE = "cite";
    public static final String CODE = "code";
    public static final String COL = "col";
    public static final String COMMAND = "command";
    public static final String CONTENT = "content";
    public static final String DIV = "div";
    public static final String DATA = "data";
    public static final String DATALIST = "datalist";
    public static final String DD = "dd";
    public static final String DEL = "del";
    public static final String DETAILS = "details";
    public static final String DFN = "dfn";
    public static final String DIALOG = "dialog";
    public static final String DIR = "dir";
    public static final String DL = "dl";
    public static final String DT = "dt";
    public static final String EM = "em";
    public static final String ELEMENT = "element";
    public static final String EMBED = "embed";
    public static final String FIELDSET = "fieldset";
    public static final String FONT = "font";
    public static final String FORM = "form";
    public static final String FIGCAPTION = "figcaption";
    public static final String FIGURE = "figure";
    public static final String FOOTER = "footer";
    public static final String FRAME = "frame";
    public static final String FRAMESET = "frameset";
    public static final String H1 = "h1";
    public static final String H2 = "h2";
    public static final String H3 = "h3";
    public static final String H4 = "h4";
    public static final String H5 = "h5";
    public static final String H6 = "h6";
    public static final String HEAD = "head";
    public static final String HR = "hr";
    public static final String HREF = "href";
    public static final String HTML = "html";
    public static final String HEADER = "header";
    public static final String HGROUP = "hgroup";
    public static final String I = "i";
    public static final String ID = "id";
    public static final String IMG = "img";
    public static final String INPUT = "input";
    public static final String IFRAME = "iframe";
    public static final String IMAGE = "image";
    public static final String INS = "ins";
    public static final String ISINDEX = "isindex";
    public static final String KBD = "kbd";
    public static final String KEYGEN = "keygen";
    public static final String LABEL = "label";
    public static final String LEGEND = "legend";
    public static final String LI = "li";
    public static final String LINK = "link";
    public static final String LISTING = "listing";
    public static final String MAIN = "main";
    public static final String MAP = "map";
    public static final String MARK = "mark";
    public static final String MARQUEE = "marquee";
    public static final String MENU = "menu";
    public static final String MENUITEM = "menuitem";
    public static final String META = "meta";
    public static final String METER = "meter";
    public static final String MULTICOL = "multicol";
    public static final String NAME = "name";
    public static final String NAV = "nav";
    public static final String NOBR = "nobr";
    public static final String NOEMBED = "noembed";
    public static final String NOFRAMES = "noframes";
    public static final String NOSCRIPT = "noscript";
    public static final String OL = "ol";
    public static final String ON_CLICK = "onclick";
    public static final String OBJECT = "object";
    public static final String OPTGROUP = "optgroup";
    public static final String OPTION = "option";
    public static final String OUTPUT = "output";
    public static final String P = "p";
    public static final String PARAM = "param";
    public static final String PICTURE = "picture";
    public static final String PLAINTEXT = "plaintext";
    public static final String PRE = "pre";
    public static final String PROGRESS = "progress";
    public static final String Q = "q";
    public static final String RB = "rb";
    public static final String RP = "rp";
    public static final String RT = "rt";
    public static final String RTC = "rtc";
    public static final String RUBY = "ruby";
    public static final String S = "s";
    public static final String SCRIPT = "script";
    public static final String SMALL = "small";
    public static final String SPAN = "span";
    public static final String STRIKE = "strike";
    public static final String STRONG = "strong";
    public static final String STYLE = "style";
    public static final String SAMP = "samp";
    public static final String SECTION = "section";
    public static final String SELECT = "select";
    public static final String SHADOW = "shadow";
    public static final String SLOT = "slot";
    public static final String SOURCE = "source";
    public static final String SPACER = "spacer";
    public static final String SUB = "sub";
    public static final String SUMMARY = "summary";
    public static final String SUP = "sup";
    public static final String TABLE = "table";
    public static final String TBODY = "tbody";
    public static final String TD = "td";
    public static final String TH = "th";
    public static final String THEAD = "thead";
    public static final String TITLE = "title";
    public static final String TR = "tr";
    public static final String TEMPLATE = "template";
    public static final String TEXTAREA = "textarea";
    public static final String TFOOT = "tfoot";
    public static final String TIME = "time";
    public static final String TRACK = "track";
    public static final String TT = "tt";
    public static final String U = "u";
    public static final String UL = "ul";
    public static final String WIDTH = "width";
    public static final String VAR = "var";
    public static final String VIDEO = "video";
    public static final String WBR = "wbr";
    public static final String XMP = "xmp";
	
	private final StringBuffer fB = new StringBuffer();
	
	void appendLines(final String... lines) {
		for (final String line: lines) {
			fB.append(line);
		}
	}
	
	public final String getString() {
		return fB.toString();
	}
	
	public void appendTable(final String value1A, final String value1B, final String value2A, final String value2B) {
		appendElement(TABLE,new Runnable() {
			@Override
			public void run() {
				appendRow(value1A,value1B);
				appendRow(value2A,value2B);
			}
		});
	}
	
	public void appendRow(final String value1, final String value2) {
		appendElement(TR,new Runnable() {
			@Override
			public void run() {
				appendDetail(value1);
				appendDetail(value2);
			}
		});}
	
	public void appendDetail(final String body) {
		appendElement(TD,new Runnable() {
			@Override
			public void run() {
				appendLines(body);
			}
		});
	}
	
	public void appendElement(final String tag, final Runnable body) {
		appendTag(tag,true);
		body.run();
		appendTag(tag,false);
	}
	
	public void appendTag(final String name, final boolean start) {
		appendLines(getTag(name,start));
	}
	
	public static String getTag(final String name, final boolean start) {
		return "<" + (start ? "" : "/") + name + ">";
	}
}
