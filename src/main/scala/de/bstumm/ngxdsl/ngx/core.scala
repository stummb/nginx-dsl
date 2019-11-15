package de.bstumm.ngxdsl.ngx

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._
import de.bstumm.ngxdsl.ngx.core.include._

object core {


  /* TODO
     accept_mutex
     accept_mutex_delay
     daemon
     debug_connection
     debug_points
     env
     load_module
     lock_file
     master_process
     multi_accept
     pid
     ssl_engine
     thread_pool
     timer_resolution
     use
     user
     worker_aio_requests
     worker_cpu_affinity
     worker_priority
     worker_rlimit_core
     worker_rlimit_nofile
     worker_shutdown_timeout
     working_directory
   */

  case class daemon(on: Boolean = false) extends BooleanDirective[AppearsWithinMain]

  // TODO could be more elaborate
  case class error_log(file: String = "logs/error.log", level: String = "error") extends Directive[
      AppearsWithinMain with
      AppearsWithinHSL with
      AppearsWithinMail with
      AppearsWithinStream with
      AppearsWithinIfInLocation] {
    override def print(indent: String, out: Writer): Unit = out append s"$indent$name $file $level;\n"
  }

  case class events(directives: Directive[AppearsWithinEvents]*) extends Context[AppearsWithinMain, AppearsWithinEvents](directives:_*)

  object include {
    case class ExternalInclude(fileOrMask: String)
    case class InternalInclude[A <: AppearsWithin](file: NginxConfigFile[A])

    def apply[A <: AppearsWithin](fileOrMask: String): include[A] = new include(Left(ExternalInclude(fileOrMask)))
    def apply[A <: AppearsWithin](file: NginxConfigFile[A]): include[A] = new include(Right(InternalInclude(file)))
  }
  case class include[A <: AppearsWithin](fileOrMask: Either[ExternalInclude, InternalInclude[A]]) extends Directive[A] {
    override def print(indent: String, out: Writer): Unit = fileOrMask match {
      case Left(ExternalInclude(fileOrMask)) =>
        out append s"""$indent$name "$fileOrMask";\n"""
      case Right(InternalInclude(file)) =>
        out append s"""$indent$name "${file.relativePath}";\n"""
    }
  }

  case class pcre_jit(on: Boolean = false) extends BooleanDirective[AppearsWithinMain]

  case class worker_connections(number: Int = 512) extends NumberDirective[AppearsWithinEvents]

  object worker_processes {
    def apply(number: Int): worker_processes = apply(Left(number))
    def apply(auto: "auto"): worker_processes = apply(Right("auto"))
  }
  case class worker_processes(value: Either[Int, String] = Right("auto")) extends Directive[AppearsWithinMain] {
    override def print(indent: String, out: Writer): Unit = value match {
      case Right(auto) => out append s"$indent$name $auto;\n"
      case Left(number) => out append s"$indent$name $number;\n"
    }
  }

}