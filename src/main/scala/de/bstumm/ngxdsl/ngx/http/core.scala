package de.bstumm.ngxdsl.ngx.http

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._

object core {

  case class absolute_redirect(on: Boolean = true) extends BooleanDirective[AppearsWithinHSL]

  object aio {
    def apply(threads: String): aio = apply(Left(threads))
    def apply(on: Boolean): aio = apply(Right(on))
  }
  case class aio(threads: Either[String, Boolean] = Right(false)) extends Directive[AppearsWithinHSL] {
    override def print(indent: String, out: Writer): Unit = threads match {
      case Right(on) => out append s"$name ${if (on) "on" else "off"};\n"
      case Left(threads) => out append s"$name threads=$threads;\n"
    }
  }

  case class aio_write(on: Boolean = true) extends BooleanDirective[AppearsWithinHSL]

  case class alias(path: String) extends StringDirective[AppearsWithinLocation] {
    def string: String = path
  }

  case class chunked_transfer_encoding(on: Boolean = true) extends BooleanDirective[AppearsWithinHSL]

  case class client_body_buffer_size(size: Long = 8192) extends SizeDirective[AppearsWithinHSL]

  object client_body_in_file_only {
    def apply(clean: "clean"): client_body_in_file_only = apply(Left(clean))
    def apply(on: Boolean): client_body_in_file_only = apply(Right(on))
  }
  case class client_body_in_file_only(value: Either[String, Boolean] = Right(false)) extends Directive[AppearsWithinHSL] {
    override def print(indent: String, out: Writer): Unit = value match {
      case Right(on) => out append s"$name ${if (on) "on" else "off"};\n"
      case Left(clean) => out append s"$name $clean;\n"
    }
  }

  case class client_body_in_single_buffer(on: Boolean = false) extends BooleanDirective[AppearsWithinHSL]

  case class client_body_temp_path(path: String = "client_body_temp", level1: Int = 0, level2: Int = 0, level3: Int = 0) extends Directive[AppearsWithinHSL] {
    private def f(level: Int) = if (level == 0) "" else level.toString
    override def print(indent: String, out: Writer): Unit =
      out append (indent + s"$name $path ${f(level1)} ${f(level2)} ${f(level3)}".trim + ";\n")
  }

  case class client_body_timeout(time: Long = 60) extends TimeDirective[AppearsWithinHSL]

  case class client_header_buffer_size(size: Long = 1024) extends SizeDirective[AppearsWithinHSL]

  case class client_header_timeout(time: Long = 60) extends TimeDirective[AppearsWithinHSL]

  case class client_max_body_size(size: Long = 1024*1024) extends SizeDirective[AppearsWithinHttp with AppearsWithinServer]

  case class connection_pool_size(size: Long = 512) extends SizeDirective[AppearsWithinHttp with AppearsWithinServer]

  case class default_type(mimeType: String = "text/plain") extends StringDirective[AppearsWithinHSL] {
    override def string: String = mimeType
  }

  object directio {
    def apply(off: false): directio = apply(Right(off))
    def apply(size: Long): directio = apply(Left(size))
  }
  case class directio(value: Either[Long, Boolean] = Right(false)) extends Directive[AppearsWithinHSL] {
    override def print(indent: String, out: Writer): Unit = value match {
      case Right(false) => out append s"$name off; \n"
      case Left(size) => out append s"$name $size; \n"
    }
  }

  case class directio_alignment_size(size: Long = 512) extends SizeDirective[AppearsWithinHSL]

  // TODO disable_symlinks
  // TODO error_page
  // TODO etag

  case class http(directives: Directive[AppearsWithinHttp]*) extends Context[AppearsWithinMain, AppearsWithinHttp](directives:_*)

  // TODO if_modified_since
  // TODO ignore_invalid_headers
  // TODO internal
  // TODO keepalive_disable
  // TODO keepalive_requests

