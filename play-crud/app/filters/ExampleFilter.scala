package filters

import akka.stream.Materializer
import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class ExampleFilter @Inject()(implicit override val mat: Materializer,
                                 exec: ExecutionContext) extends Filter {

    override def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {

        nextFilter(requestHeader).map { result =>
            println("Headers " + result)
            result.withHeaders(
                "Access-Control-Allow-Origin" -> "*"
                , "Access-Control-Allow-Methods" -> "OPTIONS, GET, POST, PUT, DELETE, HEAD" // OPTIONS for pre-flight
                , "Access-Control-Allow-Headers" -> "Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With" //, "X-My-NonStd-Option"
                , "Access-Control-Allow-Credentials" -> "true"
            )
        }
    }

}
