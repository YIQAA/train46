import io.gatling.core.Predef._
import io.gatling.http.Predef._

class OrderCreationSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")

  // 登录获取token
  val loginData = csv("users.csv").circular

  val scn = scenario("Order Creation Scenario")
    .feed(loginData)
    .exec(http("Login")
      .post("/api/auth/login")
      .body(StringBody("""{"username":"123","password":"123""")).asJson
      .check(status.is(200))
      .check(jsonPath("$.token").saveAs("token"))
    )
    .pause(1)

    // 查询车票
    .exec(http("Search Tickets")
      .get("/api/ticket-service/ticketQueryList")
      .queryParam("departureCity", "北京")
      .queryParam("destinationCity", "上海")
      .queryParam("departureDate", "2025-05-01")
      .check(status.is(200))
      .check(jsonPath("$.trainList[0].trainId").saveAs("trainId"))
    )
    .pause(2)

    // 创建订单
    .exec(http("Create Order")
      .post("/api/order-service/order/create")
      .header("Authorization", "Bearer #{token}")
      .body(StringBody("""{
        "userId": 1,
        "trainId": "${trainId}",
        "departureStation": "北京南",
        "arrivalStation": "上海虹桥",
        "passengers": [
          {
            "realName": "张三",
            "idType": 0,
            "idCard": "110101199001011234",
            "seatType": 1,
            "ticketType": 0
          }
        ],
        "isChooseSeat": false
      }""")).asJson
      .check(status.is(200))
      .check(jsonPath("$.orderSn").saveAs("orderSn"))
    )

  setUp(
    scn.inject(
      rampUsers(50) during (30 ),
      constantUsersPerSec(10) during (120 )
    ).protocols(httpProtocol)
  )
}