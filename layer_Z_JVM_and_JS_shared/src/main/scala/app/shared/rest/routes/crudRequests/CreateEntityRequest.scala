//package app.shared.rest.routes.crudRequests
//
//import app.shared.SomeError_Trait
//import app.shared.data.model.Entity.Entity
//import app.shared.data.ref.RefVal
////import app.shared.rest.routes.Command
//import scalaz.\/
//
//import scala.reflect.ClassTag
//
///**
//  * Created by joco on 14/12/2017.
//  */
//
//
//case class CreateEntityRequest[E<:Entity:ClassTag]() extends Command[E] {
//  //  type E <:Entity
//  type Params = E //uuid
//  type Result = \/[SomeError_Trait,RefVal[E]]
//  // FONTOS - meroleges, iterativ, amig nincs ra szukseg, greedy modon lehet fejleszteni
//  // a tipusosssag miatt, flexibilis a tipusossag ... szepen iterativan lehet generalizalni
//  // semmi parameterezes ... az elejen ...
//  // a skalaban az a lenyeg, hogy lehet benne mozogni ...
//  // nem ragadok lokalis optimumban...
//  // javaban ha nem talalod ki az elejen jol
//  // python-ban felrobban a kod ...
//  // greedy modon lehet csinalni ...
//  // nem azon kell gondolkodni h mit kell belerakni az elejen ...
//  // refaktoringon nem kell gondolkodni...
//  // dimenzionalitas ... => lokalis - globalis ...
//  // tapasztalat azt mutatja...
//  // java-ban sarokba tudod magad szoritani
//  // scala-ban nehez sarokba szoritani magadat ...
//  // -> scala flexibilis ... sok a moving part ... => tudod mozgatni
//  //
//  //
//  //
//  // BESZORITAS
//  //
//  //
//  //
//  // emberi nyelv flexibilis , nem szoritod be magad...
//  // allapot... konszenzus... meg lehet beszelni a dolgokat
//  // nem kell arra koncentralni h hogyan rakosgatom ossze...
//  //
//  //
//  // egyszeruseg , ami nem olyan h el tudom olvasni azonnal azt ne csinald
//  //
//  // generalizalas
//  //
//  // konkrettol => altalanossa ... -> skala-ban lehet ...
//  //
//  // GREEDY => generalizacio fele ...
//  //
//  // GRADIENS FELE
//  //
//  // egymastol fuggetlenul tudod a valtozokat optimalizalni ... dimenziok menten...
//  //
//  //
//  // egyszerre csak 1 dimenziot optimalizalok
//  //
//  // SCALA = celfuggveny ... JATEK ...
//  //
//  // primitiv celfuggveny + greedy =>
//  //
//  // lokalis ... kovetkezo allapotokat
//  //
//  //
//  // dimenziokrol ... = feature - analogia ... gazdag nyelv ...
//  //
//  //
//  //
//  // nagy dimenzionalis terben mozogsz
//  //
//  //
//  // LEPES ... ATIROD ES NEM VALTOZIK
//  //
//  // pl validacio =>
//  //
//  //
//  // HAZEPITES SZAR
//  //
//  // EPITESZET =! SZOFTVERFEJLESZTES
//  // WATERFALL =! AGILE
//  //
//  // ITERATIV => vmi garantalja h.
//  //
//  // BE TARTASZ BIZONYOS IRANYELVEKET
//  //
//  //
//  //
//  // EPITKEZES = FIZIKAI CONSTRAINT ...
//  //
//  // MUSZALY MEGTERVEZNI ALACSONY DIMENZIO MIATT - FELHOKARCOLOT
//  // (=> REFAKTORALAS)
//  //
//  // KONSTRAINTEKET
//  //
//  //
//  //
//  //
//  //
//  // NYELV RAK BE CONSTRAIN-T ET
//  //
//
//  override def getServerPath = "create" + implicitly[ClassTag[E]].runtimeClass.getName
//  // server path does not start with /
//
//  def queryURL()= "/" + getServerPath
//}
//
//object CreateEntityRequest{
//  type CEC_Res[E<:Entity] = CreateEntityRequest[E]#Result
//}
