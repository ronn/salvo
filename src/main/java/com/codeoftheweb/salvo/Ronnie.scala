import com.codeoftheweb.salvo.entity.Player
import org.springframework.http.HttpStatus

val opp: Option[String] = Option("Ronnie")

def stat(op: Option[Player]): HttpStatus = op
  .map(_ => HttpStatus.FORBIDDEN)
  .getOrElse(HttpStatus.CREATED)

val stat: Option[Player] => HttpStatus = (op: Option[Player]) => op
  .map(_ => HttpStatus.FORBIDDEN)
  .getOrElse(HttpStatus.CREATED)