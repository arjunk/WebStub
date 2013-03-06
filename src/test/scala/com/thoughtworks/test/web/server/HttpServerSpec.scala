package com.thoughtworks.test.web.server

import com.thoughtworks.test.SmartSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.thoughtworks.test.web.utils.Client

@RunWith(classOf[JUnitRunner])
class HttpServerSpec extends SmartSpec {
  val httpClient = new Client
  val server = new HttpServer(9099, "root")

  override protected def beforeAll {
    server.start
  }

  describe ("An HttpServer") {
    it ("should display a status page if started") {
      httpClient.get("http://localhost:9099/root/status").status should be(200)
    }

    it ("must not accept an invalid context root") {
      evaluating (new HttpServer(9099, null)) should produce[IllegalArgumentException]
    }

    it ("should report resource not found if context doesn't exist") {
      httpClient.get("http://localhost:9099/doesNotExist/status").status should be(404)
    }
  }

  override protected def afterAll {
    server.stop
  }
}
