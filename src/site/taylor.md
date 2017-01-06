# Taylor series: a complete example

Taylor series expansion of a function is an approximation of a differentiable function as an infinite sum of
derivatives at a given point. The [Wikipedia article](https://en.wikipedia.org/wiki/Taylor_series) defines it as

$$ f(x) = f(a) + \frac{f'(a)}{1!}(x-a) + \frac{f''(a)}{2!}(x-a)^2 + \frac{f'''(a)}{3!}(x-a)^3 + \ldots $$

By change of variables, $y = a$ and $dy = x - a = x - y$, the Taylor series can be rewritten as

$$ f(y + dy) = f(y) + \frac{f'(y)}{1!}(dy) + \frac{f''(y)}{2!}(dy)^2 + \frac{f'''(y)}{3!}(dy)^3 + \ldots $$

Each derivative quantity above has its own unique unit of measure. Keeping track of the measure is essential to
 computing the final estimate. The example below shows how the measures are defined, multiplied, summed, and simplified.

```scala sourceLinkURI=https://github.com/quantarray/skylark/tree/master/skylark-measure/src/test/scala/com/quantarray/skylark/measure/TaylorSeriesSpec.scala

  import org.apache.commons.math3.util.CombinatoricsUtils
  import com.quantarray.skylark.measure.any.measures._
  import com.quantarray.skylark.measure.any.quantities._
  import org.scalatest.{FlatSpec, Matchers}

  import scala.language.postfixOps

  type Quoble = Quantity[Double, AnyMeasure]

  case class QuantifiedTaylorSeries1(f: Quoble => Quoble) extends (Quoble => Quoble)
  {
    override def apply(x: Quoble): Quoble = f(x)

    def estimate(partials: Seq[Quoble => Quoble])(x: Quoble)(dx: Quoble): Quoble =
    {
      import com.quantarray.skylark.measure.any.arithmetic.unsafe._
      import com.quantarray.skylark.measure.any.conversion.default._
      import com.quantarray.skylark.measure.any.simplification.default._

      partials.zipWithIndex.map(pi => pi._1(x) * (dx ^ pi._2) / CombinatoricsUtils.factorial(pi._2)).reduce(_.simplify[AnyMeasure] + _.simplify[AnyMeasure])
    }
  }

  "QuantifiedTaylorSeries1" should "estimate e^x" in
    {
      import com.quantarray.skylark.measure.any.implicits._

      val ts = QuantifiedTaylorSeries1(x => Quantity(math.exp(x.value), USD))

      // Derivative of e^x is e^x
      // The function is a special value function, measured in USD
      // The function input is price of oil, measured in USD / bbl
      // Thus the first derivative (i.e. change in function per change in input) is measured in USD / (USD / bbl) = bbl
      // And the second derivative (i.e. change in first derivative per change in input) is measured in bbl / (USD / bbl) = bbl ^ 2 / USD
      val partials = Seq[Quoble => Quoble](ts, x => Quantity(math.exp(x.value), bbl), x => Quantity(math.exp(x.value), (bbl ^ 2) / USD))
      val estimate = ts.estimate(partials)(2.0 (USD / bbl))(0.01 (USD / bbl))
      val actual = ts(2.01 (USD / bbl))

      estimate.measure should be(USD)
      math.abs((estimate - actual).get.value) should be < 1e-5
    }

```