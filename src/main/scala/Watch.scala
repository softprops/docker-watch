package dockerwatch

import tugboat.{ Docker, Event }

object Watch {
  trait Stop {
    def stop(): Unit
  }
}

case class Watch(docker: Docker) {
  protected class WithFilter(filter: Event.Record => Boolean)
    extends Watch(docker) {
    override def apply(f: Event.Record => Unit): Watch.Stop =
      new Watch.Stop {
        val (s, _) = docker.events.stream { record =>
          if (filter(record)) f(record)
        }
        def stop() = s.stop()
      }
  }

  def filter(filt: Event.Record => Boolean): Watch = new WithFilter(filt)
  def apply(f: Event.Record => Unit): Watch.Stop =
    new Watch.Stop {
      val (s, _) = docker.events.stream(f)
      def stop() = s.stop()
    }
}
