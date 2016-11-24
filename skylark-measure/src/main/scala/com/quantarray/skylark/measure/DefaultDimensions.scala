/*
 * Skylark
 * http://skylark.io
 *
 * Copyright 2012-2016 Quantarray, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quantarray.skylark.measure

/**
  * Default dimensions.
  *
  * @author Araik Grigoryan
  */
trait DefaultDimensions extends Serializable
{

  case class NoDimension() extends Dimension[NoDimension]
  {
    override def toString: String = "\uD835\uDFD9"
  }

  val Dimensionless = NoDimension()

  case class TimeDimension() extends Dimension[TimeDimension]
  {
    override def toString: String = "Time"
  }

  val Time = TimeDimension()

  case class LengthDimension() extends Dimension[LengthDimension]
  {
    override def toString: String = "Length"
  }

  val Length = LengthDimension()

  case class MassDimension() extends Dimension[MassDimension]
  {
    override def toString: String = "Mass"
  }

  val Mass = MassDimension()

  case class TemperatureDimension() extends Dimension[TemperatureDimension]
  {
    override def toString: String = "Temperature"
  }

  val Temperature = TemperatureDimension()

  case class AmountDimension() extends Dimension[AmountDimension]
  {
    override def toString: String = "Amount"
  }

  val Amount = AmountDimension()

  case class ElectricCurrentDimension() extends Dimension[ElectricCurrentDimension]
  {
    override def toString: String = "Electric Current"
  }

  val ElectricCurrent = ElectricCurrentDimension()

  case class LuminousIntensityDimension() extends Dimension[LuminousIntensityDimension]
  {
    override def toString: String = "Luminous Intensity"
  }

  val LuminousIntensity = LuminousIntensityDimension()

  // E.g. digital information, such as bit
  case class InformationDimension() extends Dimension[InformationDimension]
  {
    override def toString: String = "Information"
  }

  val Information = InformationDimension()

  // E.g. Currency, such as USD
  case class MoneyDimension() extends Dimension[MoneyDimension]
  {
    override def toString: String = "Money"
  }

  val Money = MoneyDimension()

  type ForceDimension = RatioDimension[ProductDimension[MassDimension, LengthDimension], ExponentialDimension[TimeDimension]]

  val Force: RatioDimension[ProductDimension[MassDimension, LengthDimension], ExponentialDimension[TimeDimension]] = (Mass * Length) / (Time ^ 2)

  type EnergyDimension = RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]], ExponentialDimension[TimeDimension]]

  val Energy: RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]], ExponentialDimension[TimeDimension]] = (Mass * (Length ^ 2)) / (Time ^ 2)

  type PowerDimension = RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]], ExponentialDimension[TimeDimension]]

  val Power: RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]], ExponentialDimension[TimeDimension]] = (Mass * (Length ^ 2)) / (Time ^ 3)

  type PressureDimension = RatioDimension[MassDimension, ProductDimension[LengthDimension, ExponentialDimension[TimeDimension]]]

  val Pressure: RatioDimension[MassDimension, ProductDimension[LengthDimension, ExponentialDimension[TimeDimension]]] = Mass / (Length * (Time ^ 2))

  type LuminousFluxDimension = ProductDimension[LuminousIntensityDimension, NoDimension]

  val LuminousFlux: ProductDimension[LuminousIntensityDimension, NoDimension] = LuminousIntensity * Dimensionless

  type VoltageDimension = RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]],
    ProductDimension[ExponentialDimension[TimeDimension], ElectricCurrentDimension]]

  val Voltage: RatioDimension[ProductDimension[MassDimension, ExponentialDimension[LengthDimension]], ProductDimension[ExponentialDimension[TimeDimension], ElectricCurrentDimension]] = (Mass * (Length ^ 2)) / ((Time ^ 3) * ElectricCurrent)

  type TemporalFrequencyDimension = RatioDimension[NoDimension, TimeDimension]

  val TemporalFrequency: RatioDimension[NoDimension, TimeDimension] = Dimensionless / Time

  type SpatialFrequencyDimension = RatioDimension[NoDimension, LengthDimension]

  val SpatialFrequency: RatioDimension[NoDimension, LengthDimension] = Dimensionless / Length

  type AngularFrequencyDimension = RatioDimension[NoDimension, TimeDimension]

  val AngularFrequency = TemporalFrequency

  type ElectricChargeDimension = ProductDimension[ElectricCurrentDimension, TimeDimension]

  val ElectricCharge: ProductDimension[ElectricCurrentDimension, TimeDimension] = ElectricCurrent * Time

}
