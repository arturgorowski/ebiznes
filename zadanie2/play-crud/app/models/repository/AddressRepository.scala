package models.repository

import models.Address
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

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

    def getByOptionId(id: Int): Future[Option[Address]] = db.run {
        address.filter(_.id === id).result.headOption
    }

    def delete(id: Int): Future[Unit] = db.run(address.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, newAddress: Address): Future[Unit] = {
        val addressToUpdate: Address = newAddress.copy(id)
        db.run(address.filter(_.id === id).update(addressToUpdate)).map(_ => ())
    }
}
