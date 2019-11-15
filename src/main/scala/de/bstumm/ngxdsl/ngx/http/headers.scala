package de.bstumm.ngxdsl.ngx.http

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._

object headers {

  object add_header {
    def apply(name: String, value: String): add_header = apply(name, value, None)
    def apply(name: String, value: String, always: "always"): add_header = new add_header(name, value, Some("always"))
  }
  case class add_header(_name: String, value: String, always: Option["always"]) extends Directive[AppearsWithinHSL with AppearsWithinIfInLocation] {
    override def print(indent: String, out: Writer): Unit =
      out append (indent + s"$name ${_name} $value ${always.getOrElse("")}".trim + ";\n")
  }

}