package dockerwatch

import tugboat.{ Docker, Event }

case class Watch
 (docker: Docker)
 (f: Event.Record => Unit) {
  def start = docker.events.stream(f)
}
