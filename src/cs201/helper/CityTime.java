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
	
	/**
	 * Creates a CityTime with the value 7:00AM Monday
	 * 
	 */
	public CityTime() {
		this.day = WeekDay.Monday;
		this.hour = 7;
		this.minute = 0;
	}
	
	/**
	 * Creates a time where the day does not matter
	 * @param hour The hour being saved
	 * @param minute The minute being saved
	 */
	public CityTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
		this.day = WeekDay.Monday;
	}
	
	/**
	 * Determines equality between this CityTime and another CityTime
	 * @param other The CityTime being compared to this one
	 * @return True if the two are the same
	 */
	public boolean equals(CityTime other) {
		return this.day == other.day && this.hour == other.hour && this.minute == other.minute;
	}
	
	/**
	 * Determines equality between this CityTime and another CityTime, ignoring what day it is
	 * @param other The CityTime being compared to this one
	 * @return True if the two are the same
	 */
	public boolean equalsIgnoreDay(CityTime other) {
		return this.hour == other.hour && this.minute == other.minute;
	}
	
	/**
	 * Enum for describing the days of the week
	 * @author Matt Pohlmann
	 *
	 */
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
