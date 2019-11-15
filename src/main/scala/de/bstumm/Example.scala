package de.bstumm

import java.io.File

import de.bstumm.ngxdsl.Dsl._
import de.bstumm.ngxdsl.ngx.core._
import de.bstumm.ngxdsl.ngx.http.core.{alias, _}
import de.bstumm.ngxdsl.ngx.http.headers.add_header
import de.bstumm.ngxdsl.ngx.http.rewrite.`return`

object Example extends App {

  // the main file
  def `nginx.conf`: NginxConfigFile[AppearsWithinMain] = NginxConfigFile("nginx.conf")(
    worker_processes(1),
    pcre_jit (on = true),
    error_log ("/dev/stderr", "warn"),
    daemon (on = false),
    events(
      worker_connections (2048)
    ),
    http(
      include (`conf/locations.conf`),
    )
  )

  // other generated file, included by the main file
  def `conf/locations.conf`: NginxConfigFile[AppearsWithinHttp] = NginxConfigFile("conf/locations.conf")(
    server (
      server_name ("_"),
      location ("/") (
        add_header ("Content-Type", "text/html"),
        `return` (200, "No Vhost configured")
      ),
    ),

    server (
      listen (3333),
      server_name ("stuff"),
      textServingLocation("/health", "/usr/share/nginx/health.txt"),
      textServingLocation("/metrics", "/usr/share/nginx/metrics.txt"),
      location ("/config") (
        add_header ("Content-Type", "text/plain; charset=utf-8"),
        alias ("/usr/share/nginx/config.txt")
      )
    )
  )

  // reusable, parameterized function
  def textServingLocation(path: String, _alias: String) =
    location.`=` (path) (
      add_header ("Content-Type", "text/plain"),
      alias (_alias)
    )


  def completeConfiguration = NginxConfiguration(
    `nginx.conf`,
    `conf/locations.conf`
  )

  completeConfiguration.serialize(new File("dummy"))

}
