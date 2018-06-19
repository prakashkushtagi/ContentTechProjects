/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static com.trgr.dockets.core.parser.saxevent.DocketParsers.ELEMENT_DATA_PARSER;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.charsAfterParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.elementCFParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.elementParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.elementStringCFParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.internalsBeforeParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.internalsAfterParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.laterElementCFParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.laterElementParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.laterElementStringCFParser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.object2Parser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.object3Parser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.string2Parser;
import static com.trgr.dockets.core.parser.saxevent.DocketParsers.xDocParser;
import static com.trgr.dockets.core.parser.saxevent.SaxUtil.parseWithEventParser;
import static java.lang.System.out;
import static org.codehaus.jparsec.Parsers.sequence;

import java.io.File;
import java.io.FileInputStream;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.functors.Map2;
import org.xml.sax.InputSource;

import com.trgr.dockets.core.util.IndentableStrBuilder;
import com.trgr.dockets.core.util.ObjectList;
import com.trgr.dockets.core.util.Strings;

public final class DctReader {
	
	public static void readXml(final File file) throws Exception {
		final FileInputStream fis = new FileInputStream(file);
		
		final Parser<Strings> natureOfSuitBodyParser = string2Parser(
				laterElementStringCFParser("nature.of.suit"),
				elementStringCFParser("nature.of.suit.code"));
		
		final Parser<Strings> natureOfSuitBlockParser =
				laterElementCFParser("nature.of.suit.block", natureOfSuitBodyParser);
		
		final Parser<Strings> knosLevel23BlockParser = string2Parser(
				knosLevelBlockParser(2),
				knosLevelBlockParser(3).optional());
		
		final Parser<Strings> knosLevelBlocksParser = sequence(
				knosLevelBlockParser(1),
				knosLevel23BlockParser.optional(),
				new Map2<String, Strings, Strings>() {
					@Override
					public Strings map(String level1, Strings levels2And3) {
						final Strings strings = new Strings(level1);
						strings.addAll(levels2And3);
						return strings;
					}
				});
		
		final Parser<Strings> keyNatureOfSuitBodyParser = sequence(
				knosLevelBlocksParser,
				elementStringCFParser("knos.code"),
				new Map2<Strings, String, Strings>() {
					@Override
					public Strings map(Strings levels, String code) {
						final Strings strings = new Strings();
						strings.addAll(levels);
						strings.add(code);
						return strings;
					}
				});
		
		final Parser<Strings> keyNatureOfSuitBlockParser =
				elementCFParser("key.nature.of.suit.block",keyNatureOfSuitBodyParser);
		
		final Parser<ObjectList> summaryBodyParser =
				object2Parser(natureOfSuitBlockParser, keyNatureOfSuitBlockParser);
		
		final Parser<ObjectList> summaryParser =
				laterElementParser("summary",internalsAfterParser(summaryBodyParser));
		
		final Parser<ObjectList> rBodyParser = object3Parser(
				summaryParser,
				internalsBeforeParser("party.block").next(charsAfterParser(ELEMENT_DATA_PARSER)),
				internalsAfterParser(ELEMENT_DATA_PARSER));
		
		final Parser<ObjectList> rParser = elementCFParser("r",rBodyParser);
		
		final Parser<ObjectList> nDocBodyParser = laterElementCFParser("n-docbody",rParser);
		
		final Parser<ObjectList> nDocumentParser = xDocParser(elementParser("n-document",nDocBodyParser));
		
		final ObjectList data = parseWithEventParser(new InputSource(fis),null, nDocumentParser);
		
		final IndentableStrBuilder b = new IndentableStrBuilder();
		data.toBuffer(b);
		out.println(b.toString());
		
		fis.close();
	}
	
	private static Parser<String> knosLevelBlockParser(final int level) {
		return elementCFParser("knos.level" + level + ".block",laterElementStringCFParser("knos.level" + level));
	}
}
