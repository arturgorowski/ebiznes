package models.repository

import models.Category
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {
        def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
        def name = column[String]("name")
        def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
    }

    val category = TableQuery[CategoryTable]

    def create(name: String): Future[Category] = db.run {
        (category.map(c => (c.name))
            returning category.map(_.id)
            into ((name, id) => Category(id, name))
            ) += (name)
    }

    def list(): Future[Seq[Category]] = db.run {
        category.result
    }

    def getById(id: Int): Future[Category] = db.run {
        category.filter(_.id === id).result.head
    }

    def getByIdOption(id: Int): Future[Option[Category]] = db.run {
        category.filter(_.id === id).result.headOption
    }

    def delete(id: Int): Future[Unit] = db.run(category.filter(_.id === id).delete).map(_ => ())

    def update(id: Int, newCategory: Category): Future[Unit] = {
        val categoryToUpdate: Category = newCategory.copy(id)
        db.run(category.filter(_.id === id).update(categoryToUpdate)).map(_ => ())
    }
}

