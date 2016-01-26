# Skylark

Skylark is a set of libraries for quantitative and financial computation.

## skylark-measure

**skylark-measure** is a library dealing with unit-of-measure conversions in a type-safe manner. Many libraries provide similar functionality on 
their surface but in the end lack the richness and versatility necessary to use in real enterprise applications.

```scala
libraryDependencies += "com.quantarray" %% "skylark-measure" % "0.3.2"
```

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
kg / lb should be(UnitMeasure)
kg * s should be(ProductMeasure(kg, s))
kg.inverse should be(ExponentialMeasure(kg, -1.0))
kg to kg should be(Some(1))
kg to lb should be(Some(2.204625))
kg to g should be(Some(1000))
```

You can take existing units and compose more complex ones by multiplying, dividing, and exponentiating.

```scala
kg * m / sec ^ 2
```

You can find our the conversion factor from one *to* another.  No conversion factor may exist.

```scala
(kg to lb).value should be(2.204625)
```

Too many systems omit units of measure when storing or presenting numerical quantities. Now you can store units of measure along with a numeric value as a plain string.
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
10.0.kg * 4.0.m should equal(Quantity(40, kg * m))
4.0.oz_troy * 7.0.percent should equal(Quantity(0.28, oz_troy))

10.0.kg / 2.0.m should equal(Quantity(5, kg / m))
10.0.USD / 2.0.percent should equal(Quantity(500, USD))

10.0.kg + 3.0.kg should equal(13.0.kg)
10.0.kg - 3.0.kg should equal(7.0.kg)
10.0.kg + (3.0.lb to kg) should equal(11.360775642116007.kg)
```

Quantity conversions are also supported via the same `to` operator. Basic converters are pre-defined. Conversions for product, ratio, and exponential measures
is defined by converters and require their own CanConvert instances of their components' conversions.

```scala
(1.0.ft to in) should equal(12.0 in)
(12.0.in to ft) should equal(1.0 ft)
```
