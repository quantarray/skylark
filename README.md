# Skylark

Skylark is a set of libraries for quantitative and financial computation.

## skylark-measure

**skylark-measure** is a library dealing with unit-of-measure conversions in a type-safe manner. Many libraries provide similar functionality on 
their surface but in the end lack the richness and versatility necessary to use in real enterprise applications.

Many units of measure are defined for you, e.g.

```scala
kg
lb
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

You can find our the conversion factor from one *to* another (or none may exist).

```scala
(kg to lb).value should be(2.204625)
```

You can attach substances (or, in general, assets) to units of measure to distinguish substances of different quality or composition, 
which can impact the conversion factor. For example, West Texas Intermediate (WTI) barrel of oil has a different conversion factor to gallons than
a barrel of water.

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
use the '*' syntax.

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

Quantity conversions are also supported to the same `to` operator.

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
