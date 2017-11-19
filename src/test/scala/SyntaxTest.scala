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
    val result = List(new Person("Leo", "Pard", 21), new Person("Lili", "Bertine", 33), new Person("Lenny", "Bar", 49)) |>
      ("age" _lte 33) |>
      ("firstname" _eq "Lili")
      //("age" between (4 and 7))
    println(result.toList)
  }
}
