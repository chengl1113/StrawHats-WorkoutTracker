import android.os.Build
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.Instant
import java.time.ZoneId

class DateAxisValueFormatter : ValueFormatter() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        // Convert timestamp to LocalDate
        val localDate = Instant.ofEpochMilli(value.toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        // Format LocalDate as desired (e.g., "MM/dd")
        return "${localDate.monthValue}/${localDate.dayOfMonth}"
    }
}
