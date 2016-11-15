<!--
  Title: Skylark
  Description: Collection of libraries for quantitative and financial computation.
  Author: Araik Grigoryan
  Copyright: 2012-2016 Quantarray, LLC
-->
  
<meta name='keywords' content='scala, unit of measure, skylark'>

[![Build Status](https://travis-ci.org/quantarray/skylark.svg?branch=master)](https://travis-ci.org/quantarray/skylark)

# Skylark

Skylark is a collection of libraries for quantitative and financial computation.

## skylark-measure

**skylark-measure** is a library dealing with unit-of-measure conversions in a type-safe manner. Many libraries provide similar functionality on 
their surface but in the end lack the richness and versatility necessary to use in real enterprise applications.

```scala
libraryDependencies += "com.quantarray" %% "skylark-measure" % "0.13.2"
```

```scala
import com.quantarray.skylark.measure._
import com.quantarray.skylark.measure.implicits._
```

### Simple usage

Many units of measure are defined for you.

```scala
kg
lb
Pa
Hz
```

Any unit of measure will have a set of basic properties that you would naturally expect to interrogate.

```scala
kg.name should be("kg")
kg.dimension should be(Mass)
kg.system should be(SI)
kg.isStructuralAtom should be(right = true)
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
val N = kg * m / sec ^ 2
```

You can find our the conversion factor from one `to` another. No conversion factor may exist.

```scala
(kg to lb).value should be(2.204625)
```

When dealing with marshalling/serialization, ou can store units of measure along with a numeric value as a plain string. 
With `MeasureParsers` you can turn that string back into a measure.
 
```scala
parseMeasure("USD / bbl").get should equal(USD / bbl)
```

It's easy to compose numerical quantities with units of measure using a dot or postfix syntax.

```scala
10.kg
4 m
1000.0.bbl
```

You can perform the expected arithmetic operations on quantities.

```scala
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
(1.ft to in) should equal(12.0 in)
(12.in to ft) should equal(1.0 ft)
```

### Strongly-typed and untyped measures and quantities

**skylark-measure** gives you the freedom and flexibility of working with strongly-typed measures (e.g. `MassMeasure`) or looser-typed `untyped.Measure`. 
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

In other situations, where knowledge of a type of measure is uncertain, one would rely on an amorphous `untyped.Measure`, in package 
`com.quantarray.skylark.measure.untyped`. Operations on `untyped.Measure` yields another `untyped.Measure`. You can always `match` on an 
`untyped.Measure` to check or assert a certain shape.

### Overriding default behavior

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