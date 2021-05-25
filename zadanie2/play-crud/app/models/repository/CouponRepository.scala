package models.repository

import models.Coupon
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CouponRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[JdbcProfile]

    import dbConfig._
    import profile.api._

    class CouponTable(tag: Tag) extends Table[Coupon](tag, "coupon") {
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
        def code = column[Long]("code")
        def couponType = column[String]("couponType")
        def discount = column[BigDecimal]("discount")
        def isActive = column[Boolean]("isActive")
        def createdAt = column[String]("createdAt")
        def usedAt = column[String]("createdAt")

        def * = (id, code, couponType, discount, isActive, createdAt, usedAt) <> ((Coupon.apply _).tupled, Coupon.unapply)
    }
//    id: Long, code: Long, couponType: String, discount: BigDecimal, isActive: Boolean, createdAt: String, usedAt: String

    val coupon = TableQuery[CouponTable]

    def create(code: Long, couponType: String, discount: BigDecimal, isActive: Boolean, createdAt: String, usedAt: String): Future[Coupon] = db.run {
        (coupon.map(c => (c.code, c.couponType, c.discount, c.isActive, c.createdAt, c.usedAt))
            returning coupon.map(_.id)
            into {case ((code, couponType, discount, isActive, createdAt, usedAt), id) => Coupon(id, code, couponType, discount, isActive, createdAt, usedAt)}
        ) += (code, couponType, discount, isActive, createdAt, usedAt)
    }

    def list(): Future[Seq[Coupon]] = db.run {
        coupon.result
    }
}
