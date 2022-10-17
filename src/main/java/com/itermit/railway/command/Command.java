package com.itermit.railway.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itermit.railway.db.DBException;

public interface Command {

    String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException;

}
