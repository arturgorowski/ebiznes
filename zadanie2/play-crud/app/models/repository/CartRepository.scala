package models.repository

import models.Cart
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                val customerRepository: CustomerRepository,
                                val couponRepository: CouponRepository)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class CartTable(tag: Tag) extends Table[Cart](tag, "cart") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

        def customer = column[Int]("customer")
        def customerFk = foreignKey("customer_fk", customer, customerVal)(_.id)

        def coupon = column[Int]("coupon")
        def couponFk = foreignKey("coupon_fk", coupon, couponVal)(_.id)

        def * = (id, customer, coupon) <> ((Cart.apply _).tupled, Cart.unapply)
    }

    import couponRepository.CouponTable
    import customerRepository.CustomerTable

    val cart = TableQuery[CartTable]
    private val customerVal = TableQuery[CustomerTable]
    private val couponVal = TableQuery[CouponTable]

    def create(customer: Int, coupon: Int): Future[Cart] = db.run {
        (cart.map(c => (c.customer, c.coupon))
            returning cart.map(_.id)
            into {case ((customer, coupon), id) => Cart(id, customer, coupon)}
        ) += (customer, coupon)
    }

    def list(): Future[Seq[Cart]] = db.run {
        cart.result
    }

    def getById(id: Int): Future[Cart] = db.run {
        cart.filter(_.id === id).result.head
    }

    def getByIdOption(id: Int): Future[Option[Cart]] = db.run {
        cart.filter(_.id === id).result.headOption
    }

    def getByCustomerOption(customerId: Int): Future[Option[Cart]] = db.run {
        cart.filter(_.customer === customerId).result.headOption
    }

    def getByCustomer(customerId: Int): Future[Cart] = db.run {
        cart.filter(_.customer === customerId).result.head
    }

    def getByCustomers(customerIds: List[Int]): Future[Seq[Cart]] = db.run {
        cart.filter(_.customer inSet customerIds).result
    }

    def getByCoupon(couponId: Int): Future[Seq[Cart]] = db.run {
        cart.filter(_.coupon === couponId).result
    }

    def getByCoupons(couponId: List[Int]): Future[Seq[Cart]] = db.run {
        cart.filter(_.coupon inSet couponId).result
    }

    def delete(id: Int): Future[Unit] = db.run(cart.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, newCart: Cart): Future[Unit] = {
        val cartToUpdate: Cart = newCart.copy(id)
        db.run(cart.filter(_.id === id).update(cartToUpdate)).map(_ => ())
    }
}
