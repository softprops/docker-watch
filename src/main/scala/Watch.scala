package dockerwatch

import scala.concurrent.ExecutionContext
import tugboat.{ Docker, Event }

object Watch {
  trait Stop {
    def stop(): Unit
  }
  def apply(implicit ec: ExecutionContext): Watch =
    Watch(Docker())
}

case class Watch(
  docker: Docker, filter: Option[Event.Record => Boolean] = None) {
  /** start watching a stream of docker event records providing a function to invoke
   *  when a given record is made */
  def apply(f: Event.Record => Unit): Watch.Stop =
    new Watch.Stop {
      val (s, _) = docker.events.stream { rec =>
        filter match {
          case Some(filt) =>
            if (filt(rec)) f(rec)
          case _ => f(rec)
        }
      }
      def stop() = s.stop()
    }

  /** filters which event records will be processed from the stream */
  def filter(pred: Event.Record => Boolean): Watch =
    copy(filter = filter.map( filt => { rec: Event.Record => filt(rec) && pred(rec) })
                        .orElse(Some(pred)))
}
