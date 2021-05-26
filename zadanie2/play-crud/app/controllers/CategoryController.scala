package controllers

import javax.inject._
import models.Category
import models.repository.CategoryRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext

@Singleton
class CategoryController @Inject()(categoryRepository: CategoryRepository,
                                   controllerComponents: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext)
    extends MessagesAbstractController(controllerComponents) {

    val productForm: Form[CreateCategoryForm] = Form {
        mapping(
            "name" -> nonEmptyText
        )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
    }

    // VIEWS
    def getCategoriesForm: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val categories = categoryRepository.list()
        categories.map( category => Ok(views.html.categoriesList(category)))
    }

    // JSON METHODS
    def getCategories: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        val temp = categoryRepository.list()
        temp.map( categories => Ok(Json.toJson(categories)).as("application/json"))
    }

    def getCategory(id: Int): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
        categoryRepository.getByIdOption(id: Int).map {
            case Some(category) => Ok(Json.toJson(category))
            case None => Redirect(routes.CategoryController.getCategories())
        }
    }

    def delete(id: Int): Action[AnyContent] = Action {
        categoryRepository.delete(id)
        Redirect("/categories")
    }

    def addCategory(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        val category_json = request.body.asJson.get
        val category = category_json.as[Category]
        categoryRepository.create(category.name)
        Ok("Created")
    }
}

case class CreateCategoryForm(name: String)
