package com.itermit.railway.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class OpTag extends TagSupport {

	private int x;
	private int y;
	private String op;

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setOp(String op) {
		this.op = op;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			switch (op) {
			case "+":
				out.print(x + y);
				break;
			case "-":
				out.print(x - y);
				break;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return SKIP_BODY;
	}

}
