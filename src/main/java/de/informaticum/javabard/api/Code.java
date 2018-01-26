package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Comment.CommentPattern.BLOCKCOMMENT;
import static de.informaticum.javabard.api.Comment.CommentPattern.COMMENT;
import static de.informaticum.javabard.api.Comment.CommentPattern.JAVADOC;
import static de.informaticum.javabard.util.Util.allNonNull;
import static de.informaticum.javabard.util.Util.nonNull;
import static java.lang.Math.max;
import static java.util.Arrays.asList;
import java.util.IllegalFormatException;
import java.util.Locale;
import de.informaticum.javabard.impl.WrapperCode;
import de.informaticum.javabard.impl.MultiCode;
import de.informaticum.javabard.impl.SingleCode;

public abstract interface Code {

    public abstract int getIndent();

    public default Code setIndent(final int indent) {
        return this.indentBy(max(0, indent) - this.getIndent());
    }

    public abstract Code indentBy(final int diff);

    public default Code indent() {
        return this.indentBy(+1);
    }

    public default Code unindent() {
        return this.indentBy(-1);
    }

    public default Code add(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        return this.add(new SingleCode.Builder(format, args).get());
    }

    public default Code add(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return this.add(new SingleCode.Builder(format, args).setLocale(locale).get());
    }

    public default Code add(final Code code)
    throws IllegalArgumentException {
        nonNull(code);
        return new MultiCode.Builder(this, code.indentBy(this.getIndent())).get();
    }

    public default Code addAll(final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        Code code = this;
        for (final Code c : codes) {
            code = code.add(c);
        }
        return code;
    }

    public default Code addAll(final Code... codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return this.addAll(asList(codes));
    }

    public default WrapperCode asComment() {
        return new Comment.Builder(this, COMMENT).get();
    }

    public default WrapperCode asBlockComment() {
        return new Comment.Builder(this, BLOCKCOMMENT).get();
    }

    public default WrapperCode asJavadoc() {
        return new Comment.Builder(this, JAVADOC).get();
    }

    @Override
    public abstract String toString()
    throws IllegalFormatException;

    // fabric methods

    public static Code emptyCode() {
        return new MultiCode.Builder().get();
    }

    public static Code code(final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).get();
    }

    public static Code code(final Locale locale, final String format, final Object... args)
    throws IllegalArgumentException {
        nonNull(locale);
        nonNull(format);
        nonNull(args);
        return new SingleCode.Builder(format, args).setLocale(locale).get();
    }

    public static Code combine(final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return new MultiCode.Builder().add(codes).get();
    }

    public static Code combine(final Code... codes)
    throws IllegalArgumentException {
        allNonNull(codes);
        return new MultiCode.Builder(codes).get();
    }

    public static Code combine(final Code code, final Iterable<? extends Code> codes)
    throws IllegalArgumentException {
        nonNull(code);
        allNonNull(codes);
        return new MultiCode.Builder(code).add(codes).get();
    }

    public static Code combine(final Code code, final Code[] codes)
    throws IllegalArgumentException {
        nonNull(code);
        allNonNull(codes);
        return new MultiCode.Builder(code).add(codes).get();
    }

}
