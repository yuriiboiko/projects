import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class McCreightsSuffixTree {

	String sequence;
	char[] alphabet;
	Node root;
	int uniqueID;
	Node prevSL;
	int prevIndex;
	int leafID;

	
	public McCreightsSuffixTree(String sequence, char[] alphabet) {
		this.sequence = sequence+ "$";
		this.alphabet = alphabet;
		create();
	}

	private void create() {
		int i = 0;

		root = new Node(uniqueID, null, null, -1, -1, null, 0);

		root.sL = root;

		Node prevNode = root;

		while (i != sequence.length()) {
			leafID++;
			setNewSL(prevNode, i);

			prevNode = findPath(prevSL, prevIndex);
			i++;
		}
	}

	private void setNewSL(Node node, int index) {
		if (node.parent == null) {
			prevSL = node;
			prevIndex = index;
		} else {
			Node u = node.parent;

			if (u.sL != null) {
				prevSL = u.sL;
				if (u.parent != null) { // Case1A: not root and SL exists
					int alpha = u.depth - 1;
					prevIndex = alpha + index;
				} else { // Case1B: root and SL is known
					prevIndex = index;
				}
			} else {
				Node uPrime = u.parent;
				Node vPrime = uPrime.sL;
				if (uPrime.parent != null) {// Case2A: uPrime not root and SL(u) is not known
					int alpha = uPrime.depth - 1;
					String beta = sequence.substring(u.startIndex, u.endIndex + 1);

					prevSL = nodeHop(vPrime, beta, index + alpha);
					u.sL = prevSL;
					prevIndex = index + prevSL.depth;
				} else {// Case2B: SL(u) is not known and uPrime is the root
					if (u.depth == 1) {
						prevSL = uPrime;
					} else {
						String beta = sequence.substring(u.startIndex + 1, u.endIndex + 1);
						prevSL = nodeHop(vPrime, beta, index);
					}
					u.sL = prevSL;
					prevIndex = index + prevSL.depth;
				}
			}
		}
	}

	private Node nodeHop(Node node, String beta, int index) {
		Node temp = node.children.get(beta.charAt(0));
		int length = temp.endIndex - temp.startIndex + 1;
		if (length == beta.length()) {
			return temp;
		} else if (length < beta.length()) {
			return nodeHop(temp, beta.substring(length), index);
		} else {
			uniqueID++;
			// issues??(beta.length() + temp.start - 1)
			Node parent = new Node(uniqueID, node, null, temp.startIndex, temp.startIndex + beta.length() - 1, null,
					node.depth + beta.length());
			node.children.put(sequence.toCharArray()[temp.startIndex], parent);

			Node v = node.children.get(sequence.toCharArray()[temp.startIndex]);
			temp.startIndex = beta.length() + temp.startIndex;
			temp.parent = v;
			v.children = new TreeMap<Character, Node>();
			v.children.put(sequence.toCharArray()[temp.startIndex], temp);
			return v;
		}
	}

	private Node findPath(Node node, int index) {

		int i = index;
		// if there's no children, create a new node (leaf node)
		// and add it as a child to the original node and return it
		if (node.children == null) {

			node.children = new TreeMap<Character, Node>();

			Node temp = new Node(leafID, node, null, index, sequence.length() - 1, null,
					node.depth + (sequence.length() - index));

			node.children.put(sequence.toCharArray()[index], temp);

			return node.children.get(sequence.toCharArray()[index]);

		}
		// Child exists and it also has a child node matching character
		// Store that child and their starting and ending index
		// check for mismatch or until you used up all characters in the node
		// if mismatch break and create a new internal node and also a leaf node and
		// connect all of them
		else if (node.children.get(sequence.toCharArray()[index]) != null) {
			Node child = node.children.get(sequence.toCharArray()[index]);

			int start = child.startIndex;
			int end = child.endIndex;
			// check for mismatch
			while (start != end + 1) {

				if (sequence.toCharArray()[start] == sequence.toCharArray()[index]) {
					start++;
					index++;
				} else {
					Node child_1 = node.children.get(sequence.toCharArray()[i]);

					uniqueID++;

					Node temp = new Node(uniqueID, node, null, child.startIndex, start - 1, null,
							node.depth + (start - child.startIndex));
					node.children.put(sequence.toCharArray()[i], temp);

					Node new_internal_node = node.children.get(sequence.toCharArray()[i]);

					child_1.parent = new_internal_node;

					new_internal_node.children = new TreeMap<Character, Node>();

					new_internal_node.children.put(sequence.toCharArray()[start], child_1);

					child_1.startIndex = start;

					temp = new Node(leafID, child_1.parent, null, index, sequence.length() - 1, null,
							child_1.parent.depth + (sequence.length() - index));
					new_internal_node.children.put(sequence.toCharArray()[index], temp);

					return new_internal_node.children.get(sequence.toCharArray()[index]);
				}
			}
			// no mismatch and string ran out check the next node and if it doesn't exist
			// add a new leaf
			if (child.children.get(sequence.toCharArray()[index]) == null) {
				Node temp = new Node(leafID, node, null, index, sequence.length() - 1, null,
						node.depth + (sequence.length() - index));
				child.children.put(sequence.toCharArray()[index], temp);

				return child.children.get(sequence.toCharArray()[index]);
			}
			// if it exists then run the find path on that node
			else {
				return findPath(node.children.get(sequence.toCharArray()[i]), index);
			}
		}
		// there are children but none of them are matching
		// create a new node and and it as a children
		else {

			Node temp = new Node(leafID, node, null, index, sequence.length() - 1, null,
					node.depth + (sequence.length() - index));

			node.children.put(sequence.toCharArray()[index], temp);

			return node.children.get(sequence.toCharArray()[index]);
		}
	}
	
	public void printBTWIndex() {
	    try {
	        FileWriter myWriter = new FileWriter("BTW_Output.txt");
	        traverse(root, myWriter);
	        myWriter.write("Total number of internal nodes is "+ (uniqueID+1));
        	myWriter.write("\n");
	        myWriter.write("Total number of leaf nodes is "+ leafID);
        	myWriter.write("\n");
	        myWriter.write("Total number of nodes is "+ (leafID+uniqueID+1));
	        myWriter.close();
		    System.out.println("Total number of internal nodes is "+ (uniqueID+1));
		    System.out.println("Total number of leaf nodes is "+ leafID);
		    System.out.println("Total number of nodes is "+ (leafID+uniqueID+1));
	        System.out.println("Successfully wrote to the file.");
	      } catch (IOException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	}

	private void traverse(Node node, FileWriter myWriter) throws IOException {
	    if(node.children == null){
	        if((node.id - 2) == -1) {
	        	myWriter.write((sequence.toCharArray()[this.sequence.length()- 1]));
	        	myWriter.write("\n");
	            System.out.println((sequence.toCharArray()[this.sequence.length()- 1]));
	        }
	        else {
	        	myWriter.write((sequence.toCharArray()[node.id - 2]));
	        	myWriter.write("\n");
	            System.out.println((sequence.toCharArray()[node.id - 2]));
	        }
	    }
	    else{
	      for(char ch : node.children.keySet()){
	        traverse(node.children.get(ch), myWriter); 
	      }
	    }
	}

}
