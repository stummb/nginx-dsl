package de.bstumm.ngxdsl.ngx.http

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._


object proxy {

  case class proxy_ssl_session_reuse(on: Boolean = true) extends BooleanDirective[AppearsWithinHSL]

  case class proxy_temp_path(path: String = "proxy_temp", level1: Int = 0, level2: Int = 0, level3: Int = 0) extends Directive[AppearsWithinHSL] {
    private def f(level: Int) = if (level == 0) "" else level.toString
    override def print(indent: String, out: Writer): Unit =
      out append (indent + s"$name $path ${f(level1)} ${f(level2)} ${f(level3)}".trim + ";\n")
  }

}
