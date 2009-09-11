package ibs.util.xml;

import com.ximpleware.EOFException;
import com.ximpleware.EncodingException;
import com.ximpleware.EntityException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

class Navigator {
	
	private final VTDNav nav;
	private final Navigator _navigator = this;
	
	public Navigator(byte[] xml) throws EncodingException, EOFException, EntityException, ParseException {
		VTDGen gen = new VTDGen();
		gen.setDoc(xml);
		gen.parse(true);
		nav = gen.getNav();
	}

	public boolean hasRoot() throws NavException {
		return nav.toElement(VTDNav.ROOT);
	}

	public ChildrenIterator iterator(String name) {
		return new ChildrenIterator(name);
	}
	
	public boolean hasChild(String name) throws NavException {
		return nav.toElement(VTDNav.FIRST_CHILD, name);
	}

	public boolean parent() throws NavException {
		return nav.toElement(VTDNav.PARENT);
	}

	public String elementName() throws NavException {
		return nav.toString(nav.getCurrentIndex());
	}

	public TextReader reader() {
		return new TextReader();
	}

	public class ChildrenIterator {
		private final String name;
		private int pos = VTDNav.FIRST_CHILD;

		private ChildrenIterator(String name) {
			this.name = name;
		}

		public boolean hasNext() throws NavException {
			return nav.toElement(pos, name);
		}

		public Navigator next() {
			if(VTDNav.FIRST_CHILD == pos) {
				pos = VTDNav.NEXT_SIBLING;
			}
			return _navigator;
		}
	}
	
	public class TextReader {
		private final int pos;

		private TextReader() {
			this.pos = nav.getText();
		}
		
		public boolean hasText() {
			return -1 != pos;
		}

		public String getText() throws NavException {
			return nav.toString(pos);
		}
	}
}
