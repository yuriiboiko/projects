import java.util.TreeMap;

public class Node {

	int id;
	Node parent;
	TreeMap<Character, Node>children;
	int startIndex;
	int endIndex;
	Node sL;
	int depth;
	
	
	public Node(int id, Node parent, TreeMap<Character, Node>children, int startIndex, int endIndex, Node sL, int depth) {
		this.id=id;
		this.parent=parent;
		this.children=children;
		this.startIndex=startIndex;
		this.endIndex=endIndex;
		this.sL=sL;
		this.depth=depth;
	}
	
}
