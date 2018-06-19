/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.grammar;

import static org.codehaus.jparsec.PredicateParser.predicateParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.codehaus.jparsec.Parser;

import com.trgr.dockets.core.util.PredicateJ8;

public final class ParserDescriptor<T> {
	
	private final Grammar grammar;

	private final String name;
	
	private final Type resultType;
	
	private final Parser<T> parser;
	
	private final ParserComposition composition;
	
	public ParserDescriptor(
			final Grammar grammar,
			final String name,
			final Type resultType,
			final Parser<T> parser,
			final ParserComposition composition) {
		this.grammar = grammar;
		this.name = name;
		this.resultType = resultType;
		this.parser = parser;
		this.composition = composition;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getGrammarEntryString() {
		return name + " = " + composition + " -> " + shortTypeName(resultType) + ".";
	}
	
	private static String shortTypeName(final Type type) {
		if (type instanceof Class) {
			return ((Class<?>)type).getSimpleName();
		}
		return type.toString();
	}
	
	public final ParserDescriptor<T> filter(
			final String filterName, final String predicateName, final PredicateJ8<T> predicate) {
		return new ParserDescriptor<T>(
				grammar,filterName,resultType,predicateParser("filter", parser, predicate),
				new PredicateComposition(predicateName,composition));
	}
	
	@SuppressWarnings("unchecked")
	public final <U> ParserDescriptor<U> cast(final Class<U> cls) {
		final ParserDescriptor<U> result =
				new ParserDescriptor<U>(grammar,name,cls,(Parser<U>)parser.cast(),composition);
		grammar.putNewDescriptor(result);
		return result;
	}
	
	public final <U> ParserDescriptor<U> instanceOfFilter(final Class<U> cls) {
		final String targetClsName = cls.getSimpleName();
		final PredicateJ8<T> predicate = new PredicateJ8<T>() {
			@Override
			public boolean test(T r) {
				return cls.isInstance(r);
			}
		};
		return filter(targetClsName,"instanceof " + targetClsName,predicate).cast(cls);
	}
	
	public final ParserDescriptor<List<T>> many() {
		final String manyName = name + "s";
		final ParameterizedType manyResultType = new ParameterizedType() {

			@SuppressWarnings("synthetic-access")
			@Override
			public Type[] getActualTypeArguments() {
				// TODO Auto-generated method stub
				return new Type[] {resultType};
			}

			@Override
			public Type getOwnerType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Type getRawType() {
				return List.class;
			}
			
			@SuppressWarnings("synthetic-access")
			@Override
			public final String toString() {
				final StringBuilder b = new StringBuilder(shortTypeName(getRawType()));
				b.append("<");
				String separator = "";
				for (final Type subtype: getActualTypeArguments()) {
					b.append(separator);
					separator = ",";
					b.append(shortTypeName(subtype));
				}
				b.append(">");
				return b.toString();
			}
		};
		final Parser<List<T>> many = parser.many();
		final ParserDescriptor<List<T>> result =
				new ParserDescriptor<List<T>>(grammar,manyName,manyResultType,many,composition);
		return result;
	}
}
