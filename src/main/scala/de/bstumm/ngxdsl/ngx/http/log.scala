package de.bstumm.ngxdsl.ngx.http

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._


object log {
    object log_format {
      def apply(name: String, string: String): log_format = apply(name, None, string)
      def apply(name: String, escape: String, string: String): log_format = apply(name, Some(escape), string)
    }
    case class log_format(_name: String = "combined", escape: Option[String] = Some("default"), string: String) extends Directive[AppearsWithinHttp] {
      override def print(indent: String, out: Writer): Unit =
        out append s"$indent$name ${_name} ${escape.getOrElse("")} $string;\n"
    }

}