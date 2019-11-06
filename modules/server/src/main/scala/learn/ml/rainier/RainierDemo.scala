package learn.ml.rainier

import com.stripe.rainier.repl._
import com.stripe.rainier.compute.Real
import com.stripe.rainier.core.{LogNormal, Poisson, Predictor, RandomVariable}
object RainierDemo extends App {
  val data = List(
    (0, 8),
    (1, 12),
    (2, 16),
    (3, 20),
    (4, 21),
    (5, 31),
    (6, 23),
    (7, 33),
    (8, 31),
    (9, 33),
    (10, 36),
    (11, 42),
    (12, 39),
    (13, 56),
    (14, 55),
    (15, 63),
    (16, 52),
    (17, 66),
    (18, 52),
    (19, 80),
    (20, 71)
  )

  val prior: RandomVariable[(Real, Real)] = for {
    slope <- LogNormal(0, 1).param
    intercept <- LogNormal(0, 1).param
  } yield (slope, intercept)

  val regr: RandomVariable[(Real, Real)] = for {
    (slope, intercept) <- prior
     predictor <-Predictor[Int]
      .from { i =>
        Poisson(intercept + slope * i)
      }.fit(data)
  } yield (slope, intercept)

  plot2D(regr.sample())

}
