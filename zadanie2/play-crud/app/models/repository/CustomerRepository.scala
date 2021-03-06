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

        def userId = column[Int]("userId")

        def address = column[Int]("address")

        def addressFk = foreignKey("address_fk", address, addressVal)(_.id)

        def * = (id, username, firstName, lastName, userId, address) <> ((Customer.apply _).tupled, Customer.unapply)
    }

    import addressRepository.AddressTable
    private val customer = TableQuery[CustomerTable]
    val addressVal = TableQuery[AddressTable]

    def create(username: String, firstName: String, lastName: String, userId: Int, address: Int): Future[Customer] = db.run {
        (customer.map(c => (c.username, c.firstName, c.lastName, c.userId, c.address))
            returning customer.map(_.id)
            into {case((username, firstName, lastName, userId, address), id) => Customer(id, username, firstName, lastName, userId, address)}
            ) += (username, firstName, lastName, userId, address)
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

    def getByUserId(userId: Int): Future[Option[Customer]] = db.run {
        customer.filter(_.userId === userId).result.headOption
    }

    def getByAddress(addressId: Int): Future[Customer] = db.run {
        customer.filter(_.address === addressId).result.head
    }

    def delete(id: Int): Future[Unit] = db.run(customer.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, newCustomer: Customer): Future[Unit] = {
        val customerToUpdate: Customer = newCustomer.copy(id)
        db.run(customer.filter(_.id === id).update(customerToUpdate)).map(_ => ())
    }

}
