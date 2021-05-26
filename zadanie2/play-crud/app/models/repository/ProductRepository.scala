package models.repository

import models.Product
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                   val categoryRepository: CategoryRepository)
                                  (implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._


    class ProductTable(tag: Tag) extends Table[Product](tag, "product") {

        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def name = column[String]("name")
        def description = column[String]("description")
        def price = column[Float]("price")
        def category = column[Int]("category")
        def category_fk = foreignKey("category_fk",category, cat)(_.id)

        /**
         * This is the tables default "projection".
         *
         * It defines how the columns are converted to and from the Person object.
         *
         * In this case, we are simply passing the id, name and page parameters to the Person case classes
         * apply and unapply methods.
         */
        def * = (id, name, description, price, category) <> ((Product.apply _).tupled, Product.unapply)

    }

    /**
     * The starting point for all queries on the people table.
     */

    import categoryRepository.CategoryTable

    val product = TableQuery[ProductTable]
    val cat = TableQuery[CategoryTable]


    /**
     * Create a person with the given name and age.
     *
     * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
     * id for that person.
     */
    def create(name: String, description: String, price: Float, category: Int): Future[Product] = db.run {
        (product.map(p => (p.name, p.description, p.price, p.category))
            // Now define it to return the id, because we want to know what id was generated for the person
            returning product.map(_.id)
            // And we define a transformation for the returned value, which combines our original parameters with the
            // returned id
            into {case ((name, description, price, category),id) => Product(id, name, description, price, category)}
            // And finally, insert the product into the database
            ) += (name, description, price, category)
    }

    /**
     * List all the people in the database.
     */
    def list(): Future[Seq[Product]] = db.run {
        product.result
    }

    def getByCategory(category_id: Int): Future[Seq[Product]] = db.run {
        product.filter(_.category === category_id).result
    }

    def getById(id: Int): Future[Product] = db.run {
        product.filter(_.id === id).result.head
    }

    def getByIdOption(id: Int): Future[Option[Product]] = db.run {
        product.filter(_.id === id).result.headOption
    }

    def getByCategories(category_ids: List[Int]): Future[Seq[Product]] = db.run {
        product.filter(_.category inSet category_ids).result
    }

    def delete(id: Int): Future[Unit] = db.run(product.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, new_product: Product): Future[Unit] = {
        val productToUpdate: Product = new_product.copy(id)
        db.run(product.filter(_.id === id).update(productToUpdate)).map(_ => ())
    }

}

