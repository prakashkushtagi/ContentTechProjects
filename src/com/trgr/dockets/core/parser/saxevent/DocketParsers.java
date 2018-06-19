/** 
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.parser.saxevent;

import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.A;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.B;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.BODY;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.BR;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.CENTER;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.DIV;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.FONT;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.FORM;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.H2;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.H3;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.HR;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.HTML;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.I;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.ID;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.INPUT;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.P;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.SCRIPT;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.SPAN;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.TABLE;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.TBODY;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.TD;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.TH;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.TR;
import static com.trgr.dockets.core.util.PredicateJ8Defaults.negate;
import static org.codehaus.jparsec.Parsers.ANY_TOKEN;
import static org.codehaus.jparsec.Parsers.or;
import static org.codehaus.jparsec.Parsers.pair;
import static org.codehaus.jparsec.Parsers.sequence;
import static org.codehaus.jparsec.Parsers.tuple;
import static org.codehaus.jparsec.PredicateParser.predicateParser;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Map2;
import org.codehaus.jparsec.functors.Map3;
import org.codehaus.jparsec.functors.Pair;
import org.codehaus.jparsec.functors.Tuple3;
import org.codehaus.jparsec.functors.Tuple4;
import org.codehaus.jparsec.functors.Tuple5;

import com.trgr.dockets.core.parser.saxevent.data.AttributesData;
import com.trgr.dockets.core.parser.saxevent.data.CharData;
import com.trgr.dockets.core.parser.saxevent.data.ElementData;
import com.trgr.dockets.core.parser.saxevent.data.XmlData;
import com.trgr.dockets.core.parser.saxevent.data.XmlDataItems;
import com.trgr.dockets.core.parser.saxevent.events.CharacterEvent;
import com.trgr.dockets.core.parser.saxevent.events.ElementEvent;
import com.trgr.dockets.core.parser.saxevent.events.EndElementEvent;
import com.trgr.dockets.core.parser.saxevent.events.SaxEvent;
import com.trgr.dockets.core.parser.saxevent.events.StartElementEvent;
import com.trgr.dockets.core.util.ObjectList;
import com.trgr.dockets.core.util.PredicateJ8;
import com.trgr.dockets.core.util.Strings;

public final class DocketParsers {
	/**
	 * If set to true then debugging text will be printed to System.out
	 */
	public static boolean outputDebug = false;
	
	/** Tags that the generic element parser won't parse */
	public static final List<String> GENERIC_PARSER_IGNORED_TAGS = Arrays.asList("a","br");
	
	public static final Parser<SaxEvent> EVENT_PARSER = ANY_TOKEN.cast();
	
	public static final Parser<String> EVENT_SUMMARY_PARSER = eventSummaryParser(EVENT_PARSER);
	
	public static final Parser<String> EVENT_SUMMARY_DEBUG_PARSER = EVENT_SUMMARY_PARSER;
	
	public static final Parser<List<String>> EVENT_SUMMARIES_PARSER = eventSummariesParser(EVENT_PARSER);
	
	public static final Parser<List<String>> EVENT_SUMMARIES_DEBUG_PARSER = EVENT_SUMMARY_DEBUG_PARSER.many();
	
	/** Parses element */
	private static final Parser<ElementEvent> ELEMENT_EVENT_PARSER = eventParser(new PredicateJ8<SaxEvent>() {
		@Override
		public boolean test(final SaxEvent event) {
			return event.isElement();
		}
	});
	
	/** Parses out a start element */
	public static final Parser<StartElementEvent> START_ELEMENT_EVENT_PARSER =
			elementEventParserCast(new PredicateJ8<ElementEvent>() {
				@Override
				public boolean test(final ElementEvent event) {
					return event.isStart();
				}
			});
	
	/**
	 * Consumes a start or end element based on what value is given to startElement.
	 * When parsing a start element the qName is stored as the value in the qNameHolder.  
	 * When parsing an end element the qNameHolder is used to verify the qName of the end element.
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param qNameHolder The element qName is either stored here or refereneced from here depending on if a start or end element is processed respectively
	 * @param startElement If true then this will process a start element
	 * @param ignoredTags List of tags that should not be processed
	 * @return Returns the StartElementEvent or EndElementEvent
	 */
	public static final Parser<ElementEvent> identifyingElementParser(
			final Stack<String> qNameHolder, final boolean startElement, final List<String> ignoredTags) {
		return elementEventParserCast(new PredicateJ8<ElementEvent>() {
			@Override
			public boolean test(final ElementEvent event) {
				boolean success = false;
				String qName = event.getQName();
				if (ignoredTags != null && ignoredTags.contains(qName)) {
					// Don't process tags these tags
					return false;
				}
				if (startElement) {
					success = event.isStart();
					if (success) {
						qNameHolder.push(qName);
						if (outputDebug) {
							System.out.println("identifyingElementParser - Found start:" + qName);
						}
					}
				} else {
					success = event.matches(false, qNameHolder.peek());
					if (success) {
						if (outputDebug) {
							System.out.println("identifyingElementParser - matched end:" + qName);
						}
						qNameHolder.pop();
					}
				}
				return success;
			}
		});
	}
	
	/** Parses end elements */
	public static final Parser<EndElementEvent> END_ELEMENT_EVENT_PARSER =
			elementEventParserCast(new PredicateJ8<ElementEvent>() {
				@Override
				public boolean test(final ElementEvent event) {
					return event.isEnd();
				}
			});
	
	public static final Parser<StartElementEvent> BODY_START_ELEMENT_PARSER = startElementParser(BODY);
	
	public static final Parser<StartElementEvent> HTML_START_ELEMENT_PARSER = startElementParser(HTML);
	
	/** Parses out a character event */
	public static final Parser<CharacterEvent> CHARACTER_EVENT_PARSER = eventParser(new PredicateJ8<SaxEvent>() {
		@Override
		public boolean test(final SaxEvent event) {
			return event.isCharacter();
		}
	});
	
	/** Parses out the list of character events */
	public static Parser<List<CharacterEvent>> CHARACTER_EVENTS_PARSER = CHARACTER_EVENT_PARSER.many();

	/** Parses out the list of character events.  There must be at least 1 character */
	public static Parser<List<CharacterEvent>> CHARACTER_EVENTS_PARSER_PLUS = CHARACTER_EVENT_PARSER.many1();
	
	/** Identifies the end of the document */
	public static final Parser<SaxEvent> END_OF_DOC_PARSER = eventParser(new PredicateJ8<SaxEvent>() {
		@Override
		public boolean test(final SaxEvent event) {
			return event.isEndOfDoc();
		}
	});
	
	public static final Parser<SaxEvent> HTML_EVENT_PARSER = eventParser(new PredicateJ8<SaxEvent>() {
		@Override
		public boolean test(final SaxEvent event) {
			return event.isNotEndOfDoc();
		}
	});
	
	/** Combines character events into a String */
	public static final Parser<String> CHARACTER_PARSER = characterParser(CHARACTER_EVENTS_PARSER);
	
	public static final Parser<String> NON_TRIMMING_CHARACTER_PARSER = nonTrimmingCharacterParser(CHARACTER_EVENTS_PARSER);
	
	/** Combines character events into a String.  There must be at least 1 character */
	public static final Parser<String> CHARACTER_PARSER_PLUS = characterParser(CHARACTER_EVENTS_PARSER_PLUS);
	
	private static final Parser.Reference<StartElementEvent> ELEMENT_PARSER_REF = Parser.newReference();
	
	/** Element parser */
	public static final Parser<StartElementEvent> ELEMENT_PARSER = ELEMENT_PARSER_REF.lazy();
	
	/** Consumes a br Element */
	public static final Parser<StartElementEvent> BR_PARSER = simpleElementParser(BR);
	
	/** Consumes a hr Element */
	public static final Parser<StartElementEvent> HR_PARSER = simpleElementParser(HR);
	
	/** Consumes a br Element and all characters following */
	public static final Parser<StartElementEvent> BR_CF_PARSER = simpleElementCFParser(BR);
	
	/** Consumes an HR element and all characters following */
	public static final Parser<StartElementEvent> HR_CF_PARSER = simpleElementCFParser(HR);
	
	/** Consumes a 'input' Element and all characters following */
	public static final Parser<StartElementEvent> INPUT_CF_PARSER = simpleElementCFParser(INPUT);
	
	/** Consumes a 'table' Element and all characters following */
	public static final Parser<StartElementEvent> TABLE_CF_PARSER = simpleElementCFParser(TABLE);
	
	/** Consumes a TD element and all following characters */
	public static final Parser<StartElementEvent> TD_CF_PARSER = simpleElementCFParser(TD);
	
	/** Parses an element or character */
	private static final Parser<SaxEvent> INTERNAL_PARSER = or(CHARACTER_EVENT_PARSER.atomic(),ELEMENT_PARSER);
	
	/** Parses out multiple elements and characters */
	public static final Parser<List<SaxEvent>> INTERNALS_PARSER = INTERNAL_PARSER.many();
	
	public static final Parser<EndElementEvent> ELEMENT_TAIL_PARSER = INTERNALS_PARSER.next(END_ELEMENT_EVENT_PARSER);
	
	static {ELEMENT_PARSER_REF.set(START_ELEMENT_EVENT_PARSER.followedBy(ELEMENT_TAIL_PARSER));}
	
	public static final Parser<List<SaxEvent>> INTERNALS_BEFORE_BODY_PARSER = internalsBeforeParser(BODY);
	public static final Parser<List<SaxEvent>> INTERNALS_BEFORE_FORM_PARSER = internalsBeforeParser(FORM);
	public static final Parser<List<SaxEvent>> INTERNALS_BEFORE_P_PARSER = internalsBeforeParser(P);
	
	public static final Parser<String> FONT_STRING_PARSER = elementStringParser(FONT);
	public static final Parser<String> I_STRING_PARSER = elementStringParser(I);
	public static final Parser<String> SPAN_STRING_PARSER = elementStringParser(SPAN);
	public static final Parser<String> TD_STRING_PARSER = elementStringParser(TD);
	
	public static final Parser<String> B_STRING_CF_PARSER = elementStringCFParser(B);
	public static final Parser<String> BODY_STRING_CF_PARSER = elementStringCFParser(BODY);
	public static final Parser<String> DIV_STRING_CF_PARSER = elementStringCFParser(DIV);
	public static final Parser<String> H3_STRING_CF_PARSER = elementStringCFParser(H3);
	public static final Parser<String> I_STRING_CF_PARSER = elementStringCFParser(I);
	public static final Parser<String> P_STRING_CF_PARSER = elementStringCFParser(P);
	public static final Parser<String> SPAN_STRING_CF_PARSER = elementStringCFParser(SPAN);
	/**
	 * Consumes a TD element and the characters inside it returning those 
	 * characters in the form of a String object.  Continues to consume any 
	 * characters that exist after the TD element
	 * @return Returns the String value inside the element name given
	 */
	public static final Parser<String> TD_STRING_CF_PARSER = elementStringCFParser(TD);
	
	/** Consumes an a element and everything inside it. */
	public static final Parser<List<SaxEvent>> A_PARSER = elemEventsParser(A);
	/** Consumes a body element and everything inside it. */
	public static final Parser<List<SaxEvent>> BODY_PARSER = elemEventsParser(BODY);
	/** Consumes a center element and everything inside it. */
	public static final Parser<List<SaxEvent>> CENTER_PARSER = elemEventsParser(CENTER);
	/** Consumes a div element and everything inside it. */
	public static final Parser<List<SaxEvent>> DIV_PARSER = elemEventsParser(DIV);
	/** Consumes a form element and everything inside it. */
	public static final Parser<List<SaxEvent>> FORM_PARSER = elemEventsParser(FORM);
	public static final Parser<Pair<String,List<SaxEvent>>> FORM_AND_NAME_PARSER = elemHEventsParser(FORM, "name");
	/** Consumes a p element and everything inside it. */
	public static final Parser<List<SaxEvent>> P_PARSER = elemEventsParser(P);
	public static final Parser<List<SaxEvent>> SCRIPT_PARSER = elemEventsParser(SCRIPT);
	/** Consumes a table element and everything inside it. */
	public static final Parser<List<SaxEvent>> TABLE_PARSER = elemEventsParser(TABLE);
	/** Consumes a "td" element and everything inside it. */
	public static final Parser<List<SaxEvent>> TD_PARSER = elemEventsParser(TD);
	/** Consumes a "tr" element and everything inside it. */
	public static final Parser<List<SaxEvent>> TR_PARSER = elemEventsParser(TR);
	/** Consumes a "th" element and everything inside it. */
	public static final Parser<List<SaxEvent>> TH_PARSER = elemEventsParser(TH);
	
	public static final Parser.Reference<ElementData> ELEMENT_DATA_PARSER_REF = Parser.newReference();
	
	public static final Parser<ElementData> ELEMENT_DATA_PARSER = ELEMENT_DATA_PARSER_REF.lazy();
	
	public static final Parser<AttributesData> ATTRIBUTES_DATA_PARSER = 
			START_ELEMENT_EVENT_PARSER.map(new Map<StartElementEvent, AttributesData>() {
				@Override
				public AttributesData map(StartElementEvent event) {
					return new AttributesData(event);
				}
			});
	
	public static final Parser<CharData> CHAR_DATA_PARSER =
			CHARACTER_PARSER_PLUS.map(new Map<String, CharData>() {
				@Override
				public CharData map(String string) {
					return new CharData(string);
				}
			});
	
	public static final Parser<XmlData> XML_DATA_PARSER = or(CHAR_DATA_PARSER.atomic(),ELEMENT_DATA_PARSER);
	
	public static final Parser<XmlDataItems> XML_DATA_ITEMS_PARSER =
			XML_DATA_PARSER.many().map(new Map<List<XmlData>, XmlDataItems>() {
				@Override
				public XmlDataItems map(List<XmlData> list) {
					return new XmlDataItems(list);
				}
			});
	
	public static final Parser<ElementData> RAW_ELEMENT_DATA_PARSER =
			sequence(ATTRIBUTES_DATA_PARSER,XML_DATA_ITEMS_PARSER,END_ELEMENT_EVENT_PARSER,
					new Map3<AttributesData, XmlDataItems, EndElementEvent, ElementData>() {
						@Override
						public ElementData map(AttributesData attributes, XmlDataItems dataItems,
								EndElementEvent endEvent) {
							return new ElementData(attributes,dataItems,endEvent);
						}
					});
	
	static {
		ELEMENT_DATA_PARSER_REF.set(RAW_ELEMENT_DATA_PARSER);
	}
	
	/**
	 * Consumes any character events prior the reaching the baseParser
	 * @param baseParser Parser to find results
	 * @return Returns the results of the baseParser and consumes any 
	 * characters that exist leading up to the baseParser
	 */
	public static final <T> Parser<T> charsBeforeParser(final Parser<T> baseParser) {
		return CHARACTER_EVENTS_PARSER.next(baseParser);
	}
	
	/**
	 * Consumes any characters that exist after the baseParser has finished
	 * @param baseParser Parser to find the desired content
	 * @return Returns the results of the baseParser and consumes any 
	 * characters that exist after the baseParser has finished.
	 */
	public static final <T> Parser<T> charsAfterParser(final Parser<T> baseParser) {
		return baseParser.followedBy(CHARACTER_EVENTS_PARSER);
	} 
	
	public static Parser<ElementEvent> elementEventParser(final PredicateJ8<ElementEvent> predicate) {
		return predicateParser("elementEvent",ELEMENT_EVENT_PARSER,predicate);
	} 
	
	/**
	 * Parses either the start or end element with the given qName
	 * @param start If true this will parse start elements, otherwise this will parse end elements
	 * @param qName The element name to parse
	 * @return Returns the start or end element
	 */
	public static Parser<ElementEvent> elementEventParser(final boolean start, final String qName) {
		return elementEventParser(new PredicateJ8<ElementEvent>() {
			@Override
			public boolean test(final ElementEvent event) {
				boolean success = event.matches(start, qName);
				if (outputDebug) {
					String type = start ? "start" : "end";
					String foundType = event.isStart() ? "start" : "end";
					String result = success ? "Found " : "Searching for ";
					System.out.println("elementEventParser " + result + type + ":" + qName + " - Found " + foundType + ":" + event.getQName());
				}
				return success;
			}
		});
	} 
	
	private static <E extends ElementEvent> Parser<E> elementEventParserCast(
			final PredicateJ8<ElementEvent> predicate) {
		return elementEventParser(predicate).cast();
	}
	
	/**
	 * Combines a list of character events into a String
	 * @param characterEventsParser Parser to gather a list of String events
	 * @return Returns a String containing the character event values
	 */
	public static final Parser<String> characterParser(final Parser<List<CharacterEvent>> characterEventsParser) {
		return nonTrimmingCharacterParser(characterEventsParser).map(new Map< String, String>() {
			@Override
			public String map(final String events) {
				String trimmed = events.trim();
				if (outputDebug) {
					System.out.println("characterParser trimmed the following text:[" + trimmed + "]");
				}
				return trimmed;
			}
		});
	}
	
	public static final Parser<String> nonTrimmingCharacterParser(final Parser<List<CharacterEvent>> characterEventsParser) {
		return characterEventsParser.map(new Map<List<CharacterEvent>, String>() {
			@Override
			public String map(final List<CharacterEvent> events) {
				final StringBuilder b = new StringBuilder();
				for (final CharacterEvent event: events) {
					b.append(event.getText());
				}
				String str = b.toString();
				if (outputDebug) {
					System.out.println("nonTrimmingCharacterParser Found the following text:[" + str + "]");
				}
				return str;
			}
		});
	}
	
	/**
	 * Reads the String value out of an element
	 * @param qName Element to parse
	 * @return 
	 */
	public static final Parser<String> elementStringParser(final String qName) {
		return elementParser(qName,CHARACTER_PARSER);
	}
	
	/**
	 * Consumes the given element and the characters inside it returning those 
	 * characters in the form of a String object.  Continues to consume any 
	 * characters that exist after the given element
	 * @param qName Element name to parse after
	 * @return Returns the String value inside the element name given
	 */
	public static final Parser<String> elementStringCFParser(final String qName) {
		return charsAfterParser(elementStringParser(qName));
	}
	
	/**
	 * Parses out the String value from the named element if it exists.
	 * @param qName Element name to parse after
	 * @return Returns the String value inside the element name given
	 */
	public static final Parser<String> elementStringCFParserOpt(final String qName) {
		return elementStringCFParser(qName).optional();
	}
	
	public static final Parser<String> divStringCFParser() {
		return elementStringCFParser(DIV);
	}
	
	public static final Parser<String> nextElementStringParser(final String qName) {
		return charsBeforeParser(elementStringParser(qName));
	}
	
	public static final Parser<Strings> strsParser(final Parser<String> stringParser) {
		return stringsParser(stringParser.many());
	}
	
	public static final Parser<Strings> stringsParser(final Parser<List<String>> stringListParser) {
		return stringListParser.map(new Map<List<String>, Strings>() {
			@Override
			public Strings map(List<String> list) {
				return new Strings(list);
			}
		});
	}
	
	private static <E extends SaxEvent> Parser<E> eventParser(final PredicateJ8<SaxEvent> predicate) {
		return predicateParser("event",EVENT_PARSER,predicate).cast();
	}
	
	public static final Parser<String> eventSummaryParser(final Parser<SaxEvent> eventParser) {
		return eventParser.map(new Map<SaxEvent, String>() {
			@Override
			public String map(final SaxEvent event) {
				return event.getSummary();
			}
		});
	}
	
	public static final Parser<List<String>> eventSummariesParser(final Parser<SaxEvent> eventParser) {
		return eventSummaryParser(eventParser).many();
	}
	
	/**
	 * Parses the start element with the given qName
	 * @param qName Name of element to parse
	 * @return Returns the start element event
	 */
	public static Parser<StartElementEvent> startElementParser(final String qName) {
		return elementEventParser(true,qName).cast();
	}
	
	/**
	 * Consumes all characters following the start element with the given qName 
	 * @param qName Name of the start element
	 * @return Returns the start element with the given qName
	 */
	public static Parser<StartElementEvent> startElementCFParser(final String qName) {
		return charsAfterParser(startElementParser(qName));
	}
	
	/**
	 * Parses out an attribute from a start element
	 * @param startElemParser Parser to find the start element
	 * @param idName Name of the attribute to parse 
	 * @return Returns the attribute value from a start element as a String
	 */
	public static Parser<String> startElementWithAttrParser(
			final Parser<StartElementEvent> startElemParser, final String idName) {
		return startElemParser.map(new Map<StartElementEvent, String>() {
			@Override
			public String map(StartElementEvent event) {
				return event.getAttributeValue(idName);
			}
		});
	}
	
	/**
	 * Parses out the attribute value from the element with the given name.  
	 * Consumes the start element.
	 * @param elemQName Element name to parse from
	 * @param idName Attribute name to parse from
	 * @return Returns the attribute value
	 */
	public static Parser<String> startElementWithAttrParser(final String elemQName, final String idName) {
		return startElementWithAttrParser(startElementParser(elemQName),idName);
	}
	
	/**
	 * Parses out the attribute value from the element with the given name.  
	 * Consumes the start element and all characters following.
	 * @param elemQName Element name to parse from
	 * @param idName Attribute name to parse from
	 * @return Returns the attribute value
	 */
	public static Parser<String> startElementWithAttrCFParser(final String elemQName, final String idName) {
		return startElementWithAttrParser(startElementCFParser(elemQName),idName);
	}
	
	public static Parser<String> startElementWithIdParser(final String elemQName) {
		return startElementWithAttrParser(elemQName,ID);
	}
	
	public static Parser<String> startElementWithIdCFParser(final String elemQName) {
		return startElementWithAttrCFParser(elemQName,ID);
	}
	
	/**
	 * Parses the end element with the given qName
	 * @param qName Name of element to parse
	 * @return Returns the end element event
	 */
	public static Parser<ElementEvent> endElementParser(final String qName) {
		return elementEventParser(false,qName);
	}
	
	/**
	 * Consumes all characters following the end element with the given qName 
	 * @param qName Name of the end element
	 * @return Returns the end element with the given qName
	 */
	private static Parser<ElementEvent> endElementCFParser(final String qName) {
		return charsAfterParser(endElementParser(qName));
	}
	
	/**
	 * Uses the contentParser to get results and consumes the start and end 
	 * elements with the given qName that come before and after the contentParser.
	 * @param qName Element name
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> elementParser(final String qName, final Parser<T> contentParser) {
		return startElementParser(qName).next(endElemAfterParser(qName,contentParser));
	}
	
	/**
	 * Uses the contentParser to get results and consumes the start and end 
	 * elements that come before and after the contentParser.  Does not required a tag name to be given.
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> genericElementParser(final Parser<T> contentParser) {
		Stack<String> qNameStack = new Stack<String>();
		return identifyingElementParser(qNameStack, true, GENERIC_PARSER_IGNORED_TAGS)
				.next(contentParser)
				.followedBy(identifyingElementParser(qNameStack, false, GENERIC_PARSER_IGNORED_TAGS)).atomic();
	}
	
	/**
	 * Uses the contentParser to get results and consumes the start element 
	 * with the given qName that comes before the contentParser.  Does not 
	 * consume the end element.
	 * @param qName Element name
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> elementStartParser(final String qName, final Parser<T> contentParser) {
		return startElementParser(qName).next(contentParser);
	} 
	
	public static <T> Parser<Pair<StartElementEvent, T>> elemWithAttrsParser(
			final String qName, final Parser<T> contentParser) {
		return endElemAfterParser(qName,pair(startElementParser(qName), contentParser));
	}
	
	/**
	 * Uses the basePaser to get results and then consumes the end element with the given qName
	 * @param qName Name of end element to parse
	 * @param baseParser Parser that gathers the results
	 * @return Returns the objects parsed from the baseParser
	 */
	public static <T> Parser<T> endElemAfterParser(final String qName, final Parser<T> baseParser) {
		return baseParser.followedBy(endElementParser(qName));
	}
	
	/** Element, consume Chars Before Internal Parser */
	public static <T> Parser<T> elementCBIParser(final String qName, final Parser<T> contentParser) {
		return elementParser(qName,charsBeforeParser(contentParser));
	}
	
	/**
	 * Element Start, consume Chars Before Internal Parser.<br/>
	 * Consumes the start element with the given qName and any characters 
	 * before the given internal parser.
	 * 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param qName Element name to parse
	 * @param internalParser Parser that gathers the results
	 * @return Returns the objects parsed from the internalParser
	 */
	public static <T> Parser<T> elementStartCBIParser(final String qName, final Parser<T> internalParser) {
		return elementStartParser(qName,charsBeforeParser(internalParser));
	}
	
	/**
	 * Element, consume Chars Following but Not Before Internal Parser<br/>
	 * Consumes the given element name, uses the given parser, then consumes 
	 * any trailing characters after the given element.
	 * @param qName Element to parse
	 * @param internalContentParser Internal parser
	 * @return Returns what the internal parser parsed out.
	 */
	public static <T> Parser<T> elementCFNBIParser(final String qName, final Parser<T> internalContentParser) {
		return charsAfterParser(elementParser(qName,internalContentParser));
	}
	
	/**
	 * Element, consume Chars Following Parser<br/>
	 * Consumes any characters before or after the given element name and uses the given internalContentParser
	 * 
	 * @param qName Element to parse
	 * @param internalContentParser Internal parser
	 * @return Returns what the internal parser parsed out.
	 */
	public static <T> Parser<T> elementCFParser(final String qName, final Parser<T> internalContentParser) {
		return elementCFNBIParser(qName,charsBeforeParser(internalContentParser));
	}
	
	public static <T> Parser<Pair<StartElementEvent,T>> elemWithAttrsCFParser(
			final String qName, final Parser<T> contentParser) {
		return charsAfterParser(elemWithAttrsParser(qName,charsBeforeParser(contentParser)));
	}
	
	/**
	 * Consumes the element with the given qName
	 * @param qName Name of element to consume
	 * @return Returns a list of SaxEvents for that element
	 */
	public static final Parser<List<SaxEvent>> elemEventsParser(final String qName) {
		return elementCFParser(qName, INTERNALS_PARSER);
	}
	
	/**
	 * Parses out the desired attribute value from the element with the given qName.  Consumes the start and end elements of qName, everything in between, and any characters following. 
	 * @param qName Element to parse
	 * @param idName Attribute name to parse
	 * @return Returns the value of the attribute 
	 */
	public static final Parser<Pair<String,List<SaxEvent>>> elemHEventsParser(final String qName, final String idName) {
		return elementHParser(qName, idName, INTERNALS_PARSER);
	}
	
	/**
	 * Element, consume Chars Following Parser
	 * Consumes any characters before or after the 'body' element and uses the given contentParser
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> bodyCFParser(final Parser<T> contentParser) {
		return elementCFParser(BODY,contentParser);
	}
	
	public static <T> Parser<T> divCFParser(final Parser<T> contentParser) {
		return elementCFParser(DIV,contentParser);
	}
	
	/**
	 * Uses the contentParser to get results and consumes the start and end 
	 * font elements that come before the contentParser.
	 * @param qName Element name
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> fontParser(final Parser<T> contentParser) {
		return elementParser(FONT,contentParser);
	}
	
	public static <T> Parser<T> fontCFParser(final Parser<T> contentParser) {
		return elementCFParser(FONT,contentParser);
	}
	
	/**
	 * Uses the contentParser to get results.  Can also parse the same content 
	 * encapsulated by font elements
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> fontOptParser(final Parser<T> contentParser) {
		return elementOptParser(FONT,contentParser);
	}
	
	/**
	 * Uses the contentParser to get results.  Can also parse the same content 
	 * encapsulated by an element with the given qName
	 * @param qName Element Name
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> elementOptParser(final String qName, final Parser<T> contentParser) {
		return or(elementParser(qName, contentParser).atomic(),contentParser);
	}
	
	/**
	 * Element, consume Chars Following Parser<br/>
	 * Consumes any characters before or after the given element name and uses 
	 * the given internalContentParser.  Can also parse the same content 
	 * encapsulated by a FONT element
	 * 
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the objects parsed from the contentParser
	 */
	public static <T> Parser<T> fontOptCFParser(final Parser<T> contentParser) {
		return elementOptCFParser(FONT, contentParser);
	}
	
	/**
	 * Element, consume Chars Following Parser<br/>
	 * Consumes any characters before or after the given element name and uses 
	 * the given internalContentParser.  Can also parse the same content 
	 * encapsulated by an element with the given qName
	 * 
	 * @param qName Element to parse
	 * @param internalContentParser Internal parser
	 * @return Returns what the internal parser parsed out.
	 */
	public static <T> Parser<T> elementOptCFParser(final String qName, final Parser<T> contentParser) {
		return or(elementCFParser(qName, contentParser).atomic(),contentParser);
	}
	
	public static <T> Parser<T> formCFParser(final Parser<T> contentParser) {
		return elementCFParser(FORM, contentParser);
	}
	
	public static <T> Parser<T> htmlCFParser(final Parser<T> contentParser) {
		return rootElementCFParser(HTML, contentParser);
	}
	
	/**
	 * Parser that can handle the end of the document.
	 * @param qName Root element to parse
	 * @param contentParser Parser to handle the contents of the document
	 * @return Returns the results of the contentParser
	 */
	public static <T> Parser<T> rootElementCFParser(final String qName, final Parser<T> contentParser) {
		return elementCFParser(qName,contentParser).followedBy(END_OF_DOC_PARSER.optional());
	}
	
	/**
	 * Consumes any characters before or after a table element and returns the 
	 * given contentParser results
	 * @param contentParser Parser to gather the information
	 * @return Returns the results of the contentParser
	 */
	public static <T> Parser<T> tableCFParser(final Parser<T> contentParser) {
		return elementCFParser(TABLE,contentParser);
	}
	
	/**
	 * Consumes any characters before or after a CENTER element and returns the 
	 * given contentParser results
	 * @param contentParser Parser to gather the information
	 * @return Returns the results of the contentParser
	 */
	public static <T> Parser<T> centerCFParser(final Parser<T> contentParser) {
		return elementCFParser(CENTER,contentParser);
	}
	
	/**
	 * Uses the contentParser to get results inside a CENTER element.  
	 * Consumes the start and end CENTER elements.
	 * @param contentParser Parser to gather the information
	 * @return Returns the results of the contentParser
	 */
	public static <T> Parser<T> centerParser(final Parser<T> contentParser) {
		return elementParser(CENTER,contentParser);
	}
	
	public static <T> Parser<T> tableTrCFParser(final Parser<T> contentParser) {
		return tableCFParser(trCFParser(contentParser));
	}
	
	public static <T> Parser<T> tbodyCFParser(final Parser<T> contentParser) {
		return elementCFParser(TBODY,contentParser);
	}
	
	/**
	 * Consumes the TD elements before and after the contentParser
	 * @param contentParser Parser to gather information
	 * @return Returns the results of the contentParser 
	 */
	public static <T> Parser<T> tdParser(final Parser<T> contentParser) {
		return elementParser(TD,contentParser);
	}
	
	/**
	 * Element, consume Chars Following Parser
	 * Consumes any characters before or after the "td" element and then uses the given internalContentParser
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param contentParser
	 * @return
	 */
	public static <T> Parser<T> tdCFParser(final Parser<T> contentParser) {
		return elementCFParser(TD,contentParser);
	}
	
	/**
	 * Consumes any characters before or after a TR element then parses with 
	 * the given contentParser
	 * @param contentParser
	 * @return Returns the contentParser results
	 */
	public static <T> Parser<T> trCFParser(final Parser<T> contentParser) {
		return elementCFParser(TR,contentParser);
	}
	
	public static <T> Parser<T> trTdCFParser(final Parser<T> contentParser) {
		return trCFParser(tdCFParser(contentParser));
	}
	
	public static <T> Parser<T> trTdFontOptCFParser(final Parser<T> contentParser) {
		return trTdCFParser(fontOptCFParser(contentParser));
	}
	
	/**
	 * Parses the start element and consumes the end element if it follows
	 * @param qName Name of the element to parse
	 * @return Returns the start element
	 */
	public static Parser<StartElementEvent> simpleElementParser(final String qName) {
		return startElementParser(qName).followedBy(endElementParser(qName));
	}
	
	/**
	 * Parses the start element and consumes the end element if it follows and any characters following.  Use to consume the element with the given qName.
	 * @param qName Name of the element to parse
	 * @return Returns the start element
	 */
	public static Parser<StartElementEvent> simpleElementCFParser(final String qName) {
		return charsAfterParser(simpleElementParser(qName));
	}
	
	/**
	 * Parses out the desired attribute value from the element with the given qName 
	 * and then runs the contentParser.  
	 * This will also consume the end element of qName and any characters following.  
	 * @param qName The element name to look for the attribute in
	 * @param idName The attribute name to parse
	 * @param contentParser Parser to run from the qName element
	 * @return Returns a pair object containing first the attribute value that 
	 * was parsed and then the results of the contentParser.
	 */
	public static <T> Parser<Pair<String,T>> elementHParser(
			final String qName, final String idName, final Parser<T> contentParser) {
		return tuple(startElementWithAttrCFParser(qName,idName),contentParser).followedBy(endElementCFParser(qName));
	}
	
	/**
	 * Parses with baseParser and then consumes characters and start elements 
	 * @param baseParser Parser to gather information
	 * @return Returns the results of the baseParser
	 */
	public static final <T> Parser<T> internalsAfterParser(final Parser<T> baseParser) {
		return baseParser.followedBy(INTERNALS_PARSER);
	} 
	
	/**
	 * Uses the contentParser to get results and consumes the start and end 
	 * "tr" elements that come before and after the contentParser.
	 * 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param contentParser Object type that exists inside the element
	 * @return Returns the results of the contentParser
	 */
	public static <T> Parser<T> trParser(final Parser<T> contentParser) {
		return elementParser(TR,contentParser);
	}
	
	public static <T> Parser<T> tableParser(final Parser<T> contentParser) {
		return elementParser(TABLE,contentParser);
	}
	
	public static <T> Parser<T> tableCBIParser(final Parser<T> contentParser) {
		return elementCBIParser(TABLE,contentParser);
	}
	
	/**
	 * Parses anything before the given start element
	 * @param fencePredicate Element to stop parsing at
	 * @return Returns a list of SAX events
	 */
	public static Parser<List<SaxEvent>> internalsBeforeParser(final PredicateJ8<StartElementEvent> fencePredicate) {
		final PredicateJ8<StartElementEvent> includePredicate = negate(fencePredicate);
		final Parser.Reference<StartElementEvent> elementParserRef = Parser.newReference();
		final Parser<StartElementEvent> elementParser = elementParserRef.lazy();
		final Parser<SaxEvent> internalParser = or(CHARACTER_EVENT_PARSER.atomic(),elementParser);
		final Parser<List<SaxEvent>> internalsParser = internalParser.many();
		elementParserRef.set(
				predicateParser("element",START_ELEMENT_EVENT_PARSER,includePredicate).followedBy(ELEMENT_TAIL_PARSER));
		return internalsParser;
	}
	
	static int myCount = 0;
	static String checkList = null;
	/**
	 * Parses everything before the given elements
	 * @param qNames Element names to stop parsing at
	 * @return Returns the list of SAX events
	 */
	public static Parser<List<SaxEvent>> internalsBeforeParser(final String... qNames) {
		return internalsBeforeParser(new PredicateJ8<StartElementEvent>() {
			List<String> names = Arrays.asList(qNames);
			
			@Override
			public boolean test(final StartElementEvent event) {
				if (outputDebug) {
					if (checkList == null || !checkList.equals(names.toString())) {
						checkList = names.toString();
						myCount = 0;
					}
					
					System.out.println("internalsBeforeParser Searching for " + names + " - found #" + myCount + ":" + event.getQName());
				}
				
				for (final String qName: qNames) {
					if (event.hasQName(qName)) {
						if (outputDebug) {
							System.out.println("internalsBeforeParser Found " + event.getQName() + " at check #" + myCount);
							myCount = 0;
						}
						return true;
					}
				}
				myCount++;
				return false;
			}
		});
	}
	
	/**
	 * Gathers content from parser and then consumes everything until it finds 
	 * an element with the given qNames
	 * @param qNames Element names to stop consuming at
	 * @param parser Parser that gathers the information
	 * @return Returns the results of the given parser
	 */
	public static <T> Parser<T> nonInternalsAfter(final Parser<T> parser, final String... qNames) {
		return parser.followedBy(internalsBeforeParser(qNames));
	}
	
	/**
	 * Gathers content from parser and then consumes everything until it finds 
	 * the next div element
	 * @param parser Parser that gathers the information
	 * @return Returns the results of the given parser
	 */
	public static <T> Parser<T> nonDivsAfter(final Parser<T> parser) {
		return nonInternalsAfter(parser, DIV);
	}
	
	/**
	 * Gathers content from parser and then consumes everything until it finds 
	 * the next table element
	 * @param parser Parser that gathers the information
	 * @return Returns the results of the given parser
	 */
	public static <T> Parser<T> nonTablesAfter(final Parser<T> parser) {
		return nonInternalsAfter(parser, TABLE);
	}
	
	/**
	 * Gathers content from parser and then consumes everything until it finds 
	 * the next table or h2 element
	 * @param parser Parser that gathers the information
	 * @return Returns the results of the given parser
	 */
	public static <T> Parser<T> nonTablesOrH2After(final Parser<T> parser) {
		return nonInternalsAfter(parser, TABLE, H2);
	}
	
	/**
	 * Consumes everything until one of the qName elements and then runs the <code>nextParser</code>
	 * @param qNames Element names to stop consuming at
	 * @param nextParser Parser to gather information with
	 * @return Returns objects the <code>nextParser</code> found
	 */
	public static <T> Parser<T> laterParser(final Parser<T> nextParser, final String... qNames) {
		return internalsBeforeParser(qNames).next(nextParser);
	}
	
	public static <T> Parser<T> laterDivParser(final Parser<T> nextParser) {
		return laterParser(nextParser, DIV);
	}
	
	/**
	 * Parses everything before the element with the given qName then uses the internalParser inside that element
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param internalParser Parser to gather information with
	 * @param qName element to parse inside
	 * @return Returns objects the <code>internalParser</code> found
	 */
	public static <T> Parser<T> laterInternalParser(final Parser<T> internalParser, final String qName) {
		return internalsBeforeParser(qName).next(elementCFParser(qName, internalParser));
	}
	
	/**
	 * Consumes everything until the qName element and then uses the 
	 * contentParser to get results and consumes the start and end elements 
	 * with the given qName that come before the contentParser.
	 * 
	 * @param qName Element name to care about
	 * @param contentParser Parser to gather information with
	 * @return Returns objects the contentParser found
	 */
	public static <T> Parser<T> laterElementParser(final String qName, final Parser<T> contentParser) {
		return laterParser(elementParser(qName,contentParser), qName);
	}
	
	/**
	 * Consumes everything until the qName element, uses the contentParser, and then consumes any characters following the qName end element
	 * 
	 * @param qName Element name
	 * @param contentParser Parser to gather information
	 * @return Returns objects the contentParser found
	 */
	public static <T> Parser<T> laterElementCFParser(final String qName, final Parser<T> contentParser) {
		return laterParser(elementCFParser(qName,contentParser), qName).atomic();
	}
	
	public static <T> Parser<T> laterElementCBIParser(final String qName, final Parser<T> contentParser) {
		return laterParser(elementCBIParser(qName,contentParser), qName); 
	}
	
	/**
	 * Consumes everything until the start element with the given qName, then 
	 * consumes any characters before the given internalParser.
	 * 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param qName Element name
	 * @param internalParser Parser to gather information
	 * @return Returns the objects parsed from the internalParser
	 */
	public static <T> Parser<T> laterStartElementParser(final String qName, final Parser<T> internalParser) {
		return laterParser(elementStartCBIParser(qName,internalParser), qName); 
	}
	
	public static Parser<String> laterElementStringParser(final String qName) {
		return laterParser(elementStringParser(qName), qName);
	}
	
	public static Parser<String> laterElementStringCFParser(final String qName) {
		return laterParser(elementStringCFParser(qName), qName);
	}
	
	public static Parser<Strings> string2Parser(final Parser<String> parser1, final Parser<String> parser2) {
		return sequence(parser1,parser2,
				new Map2<String,String,Strings>() {
					@Override public Strings map(String string1, String string2) {
						return new Strings(string1,string2);
					}
				});
	}
	
	public static Parser<ObjectList> object2Parser(final Parser<?> parser1, final Parser<?> parser2) {
		return sequence(parser1,parser2,
				new Map2<Object,Object,ObjectList>() {
					@Override public ObjectList map(Object object1, Object object2) {
						return new ObjectList(object1,object2);
					}
				});
	}
	
	public static Parser<ObjectList> object3Parser(
			final Parser<?> parser1, final Parser<?> parser2, final Parser<?> parser3) {
		return sequence(parser1,parser2,parser3,
				new Map3<Object,Object,Object,ObjectList>() {
					@Override public ObjectList map(Object object1, Object object2, Object object3) {
						return new ObjectList(object1,object2,object3);
					}
				});
	}
	
	public static <T> Parser<T> xDocParser(final Parser<T> xParser) {
		return xParser.followedBy(END_OF_DOC_PARSER);
	}
	
	/**
	 * Parses a link element (a) and the text content inside
	 * @param map Map object that defines how to convert the link into the desired object
	 * @return Returns the results of the map
	 */
	public static <T> Parser<T> linkParser(final Map2<StartElementEvent, String, T> map) {
		final Parser<T> aParser = 
				sequence(startElementParser(A),
						CHARACTER_PARSER.followedBy(END_ELEMENT_EVENT_PARSER),
						map);
		return SCRIPT_PARSER.optional().next(aParser);
	}
	
	/**
	 * Consumes a 'br' element before using the baseParser
	 * @param baseParser Parser to gather information
	 * @return Returns the results of the baseParser
	 */
	public static <T> Parser<T> brBeforeParser(final Parser<T> baseParser) {
		return BR_PARSER.next(baseParser);
	}
	
	
	
	/**
	 * Combines the results from 2 parsers into 1 for mapping purposes 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param parserA First parser
	 * @param parserB Second parser
	 * @return Returns a Pair containing the results of both parsers
	 */
	public static <A, B> Parser<Pair<A, B>> mergeParsers(final Parser<A> parserA, final Parser<B> parserB) {
		return tuple(parserA, parserB);
	}
	
	/**
	 * Combines the results from 3 parsers into 1 for mapping purposes 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param parserA First parser
	 * @param parserB Second parser
	 * @param parserC Third parser
	 * @return Returns a Tuple containing the results of both parsers
	 */
	public static <A, B, C> Parser<Tuple3<A, B, C>> mergeParsers(final Parser<A> parserA, final Parser<B> parserB, final Parser<C> parserC) {
		return tuple(parserA, parserB, parserC);
	}
	
	/**
	 * Combines the results from 4 parsers into 1 for mapping purposes 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param parserA First parser
	 * @param parserB Second parser
	 * @param parserC Third parser
	 * @param parserD Fourth parser
	 * @return Returns a Tuple containing the results of both parsers
	 */
	public static <A, B, C, D> Parser<Tuple4<A, B, C, D>> mergeParsers(
			final Parser<A> parserA, final Parser<B> parserB, 
			final Parser<C> parserC, final Parser<D> parserD) {
		return tuple(parserA, parserB, parserC, parserD);
	}
	
	/**
	 * Combines the results from 4 parsers into 1 for mapping purposes 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param parserA First parser
	 * @param parserB Second parser
	 * @param parserC Third parser
	 * @param parserD Fourth parser
	 * @return Returns a Tuple containing the results of both parsers
	 */
	public static <A, B, C, D, E> Parser<Tuple5<A, B, C, D, E>> mergeParsers(
			final Parser<A> parserA, final Parser<B> parserB, 
			final Parser<C> parserC, final Parser<D> parserD, final Parser<E> parserE) {
		return tuple(parserA, parserB, parserC, parserD, parserE);
	}
}
