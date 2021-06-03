package models.repository

import models.Order
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                val customerRepository: CustomerRepository,
                                val couponRepository: CouponRepository)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def createdAt = column[String]("createdAt")
        def customer = column[Int]("customer")
        def customer_fk = foreignKey("customer_fk", customer, customerVal)(_.id)
        def isPaid = column[Boolean]("isPaid")
        def paidAt = column[String]("paidAt")
        def totalOrderValue = column[Float]("totalOrderValue")
        def coupon = column[Int]("coupon")
        def coupon_fk = foreignKey("coupon_fk", coupon, couponVal)(_.id)

        def * = (id, createdAt, customer, isPaid, paidAt, totalOrderValue, coupon) <> ((Order.apply _).tupled, Order.unapply)
    }

    import couponRepository.CouponTable
    import customerRepository.CustomerTable

    val order = TableQuery[OrderTable]
    val customerVal = TableQuery[CustomerTable]
    val couponVal = TableQuery[CouponTable]

    def create(createdAt: String, customer: Int, isPaid: Boolean, paidAt: String, totalOrderValue: Float, coupon: Int): Future[Order] = db.run {
        (order.map(o => (o.createdAt, o.customer, o.isPaid, o.paidAt, o.totalOrderValue, o.coupon))
            returning order.map(_.id)
            into {case((createdAt, customer, isPaid, paidAt, totalOrderValue, coupon), id) => Order(id, createdAt, customer, isPaid, paidAt, totalOrderValue, coupon)}
        ) += (createdAt, customer, isPaid, paidAt, totalOrderValue, coupon)
    }

    def list(): Future[Seq[Order]] = db.run {
        order.result
    }

    def getById(id: Int): Future[Order] = db.run {
        order.filter(_.id === id).result.head
    }

    def getByIdOption(id: Int): Future[Option[Order]] = db.run {
        order.filter(_.id === id).result.headOption
    }

    def getByCustomer(customer_id: Int): Future[Seq[Order]] = db.run {
        order.filter(_.customer === customer_id).result
    }

    def delete(id: Int): Future[Unit] = db.run(order.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, new_order: Order): Future[Unit] = {
        val orderToUpdate: Order = new_order.copy(id)
        db.run(order.filter(_.id === id).update(orderToUpdate)).map(_ => ())
    }

}
