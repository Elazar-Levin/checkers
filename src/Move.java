
public class Move {
	Position src;
	Position dest;
	public Move(Position src, Position dest) {
		super();
		this.src = src;
		this.dest = dest;
	}
	@Override
	public String toString() {
		return src.toString() + " " + dest.toString();
	}
	
}
