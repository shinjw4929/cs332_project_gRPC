import io.grpc.ServerBuilder
import scala.concurrent.{ExecutionContext, Future}
import hello._

object GreeterServer {
  def main(args: Array[String]): Unit = {
    val server = new GreeterServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }
}

class GreeterServer(executionContext: ExecutionContext) { self =>
  private[this] var server: io.grpc.Server = null

  def start(): Unit = {
    server = ServerBuilder.forPort(50051)
      .addService(GreeterGrpc.bindService(new GreeterImpl, executionContext))
      .build()
      .start()
    println("Server started, listening on " + 50051)
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down")
    }
  }

  def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  class GreeterImpl extends GreeterGrpc.Greeter {
    override def sayHello(req: HelloRequest): Future[HelloReply] = {
      val reply = HelloReply(message = "Hello " + req.name)
      Future.successful(reply)
    }
  }
}
