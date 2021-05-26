package models.repository

import models.CartItem
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartItemRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                    val cartRepository: CartRepository,
                                    val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    private class CartItemTable(tag: Tag) extends Table[CartItem](tag, "cart_item") {
        def id = column[Int]("id")

        def cart = column[Int]("cart")
        def cart_fk = foreignKey("cart_fk", cart, cartVal)(_.id)

        def product = column[Int]("product")
        def product_fk = foreignKey("product_fk", product, productVal)(_.id)

        def productQuantity = column[Int]("productQuantity")

        def * = (id, cart, product, productQuantity) <> ((CartItem.apply _).tupled, CartItem.unapply)
    }

    import cartRepository.CartTable
    import productRepository.ProductTable

    private val cartItem = TableQuery[CartItemTable]
    private val cartVal = TableQuery[CartTable]
    private val productVal = TableQuery[ProductTable]

    def create(cart: Int, product: Int, productQuantity: Int): Future[CartItem] = db.run {
        (cartItem.map(ci => (ci.cart, ci.product, ci.productQuantity))
            returning cartItem.map(_.id)
            into {case ((cart, product, productQuantity), id) => CartItem(id, cart, product, productQuantity)}
        ) += (cart, product, productQuantity)
    }

    def list(): Future[Seq[CartItem]] = db.run {
        cartItem.result
    }
}
