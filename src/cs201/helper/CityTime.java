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
		this.hour = 6;
		this.minute = 0;
	}
	
	/**
	 * Creates a time where the day does not matter (is actually null)
	 * @param hour The hour being saved
	 * @param minute The minute being saved
	 */
	public CityTime(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
		this.day = null;
	}
	
	/**
	 * Creates a CityTime with the given time (times are 24-hour based) so 2:45PM would be 14:45
	 * @param hour Int
	 * @param minute Int
	 * @param day WeekDay
	 */
	public CityTime(int hour, int minute, WeekDay day) {
		this.hour = hour;
		this.day = day;
		this.minute = minute;
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
	 * Determines equality between this CityTime and a given time
	 * @param hour The hour to compare to (24-hour based)
	 * @param minute The minute to compare to
	 * @return True if CityTime equals the time given (ignores the day)
	 */
	public boolean equals(int hour, int minute) {
		return this.hour == hour && this.minute == minute;
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
	 * Adds minutesToAdd to this CityTime and returns a new CityTime (Does not change this CityTime)
	 * @param minutesToAdd How many minutes to add (can be negative)
	 * @return A new CityTime object
	 */
	public CityTime add(int minutesToAdd) {
		int minutes = this.minute + minutesToAdd;
		int hours = ((this.hour + minutes / 60) % 24 + 24) % 24;
		if (minutesToAdd < 0) {
			hours--;
		}
		minutes = (minutes % 60 + 60) % 60;
		
		return new CityTime(hours, minutes);
	}
	
	/**
	 * Increments this CityTime by minutesToAdd. Will properly roll over the day
	 * @param minutesToAdd How many minutes to increment by
	 */
	public void increment(int minutesToAdd) {
		int minutes = this.minute + minutesToAdd;
		int hours = ((this.hour + minutes / 60) % 24 + 24) % 24;
		if (minutesToAdd < 0) {
			hours--;
		}
		minutes = (minutes % 60 + 60) % 60;
		
		if (this.day != null && hour > hours) {
			int currentDay = this.day.ordinal();
			currentDay = (currentDay + 1) % WeekDay.values().length;
			this.day = WeekDay.values()[currentDay];
		}
		
		this.minute = minutes;
		this.hour = hours;
	}
	
	/**
	 * Finds the difference in minutes between two CityTime objects (not counting the Day)
	 * @param a CityTime 1
	 * @param b CityTime 2
	 * @return int representing the difference in minutes between the two times. Positive if a is larger, negative is b is larger
	 */
	public static int timeDifference(CityTime a, CityTime b) {
		int difference = (a.hour - b.hour) * 60;
		difference += a.minute - b.minute;
		
		return difference;
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		if (this.day != null) {
			s.append(String.format("%9s", this.day));
			s.append(", ");
		}
		if (this.hour == 0 || this.hour == 12) {
			s.append(12);
		} else {
			s.append(String.format("%2d", this.hour % 12));
		}
		s.append(":");
		s.append(String.format("%02d", this.minute));
		s.append(this.hour < 12 ? "AM" : "PM");
		
		return s.toString();
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
