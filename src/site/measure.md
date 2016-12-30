# Measure

**skylark-measure** is a library dealing with unit-of-measure definition, arithmetic, simplification and conversions in a type-safe manner.

### Configuration

```scala
libraryDependencies += "com.quantarray" %% "skylark-measure" % "0.15.0"
```

### Simple usage

Many units of measure are defined for you.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.measures._

kg
lb
Pa
Hz
```

Any unit of measure will have a set of basic properties that you would naturally expect to interrogate.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.measures._

kg.name should be("kg")
kg.dimension should be(Mass)
kg.system should be(SI)
kg.isStructuralAtom should be(true)
kg.exponent should be(1.0)
kg * s should be(ProductMeasure(kg, s))
kg.inverse should be(ExponentialMeasure(kg, -1.0))
kg to kg should be(Some(1))
kg to lb should be(Some(2.204625))
kg to g should be(Some(1000))
kg to cg should be(Some(100000))
kg to oz_metric should be(None) // Default conversion is not guaranteed to exist
```

You can take existing units and compose more complex ones by multiplying, dividing, and exponentiating.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.measures._

val N = kg * m / sec ^ 2
```

You can find our the conversion factor from one `to` another. No conversion factor may exist.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.measures._
import org.scalatest.OptionValues._

(kg to lb).value should be(2.204625)
```

When dealing with marshalling/encoding/serialization, you can store units of measure along with a numeric value as a plain string.
With `AnyMeasureParsers` you can turn that string back into a measure.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.measures._
import org.scalatest.OptionValues._

val parser = AnyMeasureParsers(USD, bbl) // Declare the measure atom instances you expect to be present
parser.parse("USD / bbl").value should equal(USD / bbl)
```

It's easy to compose numerical quantities with units of measure using a dot or postfix syntax.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.quantities._

10.kg
4 m
1000.0.bbl
```

You can perform the expected arithmetic operations on quantities.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.quantities._

10.kg * 4.m should equal(40.0 * (kg * m))
(4.oz_troy * 7.percent).to(oz_troy) should equal(0.28.oz_troy)

10.kg / 2.m should equal(5.0 * (kg / m))
(10.USD / 2.percent).to(USD) should equal(500.USD)

10.kg + 3.kg should equal(13.kg)
10.kg - 3.kg should equal(7.kg)
10.kg + (3.lb to kg) should equal(11.360775642116007.kg)
```

Quantity conversions are also supported via the same `to` operator. Basic converters are pre-defined. Conversions for product, ratio, and exponential measures
are defined by converters and require their own `CanConvert` instances of their components' conversions.

```scala
import com.quantarray.skylark.measure.implicits._
import com.quantarray.skylark.measure.quantities._
import org.scalatest.OptionValues._

(1.ft to in).value should equal(12.0 in)
(12.in to ft).value should equal(1.0 ft)
```

### Measure vs. AnyMeasure

**skylark-measure** gives you the freedom and flexibility to work with measures of a defined dimension (e.g. `MassMeasure`) or `AnyMeasure` - a measure whose dimension can only be known at run time.
The choice of which to work with depends on the individual API you would like to expose and enforce.

In the situation where you know you must receive a `MassMeasure`, you would encode exactly as natural logic or physics would dictate. There is, hence, no chance
someone can pass a quantity in units of `LuminousFluxMeasure`, for example, where a quantity in units of `MassMeasure` is expected (unless one finds compiler errors
aesthetically pleasing).

```scala
type VelocityMeasure = RatioMeasure[LengthMeasure, TimeMeasure]

type MomentumMeasure = ProductMeasure[MassMeasure, VelocityMeasure]

type Mass = Quantity[Double, MassMeasure]

type Velocity = Quantity[Double, VelocityMeasure]

type Momentum = Quantity[Double, MomentumMeasure]

