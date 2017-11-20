import com.ideal.aryel.datasource.FieldElement
import com.ideal.aryel.dsl.AryelDSL._

object SyntaxTest {
  case class Person(firstname: String, lastname: String, age: Int) extends FieldElement {
    override def access(attribute: String): Comparable[_] = attribute match {
      case "firstname" => firstname
      case "lastname" => lastname
      case "age" => age
    }
  }

  def main(args: Array[String]): Unit = {
    val result = List(new Person("Leo", "Pard", 21), new Person("Lili", "Bertine", 33), new Person("Lenny", "Bar", 49), new Person("Ulla", "Vesuve", 29), new Person("Max", "Puissant", 31)) |>
      ("age" _between (27 and 37)) |>
      ("firstname" _contains "l") |>
      (orderBy("firstname") desc) |>
      (limit(1))
    println(result.toList)
  }
}
