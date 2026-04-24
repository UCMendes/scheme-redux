package com.umendes.schemeredux.lexer;

// Class used for keeping keywords/procedure names, in separate file for neatness
public class SchemeSpecialWordList {

    static String[] STRS_SPECIAL_CHAR_NAME = {
            "alarm", "backspace", "delete", "escape", "newline",
            "null", "return", "space", "tab", "x03BB", "iota"};

    static String[] STRS_KEYWORD = {
            "quote", "lambda", "if", "set!", "include", "include-ci",
            "cond", "else", "=>", "case", "and", "or", "when", "unless",
            "cond-expand", "library", "let", "let*", "letrec", "letrec*", "let-values",
            "let*-values", "begin", "do", "parameterize", "guard",
            "quasiquote", "unquote", "unquote-splicing", "let-syntax",
            "letrec-syntax", "syntax-rules", "...", "syntax-error",
            "define", "define-values", "define-record-type"};

    static String[] STRS_BUILTIN_PROCEDURE = {
            "make-parameter", "eqv?", "eq?", "equal?", "number?",
            "complex?", "real?", "rational?", "integer?", "exact?",
            "inexact?", "exact-integer?", "finite?", "infinite?",
            "=", "<", ">", "<=", ">=", "zero?", "positive?", "negative?",
            "odd?", "even?", "max", "min", "+", "*", "-", "/", "abs",
            "floor/", "floor-quotient", "floor-remainder", "truncate/",
            "truncate-quotient", "truncate-remainder", "quotient",
            "remainder", "modulo", "gcd", "lcm", "numerator", "denominator",
            "floor", "ceiling", "truncate", "round", "rationalize",
            "square", "exact-integer-sqrt", "expt", "inexact", "exact",
            "number->string", "string->number", "not", "boolean?",
            "boolean=?", "pair?", "cons", "car", "cdr", "set-car!",
            "set-cdr!", "null?", "list?", "make-list", "list", "length",
            "append", "reverse", "list-tail", "list-ref", "list-set!",
            "memq", "memv", "member", "assq", "assv", "assoc", "list-copy",
            "symbol?", "symbol=?", "symbol->string", "string->symbol",
            "char?", "char=?", "char<?", "char>?", "char<=?", "char>=?",
            "char->integer", "integer->char", "string?", "make-string",
            "string", "string-length", "string-ref", "string-set!",
            "string=?", "string<?", "string>?", "string<=?", "string>=?",
            "substring", "string-append", "string->list", "list->string",
            "string-copy", "string-copy!", "string-fill!", "vector?",
            "make-vector", "vector", "vector-length", "vector-ref",
            "vector-set!", "vector->list", "list->vector", "vector->string",
            "string->vector", "vector-copy", "vector-copy!", "vector-append",
            "vector-fill!", "bytevector?", "make-bytevector", "bytevector",
            "bytevector-length", "bytevector-u8-ref", "bytevector-u8-set!",
            "bytevector-copy", "bytevector-copy!", "bytevector-append",
            "utf8->string", "string->utf8", "procedure?", "apply", "map",
            "string-map", "vector-map", "for-each", "string-for-each",
            "vector-for-each", "call-with-current-continuation", "call/cc",
            "values", "call-with-values", "dynamic-wind",
            "with-exception-handler", "raise", "raise-continuable", "error",
            "error-object?", "error-object-message", "error-object-irritants",
            "read-error?", "file-error?", "call-with-port", "input-port?",
            "output-port?", "textual-port?", "binary-port?", "port?",
            "input-port-open?", "output-port-open?", "current-input-port",
            "current-output-port", "current-error-port", "close-port",
            "close-input-port", "close-output-port", "open-input-string",
            "open-output-string", "get-output-string", "open-input-bytevector",
            "open-output-bytevector", "get-output-bytevector", "read-char",
            "peek-char", "read-line", "eof-object?", "eof-object",
            "char-ready?", "read-string", "read-u8", "peek-u8", "u8-ready?",
            "read-bytevector", "read-bytevector!", "newline", "write-char",
            "write-string", "write-u8", "write-bytevector", "flush-output-port",
            "features", "caar", "cadr", "cdar", "cddr"};

}
