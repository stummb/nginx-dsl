package de.bstumm.ngxdsl.ngx.http

import de.bstumm.ngxdsl.Dsl._

object gzip {
  case class gzip_vary(on: Boolean = false) extends BooleanDirective[AppearsWithinHSL]

}