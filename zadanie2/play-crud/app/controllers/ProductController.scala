package controllers

import models.repository.{CategoryRepository, ProductRepository}
import play.api.mvc._

import javax.inject._


@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents,
                                  productRepository: ProductRepository,
                                  categoryRepository: CategoryRepository) extends BaseController {

    def index() = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.index())
    }
}
