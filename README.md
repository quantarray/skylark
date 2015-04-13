# Skylark

Skylark is a set of libraries for quantitative and financial computation.

# skylark-measure

skylark-measure is a library dealing with unit-of-measure conversions. Many libraries provide similar functionality on their surface 
but in the end lack the richness and versatility necessary to use in real enterprise applications.

Many units of measure are defined for you, e.g.

```scala
kg
lb
```

You can take existing units and compose more complex ones by multiplying, dividing, and exponentiating.

```scala
kg * m / sec ^ 2
```

You can find our the conversion factor from one to another (or none may exist).

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
Conversions are done though implicit ConversionProviders. Default ConversionProviders should suffice for most needs but you can roll your own at any time.

Too many systems omit units of measure when storing or presenting numerical results. Now you can store units of meausre along with a numeric value as a plain string.
With *MeasureParsers* you can turn that string back into a measure.
 
```scala
parseAll(measureExpression, "USD / bbl").get should be(USD / bbl)
parseAll(measureExpression, "((USD * (MMBtu / bbl) ^ 3) ^ 2) / MMBtu").get should be(((USD * ((MMBtu / bbl) ^ 3)) ^ 2) / MMBtu)
```

It's easy to compose numerical quantities with units of measure using a 'dot' or postfix syntax.

```scala
10.kg
4 m
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