package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

//    id: Int, street: String, number: Number, city: String, postalCode: String, voivodeship: String
//    id, street, number, city, postalCode, voivodeship
    class AddressTable(tag: Tag) extends Table[Address](tag, "address") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def street = column[String]("street")
        def number = column[Int]("number")
        def city = column[String]("city")
        def postalCode = column[String]("postalCode")
        def voivodeship = column[String]("voivodeship")
        def * = (id, street, number, city, postalCode, voivodeship) <> ((Address.apply _).tupled, Address.unapply)
    }

    val address = TableQuery[AddressTable]

    def create(street: String, number: Int, city: String, postalCode: String, voivodeship: String): Future[Address] = db.run {
        (address.map(a => (a.street, a.number, a.city, a.postalCode, a.voivodeship))
            returning address.map(_.id)
            into {case((street, number, city, postalCode, voivodeship), id) => Address(id, street, number, city, postalCode, voivodeship)}
        ) += (street, number, city, postalCode, voivodeship)
    }

    def list(): Future[Seq[Address]] = db.run {
        address.result
    }

    def getById(id: Int): Future[Address] = db.run {
        address.filter(_.id === id).result.head
    }
}
