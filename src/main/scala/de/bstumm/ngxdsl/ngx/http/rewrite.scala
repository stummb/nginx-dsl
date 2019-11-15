package de.bstumm.ngxdsl.ngx.http

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._


object rewrite {

  object `return` {
    def apply(code: Int): `return` = apply(Left(code, None))
    def apply(code: Int, text: String): `return` = apply(Left(code, Some(text)))
    // TODO def apply(code: Int, url: String): Unit = apply(on)
    def apply(url: String): `return` = apply(Right(None, url))
  }
    case class `return`(params: Either[(Int, Option[String]),(Option[Int], String)]) extends Directive[AppearsWithinServer with AppearsWithinLocation with AppearsWithinIfInLocation] {
      def format: String = params match {
        case Left((code, Some(text))) => s"$name $code $text"
        case Left((code, None)) => s"$name $code"
        case Right((Some(code), url)) => s"$name $code $url"
        case Right((None, url)) => s"$name $url"
      }

      override def print(indent: String, out: Writer): Unit = params match {
        case Left((code, Some(text))) => out append s"$indent$name $code $text;\n"
        case Left((code, None)) => out append s"$indent$name $code;\n"
        case Right((Some(code), url)) => out append s"$indent$name $code $url;\n"
        case Right((None, url)) => out append s"$indent$name $url;\n"
      }
    }
}
