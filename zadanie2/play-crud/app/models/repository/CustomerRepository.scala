package models.repository

import models.Customer
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CustomerRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                    val addressRepository: AddressRepository)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class CustomerTable(tag: Tag) extends Table[Customer](tag, "customer") {

        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

        def username = column[String]("username")

        def firstName = column[String]("firstName")

        def lastName = column[String]("lastName")

        def password = column[String]("password")

        def createdAt = column[String]("createdAt")

        def address = column[Int]("address")

        def address_fk = foreignKey("address_fk", address, addressVal)(_.id)

        def * = (id, username, firstName, lastName, password, createdAt, address) <> ((Customer.apply _).tupled, Customer.unapply)
    }

    import addressRepository.AddressTable
    private val customer = TableQuery[CustomerTable]
    val addressVal = TableQuery[AddressTable]

    def create(username: String, firstName: String, lastName: String, password: String, createdAt: String, address: Int): Future[Customer] = db.run {
        (customer.map(c => (c.username, c.firstName, c.lastName, c.password, c.createdAt, c.address))
            returning customer.map(_.id)
            into {case((username, firstName, lastName, password, createdAt, address), id) => Customer(id, username, firstName, lastName, password, createdAt, address)}
            ) += (username, firstName, lastName, password, createdAt, address)
    }

    def list(): Future[Seq[Customer]] = db.run {
        customer.result
    }

    def getById(id: Int): Future[Customer] = db.run {
        customer.filter(_.id === id).result.head
    }

    def getByIdOption(id: Int): Future[Option[Customer]] = db.run {
        customer.filter(_.id === id).result.headOption
    }

    def getByAddress(address_id: Int): Future[Customer] = db.run {
        customer.filter(_.address === address_id).result.head
    }

    def delete(id: Int): Future[Unit] = db.run(customer.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, new_customer: Customer): Future[Unit] = {
        val customerToUpdate: Customer = new_customer.copy(id)
        db.run(customer.filter(_.id === id).update(customerToUpdate)).map(_ => ())
    }

}
