package models.repository

import models.OrderItem
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderItemRepository @Inject()(dbConfigProvider: DatabaseConfigProvider,
                                    val orderRepository: OrderRepository,
                                    val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    //    id: Long, order: Long, product: Long
    class OrderItemTable(tag: Tag) extends Table[OrderItem](tag, "order_item") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

        def order = column[Int]("order")

        def order_fk = foreignKey("order_fk", order, orderVal)(_.id)

        def product = column[Int]("product")

        def product_fk = foreignKey("product_fk", product, productVal)(_.id)

        def * = (id, order, product) <> ((OrderItem.apply _).tupled, OrderItem.unapply)
    }

    import orderRepository.OrderTable
    import productRepository.ProductTable

    val orderItem = TableQuery[OrderItemTable]
    val orderVal = TableQuery[OrderTable]
    val productVal = TableQuery[ProductTable]

    def create(order: Int, product: Int): Future[OrderItem] = db.run {
        (orderItem.map(oi => (oi.order, oi.product))
            returning orderItem.map(_.id)
            into { case ((order, product), id) => OrderItem(id, order, product) }
            ) += (order, product)
    }

    def list(): Future[Seq[OrderItem]] = db.run {
        orderItem.result
    }

    def getByOrder(order_id: Int): Future[Seq[OrderItem]] = db.run {
        orderItem.filter(_.order === order_id).result
    }

    def getByProduct(product_id: Int): Future[Seq[OrderItem]] = db.run {
        orderItem.filter(_.product === product_id).result
    }
}
