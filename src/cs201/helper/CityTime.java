package cs201.helper;

/**
 * General-purpose class for determining the time in SimCity201
 * @author Matt Pohlmann
 *
 */
public class CityTime {
	public WeekDay day;
	public int hour;
	public int minute;
	
	public boolean equals(CityTime other) {
		return this.day == other.day && this.hour == other.hour && this.minute == other.minute;
	}
	
	public enum WeekDay {
		Sunday,
		Monday,
		Tuesday,
		Wednesday,
		Thursday,
		Friday,
		Saturday
	}
}
