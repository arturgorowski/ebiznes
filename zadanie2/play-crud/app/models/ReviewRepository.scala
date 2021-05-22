package models

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReviewRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                  productRepository: ProductRepository,
                                  customerRepository: CustomerRepository)(implicit ec: ExecutionContext) {
    private val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    private class ReviewTable(tag: Tag) extends Table[Review](tag, "review") {

        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def product = column[Long]("product")
        def product_fk = foreignKey("product_fk", product, productValue)(_.id)
        def customer = column[Long]("customer")
        def customer_fk = foreignKey("customer_fk", customer, customerValue)(_.id)
        def content = column[String]("content")
        def score = column[Float]("score")
        def * = (id, product, customer, content, score) <> ((Review.apply _).tupled, Review.unapply)
    }

    import productRepository.ProductTable
    import customerRepository.CustomerTable

    private val review = TableQuery[ReviewTable]
    private val productValue = TableQuery[ProductTable]
    private val customerValue = TableQuery[CustomerTable]

    def create(product: Int, customer: Int, content: String, score: Float): Future[Review] = db.run {
        (review.map(r => (r.product, r.customer, r.content, r.score))
            returning review.map(_.id)
            into {case((product, customer, content, score), id) => Review(id, product, customer, content, score)}
            ) += (product, customer, content, score)
    }

    def list(): Future[Seq[Review]] = db.run {
        review.result
    }

    def getByProduct(product_id: Long): Future[Seq[Review]] = db.run {
        review.filter(_.product === product_id).result
    }

    def getByCustomer(customer_id: Long): Future[Seq[Review]] = db.run {
        review.filter(_.customer === customer_id).result
    }

}
