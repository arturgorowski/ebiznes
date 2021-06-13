package controllers

import models.Review
import models.repository.{CustomerRepository, ProductRepository, ReviewRepository}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.floatFormat
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import javax.inject._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}


@Singleton
class ReviewController @Inject()(reviewRepository: ReviewRepository,
                                 productRepository: ProductRepository,
                                 customerRepository: CustomerRepository,
                                 controllerComponents: MessagesControllerComponents)
                                (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val reviewForm: Form[CreateReviewForm] = Form {
        mapping(
            "customer" -> number,
            "productsQuantity" -> number,
            "content" -> nonEmptyText,
            "score" -> of[Float]
        )(CreateReviewForm.apply)(CreateReviewForm.unapply)
    }

    // VIEWS
    def getReviewForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val reviews = reviewRepository.list()
        reviews.map( review => Ok(views.html.reviewsList(review)))
    }

    //JSON
    def getReviews: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val reviews = Await.ready(reviewRepository.list(), Duration.Inf).value.get.get

        var reviewsModel: List[ReviewModel] = List[ReviewModel]()
        reviews.foreach{ review =>
            val product = Await.ready(productRepository.getById(review.product), Duration.Inf).value.get.get
            val customer = Await.ready(customerRepository.getById(review.customer), Duration.Inf).value.get.get
            reviewsModel = ReviewModel(
                review.id,
                ProductModel(product.id, product.name),
                CustomerModel(customer.id, customer.firstName, customer.lastName),
                review.content,
                review.score) :: reviewsModel
        }

        Ok(Json.toJson(reviewsModel))

//        reviewRepository.list().map { reviews =>
//            Ok(Json.toJson(reviews)).as("application/json")
//        }
    }

    def getReview(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        reviewRepository.getByIdOption(id: Int).map {
            case Some(review) => Ok(Json.toJson(review))
            case None => Ok(Json.toJson(AnyContentAsEmpty.asJson))
        }
    }

    def getCustomerReviews(customer_id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        reviewRepository.getByCustomer(customer_id: Int).map {
            case Some(reviews) => Ok(Json.toJson(reviews)).as("application/json")
            case None => Ok(Json.toJson(AnyContentAsEmpty.asJson))
        }
    }

//    def getProductReviews(product_id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//        reviewRepository.getByProduct(product_id: Int).map {
//            case Some(reviews) => Ok(Json.toJson(reviews)).as("application/json")
//            case None => Ok(Json.toJson(AnyContentAsEmpty.asJson))
//        }
//    }

    def getProductReviews(product_id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        reviewRepository.getByProduct(product_id: Int).map {
            reviews => Ok(Json.toJson(reviews)).as("application/json")
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        reviewRepository.delete(id)
        Redirect("/reviews")
    }

    def addReview(): Action[AnyContent] = Action { implicit request =>
        val review_json = request.body.asJson.get
        val review = review_json.as[Review]
        reviewRepository.create(review.product, review.customer, review.content, review.score)
        Redirect("/customerreviews/" + review.customer)
    }
}

case class CreateReviewForm(product: Int, customer: Int, content: String, score: Float)

case class ReviewModel(id: Int, product: ProductModel, customer: CustomerModel, content: String, score: Float)
object ReviewModel{
    implicit val reviewFormat: OFormat[ReviewModel] = Json.format[ReviewModel]
}

case class ProductModel(id: Int, name: String)
object ProductModel{
    implicit val productFormat: OFormat[ProductModel] = Json.format[ProductModel]
}
case class CustomerModel(id: Int, firstName: String, lastName: String)
object CustomerModel{
    implicit val customerFormat: OFormat[CustomerModel] = Json.format[CustomerModel]
}
