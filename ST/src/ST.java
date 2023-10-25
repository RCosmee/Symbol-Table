import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ST<Key extends Comparable<Key>, Value> {

	private Node root;

	private class Node {
		Key key;
		Value val;
		Node left;
		Node right;
		int size;

		Node(Key k, Value v, int s) {
			key = k;
			val = v;
			size = s;
		}
	}

	// Initialise an empty ordered symbol table
	public ST() {
		root = null;
	}

	// Put the key-value pair into this table
	public void put(Key key, Value val) {
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		if (val == null)
			throw new IllegalArgumentException("Argument is null");
		root = put(root, key, val);
	}

	public Node put(Node x, Key key, Value val) {
		// criterios de paragem
		if (x == null)
			return new Node(key, val, 1);
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		if (val == null)
			throw new IllegalArgumentException("Argument is null");
		
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			x.val = val;
		// Casos recursivos - avancar pela arvore
		else if (cmp < 0)
			x.left = put(x.left, key, val);
		else
			x.right = put(x.right, key, val);

		x.size = 1 + size(x.right) + size(x.left);
		return x;

	}

	/*
	 * ITERATIVO // Get the value paired with key (or null) 
	 * public Value get(Key key) { if (key == null) throw new
	 * IllegalArgumentException("The given key is null!");
	 * 
	 * Node x = root; while(x != null){ int cmp = key.compareTo(x.key); if (cmp ==
	 * 0) return x.val; else if (cmp < 0) x = x.left; else x = x.right; } return
	 * null; }
	 */

	// RECURSSIVO
	public Value get(Key key) {
		return get(root, key);
	}

	private Value get(Node x, Key key) {
		// criterios de paragem
		if (x == null)
			return null;
		if (key == null)
			throw new IllegalArgumentException("Argument is null");

		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return x.val;
		// Casos recursivos - avancar pela arvore
		else if (cmp < 0)
			return get(x.left, key);
		else
			return get(x.right, key);
	}

	// Remove the pair that has this key
	public void delete(Key key) {
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
			
		root = delete(root, key);
	}

	public Node delete(Node x, Key key ) {
		if (x == null) 
			return null;
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		
		int cmp = key.compareTo(x.key);
		if (cmp < 0) // se key for menor que x.key vai para a direita
			x.left = delete(x.left, key);
		else if (cmp > 0) // se key for maior que x.key vai para a esquerda
			x.right = delete(x.right, key);
		else { 
			if (x.right == null) { //se nao existir direita, devolve a esquerda
				return x.left;
			}
			if (x.left == null) { //se nao existir esquerda, devolve a direita
				return x.right;
			}
			
			Node tmp = x;
			x.key = min(tmp.right);
			x.right = deleteMin(tmp.right);
			x.left = tmp.left;
			
			
		} 

		x.size = 1 + size(x.right) + size(x.left);
		return x;
		
	}

	// Is there a value paired with the key?
	public boolean contains(Key key) {
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		return contains (root, key);
	}
	
	public boolean contains(Node x, Key key) {
		if (x == null) 
			return false;
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		
		int cmp = key.compareTo(x.key);
		if (cmp < 0) // se key for menor que x.key vai para a direita
			return contains(x.left, key);
		else if (cmp > 0) // se key for maior que x.key vai para a esquerda
			return contains(x.right, key);
		else {
			return true;
		}
	}

	// Is this symbol table empty?
	public boolean isEmpty() {
		return root == null;
	}

	// Number of key-value pairs in this table
	public int size() {
		return size(root);
	}
	
	public int size (Node x) {
		if (x == null) {
			return 0;
		}
		else {
			return x.size;
		}
		
	}

	// Smallest key
	public Key min() {
		if (isEmpty())
			return null;
		return min(root);
	}

	public Key min(Node x) {
		if (x == null)
			throw new IllegalArgumentException("Argument is null");
		if (x.left == null)
			return x.key;
		else
			return min(x.left);
	}

	// Largest key
	public Key max() {
		if (isEmpty())
			return null;
		return max(root);
	}

	public Key max(Node x) {
		if (x == null)
			throw new IllegalArgumentException("Argument is null");
		if (x.right == null)
			return x.key;
		else
			return max(x.right);
	}

	// Largest key less than or equal to key
	public Key floor(Key key) {
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		return floor(root, key);
	}
		
	public Key floor(Node x, Key key) {
		if(x == null)
			return null;
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		
		int cmp = x.key.compareTo(key);
		if (cmp == 0)
			return x.key;
		if (cmp > 0) { // a x.key é maior que a key?
				return floor(x.left, key);
		}
		
		Key tmp = floor(x.right, key); //se x.key for menor que key
		
		if (tmp == null)
			return x.key;
		
		return tmp;
	}
	
	
	// Smallest key greater than or equal to key
	public Key ceiling(Key key) {
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		return ceiling(root, key);
	}
		
	public Key ceiling(Node x, Key key) {
		if(x == null)
			return null;
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		
		int cmp = x.key.compareTo(key);
		if (cmp == 0)
			return x.key;
		if (cmp < 0) { // a x.key é menor que a key?
				return ceiling(x.right, key);
		}
		
		Key tmp = ceiling(x.left, key); // se a x.key for maior do que a key...
		
		if (tmp == null)
			return x.key;
		
		return tmp;
	}

	// Number of keys less than key
	public int rank(Key key) {
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		
		return rank(root,key);
	}

	public int rank(Node x, Key key) {
		if (x == null) 
			return 0;
		if (key == null)
			throw new IllegalArgumentException("Argument is null");
		
		int cmp = key.compareTo(x.key);
		if (cmp < 0) // se key for menor que x.key ...
			return rank(x.left, key);
		else if (cmp > 0) // se key for maior que x.key ...
			return 1 + size(x.left) + rank(x.right, key);
		 
		return size(x.left); 
		
		
	}
	
	// Get a key of rank r
	public Key select(int r) {
		if (r < 0)
			throw new IllegalArgumentException("Argument is less than 0");
		
		return select (root, r);
	}

	public Key select(Node x, int r) {
		if (x == null) 
			return null;
		if (r < 0)
			throw new IllegalArgumentException("Argument is less than 0");
		int rank = rank(x.key);
		if (rank < r) // se rank de x.key for menor que r ...
			return select(x.left, r);
		else if (rank > r) // se rank de x.key for maior que r ...
			return select(x.right, r);
		
		return x.key; 
		
	}
	// Delete the pair with the smallest key
	public void deleteMin() {
		root = deleteMin(root);
	}

	public Node deleteMin(Node x) {
		if (x == null)
			throw new IllegalArgumentException("Argument is null");
		
		if (x.left == null)
			return x.right; //devolve o q esta a direita (se n tiver nada devolve null)
		else
			x.left = deleteMin(x.left);
		x.size = 1 + size(x.right) + size(x.left);
		return x;
	}

	// Delete the pair with the largest key
	public void deleteMax() {
		root = deleteMax(root);
	}

	public Node deleteMax(Node x) {
		if (x == null)
			throw new IllegalArgumentException("Argument is null");
		
		if (x.right == null)
			return x.left; //devolve o q esta a esquerda (se n tiver nada devolve null)
		else
			x.right = deleteMax(x.right);
		x.size = 1 + size(x.right) + size(x.left);
		return x;
	}

	// Number of keys in [lo, hi]
	public int size(Key lo, Key hi) {
		if (lo == null)
			throw new IllegalArgumentException("Argument is null");
		if (hi == null)
			throw new IllegalArgumentException("Argument is null");
		
		if (contains(hi))
			return rank(hi) - rank(lo) + 1; //verificaçoes?
		else 
			return rank(hi) - rank(lo);
	}

	
	
	public class Queue<Item> implements Iterable<Item> {
		
		private Node first, last;
		private int size;

		private class Node {
			private Item item;
			private Node next;
		}

		// create an empty queue
		public Queue() {
			first = null;
			last = null;
			size = 0;
		}

		// add an item
		public void enqueue(Item item) {
			
			Node oldLast = last;
			
			
			
			last = new Node();
			last.item = item;
			last.next = null;

			// caso vazio
			if (isEmpty()) {
				first = last;
			} else {
				oldLast.next = last;
			}
			size++;
		}

		// remove the least recently added item e mostra
		public Item dequeue() {
			// caso vazio
			if (isEmpty())
				throw new IllegalStateException("Queue is Empty!");

			Item item = first.item;

			//caso 1 item
			if (size == 1) {
				first = null;
				last = null;
				size = 0;
				return item;
			}

			// caso geral
			first = first.next;
			size--;
			
			if (isEmpty())
				last = null;
				
			return item;

		}

		// is the queue empty?
		public boolean isEmpty() {
			return (first == null);
		}

		// number of items in the queue
		public int size() {
			return size;
		}

		// support iteration
		public Iterator<Item> iterator() {
			return new QueueIterator();
		}

		private class QueueIterator implements Iterator<Item> {
			Node cur = first;

			@Override
			public boolean hasNext() {
				return cur!=null;
			}

			@Override
			public Item next() {
				if (!hasNext())
					throw new NoSuchElementException("There is no next!");
				Item i = cur.item;
				cur = cur.next;
				return i;
			}

		}
	}
	
	
	// Keys in [lo, hi] in sorted order
	public Iterable<Key> keys(Key lo, Key hi) {
		if (lo == null)
			throw new IllegalArgumentException("Argument is null");
		if (hi == null)
			throw new IllegalArgumentException("Argument is null");
		
		Queue<Key> queue = new Queue<Key>();				
		keys(root, queue, lo, hi);
		return queue; 
	}

	public void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
		if (x == null)
			return;
		if (lo == null)
			throw new IllegalArgumentException("Argument is null");
		if (hi == null)
			throw new IllegalArgumentException("Argument is null");
		if (queue == null)
			throw new IllegalArgumentException("Argument is null");
		
		int cmpl = lo.compareTo(x.key);
		int cmph = hi.compareTo(x.key);
		
		if(cmpl < 0) 
			keys(x.left, queue, lo, hi);
		if (cmpl <= 0 && cmph >= 0) //se estiver no meio de lo e hi, dá enqueue na fila
			queue.enqueue(x.key);
		if(cmph > 0)
			keys(x.right, queue, lo, hi);
		
	}
	
	// All keys in the table, in sorted order
	public Iterable<Key> keys() {
		Queue<Key> queue = new Queue<Key>();				
		keys(root, queue);
		return queue;
	}
	
	public void keys(Node x, Queue<Key> queue) {
		if (x == null)
			return;
		if (queue == null)
			throw new IllegalArgumentException("Argument is null");
		
		keys(x.left, queue);
		queue.enqueue(x.key);
		keys(x.right, queue);
	}
	

	public String toString() {
		return toString(root) + "\n";
	}

	public String toString(Node x) {
		// criterio de paragem
		if (x == null)
			return "";
		// casos recursivos
		return toString(x.left) + " " + x.key + "(" + x.val + ")"+ " " + toString(x.right);

	}

	public static void main(String[] args) {
		ST<String, Integer> st = new ST<String, Integer>();
		
		Scanner sc = new Scanner(System.in);
		
		int i = 0;
		
		
		while(true) {
			String key = sc.next();
			if (key.equals("end"))
				break;
			st.put(key, i);
			i++;
		}
		
		System.out.print("O size da ST é de : " + st.size());
		System.out.println();
		//
		System.out.println("Qual key quer ver se existe?");
		String keycontains = sc.next();
		if (st.contains(keycontains))
			System.out.println("A ST contém a key " + keycontains);
		else
			System.out.println("A ST não contém a key " + keycontains);
		System.out.println();
		//
		if (st.isEmpty())
			System.out.println("A ST está vazia");
		else
			System.out.println("A ST não está vazia");
		System.out.println();
		//
		System.out.println("Qual key quer apagar?");
		String apagar= sc.next();
		st.delete(apagar);
		System.out.println("ST after delete: " + st.toString());
		System.out.println();
		//
		System.out.println("ST after inserion: " + st.toString());
		System.out.println("Min:" + st.min());
		System.out.println("Max:" + st.max());
		st.deleteMin();
		System.out.println("ST after delMin: " + st.toString());
		st.deleteMax();
		System.out.println("ST after delMax: " + st.toString());
		System.out.println("Min:" + st.min());
		System.out.println("Max:"+ st.max());
		//
		System.out.println("Qual key quer testar com floor?");
		String keyf = sc.next();
		System.out.print("O floor de " + keyf + " é: " + st.floor(keyf));
		System.out.println();
		
		System.out.println("Qual key quer testar com floor?");
		keyf = sc.next();
		System.out.print("O floor de " + keyf + " é: " + st.floor(keyf));
		System.out.println();
		
		System.out.println("Qual key quer testar com floor?");
		keyf = sc.next();
		System.out.print("O floor de " + keyf + " é: " + st.floor(keyf));
		System.out.println();
		//
		System.out.println("Qual key quer testar com ceiling?");
		String keyc = sc.next();
		System.out.print("O ceiling de " + keyc + " é: " + st.ceiling(keyc));
		System.out.println();
		
		System.out.println("Qual key quer testar com ceiling?");
		keyc = sc.next();
		System.out.print("O ceiling de " + keyc + " é: " + st.ceiling(keyc));
		System.out.println();
		
		System.out.println("Qual key quer testar com ceiling?");
		keyc = sc.next();
		System.out.print("O ceiling de " + keyc + " é: " + st.ceiling(keyc));
		System.out.println();
		//
		System.out.println("Qual value quer ver?");
		String key = sc.next();
		System.out.print("O value de  " + key + " é: " + st.get(key));
		System.out.println();
		//
		System.out.println("Qual key quer ver o rank?");
		String keyr= sc.next();
		System.out.println("Rank de " + keyr + " é: " + st.rank(keyr));
		System.out.println();
		//
		System.out.println("Qual rank quer ver?");
		int rank = sc.nextInt();
		System.out.print("A key de rank " + rank + " é: " + st.select(rank));
		System.out.println();
		//
		System.out.print("O size da ST é de : " + st.size());
		System.out.println();
		

		sc.close();
	
	}
	
	
	
}