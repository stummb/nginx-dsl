

Little experiment to make it somewhat easier to maintain large nginx configurations.

Basically, each nginx directive is modeled as a case class, with a few convenience
apply-functions in their companion object. Each case class knows how to print itself,
and its children in case of container directives.

So, you have the full power of scala to modularize and parameterize your nginx config.

Note that I do not claim any real-world usability of this project. It might be awkward to
let the compiler and the JVM delay the start of your container by two orders of
magnitude.

