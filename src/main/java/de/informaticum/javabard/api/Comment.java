package de.informaticum.javabard.api;

import static de.informaticum.javabard.api.Code.code;
import static de.informaticum.javabard.api.Code.emptyCode;
import static de.informaticum.javabard.util.Util.nonNull;
import java.util.function.Supplier;
import de.informaticum.javabard.impl.WrapperCode.AbstractWrapperCode;

public class Comment
extends AbstractWrapperCode {

    public static enum CommentPattern {

        COMMENT(emptyCode(), "// %s", emptyCode()),

        BLOCKCOMMENT(code("/*"), " * %s", code(" */")),

        JAVADOC(code("/**"), " * %s", code(" */"));

        private final Code prefix;

        private final String infixFormat;

        private final Code suffix;

        private CommentPattern(final Code prefix, final String infixFormat, final Code suffix) {
            this.prefix = prefix;
            this.infixFormat = infixFormat;
            this.suffix = suffix;
        }

    }

    private final Code code;

    private Comment(final CommentPattern pattern, final Code code) {
        super(pattern.prefix, pattern.infixFormat, pattern.suffix);
        assert code != null;
        this.code = code;
    }

    private Comment(final AbstractWrapperCode blueprint, final Code code) {
        super(blueprint);
        assert code != null;
        this.code = code;
    }

    @Override
    public Code indentBy(final int diff) {
        return new Comment(this, this.code.indentBy(diff));
    }

    @Override
    public Code getCode() {
        return this.code;
    }

    public static class Builder
    implements Supplier<Comment> {

        private final Code code;

        private CommentPattern pattern;

        public Builder(final Code code, final CommentPattern pattern) {
            this.code = nonNull(code);
            this.pattern = nonNull(pattern);
        }

        public Builder withPattern(final CommentPattern pattern) {
            this.pattern = nonNull(pattern);
            return this;
        }

        @Override
        public Comment get() {
            return new Comment(this.pattern, this.code);
        }

    }

}
