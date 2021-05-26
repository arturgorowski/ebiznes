package models.repository

import models.Review
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ReviewRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                  val productRepository: ProductRepository,
                                  val customerRepository: CustomerRepository)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class ReviewTable(tag: Tag) extends Table[Review](tag, "review") {

        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def product = column[Int]("product")
        def product_fk = foreignKey("product_fk", product, productValue)(_.id)
        def customer = column[Int]("customer")
        def customer_fk = foreignKey("customer_fk", customer, customerValue)(_.id)
        def content = column[String]("content")
        def score = column[Float]("score")
        def * = (id, product, customer, content, score) <> ((Review.apply _).tupled, Review.unapply)
    }

    import customerRepository.CustomerTable
    import productRepository.ProductTable

    val review = TableQuery[ReviewTable]
    val productValue = TableQuery[ProductTable]
    val customerValue = TableQuery[CustomerTable]

    def create(product: Int, customer: Int, content: String, score: Float): Future[Review] = db.run {
        (review.map(r => (r.product, r.customer, r.content, r.score))
            returning review.map(_.id)
            into {case((product, customer, content, score), id) => Review(id, product, customer, content, score)}
            ) += (product, customer, content, score)
    }

    def list(): Future[Seq[Review]] = db.run {
        review.result
    }

    def getByProduct(product_id: Int): Future[Seq[Review]] = db.run {
        review.filter(_.product === product_id).result
    }

    def getByCustomer(customer_id: Int): Future[Seq[Review]] = db.run {
        review.filter(_.customer === customer_id).result
    }

}
