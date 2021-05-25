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
        def id = column[Long]("id")

        def customer = column[Long]("customer")
        def customer_fk = foreignKey("customer_fk", customer, customerVal)(_.id)

        def productsQuantity = column[Int]("productsQuantity")
        def totalProductsPrice = column[BigDecimal]("totalProductsPrice")

        def coupon = column[Long]("coupon")
        def coupon_fk = foreignKey("coupon_fk", coupon, couponVal)(_.id)

        def createdAt = column[String]("createdAt")

        def * = (id, customer, productsQuantity, totalProductsPrice, coupon, createdAt) <> ((Cart.apply _).tupled, Cart.unapply)
    }

    import couponRepository.CouponTable
    import customerRepository.CustomerTable

    val cart = TableQuery[CartTable]
    private val customerVal = TableQuery[CustomerTable]
    private val couponVal = TableQuery[CouponTable]

    def create(customer: Long, productsQuantity: Int, totalProductsPrice: BigDecimal, coupon: Long, createdAt: String): Future[Cart] = db.run {
        (cart.map(c => (c.customer, c.productsQuantity, c.totalProductsPrice, c.coupon, c.createdAt))
            returning cart.map(_.id)
            into {case ((customer, productsQuantity, totalProductsPrice, coupon, createdAt), id) => Cart(id, customer, productsQuantity, totalProductsPrice, coupon, createdAt)}
        ) += (customer, productsQuantity, totalProductsPrice, coupon, createdAt)
    }

    def list(): Future[Seq[Cart]] = db.run {
        cart.result
    }

    def getByCustomer(customer_id: Long): Future[Seq[Cart]] = db.run {
        cart.filter(_.customer === customer_id).result
    }
}
