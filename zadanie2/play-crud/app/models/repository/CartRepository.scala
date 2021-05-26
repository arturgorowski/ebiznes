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
        def id = column[Int]("id")

        def customer = column[Int]("customer")
        def customer_fk = foreignKey("customer_fk", customer, customerVal)(_.id)

        def productsQuantity = column[Int]("productsQuantity")
        def totalProductsPrice = column[BigDecimal]("totalProductsPrice")

        def coupon = column[Int]("coupon")
        def coupon_fk = foreignKey("coupon_fk", coupon, couponVal)(_.id)

        def createdAt = column[String]("createdAt")

        def * = (id, customer, productsQuantity, totalProductsPrice, coupon, createdAt) <> ((Cart.apply _).tupled, Cart.unapply)
    }

    import couponRepository.CouponTable
    import customerRepository.CustomerTable

    val cart = TableQuery[CartTable]
    private val customerVal = TableQuery[CustomerTable]
    private val couponVal = TableQuery[CouponTable]

    def create(customer: Int, productsQuantity: Int, totalProductsPrice: BigDecimal, coupon: Int, createdAt: String): Future[Cart] = db.run {
        (cart.map(c => (c.customer, c.productsQuantity, c.totalProductsPrice, c.coupon, c.createdAt))
            returning cart.map(_.id)
            into {case ((customer, productsQuantity, totalProductsPrice, coupon, createdAt), id) => Cart(id, customer, productsQuantity, totalProductsPrice, coupon, createdAt)}
        ) += (customer, productsQuantity, totalProductsPrice, coupon, createdAt)
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

    def getByCustomer(customer_id: Int): Future[Seq[Cart]] = db.run {
        cart.filter(_.customer === customer_id).result
    }

    def getByCustomers(customer_ids: List[Int]): Future[Seq[Cart]] = db.run {
        cart.filter(_.customer inSet customer_ids).result
    }

    def getByCoupon(coupon_id: Int): Future[Seq[Cart]] = db.run {
        cart.filter(_.coupon === coupon_id).result
    }

    def getByCoupons(coupon_id: List[Int]): Future[Seq[Cart]] = db.run {
        cart.filter(_.coupon inSet coupon_id).result
    }

    def delete(id: Int): Future[Unit] = db.run(cart.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, new_cart: Cart): Future[Unit] = {
        val cartToUpdate: Cart = new_cart.copy(id)
        db.run(cart.filter(_.id === id).update(cartToUpdate)).map(_ => ())
    }
}
