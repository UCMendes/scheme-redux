# Scheme-redux: Up-to-date plugin for scheme development on Intellij 

A final year project based around updating and improving the overall capabilities of intellij-scheme, a plugin for Intellij IDEA created to support editing in the scheme programming language.

This project and code is heavily based on the work of the following repositories:
- [saigut/intellij-scheme](https://github.com/Saigut/intellij-scheme)
- [cmf/schemely](https://github.com/cmf/schemely)

Additional libraries used:
- [mug/dot-parse](https://github.com/google/mug/blob/master/dot-parse/README.md)
- [jparsec/jparsec](https://github.com/jparsec/jparsec)
- [junit-team/junit-framework](https://github.com/junit-team/junit-framework)

---

## Major differences from intellij-scheme

### New build system
- Plugin has been migrated to the gradle build system
- Because of this, library files are no longer bound to the project (and will be fetched by gradle instead)

### Reworked lexer
- SchemeLexer.java now uses the **dot-parse** library for parsing, instead of jparsec (except in specific cases)
- Helper functions created to support lexer operations (and to tidy code)
- recognised procedure/keyword/character names have been updated to match base library (as described in [R7RS](https://standards.scheme.org/unofficial/errata-corrected-r7rs.pdf))

### New syntax construct, Datum Label
- Datum label (#X=) and Datum reference (#X#) now recognised as syntax, to be read by lexer and marked by parser.
- If a label and a reference have the same "X", a datum reference will act as an in-editor reference to a datum label (Ctrl+B)

### Improved compehension of existing syntax/functions
- procedures that only accept numbers as arguments now notify of error if those arguments are of an incorrect type
- keywords "let-syntax" and "syntax-rules" now interpreted as a sum of their individual parts (binder/transformer/syntax rule), complete with input validation and symbol referencing where possible  

### Other fixes
- Deprecated procedure calls replaced where possible
- More distinct syntax highlighting

---

## Known Issues

### "Failed to unload plugin"
- If this happens, restart the plugin.

### Highlighting not displaying correctly
- If this happens, restart the plugin.
  
---

## To Run

> As this plugin was made to support the Intellij IDE, you will need to have it installed in order to use this plugin.

1. Download and unzip the project zip from the repository front page.
2. In Intellij, navigate to File > New > Project from existing sources
3. Navigate to **build.gradle.kts** and select open
4. Wait for the gradle project to finish importing (may take up to 15 minutes)
5. Press "Run IDE with Plugin", create a new project, and create a file within that project with file extension ".scm"
6. Start typing!

> For a more-detailed look at the syntax breakdown, install the [PsiViewer](https://plugins.jetbrains.com/plugin/227-psiviewer) Plugin in the IDE instance running scheme-redux.

You can either use the plugin as you wish, or copy in the below code for a quick feature overview:
```
;; Line Comment

#|
  Multiline
  Comment
|#

"String\r\n"

(define string "Some string")

(define quoted '(my quoted 3 items "with quoted string"))

(define char #\c)

(define special #\null)

(eq? char special)

(/ 8 9)

#t #f #true #false

(let ((x (#5435=(a b) #5435#))) (eq? (car x) (cadr x)))

'(eq? 1 2)
`(list ,(eq? 1 2) ,@(eq? 1 2))
#'(eq? 1 2)
#`(list #,(eq? 1 2) #,@(eq? 1 2))

(let ((x '(1 3 5 7 9)))
  (do ((x x (cdr x))
        (sum 0 (+ sum (car x))))
    ((null? x) sum)))

(do ((vec (make-vector 5))
      (i 0 (+ i 1)))
  ((= i 5) vec)
  (vector-set! vec i i))

(let loop ((numbers '(3 -2 1 6 -5))
            (nonneg '())
            (neg '()))
  (cond ((null? numbers) (list nonneg neg))
    ((>= (car numbers) 0)
      (loop (cdr numbers)
        (cons (car numbers) nonneg)
        neg))
    ((< (car numbers) 0)
      (loop (cdr numbers)
        nonneg
        (cons (car numbers) neg)))))

(let-syntax ((given-that (syntax-rules () ((given-that test stmt1 stmt2 ...) (if test (begin stmt1 stmt2 ...))))))
  (let ((if #t)) (given-that if (set! if 'now)) if))

(let ((x 'outer))
  (let-syntax ((m (syntax-rules () ((m) x))))
    (let ((x 'inner))
      (m))))

(define my-function
  (lambda (x)
    (+ x 2)))

(letrec-syntax
  ((my-or (syntax-rules ()
            ((my-or) #f)
            ((my-or e) e)
            ((my-or e1 e2 ...)
              (let ((temp e1))
                (if temp
                  temp
                  (my-or e2 ...)))))))
  (let ((x #f)
         (y 7)
         (temp 8)
         (let odd?)
         (if even?))
    (my-or x
      (let temp)
      (if y)
      y)))
```



