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
        def productFk = foreignKey("product_fk", product, productValue)(_.id)
        def customer = column[Int]("customer")
        def customerFk = foreignKey("customer_fk", customer, customerValue)(_.id)
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

    def getById(id: Int): Future[Review] = db.run {
        review.filter(_.id === id).result.head
    }

    def getByIdOption(id: Int): Future[Option[Review]] = db.run {
        review.filter(_.id === id).result.headOption
    }

    def getByProduct(productId: Int): Future[Seq[Review]] = db.run {
        review.filter(_.product === productId).result
    }

    def getByProductOption(productId: Int): Future[Option[Review]] = db.run {
        review.filter(_.product === productId).result.headOption
    }

    def getByCustomer(customerId: Int): Future[Option[Review]] = db.run {
        review.filter(_.customer === customerId).result.headOption
    }

    def delete(id: Int): Future[Unit] = db.run(review.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, newReview: Review): Future[Unit] = {
        val reviewToUpdate: Review = newReview.copy(id)
        db.run(review.filter(_.id === id).update(reviewToUpdate)).map(_ => ())
    }
}
