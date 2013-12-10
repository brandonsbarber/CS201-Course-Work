package cs201.helper.transit;

public enum MovementDirection {

	None,
	Horizontal,
	Vertical,
	Up,
	Down,
	Left,
	Right,
	Turn;
	
	public boolean isValid()
	{
		return this != None;
	}
	
	public boolean isVertical()
	{
		return this == Up || this == Down || this == Vertical;
	}
	
	public boolean isHorizontal()
	{
		return this == Left || this == Right || this == Horizontal;
	}
	
	public boolean opposites(MovementDirection dir1, MovementDirection dir2)
	{
		return (dir1 == Left && dir2 == Right) || (dir1 == Right && dir2 == Left) || (dir1 == Down && dir2 == Up) || (dir1 == Up && dir2 == Right);
	}
}
