package nodes;

import custom.processor.CSSLoader;

public class NodeCreator extends CSSLoader{
	String Tag="";
	String content="";
	String layout="";
	String styleID="";
	String style="";
	String id="";
	String href="";
	public String createPairNode(){
		if(Tag.equals("img")) {
			return "<"+this.Tag+style+id+" "+"src ="+href+"/>";
		}
		if(href.length()>0) {
			return "<"+this.Tag+style+id+" "+"href ="+href+">"+ this.content   + "</"+this.Tag+">";
		}
		return "<"+this.Tag+style+id+">"+ this.content   + "</"+this.Tag+">";
	}
	public void setTag(String tag) {
			this.Tag=tag;
	}
	public void setContent(String content) {
		this.content=content;
	}
	public void setNodeLink(String href) {
		this.href=href;
	}
	public String getContent() {
		return content;
	}
	public void setStyle(String styleClass) {
		this.style=" style='"+getProperty(styleClass)+"' ";
	}
	public void setID(String id) {
		this.id="   id ="+id+" ";
	}
	
}
