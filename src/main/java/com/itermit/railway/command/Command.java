package com.itermit.railway.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itermit.railway.db.CommandException;
import com.itermit.railway.db.DBException;

/**
 * Command interface.
 *
 * @author O.Savinov
 */
public interface Command {

    /**
     * Command execution.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Address string
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, CommandException;

}
