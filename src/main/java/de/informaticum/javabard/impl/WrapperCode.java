package de.informaticum.javabard.impl;

import static de.informaticum.javabard.api.Code.combine;
import static de.informaticum.javabard.api.Code.emptyCode;
import static de.informaticum.javabard.api.Comment.CommentPattern.BLOCKCOMMENT;
import static de.informaticum.javabard.api.Comment.CommentPattern.COMMENT;
import static de.informaticum.javabard.api.Comment.CommentPattern.JAVADOC;
import java.util.Scanner;
import de.informaticum.javabard.api.Code;
import de.informaticum.javabard.api.Comment;

public abstract interface WrapperCode
extends Code {

    public abstract Code getCode();

    @Override
    public default int getIndent() {
        return this.getCode().getIndent();
    }

    @Override
    public default WrapperCode asComment() {
        return new Comment.Builder(this.getCode(), COMMENT).get();
    }

    @Override
    public default WrapperCode asBlockComment() {
        return new Comment.Builder(this.getCode(), BLOCKCOMMENT).get();
    }

    @Override
    public default WrapperCode asJavadoc() {
        return new Comment.Builder(this.getCode(), JAVADOC).get();
    }

    @Override
    public abstract String toString();

    /**
     * Because one cannot supply a {@code default} implementation of the {@link Object#toString()} method, this abstract
     * implementation of {@link WrapperCode} does instead.
     */
    public static abstract class AbstractWrapperCode
    implements WrapperCode {

        private final Code prefix;

        private final String infixFormat;

        private final Code suffix;

        protected AbstractWrapperCode(final Code prefix, final String infixFormat, final Code suffix) {
            assert prefix != null;
            assert infixFormat != null;
            assert suffix != null;
            this.prefix = prefix;
            this.infixFormat = infixFormat;
            this.suffix = suffix;
        }

        protected AbstractWrapperCode(final AbstractWrapperCode blueprint) {
            this(blueprint.prefix, blueprint.infixFormat, blueprint.suffix);
        }

        @Override
        public String toString() {
            Code code = emptyCode();
            try (final Scanner scanner = new Scanner(this.getCode().setIndent(0).toString())) {
                while (scanner.hasNextLine()) {
                    code = code.add(this.infixFormat, scanner.nextLine());
                }
            }
            return combine(this.prefix, code, this.suffix).setIndent(this.getIndent()).toString();
        }

    }

}
