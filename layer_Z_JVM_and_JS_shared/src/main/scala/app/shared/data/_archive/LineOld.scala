package app.shared.data.utils.model

import app.shared.CommonStrings
import app.shared.data.model.Entity.Data
import monocle.macros.Lenses

@Lenses
case class LineOld(val pl: LinePayload) extends Data{
//  def getImgSrc: ImgUrl = ? ? ? //TO DO implement this
//ad image as entity.... need to wait for ref implementation
  def getImgSrc: ImgUrl = ImgUrl(CommonStrings.imageRoute + "/" + 0 + ".jpg")
}

case class ImgUrl(url: String)

object LineOld {
//  def apply(t: String)(implicit ev: OnlyServerCanCreateEntity): Line = new Line(LinePayload(t))
  def apply(t: String): LineOld = new LineOld(LinePayload(t))
}

/**
  *
  * @param text
  * @param position_of_que_pointing_to_next_picture
  * @param que_definition part of the picture for current line that will be the que to this line and
  *                       will be displayed in an other picture (as a que)
  */


@Lenses
case class LinePayload(text: String,
                       position_of_que_pointing_to_next_picture: Option[Rect] = None,
                       que_definition: Option[Rect] = None)

case class Coord(x: Double, y: Double) {
  def map(f: Double => Double) = Coord(f(x), f(y))
  def *(f: Double)             = map(_ * f)
  def /(f: Double)             = map(_ / f)
  def +(c: Coord)              = Coord(x + c.x, y + c.y)
  def -(c: Coord)              = Coord(x - c.x, y - c.y)
}

case class Rect(x: Double, y: Double, h: Double, w: Double) {
  def center: Coord = {
    val ul = Coord(x, y)
    val bl = Coord(x + w, y + h)
    (ul + bl) / 2
  }

  def upperLeftCorner=Coord(x,y)

  /**
    * Scale the size.
    * @param f scaling factor
    * @return scaled `Rect`
    */
  def *(f: Double) = copy(h = h * f, w = w * f)

  /**
    *
    * @param r Target `Rect` into which we want to fit this `Rect`
    * @return Scaling factor that needs to be applied to this `Rect` such that it fits into `r`
    */
  def scalingFactor(r: Rect): Double =
    if (r.aspectRatio < aspectRatio) {
      // a magassag szerint kell skalazni
      r.h/h
    } else r.w/w

  def aspectRatio = h / w

  def setCenter(newCenter:Coord):Rect={
    copy(x=newCenter.x-w/2, y=newCenter.y-h/2)
  }

  /**
    *
    * @param r Target `Rect` into which we want to fit this `Rect`
    * @return The `Rect` that fits.
    */
  def fitInto(r: Rect): Rect = {
    val r1=r * scalingFactor(r)
    r1.setCenter(r.center)
  }
}