  object keepalive_timeout {
    def apply(timeout: Long): keepalive_timeout = apply(timeout, None)
    def apply(timeout: Long, header_timeout: Long): keepalive_timeout = apply(timeout, Some(header_timeout))
  }
  case class keepalive_timeout(timeout: Long = 75, header_timeout: Option[Long]) extends Directive[AppearsWithinHSL] {
    override def print(indent: String, out: Writer): Unit =
      out append s"$indent$name ${timeout}s ${header_timeout.map(t => s"${t}s").getOrElse("")};\n"
  }

  case class large_client_header_buffers(number: Int = 4, size: Long = 8192) extends Directive[AppearsWithinHttp with AppearsWithinServer] {
    override def print(indent: String, out: Writer): Unit = out append s"$indent$name $number $size;\n"
  }

  // TODO limit_except
  // TODO limit_rate
  // TODO limit_rate_after
  // TODO lingering_close
  // TODO lingering_time
  // TODO lingering_timeout

  // TODO listen is more complex
  case class listen(port: Int) extends NumberDirective[AppearsWithinServer] {
    def number: Int = port
  }

  // TODO location is more complex
  object location {
    def apply(prefix: String)(directives: Directive[AppearsWithinLocation]*): location = apply(None, prefix)(directives:_*)
    def == (prefix: String)(directives: Directive[AppearsWithinLocation]*): location = apply(Some("="), prefix)(directives:_*)
    def `=` (prefix: String)(directives: Directive[AppearsWithinLocation]*): location = apply(Some("="), prefix)(directives:_*)
  }
  case class location(modifier: Option[String], value: String)(directives: Directive[AppearsWithinLocation]*) extends Context[AppearsWithinServer with AppearsWithinLocation, AppearsWithinLocation](directives:_*) {
    override def print(indent: String, out: Writer): Unit = {
      out append s"$indent$name ${modifier.getOrElse("")} $value {\n"
      directives.foreach(_.print(s"    $indent", out))
      out append s"$indent}\n"
    }
  }

  // TODO log_not_found
  // TODO log_subrequest
  // TODO max_ranges
  // TODO merge_slashes
  // TODO msie_padding
  // TODO msie_refresh
  // TODO open_file_cache
  // TODO open_file_cache_errors
  // TODO open_file_cache_min_uses
  // TODO open_file_cache_min_valid
  // TODO output_buffers
  // TODO port_in_redirect
  // TODO postpone_output
  // TODO read_ahead
  // TODO recursive_error_pages
  // TODO request_pool_size
  // TODO reset_timedout_connection
  // TODO resolver
  // TODO resolver_timeout

  case class root(path: String = "html") extends StringDirective[AppearsWithinHSL] {
    def string: String = path
  }

  // TODO satisfy
  // TODO send_lowat
  // TODO send_timeout

  case class sendfile(on: Boolean = false) extends BooleanDirective[AppearsWithinHSL]

  // TODO sendfile_max_chunk

  case class server(directives: Directive[AppearsWithinServer]*) extends Context[AppearsWithinHttp, AppearsWithinServer](directives:_*)

  case class server_name(names: String*) extends Directive[AppearsWithinServer] {
    override def print(indent: String, out: Writer): Unit = out append s"$indent$name ${names.mkString(" ")};\n"
  }

  // TODO server_name_in_redirect
  // TODO server_names_hash_bucket_size
  // TODO server_names_hash_max_size
  // TODO server_tokens
  // TODO subrequest_output_buffer_size

  case class tcp_nodelay(on: Boolean = true) extends BooleanDirective[AppearsWithinHSL]

  // TODO tcp_nopush
  // TODO try_files
  // TODO types
  // TODO types_hash_bucket_size
  // TODO types_hash_max_size
  // TODO underscores_in_headers
  // TODO variables_hash_bucket_size

  case class variables_hash_max_size(size: Long = 1024) extends SizeDirective[AppearsWithinHttp]

}
