import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.recorder.internal.bouncycastle.oer.its.ieee1609dot2.basetypes.Duration.seconds

class TicketQuerySimulation extends Simulation {
  // 配置HTTP协议
  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")
    .userAgentHeader("Mozilla/5.0")

  // 定义查询数据
  val searchData = csv("ticket_queries.csv").circular

  // 定义场景
  val scn = scenario("Ticket Query Scenario")
    .feed(searchData)
    .exec(http("Search Tickets")
      .get("/api/ticket-service/ticketQueryList")
      .queryParam("departureCity", "${departureCity}")
      .queryParam("destinationCity", "${destinationCity}")
      .queryParam("departureDate", "${departureDate}")
      .check(status.is(200))
      .check(jsonPath("$.trainList").exists)
    )
    .pause(1, 3) // 思考时间1-3秒

  // 设置负载
  setUp(
    scn.inject(
      rampUsersPerSec(10) to 50 during (60 ), // 1分钟内从10并发递增到50
      constantUsersPerSec(50) during (300 )  // 持续5分钟50并发
    ).protocols(httpProtocol)
  )
}