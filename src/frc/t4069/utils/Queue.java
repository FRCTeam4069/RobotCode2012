package frc.t4069.utils;

/**
 * A Queue object compatible with J2ME on the cRIO.
 * 
 * @author Shuhao
 */
public class Queue {
	private static class Link {
		public Link next;
		public Object element;

		public Link(Object element) {
			this.element = element;
		}
	}

	private int m_size = 0;
	private Link m_first = null;
	private Link m_last = null;

	/**
	 * Return the current size of the queue.
	 * 
	 * @return
	 */
	public int qsize() {
		return m_size;
	}

	/**
	 * Puts an object to the end of the queue
	 * 
	 * @param element Anything here.
	 */
	public void put(Object element) {
		if (m_first == null) {
			m_first = new Link(element);
			m_last = m_first;
		} else {
			Link tempLink = new Link(element);
			m_last.next = tempLink;
			m_last = tempLink;
		}
		m_size++;
	}

	/**
	 * Gets the object at the front of the queue and removes it from the queue
	 * 
	 * @return The object stored.
	 */
	public Object get() {
		Link tempLink = m_first;
		m_first = m_first.next;
		m_size--;
		return tempLink.element;
	}

	/**
	 * Gets the object at the front of the queue without removing it.
	 * 
	 * @return The object stored.
	 */
	public Object peek() {
		return m_first.element;
	}
}
