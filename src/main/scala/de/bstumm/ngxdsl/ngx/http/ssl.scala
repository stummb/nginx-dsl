package de.bstumm.ngxdsl.ngx.http

import de.bstumm.ngxdsl.Dsl._

object ssl {
    case class ssl_prefer_server_ciphers(on: Boolean = false) extends BooleanDirective[AppearsWithinHttp with AppearsWithinServer]

    object ssl_session_cache {
      abstract class Value(val strval: String)
      case object off extends Value("off")
      case object none extends Value("none")
      case class Builtin(size: Long) extends Value(s"builtin:$size")
      case class Shared(name: String, size: Long) extends Value(s"shared:$name:$size")

      def apply(offOrNone: String): ssl_session_cache = {
        offOrNone match {
          case "off"  => apply(off)
          case "none" =>  apply(none)
          case _      => throw new Exception // TODO error handling
        }
      }
      def apply(builtin: "builtin", size: Long): ssl_session_cache = apply(ssl_session_cache.Builtin(size))
      def apply(builtin: "shared", name: String, size: Long): ssl_session_cache = apply(ssl_session_cache.Shared(name, size))

    }
    case class ssl_session_cache(value: ssl_session_cache.Value) extends StringDirective[AppearsWithinHttp with AppearsWithinServer] {
      override def string: String = value.strval
    }

}

