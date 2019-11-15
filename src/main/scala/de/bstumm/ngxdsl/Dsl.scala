package de.bstumm.ngxdsl

import java.io.{File, PrintWriter, Writer}

object Dsl {

  sealed trait AppearsWithin

  trait TopLevel extends AppearsWithin
  trait AppearsWithinMain extends AppearsWithin
  trait AppearsWithinHttp extends AppearsWithin
  trait AppearsWithinMail extends AppearsWithin
  trait AppearsWithinStream extends AppearsWithin
  trait AppearsWithinServer extends AppearsWithin
  trait AppearsWithinLocation extends AppearsWithin
  trait AppearsWithinIf extends AppearsWithin
  trait AppearsWithinIfInLocation extends AppearsWithin
  trait AppearsWithinEvents extends AppearsWithin

  trait AppearsAnywhere extends
    AppearsWithinMain with
    AppearsWithinHttp with
    AppearsWithinMail with
    AppearsWithinStream with
    AppearsWithinServer with
    AppearsWithinLocation with
    AppearsWithinIf with
    AppearsWithinIfInLocation with
    AppearsWithinEvents

  trait AppearsWithinHSL extends
    AppearsWithinHttp with
    AppearsWithinServer with
    AppearsWithinLocation

  trait Directive[+A <: AppearsWithin] {
    def name: String = this.getClass.getSimpleName

    def check(ancestors: List[Context[_, _]]): Unit = {

    }

    def print(indent: String, out: Writer): Unit
  }

  abstract class Context[A <: AppearsWithin, B <: AppearsWithin](directives: Directive[B]*) extends Directive[A] {
    override def print(indent: String, out: Writer): Unit = {
      out append s"$indent$name {\n"
      directives.foreach(_.print(s"    $indent", out))
      out append s"$indent}\n"
    }
  }

  // Serves as a container for a list of directives to be inserted into an context
  case class DirectivesList[A <: AppearsWithin](directives: Directive[A]*) extends Directive[A] {
    override def print(indent: String, out: Writer): Unit = {
      directives.foreach(_.print(indent, out))
    }
  }

  case class Main(directives: Directive[AppearsWithinMain]*) extends Context[TopLevel, AppearsWithinMain](directives:_*) {
    override def print(indent: String, out: Writer): Unit = {
      directives foreach (_.print(s"    $indent", out))
    }
  }

  object Comment {
    def `#`(comment: String) = --(comment)
    def ###(comment: String) = --(comment)
  }
  case class --(comment: String) extends Directive[AppearsAnywhere] {
    override def print(indent: String, out: Writer): Unit = {
      out append s"$indent# $comment\n"
    }
  }

  // TODO define types: time, size, booleanOrXXX

  abstract class BooleanDirective[A <: AppearsWithin] extends Directive[A] {
    def on: Boolean
    override def print(indent: String, out: Writer): Unit =
      out append s"$indent$name ${if (on) "on" else "off"};\n"
  }

  abstract class SizeDirective[A <: AppearsWithin] extends Directive[A] {
    def size: Long
    override def print(indent: String, out: Writer): Unit =
      out append s"$indent$name $size;\n"
  }

  abstract class NumberDirective[A <: AppearsWithin] extends Directive[A] {
    def number: Int
    override def print(indent: String, out: Writer): Unit =
      out append s"$indent$name $number;\n"
  }

  abstract class TimeDirective[A <: AppearsWithin] extends Directive[A] {
    def time: Long
    override def print(indent: String, out: Writer): Unit =
      out append s"$indent$name $time;\n"
  }

  abstract class StringDirective[A <: AppearsWithin] extends Directive[A] {
    def string: String
    override def print(indent: String, out: Writer): Unit =
      out append s"$indent$name $string;\n"
  }

  case class NginxConfiguration(files: NginxConfigFile[_]*) {
    def serialize(basedir: File): Unit = {
      val out = new PrintWriter(System.out)
      for {
        file <- files
      } {
        out.println("======================================================")
        out.println(file.relativePath)
        out.println("======================================================")
        file.serialize(out) // TODO write to actual file
        out.flush()
      }
    }
  }

  case class NginxConfigFile[A <: AppearsWithin](relativePath: String)(directives: Directive[A]*) {
    def serialize(out: Writer): Unit = {
      DirectivesList(directives:_*).print("", out)
    }
  }
}
