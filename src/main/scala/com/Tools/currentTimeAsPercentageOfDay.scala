object CurrentTimeAsPercentageOfDay {
//  Takes current time in milliseconds as input and calculates percentage of day elapsed.
  def converter (time: Long): Double = {
    return time / 86400000.00
  }
}
