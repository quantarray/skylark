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

You can find our the conversion factor from one to another (or none may exist).

```scala
(kg to lb).value should be(2.204625)
```

You can attach substances (or, in general, assets) to units of measure to distinguish substances of different quality or composition, 
which can impact the conversion factor. West Texas Intermediate (WTI) barrel of oil has a different conversion factor to gallons than
a barrel of water.

```scala
(bbl to gal).value should equal(31.5)
(bbl of wti to gal).value should equal(42)
```
