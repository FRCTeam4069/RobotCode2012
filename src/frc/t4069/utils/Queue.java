package frc.t4069.utils;

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

	public int qsize() {
		return m_size;
	}

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

	public Object get() {
		Link tempLink = m_first;
		m_first = m_first.next;
		m_size--;
		return tempLink.element;
	}

	public Object peek() {
		return m_first.element;
	}
}
