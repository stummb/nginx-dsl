package de.bstumm.ngxdsl.ngx.http

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._

object map {

    object map {
      abstract class Entry(val format: String)
      case class default(value: String) extends Entry(s"default $value")
      case class map_include(file: String) extends Entry(s"include $file")
      case object volatile extends Entry("volatile")
      case class Mapping(string: String, mapping: String) extends Entry(s"$string $mapping")
      implicit def mapping(pair: (String, String)): Mapping = Mapping(pair._1, pair._2)
      // case class HostnameMapping(hostname: String, mapping: String) extends Entry(s"$hostname $mapping")

      //  def map(string: String, variable: String)(entries: Map.Entry*): Unit =
      //    cfg += (Map(string, variable, entries.toList), f, l)

    }
    case class map(string: String, variable: String)(entries: map.Entry*) extends Directive[AppearsWithinHttp] {
      override def print(indent: String, out: Writer): Unit = {
        out append s"$indent$name $string $variable {\n"
        entries.foreach(e => out append s"$indent    $e;\n")
        out append s"$indent}\n"
      }
    }

}
