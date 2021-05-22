package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                    addressRepository: AddressRepository)(implicit ec: ExecutionContext) {
    private val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class CustomerTable(tag: Tag) extends Table[Customer](tag, "customer") {

        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

        def username = column[String]("username")

        def firstName = column[String]("firstName")

        def lastName = column[String]("lastName")

        def password = column[String]("password")

        def salt = column[String]("salt")

        def createdAt = column[String]("createdAt")

        def address = column[Int]("address")

        def address_fk = foreignKey("address_fk", address, addressVal)(_.id)

        def * = (id, username, firstName, lastName, password, salt, createdAt, address) <> ((Customer.apply _).tupled, Customer.unapply)
    }

    import addressRepository.AddressTable
    val customer = TableQuery[CustomerTable]
    val addressVal = TableQuery[AddressTable]

    def create(username: String, firstName: String, lastName: String, password: String, salt: String, createdAt: String, address: Int): Future[Customer] = db.run {
        (customer.map(c => (c.username, c.firstName, c.lastName, c.password, c.salt, c.createdAt, c.address))
            returning customer.map(_.id)
            into {case((username, firstName, lastName, password, salt, createdAt, address), id) => Customer(id, username, firstName, lastName, password, salt, createdAt, address)}
            ) += (username, firstName, lastName, password, salt, createdAt, address)
    }

    def list(): Future[Seq[Customer]] = db.run {
        customer.result
    }

}
