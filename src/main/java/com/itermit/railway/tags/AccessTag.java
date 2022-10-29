package com.itermit.railway.tags;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Tag to grant user permission depending on some factors.
 * <p>
 * These factors are combinations of attributes isAdmin and isAuthorized.
 * It can be interpreted as roles: "admin", "user" and "guest".
 * <p>
 * Roles "admin" and "user" have common role - "authorized".
 * All others (guests) are unauthorized.
 *
 * @author O.Savinov
 */
public class AccessTag extends TagSupport {

    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_GUEST = "guest";
    private static final String ROLE_USER = "user";
    private static final String ROLE_AUTHORIZED = "authorized";
    private static final Logger logger = LogManager.getLogger(AccessTag.class);
    private String role;
    private String modifier;

    @Override
    public int doStartTag() {

        Boolean isAuthorized = (Boolean) pageContext.getSession().getAttribute("isAuthorized");
        isAuthorized = isAuthorized != null && isAuthorized;

        Boolean isAdmin = (Boolean) pageContext.getSession().getAttribute("isAdmin");
        isAdmin = isAdmin != null && isAdmin;

        JspWriter out = pageContext.getOut();

        try {
            switch (role) {
                case ROLE_ADMIN:
                    if (isAdmin) {
                        out.print(modifier);
                    }
                    break;
                case ROLE_AUTHORIZED:
                    if (isAuthorized) {
                        out.print(modifier);
                    }
                    break;
                case ROLE_GUEST:
                    if (!isAuthorized) {
                        out.print(modifier);
                    }
                    break;
                case ROLE_USER:
                    if (isAuthorized && !isAdmin) {
                        out.print(modifier);
                    }
                    break;
                default:
                    logger.error("UNHANDLED role: {}", role);
                    logger.debug("AccessTag: isAuthorized  {}", isAuthorized);
                    logger.debug("AccessTag: setIsAdmin  {}", isAdmin);
                    logger.debug("AccessTag: role  {}", role);
                    logger.debug("AccessTag: modifier  {}", modifier);
                    break;
            }
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }

        return SKIP_BODY;
    }

    /**
     * Correctly sets role.
     *
     * @param role String
     */
    public void setRole(String role) {
        this.role = (role == null ? "" : role);
    }


    /**
     * Correctly sets modifier.
     *
     * @param modifier String
     */
    public void setModifier(String modifier) {
        this.modifier = (modifier == null ? "" : modifier);
    }

}
