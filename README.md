# Skylark

Skylark is a set of libraries for quantitative and financial computation.

## skylark-measure

**skylark-measure** is a library dealing with unit-of-measure conversions in a type-safe manner. Many libraries provide similar functionality on 
their surface but in the end lack the richness and versatility necessary to use in real enterprise applications.

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
kg.multBase.value should be((1000, g)) // Gram is a base unit of Mass in the SI system
```

You can take existing units and compose more complex ones by multiplying, dividing, and exponentiating.

```scala
kg * m / sec ^ 2
```

You can find our the conversion factor from one *to* another.  No conversion factor may exist.

```scala
(kg to lb).value should be(2.204625)
```

You can attach substances (or, in general, assets) to units of measure to distinguish substances of different quality or composition, 
which can impact the conversion factor. For example, [West Texas Intermediate](http://en.wikipedia.org/wiki/West_Texas_Intermediate) (WTI) barrel of 
oil has a different conversion factor to gallons than a barrel of water.

```scala
(bbl to gal).value should equal(31.5)
(bbl of wti to gal).value should equal(42)
```
Conversions are done though `implicit` *ConversionProviders*. The default *ConversionProviders* should suffice for most needs but you can roll your own at any time.

Too many systems omit units of measure when storing or presenting numerical quantities. Now you can store units of measure along with a numeric value as a plain string.
With `MeasureParsers` you can turn that string back into a measure.
 
```scala
parseAll(measureExpression, "USD / bbl").get should be(USD / bbl)
parseAll(measureExpression, "((USD * (MMBtu / bbl) ^ 3) ^ 2) / MMBtu").get should be(((USD * ((MMBtu / bbl) ^ 3)) ^ 2) / MMBtu)
```

It's easy to compose numerical quantities with units of measure using a dot or postfix syntax. If the unit is structurally more complex than a single word, then
use the `*` syntax.

```scala
10.kg
4 m
1000 * (bbl of wti)
```

You can perform the expected arithmetic operations on quantities.

```scala
10.kg * 5 should be(50.kg)
10 * (kg of cotton)) * 5 should be(50 * (kg of cotton))
10.kg * 4.m should be(40 * (kg * m))
```

Arithmetic on different units may result in raw unit representation that may not be desirable. You may compact it as necessary.

```scala
val potOfGold = 30000.USD
val rate = 5.percent
val panOfGold = potOfGold * rate
panOfGold should equal(1500 * (USD * UnitMeasure))
panOfGold.measure.compact should equal(USD)
```

Quantity conversions are also supported via the same `to` operator.

```scala
((10 kg) to lb) should be(22.04625 lb)
50 bp to percent should be(0.5.percent)
```

Even when measures have non-trivial structural complexity, quantity conversions work as expected.

```scala
val rhoPercent = 2.5 * ((USD / MMBtu) / percent)
val rhoBasisPoint = rhoPercent to ((USD / MMBtu) / bp)
rhoBasisPoint should equal(0.025 * ((USD / MMBtu) / bp))
```

## skylark-measure-market

**skylark-measure-market** is a library that extends **skylark-measure** to the financial domain, where unit conversion factors are not fixed 
and normally vary as a function of time or space or both. For example, the conversion factor from US Dollars (`USD`) to Japanese Yen (`JPY`) will
depend on the observed time; similarly, a tanker of oil at two different locations around the globe will be worth a 
different quantity of dollars, even when controlling for the currency exchange rate between the two locations.

You can control the variability of a particular unit of measure with respect to another (e.g. how does `bbl of WTI` vary with respect to `USD`, otherwise known as a price)
by constructing a market manifold, which is a fancy name for a curve or a surface. In general, a market manifold is a function; in can be discrete or continuous and can
give you a value given a key.

In finance, a forward curve carries a price - a value of something vs. something else (e.g. `bbl of wti / USD` or `USD / GBP`).

```scala
val wtiForwardCurve = DiscreteForwardCurve(USD / (bbl of wti), Seq(("2014-04-01".d, 100.0), ("2017-01-01".d, 150.0)))
```

You can combine multiple manifolds into a `Market`, which is a special kind of a bag carrying all data relevant to a given observation date/time.

```scala
val observationDate: DateTime = "2014-04-01".d
val market = new GlobalMarket(
    Seq(
      TimeInstantCurve(observationDate),
      DiscreteForwardCurve(USD / (bbl of wti), Seq(("2014-04-01".d, 100.0), ("2017-01-01".d, 150.0))),
      DiscreteForwardCurve.flat(1.2 * (CAD / USD), observationDate.until("2018-01-01".d).by(Days.ONE)),
      DiscreteForwardCurve.flat(120 * (CAD / JPY), observationDate.until("2018-01-01".d).by(Days.ONE)),
      DiscreteForwardCurve.flat(100 * (USD / JPY), observationDate.until("2018-01-01".d).by(Days.ONE))
    )
  )
```

Note that `observationDate` is attached to the `Market` via a special `TimeInstantCurve` instead of making it a special property of the `Market`.

With the above assembled, you can do a lot of neat stuff.

```scala
val barrelPrice = 42 * (USD / (bbl of wti))
val gallonPrice = 1 * (CAD / gal)

val price1 = (barrelPrice + gallonPrice) to (CAD / gal)
val price2 = (gallonPrice + barrelPrice) to (USD / (bbl of wti))

price1.value should equal(2.2 +- 0.0000000000001)
price1.measure should be(CAD / gal)

price2.value should equal(77.0 +- 0.0000000000001)
price2.measure should be(USD / (bbl of wti))
```