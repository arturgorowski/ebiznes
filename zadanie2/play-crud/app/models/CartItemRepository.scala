package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class CartItemRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                    cartRepository: CartRepository,
                                    productRepository: ProductRepository)(implicit ec: ExecutionContext) {
    private val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class CartItemTable(tag: Tag) extends Table[CartItem](tag, "cart_item") {
        def id = column[Long]("id")

        def cart = column[Long]("cart")
        def cart_fk = foreignKey("cart_fk", cart, cartVal)(_.id)

        def product = column[Long]("product")
        def product_fk = foreignKey("product_fk", product, productVal)(_.id)

        def productQuantity = column[Int]("productQuantity")

        def * = (id, cart, product, productQuantity) <> ((CartItem.apply _).tupled, CartItem.unapply)
    }

    import cartRepository.CartTable
    import productRepository.ProductTable

    private val cartItem = TableQuery[CartItemTable]
    private val cartVal = TableQuery[CartTable]
    private val productVal = TableQuery[ProductTable]

    def create(cart: Long, product: Long, productQuantity: Int): Future[CartItem] = db.run {
        (cartItem.map(ci => (ci.cart, ci.product, ci.productQuantity))
            returning cartItem.map(_.id)
            into {case ((cart, product, productQuantity), id) => CartItem(id, cart, product, productQuantity)}
        ) += (cart, product, productQuantity)
    }

    def list(): Future[Seq[CartItem]] = db.run {
        cartItem.result
    }
}
