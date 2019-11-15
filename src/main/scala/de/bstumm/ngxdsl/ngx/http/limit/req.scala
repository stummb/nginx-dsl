package de.bstumm.ngxdsl.ngx.http.limit

import java.io.Writer

import de.bstumm.ngxdsl.Dsl._
import req.limit_req_zone._

object req {

  object limit_req {
    def apply(zone: String): limit_req = apply(zone, None, None)
    def apply(zone: String, burst: Int): limit_req = new limit_req(zone, Some(burst), None)
    def apply(zone: String, burst: Int, nodelay: "nodelay"): limit_req = new limit_req(zone, Some(burst), Some(Left(nodelay)))
    def apply(zone: String, burst: Int, delay: Int): limit_req= apply(zone, Some(burst), Some(Right(delay)))
  }
  case class limit_req(zone: String, burst: Option[Int], delay: Option[Either["nodelay", Int]]) extends Directive[AppearsWithinHSL] {
    override def print(indent: String, out: Writer): Unit = {
      val d = delay match {
        case None => ""
        case Some(Left(x)) => x
        case Some(Right(y)) => y
      }
      out append (indent + s"$name zone=$zone ${burst.map(b => s"burst=$b")} $d".trim)
    }
  }


  object limit_req_zone{
    type Zone = String // TODO
    type Rate = String // TODO

    def apply(key: String, zone: Zone, rate: Rate): limit_req_zone = apply(key, zone, rate, None)
    def apply(key: String, zone: Zone, rate: Rate, sync: "sync"): limit_req_zone = new limit_req_zone(key, zone, rate, Some("sync"))
  }
  case class limit_req_zone(key: String, zone: Zone, rate: Rate, sync: Option["sync"] = None) extends Directive[AppearsWithinHttp] {
    override def print(indent: String, out: Writer): Unit =
      out append (indent + s"$name $key zone=$zone rate=$rate ${sync.getOrElse("")}".trim + ";\n")
  }
}