def momentum(mass: Mass, velocity: Velocity): Momentum = mass * velocity
```

In other situations, where knowledge of a measure's dimension is uncertain, one would rely on `AnyMeasure`.
Operations on `AnyMeasure` yields another `AnyMeasure` and thus have less strict type requirements than a type derived from `Measure`.
You can always `match` on `AnyMeasure` to check or assert a certain shape.

### Overriding default behavior

The default arithmetic, conversion, and simplification operations are defined for both dimensional measure types and `AnyMeasure`.

#### Arithmetic

**skylark-measure** relies on the presence of `implicit` type classes `CanMultiply`, `CanDivide`, and `CanExponentiate` to perform arithmetic operations.

By default

* `m1 * m2` returns `ProductMeasure(m1, m2)`;
* `m1 / m2` returns `RatioMeasure(m1, m2)`;
* `m ^ n` return `ExponentialMeasure(m, n`).

One can, however, override the return type by proving a custom implicit class that derives from one of the three `Can*` traits.

For example, say when one does `b / s` (bits per second), one wants to work with a custom `BitRateMeasure` instead of the default `RatioMeasure(bit, s)`.

One would then need to define the custom object like `InformationTimeCanDivide`:

```scala
object custom extends com.quantarray.skylark.measure.arithmetic.SafeArithmeticImplicits
{

  implicit object InformationTimeCanDivide extends CanDivide[InformationMeasure, TimeMeasure, BitRateMeasure]
  {
    override def divide(numerator: InformationMeasure, denominator: TimeMeasure): BitRateMeasure = BitRateMeasure(numerator, denominator)
  }

}
```

#### Conversion

**skylark-measure** relies on the presence of `implicit` type class `CanConvert` and helper `Converter` type to convert between measures of different type.

```scala
object VolumeToExponentialLengthConverter extends Converter[VolumeMeasure, ExponentialLengthMeasure]
{
  import com.quantarray.skylark.measure.measures._

  override def apply(from: VolumeMeasure, to: ExponentialLengthMeasure): Option[Double] = Conversion(from, to) match
  {
    case `bbl` ⤇ `gal` => Some(42.0)
  }
}

trait BaseCommodityConversionImplicits
{

  implicit val volumeToExponentialLengthCanConvert = new CanConvert[VolumeMeasure, ExponentialLengthMeasure]
  {
    override def convert: Converter[VolumeMeasure, ExponentialLengthMeasure] = VolumeToExponentialLengthConverter
  }

}
```

#### Simplification

```scala
type EnergyPriceTimesFXMeasure = ProductMeasure[EnergyPrice, FX]

implicit val energyPriceTimesCurrencyPriceCanSimplify = new CanSimplify[EnergyPriceTimesFXMeasure, Option[EnergyPrice]]
{
  override def simplify(inflated: EnergyPriceTimesFXMeasure): Option[EnergyPrice] =
  {
    if (inflated.multiplicand.numerator == inflated.multiplier.denominator)
    {
      Some(RatioMeasure(inflated.multiplier.numerator, inflated.multiplicand.denominator))
    }
    else
    {
      None
    }
  }
}
```

### Package organization

#### Types

```scala
// Types
import com.quantarray.skylark.measure._
```

#### Measures

```scala
// Dimensional measures
import com.quantarray.skylark.measure.measures._
```

```
// AnyMeasure(s) (use these if it's more convenient to match on the measure's shape at run time)
import com.quantarray.skylark.measure.any.measures._
```

#### Quantities

```scala
// Dimensional quantities
import com.quantarray.skylark.measure.quantities._
```

```
// AnyQuantity(s) (use these if it's more convenient to match on the quantity measure's shape at run time)
import com.quantarray.skylark.measure.any.quantities._
```

#### Arithmetic, simplification, conversion

```scala
// Safe arithmetic (no exceptions thrown)
import com.quantarray.skylark.measure.arithmetic.safe._

// Default simplification
import com.quantarray.skylark.measure.simplification.default._

// Default conversion
import com.quantarray.skylark.measure.conversion.default._

// Safe arithmetic, default simplification, and default conversion in one shot
import com.quantarray.skylark.measure.implicits._
```

```scala
// Safe arithmetic (no exceptions thrown)
import com.quantarray.skylark.measure.any.arithmetic.safe._

// Default simplification
import com.quantarray.skylark.measure.any.simplification.default._

// Default conversion
import com.quantarray.skylark.measure.any.conversion.default._

// Safe arithmetic, default simplification, and default conversion in one shot
import com.quantarray.skylark.measure.any.implicits._
```

### Complete example: Taylor series

Taylor series expansion of a function is an approximation of a differentiable function as an infinite sum of
derivatives at a given point. The [Wikipedia article](https://en.wikipedia.org/wiki/Taylor_series) defines it as

$$ f(x) = f(a) + \frac{f'(a)}{1!}(x-a) + \frac{f''(a)}{2!}(x-a)^2 + \frac{f'''(a)}{3!}(x-a)^3 + \ldots $$

By change of variables, $y = a$ and $dy = x - a = x - y$, the Taylor series can be rewritten as

$$ f(y + dy) = f(y) + \frac{f'(y)}{1!}(dy) + \frac{f''(y)}{2!}(dy)^2 + \frac{f'''(y)}{3!}(dy)^3 + \ldots $$

Each derivative quantity above has its own unique unit of measure. Keeping track of the measure is essential to
 computing the final estimate. The example below shows how the measures are defined, multiplied, summed, and simplified.

```scala sourceLinkURI=https://github.com/quantarray/skylark/tree/master/skylark-measure/src/test/scala/com/quantarray/skylark/measure
import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.any.measures._
import com.quantarray.skylark.measure.any.quantities._
import org.apache.commons.math3.util.CombinatoricsUtils
import org.scalatest.{FlatSpec, Matchers}

case class QuantifiedTaylorSeries1(f: AnyQuantity[Double] => AnyQuantity[Double]) extends ((AnyQuantity[Double]) => AnyQuantity[Double])
{
  override def apply(x: AnyQuantity[Double]): AnyQuantity[Double] = f(x)

  def estimate(partials: Seq[AnyQuantity[Double] => AnyQuantity[Double]])(x: AnyQuantity[Double])(dx: AnyQuantity[Double]): AnyQuantity[Double] =
  {
    import com.quantarray.skylark.measure.any.arithmetic.unsafe._
    import com.quantarray.skylark.measure.any.conversion.default._
    import com.quantarray.skylark.measure.any.simplification.default._

    partials.zipWithIndex.map(pi => pi._1(x) * (dx ^ pi._2) / CombinatoricsUtils.factorial(pi._2)).reduce(_.simplify + _.simplify)
  }
}

"QuantifiedTaylorSeries1" should "estimate e^x" in
  {
    import com.quantarray.skylark.measure.any.implicits._

    val ts = QuantifiedTaylorSeries1(x => AnyQuantity(math.exp(x.value), USD))

    // Derivative of e^x is e^x
    // The function is a special value function, measured in USD
    // The function input is price of oil, measured in USD / bbl
    // Thus the first derivative (i.e. change in function per change in input) is measured in USD / (USD / bbl) = bbl
    // And the second derivative (i.e. change in first derivative per change in input) is measured in bbl / (USD / bbl) = bbl ^ 2 / USD
    val partials = Seq[AnyQuantity[Double] => AnyQuantity[Double]](ts, x => AnyQuantity(math.exp(x.value), bbl),
      x => AnyQuantity(math.exp(x.value), (bbl ^ 2) / USD))
    val estimate = ts.estimate(partials)(2.0 (USD / bbl))(0.01 (USD / bbl))
    val actual = ts(2.01 (USD / bbl))

    estimate.measure should be(USD)
    math.abs((estimate - actual).get.value) should be < 1e-5
  }

```