package izumi.reflect.dottyreflection

import scala.quoted.{Expr, QuoteContext}

object Inspect {
  inline def inspect[T <: AnyKind]: String = ${ inspectAny[T] }

  def inspectAny[T <: AnyKind : quoted.Type](using qctx0: QuoteContext): Expr[String] = {
    val ref = new Inspector().buildTypeRef[T]
    println(s"result: $ref")
    Expr(ref.toString)
  }
}

final case class NameReference(ref: String, prefix: Option[NameReference]) {
  override def toString: String = prefix.fold("")(p => s"$p::") + ref
}

class Inspector(implicit qctx: QuoteContext) {
  import qctx.tasty._

  protected def log(s: String) = {
    println(" -> " + s)
  }

  def buildTypeRef[T <: AnyKind](using tpe: quoted.Type[T]): NameReference = {
    val uns = tpe.unseal
    log(s" -------- about to inspect ${tpe} --------")
    val v = inspectTree(uns)
    log(s" -------- done inspecting ${tpe} --------")
    v
  }

  private def inspectTree(tpeTree: TypeTree): NameReference = {
    log(s"INSPECT: $tpeTree: ${tpeTree.getClass}")
    if (tpeTree.symbol.isNoSymbol) {
      inspectType(tpeTree.tpe)
    } else {
      inspectSymbol(tpeTree.symbol)
    }
  }

  private def inspectType(tpe: Type): NameReference = {
    log(s"INSPECT: type `$tpe`")
    tpe match {
      case r: TypeRef =>
        inspectSymbol(r.typeSymbol)

      case a: AnnotatedType =>
        inspectType(a.underlying)

      case lazyref if lazyref.getClass.getName.contains("LazyRef") => // upstream bug seems like
        log(s"LazyRef occured $lazyref")
        throw new RuntimeException(s"LazyRef occured $lazyref")

      case o =>
        log(s"Type, UNSUPPORTED: ${o.getClass} - $o")
        throw new RuntimeException(s"Type, UNSUPPORTED: ${o.getClass} - $o")
    }
  }

  private def inspectSymbol(symbol: Symbol): NameReference = {
    log(s"INSPECT: symbol `$symbol`")
    symbol.tree match {
      case c: ClassDef =>
        symbolToNameReference(symbol)

      case t: TypeDef =>
        inspectTree(t.rhs.asInstanceOf[TypeTree])

      case v: ValDef =>
        inspectTree(v.tpt)

      case o =>
        log(s"SYMBOL TREE, UNSUPPORTED: $o")
        throw new RuntimeException(s"SYMBOL TREE, UNSUPPORTED: ${o.getClass} - $o")
    }
  }

  private def symbolToNameReference(sym: Symbol): NameReference = {
    val prefix = if (sym.maybeOwner.isNoSymbol) {
      None
    } else {
      sym.owner.tree match {
        case _: PackageDef => None
        case _ => Some(inspectSymbol(sym.maybeOwner))
      }
    }
    NameReference(sym.fullName, prefix)
  }

}
