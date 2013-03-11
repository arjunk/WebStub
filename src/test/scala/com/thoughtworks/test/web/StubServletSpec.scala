package com.thoughtworks.test.web

import com.thoughtworks.test.SmartSpec
import config.StubConfiguration
import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StubServletSpec extends SmartSpec {
  val servlet = new StubServlet(configuration("GET", "/test", 302))

  it("should return the configured response, if method and uri match") {
    val response = mock[HttpServletResponse]
    servlet.doGet(mockRequest("GET", "/test"), response)

    verify(response).setStatus(302)
  }

  it("should return server error if method is not supported") {
    val response = mock[HttpServletResponse]
    servlet.doPost(mockRequest("POST", "/test"), response)

    verify(response).sendError(501)
    verify(response, never()).setStatus(any())
  }

  it("should return client error if uri doesn't match") {
    val response = mock[HttpServletResponse]
    servlet.doGet(mockRequest("GET", "/non-existent"), response)

    verify(response).sendError(404)
    verify(response, never()).setStatus(any())
  }

  private def mockRequest(method: String, uri: String) = {
    val request = mock[HttpServletRequest]
    when(request.getMethod).thenReturn(method)
    when(request.getRequestURI).thenReturn(uri);
    request
  }

  private def configuration(method: String, uri: String, status: Int) = {
    val config = mock[StubConfiguration]

    when(config.method()).thenReturn(method)
    when(config.uri()).thenReturn(uri)
    when(config.statusCode()).thenReturn(status)

    config
  }
}