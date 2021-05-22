package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                customerRepository: CustomerRepository,
                                couponRepository: CouponRepository)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
        def createdAt = column[String]("createdAt")
        def customer = column[Long]("customer")
        def customer_fk = foreignKey("customer_fk", customer, customerVal)(_.id)
        def isPaid = column[Boolean]("isPaid")
        def paidAt = column[String]("paidAt")
        def totalOrderValue = column[Int]("totalOrderValue")
        def coupon = column[Long]("coupon")
        def coupon_fk = foreignKey("coupon_fk", coupon, couponVal)(_.id)

        def * = (id, createdAt, customer, isPaid, paidAt, totalOrderValue, coupon) <> ((Order.apply _).tupled, Order.unapply)
    }

    import customerRepository.CustomerTable
    import couponRepository.CouponTable

    private val order = TableQuery[OrderTable]
    private val customerVal = TableQuery[CustomerTable]
    private val couponVal = TableQuery[CouponTable]

    def create(createdAt: String, customer: Long, isPaid: Boolean, paidAt: String, totalOrderValue: Int, coupon: Long): Future[Order] = db.run {
        (order.map(o => (o.createdAt, o.customer, o.isPaid, o.paidAt, o.totalOrderValue, o.coupon))
            returning order.map(_.id)
            into {case((createdAt, customer, isPaid, paidAt, totalOrderValue, coupon), id) => Order(id, createdAt, customer, isPaid, paidAt, totalOrderValue, coupon)}
        ) += (createdAt, customer, isPaid, paidAt, totalOrderValue, coupon)
    }

    def list(): Future[Seq[Order]] = db.run {
        order.result
    }

    def getById(id: Long): Future[Order] = db.run {
        order.filter(_.id === id).result.head
    }

    def getByCustomer(customer_id: Long): Future[Seq[Order]] = db.run {
        order.filter(_.customer === customer_id).result
    }

}
