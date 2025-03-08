import java.text.ParseException
import java.text.SimpleDateFormat

// inputFormat = "dd/MM/yyyy"
// outputFormat = "dd MMM yyyy"
fun String.formatDate(inputFormat : String, outputFormat : String) : String {
    return try {
        val date = SimpleDateFormat(inputFormat).parse(this)    // parse input
        SimpleDateFormat(outputFormat).format(date)    // format output
    } catch (e: ParseException) {
        ""
    }
}
